package com.dr.common;

public class Const {
    //session 的key
    public static final String CURRENTUSER ="current_user";

    //用户role常量

    public enum RoleEnum{
        ROLE_ADMIN(0,"管理员"),
        ROLE_CUSTOMER(1,"普通用户")
        ;

        private int code;
        private String desc;
        private  RoleEnum(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public enum ResponseCodeEnum{
        NEED_LOGIN(2,"需要登录"),
        NO_PRIVILEGE(3,"没有权限")
        ;

        private int code;
        private String desc;

        ResponseCodeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 商品状态枚举
     * */
    public enum ProductStatusEnum{
        PRODUCT_ONLINE(1,"在售"),
        PRODUCT_OFFLINE(2,"下架"),
        PRODUCT_DELETE(3,"删除")
        ;

        private int code;
        private String desc;
        ProductStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 购物车商品是否勾选
     * **/
    public enum CartCheckedEnum{
        PRODUCT_CHECKED(1,"已勾选"),
        PRODUCT_UNCHECKED(0,"未勾选")
        ;
        private int code;
        private String desc;

        CartCheckedEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


    /**
     * 订单状态
     * */
    public enum OrderStatusEnum{
        ORDER_CLEAR(0,"已取消"),
        ORDER_UN_PAY(10,"未付款"),
        ORDER_PAYED(20,"已付款"),
        ORDER_SEND(40,"已发货"),
        ORDER_SUCCESS(50,"交易成功"),
        ORDER_CLOSE(60,"已关闭")
        ;

        private int code;
        private String desc;
        OrderStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * 遍历枚举
         * */
        public static OrderStatusEnum codeOf(Integer code) {
            for (OrderStatusEnum orderStatusEnum:values()) {
                if (code == orderStatusEnum.getCode()){
                    return orderStatusEnum;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }



    /***
     * 订单支付方式
     * */
    public enum PaymentEnum {
        PAY_ONLINE(1,"线上支付")
        ;
        private int code;
        private String desc;

        PaymentEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public static PaymentEnum codeOf(Integer code) {
            for (PaymentEnum paymentEnum:values()) {
                if (code == paymentEnum.getCode()){
                    return paymentEnum;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }


}
