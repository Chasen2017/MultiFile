package com.example.administrator.multifile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by 东 on 2016/12/11.
 * 文件列表的Adapter
 */

public class FileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int ITEM_TYPE_DIRECTORY = 1;
    private static int ITEM_TYPE_DATA = 2;

    private OnItemClickListener mOnItemClickListener;


    //传递一个文件列表作为参数
    private ArrayList<FileEntity> fileList;

    public FileAdapter(ArrayList<FileEntity> fileList) {
        this.fileList = fileList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_DIRECTORY) {
            return new DirectoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_directory, parent,false));
        } else if (viewType == ITEM_TYPE_DATA) {
            return new DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final FileEntity fileEntity=fileList.get(position);
        if (getItemViewType(position) == ITEM_TYPE_DIRECTORY) {
            //如果是目录文件
            DirectoryViewHolder directoryViewHolder= (DirectoryViewHolder) holder;
            directoryViewHolder.tvFileName.setText(fileEntity.getName());
            //目录文件多了一个点击的按钮
            directoryViewHolder.btGoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(fileEntity);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onDirectoryItemClick(fileEntity,position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onDirectoryItemLongClick(fileEntity,position);
                    return false;
                }
            });

        } else if (getItemViewType(position) == ITEM_TYPE_DATA) {
            //如果是数据文件
            DataViewHolder dataViewHolder= (DataViewHolder) holder;
            dataViewHolder.tvFileName.setText(fileEntity.getName());

            dataViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onDataItemClick(fileEntity,position);
                }
            });
            dataViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onDataItemLongClick(fileEntity,position);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }


    @Override
    public int getItemViewType(int position) {
        FileEntity fileEntity = fileList.get(position);
        if (fileEntity.isDirectory()) {
            return ITEM_TYPE_DIRECTORY;
        } else {
            return ITEM_TYPE_DATA;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }

    //目录文件的ViewHolder
    public static class DirectoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvFileName;
        Button btGoto;
        public DirectoryViewHolder(View itemView) {
            super(itemView);
            tvFileName= (TextView) itemView.findViewById(R.id.directory_tv_fileName);
            btGoto= (Button) itemView.findViewById(R.id.directory_bt_goto);
        }
    }

    //数据文件的ViewHodler
    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;

        public DataViewHolder(View itemView) {
            super(itemView);
            tvFileName= (TextView) itemView.findViewById(R.id.data_tv_fileName);

        }

    }

    public interface OnItemClickListener{
        void onDirectoryItemClick(FileEntity fileEntity,int position);
        void onDirectoryItemLongClick(FileEntity fileEntity,int position);
        void onDataItemClick(FileEntity fileEntity,int position);
        void onDataItemLongClick(FileEntity fileEntity,int position);
    }


}
