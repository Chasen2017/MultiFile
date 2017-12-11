package com.example.administrator.multifile;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by 东 on 2016/12/11.
 * 目录文件或者数据文件
 */

public class FileEntity implements Parcelable {

    String name;//文件名字
    boolean isDirectory;//是否是目录，true表示是个目录文件，false表示是个数据文件

    FileEntity parent;//父目录
    ArrayList<FileEntity> childList;//子目录
    int address;//表示文件的物理地址
    int MaxLength;//文件的最大容量

    public FileEntity(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getMaxLength() {
        return MaxLength;
    }

    public void setMaxLength(int maxLength) {
        MaxLength = maxLength;
    }


    protected FileEntity(Parcel in) {
        name = in.readString();
        isDirectory = in.readByte() != 0;
        parent = in.readParcelable(FileEntity.class.getClassLoader());
        childList = in.createTypedArrayList(FileEntity.CREATOR);
    }

    public static final Creator<FileEntity> CREATOR = new Creator<FileEntity>() {
        @Override
        public FileEntity createFromParcel(Parcel in) {
            return new FileEntity(in);
        }

        @Override
        public FileEntity[] newArray(int size) {
            return new FileEntity[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public FileEntity getPamrent() {
        return parent;
    }

    public void setParent(FileEntity parent) {
        this.parent = parent;
    }

    public ArrayList<FileEntity> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<FileEntity> childList) {
        this.childList = childList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeByte((byte) (isDirectory ? 1 : 0));
        parcel.writeParcelable(parent, i);
        parcel.writeTypedList(childList);
    }


}
