package com.sdut.soft.ireciteword.bean;

import com.sdut.soft.ireciteword.utils.Const;

/**
 * 用户
 */
public class User {
    Integer id;
    String name;
    String password;
    Long rcindex; //已经背过的最大序号
    Long rvindex; //已经复习过的最大序号
    Integer perday;    //每天背诵个数

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(Integer id, String name, String password, Long rcindex, Long rvindex, Integer perday) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.rcindex = rcindex;
        this.rvindex = rvindex;
        this.perday = perday;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRcindex() {
        return rcindex;
    }

    public void setRcindex(Long rcindex) {
        this.rcindex = rcindex;
    }

    public Long getRvindex() {
        return rvindex;
    }

    public void setRvindex(Long rvindex) {
        this.rvindex = rvindex;
    }

    public Integer getPerday() {
        if(perday <=0) {
            return Const.PER_DAY;
        }
        return perday;
    }

    public void setPerday(Integer perday) {
        this.perday = perday;
    }
}
