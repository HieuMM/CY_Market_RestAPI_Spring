package com.example.springboot_cy_marketplace.dto;


public class ResponseDTO {
    private int code;
    private String message;
    private Object data;
    public ResponseDTO(Object data, String message){
        this.data = data;
        this.message = message;
    }

    public ResponseDTO(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Object of(Object data, String message){
        if(data == null || data instanceof String){
            return new ResponseDTO(data, message + " failed!");
        }else{
            return new ResponseDTO(data, message + " successfully!");
        }
    }

    public static Object show(int code, String message, Object data){
        if(data != null && !(data instanceof String)){
            return new ResponseDTO(code, message + " successfully!", data);
        }else {
            return new ResponseDTO(code, message + " fail", data);
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
