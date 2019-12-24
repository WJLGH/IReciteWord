package com.sdut.soft.ireciteword.bean;

/**
 * 词汇表
 */
public class Unit {

   private  int id;
   private String name;
   private Integer cnt;
   private int progress;

    public Unit(int id, String name,int progress,int cnt) {
        this.id = id;
        this.name = name;
        this.progress = progress;
        this.cnt = cnt;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cnt=" + cnt +
                ", progress=" + progress +
                '}';
    }
}
