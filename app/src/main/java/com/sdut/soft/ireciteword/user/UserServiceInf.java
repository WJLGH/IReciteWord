package com.sdut.soft.ireciteword.user;

import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.bean.UserVo;
import com.sdut.soft.ireciteword.utils.UserResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserServiceInf {
    @POST("/user/login")
    Call<UserResult> login(@Body User user);
    @POST("/user/register")
    Call<UserResult> register(@Body User user);
    @POST("/user/updatepwd")
    Call<UserResult> updatePwd(@Body UserVo user);
}
