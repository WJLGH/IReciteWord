package com.sdut.soft.ireciteword.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.sdut.soft.ireciteword.api.BaseApi;
import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.bean.UserVo;
import com.sdut.soft.ireciteword.dao.UnitDao;
import com.sdut.soft.ireciteword.utils.Const;
import com.sdut.soft.ireciteword.utils.UserResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    Context context;
    UnitDao unitDao;

    public String getUserScb(){
        return currentUser().getName()+"的生词表";
    }
    public UserService(Context context) {
        this.context = context;
        this.unitDao = new UnitDao(context);
    }

    // abc
    public void login(User user, final Handler handler) {
            UserServiceInf userServiceInf = BaseApi.retrofit(Const.USER_SERVER).create(UserServiceInf.class);
            Call<UserResult> call = userServiceInf.login(user);
            call.enqueue(new Callback<UserResult>() {
                @Override
                public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                    Message message  = new Message();
                    User user1 = response.body().getData();
                    message.obj = user1;
                    if (user1 != null) {
                        message.what = Const.LOG_IN_SUCCESS;
                    } else {
                        message.what = Const.LOG_IN_FAILURE;
                    }
                    handler.sendMessage(message);
                }

                @Override
                public void onFailure(Call<UserResult> call, Throwable t) {
                    Message message  = new Message();
                    message.what = Const.LOG_IN_FAILURE;
                    handler.sendMessage(message);
                }
            });

    }

    public void register(User user, final Handler handler) {
        UserServiceInf userServiceInf = BaseApi.retrofit(Const.USER_SERVER).create(UserServiceInf.class);
        Call<UserResult> call = userServiceInf.register(user);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                Message message  = new Message();
                User user1 = response.body().getData();
                message.obj = user1;
                if (user1 != null) {
                    message.what = Const.REGISTER_SUCCESS;
                } else {
                    message.what = Const.REGISTER_FAILURE;
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Message message  = new Message();
                message.what = Const.REGISTER_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    public void commitProgress(UserVo user, final Handler handler) {
        UserServiceInf userServiceInf = BaseApi.retrofit(Const.USER_SERVER).create(UserServiceInf.class);
        Call<UserResult> call = userServiceInf.updatePwd(user);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                Message message  = new Message();
                User user1 = response.body().getData();
                message.obj = response.body().getMessage();
                if (user1 != null) {
                    message.what = Const.PWD_UPDATE_SUCCESS;
                } else {
                    message.what = Const.PWD_UPDATE_FAILURE;
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Message message  = new Message();
                message.what = Const.PWD_UPDATE_FAILURE;
                handler.sendMessage(message);
            }
        });
    }

    public void saveUser(User user) {
        //为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.commit();
    }

    public User currentUser() {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        int id = preferences.getInt("id", -1);
        String name = preferences.getString("name", null);
        if(id == -1 || name == null) {
            return  null;
        }
        User user = new User(id, name);
        return user;
    }

    public void removeUser() {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("id");
        editor.remove("name");
        editor.commit();
    }

    public void createUnit(User user) {
        unitDao.createUnit(user.getName()+"的生词表");
    }
}
