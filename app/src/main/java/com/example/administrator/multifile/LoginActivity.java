package com.example.administrator.multifile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 东 on 2016/12/11.
 * 登录模块
 */

public class LoginActivity extends Activity implements View.OnClickListener{



    private EditText mEtUserName;
    private EditText mEtPassWord;
    private Button mBtLogin;
    private Button mBtRegister;

    public static List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BitMapManager.initBitMap();//格式化位示图
        initView();
        initData();


    }


    private void initView() {
        mEtUserName= (EditText) findViewById(R.id.login_et_userName);
        mEtPassWord = (EditText) findViewById(R.id.login_et_password);
        mBtLogin= (Button) findViewById(R.id.login_bt_login);
        mBtRegister= (Button) findViewById(R.id.login_bt_register);
        mBtLogin.setOnClickListener(this);
        mBtRegister.setOnClickListener(this);
    }

    private void initData() {
        userList=new ArrayList<>();
        /**
         * 假数据，默认有admin这个用户
         */
        User user=new User("admin","admin");
        ArrayList<FileEntity> fileList=new ArrayList<>();
        fileList.add(new FileEntity("测试目录",true));
        user.setFileList(fileList);
        userList.add(user);

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_bt_login:
                login();
                break;
            case R.id.login_bt_register:
                register();
                break;
        }
    }


    //登录功能
    private void login() {
        String userName=mEtUserName.getText().toString();
        String passWord= mEtPassWord.getText().toString();
        if (!isExist(userName)){
            Toast.makeText(LoginActivity.this,"该用户不存在",Toast.LENGTH_SHORT).show();
        }else if (!isRightPassWord(userName,passWord)){
            Toast.makeText(LoginActivity.this,"输入的密码错误",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("userName",userName);
            intent.putParcelableArrayListExtra("fileList",getFileList(userName));
            startActivity(intent);
        }


    }
    //注册功能
    private void register() {
        String userName=mEtUserName.getText().toString();
        String passWord= mEtPassWord.getText().toString();
        if (isExist(userName)){
            Toast.makeText(LoginActivity.this,"用户已经存在",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
            //保存用户名和密码
            save(userName,passWord);
        }

    }

    //判断注册的用户名是否已经存在
    private boolean isExist(String userName){
        for (int i = 0; i < userList.size(); i++) {
            if (userName.equals(userList.get(i).getUserName())){
                return true;
            }
        }
        return false;
    }
    //判断用户名和密码是否正确
    private boolean isRightPassWord(String userName,String passWord){
        for (int i = 0; i < userList.size(); i++) {
            if (userName.equals(userList.get(i).getUserName())
                    &&passWord.equals(userList.get(i).getPassWord())){
                return true;
            }
        }
        return false;
    }
    //保存账号和密码
    private void save(String userName,String passWord){
        User user=new User(userName,passWord);
        userList.add(user);
    }

    //得到某个用户的文件列表
    private ArrayList<FileEntity> getFileList(String userName){
        for (int i = 0; i <userList.size() ; i++) {
            if (userName.equals(userList.get(i).getUserName())){
                return userList.get(i).getFileList();
            }
        }
        return null;
    }






}
