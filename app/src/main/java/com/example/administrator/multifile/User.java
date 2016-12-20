package com.example.administrator.multifile;

import java.util.ArrayList;

/**
 * Created by 东 on 2016/12/18.
 * 用户类
 */

public class User {
    String userName;
    String passWord;
    ArrayList<FileEntity> fileList;

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.fileList=new ArrayList<>();
    }

    public ArrayList<FileEntity> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<FileEntity> fileList) {
        this.fileList = fileList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
