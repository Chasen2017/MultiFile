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
    boolean hasChildren;//是否有子目录，默认为false
    boolean hasParent;//是否有父目录，默认为false

    boolean canRead;//设置可读,默认为true
    boolean canWrite;//设置可写,默认为false

    String content;//当做数据文件里面的数据

    int MaxLength=100;//文件的最大容量，默认为100

    public FileEntity(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
        if (isDirectory){
            hasChildren=true;
            childList = new ArrayList<>();
        }else {
            hasChildren = false;
            content="";//默认内容为空
        }

        hasParent = false;
        canRead = true;
        canWrite = false;
    }

    public int getMaxLength() {
        return MaxLength;
    }

    public void setMaxLength(int maxLength) {
        MaxLength = maxLength;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    protected FileEntity(Parcel in) {
        name = in.readString();
        isDirectory = in.readByte() != 0;
        parent = in.readParcelable(FileEntity.class.getClassLoader());
        childList = in.createTypedArrayList(FileEntity.CREATOR);
        hasChildren = in.readByte() != 0;
        hasParent = in.readByte() != 0;
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

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean isHasParent() {
        return hasParent;
    }

    public void setHasParent(boolean hasParent) {
        this.hasParent = hasParent;
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
        parcel.writeByte((byte) (hasChildren ? 1 : 0));
        parcel.writeByte((byte) (hasParent ? 1 : 0));
    }


}
