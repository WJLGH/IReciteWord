package com.sdut.soft.ireciteword.utils;

import com.sdut.soft.ireciteword.bean.User;

import java.io.Serializable;

public class UserResult implements Serializable {

    private boolean success;
    private Integer code; // 網絡返回是否成功

    @Override
    public String toString() {
        return "UserResult{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    private String message;
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
