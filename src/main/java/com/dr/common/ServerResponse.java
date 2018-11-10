package com.dr.common;
/**
 * 服务器返回前端的高复用的相应对象
 * */

public class ServerResponse<T> {

    private  int status;//返回到前端的状态码
    private T data;//返回个前端的数据，必须为泛型
    private  String msg;//status!=0时，返回的错误信息

    public ServerResponse() {}
    public ServerResponse(int status) {
        this.status = status;
    }
    public ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
    //status==1,和返回错误信息，调用此方法
    public ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    public ServerResponse(int status, T data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    /**
     * 成功时调用的
     * */
    public static ServerResponse serverResponseBySuccess(){
        return new ServerResponse(ResponseCode.SUCCESS);
    }
    public static <T>ServerResponse serverResponseBySuccess(T data){
        return new ServerResponse(ResponseCode.SUCCESS,data);
    }
    //往data传字符串时，调用此方法
    public static <T>ServerResponse serverResponseBySuccess(T data, String msg){
        return new ServerResponse(ResponseCode.SUCCESS,data,msg);
    }

    /**
     * 失败是调用的方法
     * */
    public static ServerResponse serverResponseByError(){
        return new ServerResponse(ResponseCode.ERROR);
    }
    public static ServerResponse serverResponseByError(String msg){
        return new ServerResponse(ResponseCode.ERROR,msg);
    }
    public static ServerResponse serverResponseByError(int status){
        return new ServerResponse(status);
    }
    public static ServerResponse serverResponseByError(int status, String msg){
        return new ServerResponse(status,msg);
    }

    /**
     * 判断接口是否正确返回，判断status返回值是否为0
     * */
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
