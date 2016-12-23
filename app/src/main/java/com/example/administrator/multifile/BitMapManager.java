package com.example.administrator.multifile;

import android.util.Log;

/**
 * Created by Administrator on 2016/12/21.
 * 位示图的各种操作和管理
 */

public class BitMapManager {
    //默认一个盘块存放一个字符
    //默认位示图为50行50列,每次启动app后都进行格式化
    //0表示空闲，1表示已分配
    //盘块号从0开始
    //m行n列，都是默认从0开始，01234---到n
    public static int BITMAP_M = 50;
    public static int BITMAP_N = 50;
    public static int  BITMAP[][] = new int[BITMAP_M][BITMAP_N];
    //用来保存盘块的内容
    public static String[] contentlist=new String[BITMAP_M*BITMAP_N];

    public static void initBitMap() {
        for (int i = 0; i < BITMAP_M; i++) {
            for (int j = 0; j < BITMAP_N; j++) {
                BITMAP[i][j]=0;
            }
        }
        for (int i = 0; i <contentlist.length ; i++) {
            contentlist[i]="";
        }

    }

    /**
     * 文件物理结构可采用连续结构
     * 传进来文件的最大长度，为文件分配合适的空闲盘快
     *
     * @param maxLength
     * @return 返回的是盘块的首地址
     */
    public synchronized static int createNewData(int maxLength) {
        int count = 0;//表示空闲的数量
        int address = -1;//物理地址,默认为-1表示还没有分配

        for (int i = 0; i <BITMAP_M; i++) {
            for (int j = 0; j < BITMAP_N; j++) {
                if (BITMAP[i][j] == 0) {
                    if (count == 0) {
                        address = BITMAP_N * i + j;
                    }
                    count++;
                    if (count==maxLength){
                        //表示已经找到,将要分配的盘块设置已经分配状态
                        for (int k = address; k<(address+maxLength)&&k<BITMAP_M*BITMAP_N; k++) {
                            //将盘块号转化为列号
                            int temp_i=k/BITMAP_N;
                            int temp_j=k%BITMAP_N;
                            BITMAP[temp_i][temp_j]=1;
                        }

                        return address;
                    }
                } else if (BITMAP[i][j]== 1) {
                    //重置,再次寻找连续几个符合长度的空闲盘块
                    count=0;
                    address=-1;
                }
            }
        }
        return -1;//表示找不到，分配失败

    }


    /**
     * 文件删除后，回收相应的盘块
     * @param address 文件的物理地址,文件的最大长度
     */
    public synchronized static void delete(int address,int length){
        for (int k = 0; k<length; k++) {
            //将盘块号转化为列号
            int temp_i=(k+address)/BITMAP_N;
            int temp_j=(k+address)%BITMAP_N;
            BITMAP[temp_i][temp_j]=0;
            contentlist[address+k]="";
            Log.d("FileFragment", "删除盘块"+(address+k));
        }
    }


    //写操作,假设一个盘块存放一个字符,若想存放多个字符，直接在这里修改substring就可以
    public synchronized static void write(int address,int length,String content){
        for (int k = 0; k<length&&k<content.length(); k++) {
            //将盘块号转化为列号;
            contentlist[address+k]=content.substring(k,k+1);
            Log.d("FileFragment", "写入盘块"+(address+k)+":"+contentlist[address+k]);
        }


    }

    //读操作
    public synchronized static String read(int address,int length){
        StringBuffer result=new StringBuffer();
        for (int k = 0; k<length; k++) {
            //将盘块号转化为列号;
            String content=contentlist[address+k];
            result.append(content);
            Log.d("FileFragment", "读取盘块"+(address+k)+":"+content);
        }


        for (int i = 0; i < BITMAP_M; i++) {
            String string="";
            string=string+i+":";
            for (int j = 0; j <BITMAP_N ; j++) {
                string=string+BITMAP[i][j];
            }
            Log.d("FileFragment", string);
        }

//        Log.d("FileFragment",result.toString());

        //去掉空值null
        return result.toString().replace("null","") ;
    }

}
