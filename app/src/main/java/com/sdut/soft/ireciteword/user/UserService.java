package com.sdut.soft.ireciteword.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.dao.UnitDao;
import com.sdut.soft.ireciteword.dao.UserDao;

public class UserService {
    UserDao userDao;
    Context context;
    UnitDao unitDao;

    public String getUserScb(){
        return currentUser().getName()+"的生词表";
    }
    public UserService(Context context) {
        this.userDao = new UserDao(context);
        this.context = context;
        this.unitDao = new UnitDao(context);
    }

    // abc
    public User login(User user) {
        return userDao.checkUser(user);
    }

    public boolean register(User user) {
        unitDao.createUnit(user.getName()+"的生词表");
        return userDao.insert(user) > 0;
    }

    public void commitProgress(User u) {
        userDao.update(u);
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
        return userDao.getUserById(id);
    }

    public void removeUser() {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("id");
        editor.remove("name");
        editor.commit();
    }

}
