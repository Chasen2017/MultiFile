package com.example.administrator.multifile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String CurrentDirectory = "";//当前的目录,默认为空

    private String mUserName;
    private ArrayList<FileEntity> mFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        mUserName=getIntent().getStringExtra("userName");
        mFileList =getIntent().getParcelableArrayListExtra("fileList");

//        UserList = new ArrayList<>();
//        UserList.add("冬瓜");
//        //默认会有这样一个用户
//        //默认用户的文件目录是空的
//        ArrayList<FileEntity> mFileList = new ArrayList<>();
//        mFileList.add(new FileEntity("冬瓜", true));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fl_container, FileFragment.newInstance(mFileList, CurrentDirectory))
                .commit();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        //保存修改
        for (int i = 0; i <LoginActivity.userList.size() ; i++) {
            if (mUserName.equals(LoginActivity.userList.get(i).getUserName())){
                LoginActivity.userList.get(i).setFileList(mFileList);
            }
        }

    }

    //进去到子目录的列表
    @Subscribe
    public void GoToChild(FileEntity fileEntity) {
        ArrayList<FileEntity> fileList = fileEntity.getChildList();
        CurrentDirectory = CurrentDirectory + "/" + fileEntity.getName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fl_container, FileFragment.newInstance(fileList, CurrentDirectory))
                .addToBackStack(null)
                .commit();

    }


    @Override
    public void onBackPressed() {

        //如果当前目录不为空
        if (!CurrentDirectory.isEmpty()) {
            String string[] = CurrentDirectory.split("/");
            CurrentDirectory = "";
             //多加一个string不为空的判断，防止//这种形式出现
            for (int i = 0; i < string.length - 1; i++) {
                if (!string[i].isEmpty()) {
                    CurrentDirectory = CurrentDirectory + "/" + string[i];
                }
            }
        }

        super.onBackPressed();
    }
}
