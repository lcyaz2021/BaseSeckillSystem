package com.miaoshaproject.response;

public class CommenReturnType {
    // 表示请求返回结果，有 success 或 fail
    private String status;

    // 若status==success，则data返回前端所需要的json数据
    // 若为fail，则data内使用通用的错误码格式
    private Object data;

    public static CommenReturnType create(Object result){
        return create(result, "success");
    }

    public static CommenReturnType create(Object result, String status) {
        CommenReturnType commenReturnType = new CommenReturnType();
        commenReturnType.setStatus(status);
        commenReturnType.setData(result);
        return commenReturnType;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
