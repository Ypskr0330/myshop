package com.dr.service.impl;

import com.dr.common.Const;
import com.dr.common.ServerResponse;
import com.dr.dao.*;
import com.dr.pojo.*;
import com.dr.service.IOrderService;
import com.dr.utils.BigDecimalUtils;
import com.dr.utils.DateUtils;
import com.dr.utils.PropertiesUtils;
import com.dr.vo.CartOrderItemVO;
import com.dr.vo.OrderItemVO;
import com.dr.vo.OrderVO;
import com.dr.vo.ShippingVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ShippingMapper shippingMapper;

    /**
     * 创建订单
     * */
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //1.非空校验
        if (shippingId == null){
            return ServerResponse.serverResponseByError("地址参数不能为空");
        }
        //2.根据userId查询购物车中选中商品
        List<Cart> cartList = cartMapper.findCartListByUserIdAndChecked(userId, Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
        //3.List<Cart>-->orderItemList;将cart转为订单明细
        ServerResponse serverResponse = getCartOrderItem(userId,cartList);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemsList = (List<OrderItem>) serverResponse.getData();
        //计算订单价格
        BigDecimal orderTotalPrice = new BigDecimal("0");
        if (orderItemsList == null || orderItemsList.size()==0){
            return ServerResponse.serverResponseByError("订单为空");
        }
        orderTotalPrice = getOrderPrice(orderItemsList);
        //4.创建订单order并保存到数据库
        Order order = createOrder(userId,shippingId,orderTotalPrice);
        int result = orderMapper.insert(order);
        if (order == null){
            return ServerResponse.serverResponseBySuccess("订单创建失败");
        }
        //5.将orderItem存到数据库
        //从订单表中获取订单编号
        for (OrderItem orderItem:orderItemsList) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        //批量插入，把该集合插入数据库
        int result1 = orderItemMapper.insertBatch(orderItemsList);
        //6.从product表中扣除库存
        reduceProductStock(orderItemsList);
        //7.购物车中清空已下单的商品
        //批量删除购物车的商品
        cleanCart(cartList);
        //8.返回结果，OrderVO
        OrderVO orderVO = assembleOrderVO(order,orderItemsList,shippingId);
        return ServerResponse.serverResponseBySuccess(orderVO);
    }

    /**
     * 取消订单
     * */
    @Override
    public ServerResponse cancel(Integer userId, Long orderNo) {
        //1.参数非空校验
        if (orderNo == null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.查询订单是否存在,根据userId和orderId
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.serverResponseByError("订单不存在");
        }
        //3.判断订单的状态，只有未付款的订单可以取消，（10，"未付款")
        if (order.getStatus() != Const.OrderStatusEnum.ORDER_UN_PAY.getCode()) {
            return ServerResponse.serverResponseByError("该订单" + Const.OrderStatusEnum.codeOf(order.getStatus()).getDesc() + "，不可取消");
        }
        //4.改变订单的状态（ORDER_CLEAR(0,"已取消")）
        order.setStatus(Const.OrderStatusEnum.ORDER_CLEAR.getCode());
        List<OrderItem> orderItemList = Lists.newArrayList();
        int result = orderMapper.updateByPrimaryKey(order);
        if (result > 0) {
            //5.需要修改product表中商品库存
            orderItemList = orderItemMapper.selectByUserIdAndOrderNo(userId, orderNo);
            for (OrderItem orderItem : orderItemList) {
                Product product = productMapper.findByProductId(orderItem.getProductId());
                product.setStock(product.getStock() + orderItem.getQuantity());
                int result1 = productMapper.updateByPrimaryKey(product);
            }
            return ServerResponse.serverResponseBySuccess();
        } else {
            //6.返回结果
            return ServerResponse.serverResponseByError("取消失败");
        }
    }

    /**
     * 获取订单的商品信息
     * **/
    @Override
    public ServerResponse get_order_cart_product(Integer userId) {
        //1.根据userId查询购物车中选中商品
        List<Cart> cartList = cartMapper.findCartListByUserIdAndChecked(userId, Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
        //2.List<Cart>-->orderItemList;将cart转为订单明细
        ServerResponse serverResponse = getCartOrderItem(userId,cartList);
        if (!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        if (orderItemList == null || orderItemList.size() == 0){
            return ServerResponse.serverResponseByError("购物车为空");
        }
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        for (OrderItem orderItem:orderItemList) {
            OrderItemVO orderItemVO = assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        //创建CartOrderItemVO,赋值
        CartOrderItemVO cartOrderItemVO = new CartOrderItemVO();
        cartOrderItemVO.setOrderItemVOList(orderItemVOList);
        cartOrderItemVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        cartOrderItemVO.setProductTotalPrice(getOrderPrice(orderItemList));


        //返回结果
        return ServerResponse.serverResponseBySuccess(cartOrderItemVO);
    }

    /**
     * 订单List
     * **/
    @Override
    public ServerResponse list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = Lists.newArrayList();
        if (userId == null){
            //查询所有
            orderMapper.selectAll();
        }else{
            //查询当前用户
            orderList = orderMapper.selectByUserId(userId);
        }
        if (orderList == null || orderList.size() == 0){
            return ServerResponse.serverResponseByError("该用户暂无订单");
        }
        List<OrderVO> orderVOList = Lists.newArrayList();
        for (Order order:orderList) {
            List<OrderItem> orderItemList = orderItemMapper.selectByUserIdAndOrderNo(userId,order.getOrderNo());
            OrderVO orderVO = assembleOrderVO(order,orderItemList,order.getShippingId());
            orderVOList.add(orderVO);
        }
        PageInfo pageInfo = new PageInfo(orderVOList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    /**
     * 订单详情
     * */
    @Override
    public ServerResponse selectDetail(Long orderNo) {
        //1.参数非空校验
        if (orderNo == null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.根据订单编号查询订单
        Order order = orderMapper.selectByOrderNo(orderNo);
        //3.查询OrderItem
        if (order == null){
            return ServerResponse.serverResponseByError("该订单不存在");
        }
            //根据Order查询OrderItem
        List<OrderItem> orderItemList = orderItemMapper.selectByUserIdAndOrderNo(order.getUserId(),order.getOrderNo());
        if (orderItemList == null){
            return ServerResponse.serverResponseByError("该订单为空");
        }
        //4.转为OrderVO
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        OrderVO orderVO = assembleOrderVO(order,orderItemList,order.getShippingId());
        //5.返回结果
        return ServerResponse.serverResponseBySuccess(orderVO);
    }

    /**
     * 根据订单号查询订单信息
     * */
    @Override
    public ServerResponse search(Long orderNo) {
       return  null;
    }

    /**
     * 订单发货
     * */
    @Override
    public ServerResponse send_goods(Long orderNo) {
        //1.参数非空校验
        if (orderNo == null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.根据订单编号查询订单
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            return ServerResponse.serverResponseByError("该订单不存在");
        }
        //3.改变订单状态
        if (order.getStatus() != Const.OrderStatusEnum.ORDER_PAYED.getCode()){
            return ServerResponse.serverResponseByError("该订单"+Const.OrderStatusEnum.codeOf(order.getStatus()).getDesc());
        }
        order.setStatus(Const.OrderStatusEnum.ORDER_SEND.getCode());
        int result = orderMapper.updateOrderToSend(order);
        if (result>0){
            return ServerResponse.serverResponseBySuccess("发货成功");
        }
        return ServerResponse.serverResponseByError("发货失败");
    }

    /**
     * OrderVO,orderVO里把orderItemList转为OrderItemVOList，shipping转为了shippingVO
     * */
    private OrderVO assembleOrderVO(Order order, List<OrderItem> orderItemList, Integer shippingId){
        OrderVO orderVO = new OrderVO();
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        //把orderItemList转为OrderItemVOList
        for (OrderItem orderItem:orderItemList) { ;
            orderItemVOList.add(assembleOrderItemVO(orderItem));
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if (shipping != null){
            orderVO.setShippingId(shippingId);
            ShippingVO shippingVO = assembleShippingVO(shipping);
            orderVO.setShippingVO(shippingVO);
            orderVO.setReceiverName(shipping.getReceiverName());
        }
        orderVO.setStatus(order.getStatus());
        Const.OrderStatusEnum orderStatusEnum = Const.OrderStatusEnum.codeOf(order.getStatus());
        if (orderStatusEnum!=null){
            orderVO.setStatusDesc(orderStatusEnum.getDesc());
        }
        orderVO.setPostage(0);
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        Const.PaymentEnum paymentEnum = Const.PaymentEnum.codeOf(order.getPaymentType());
        if (paymentEnum!=null){
            orderVO.setPaymentTypeDesc(paymentEnum.getDesc());
        }
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setCreateTime(DateUtils.dataToString(order.getCreateTime()));
        return orderVO;

    }

    /**
     * 返回OrderItemVO
     * */
    private OrderItemVO assembleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO = new OrderItemVO();
        if (orderItem != null){
            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setOrderNo(orderItem.getOrderNo());
            orderItemVO.setCreateTime(DateUtils.dataToString(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        }
        return orderItemVO;
    }
    /**
     * 返回shippingVO
     * */
    private ShippingVO assembleShippingVO(Shipping shipping){
        ShippingVO shippingVO = new ShippingVO();
        if (shipping != null) {
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverProvince(shipping.getReceiverProvince());
            shippingVO.setReceiverCity(shipping.getReceiverCity());
            shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
        }
        return shippingVO;
    }

    /**
     *清空购物车中已选中的商品
     * */
    private void cleanCart(List<Cart> cartList){
        if (cartList!= null && cartList.size()>0){
            cartMapper.deleteBatch(cartList);
        }
    }

    /**
     *扣库存
     * */
    private void reduceProductStock(List<OrderItem> orderItemList){
        if (orderItemList != null && orderItemList.size()>0){
            for (OrderItem orderItem:orderItemList) {
                Integer productId = orderItem.getProductId();
                Integer count = orderItem.getQuantity();
                Product product = productMapper.selectByPrimaryKey(productId);
                product.setStock(product.getStock()-count);
                productMapper.updateByPrimaryKey(product);
            }
        }
    }

    /**
     * 计算订单价格
     * */
    private BigDecimal getOrderPrice(List<OrderItem> orderItemList){
        BigDecimal bigDecimal = new BigDecimal("0");
        for (OrderItem orderItem:orderItemList) {
           bigDecimal = BigDecimalUtils.add(bigDecimal.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }

        return bigDecimal;
    }

    /**
     * 创建订单
     * */
    private Order createOrder(Integer userId, Integer shoppingId, BigDecimal orderTotalPrice){
        Order order = new Order();
        order.setOrderNo(generateOrderNO());
        order.setUserId(userId);
        order.setShippingId(shoppingId);
        order.setStatus(Const.OrderStatusEnum.ORDER_UN_PAY.getCode());
        order.setPayment(orderTotalPrice);
        order.setPostage(0);
        order.setPaymentType(Const.PaymentEnum.PAY_ONLINE.getCode());
        return order;
    }
    /**
     * 生成订单编号
     * */
    private Long generateOrderNO(){
        return System.currentTimeMillis()+new Random().nextInt(100);
    }

    /**
     * 获取购物车中已选中商品的订单明细
     * */
    private ServerResponse getCartOrderItem(Integer userId, List<Cart> cartList){
        if (cartList == null){
            return ServerResponse.serverResponseByError("购物车为空");
        }
        List<OrderItem> orderItemList = Lists.newArrayList();

        for (Cart cart:cartList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(userId);
            //根据购物车中product_id查询该商品
            Product product =  productMapper.selectByPrimaryKey(cart.getProductId());
            if (product == null){
                return ServerResponse.serverResponseByError("商品不存在");
            }
            //判断商品状态
            if (product.getStatus() != Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
                return ServerResponse.serverResponseByError("该商品已下架");
            }
            //判断商品库存
            if (product.getStock()< cart.getQuantity()){
                return ServerResponse.serverResponseByError("该商品库存不足");
            }
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            //价格
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.serverResponseBySuccess(orderItemList);
    }
}
