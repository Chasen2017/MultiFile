package com.example.administrator.multifile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by 东 on 2016/12/11.
 */

public class FileFragment extends Fragment implements View.OnClickListener {

    View rootView;

    private TextView mTvCurrentDirectory;//显示当前目录
    private RecyclerView mRvShow;//用于展示文件列表
    private Button mBtAddDirectory;//添加目录文件
    private Button mBtAddData;//添加数据文件
//    private Button mBtSort;//排序

    private ArrayList<FileEntity> mFileList;
    private String mCurrentDirectory;

    private FileAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_file, container, false);
        //初始化控件
        initView();
        //获取传递过来的文件列表和当前目录
        Bundle bundle = getArguments();
        mFileList = bundle.getParcelableArrayList("FileList");
        mCurrentDirectory = bundle.getString("CurrentDirectory");
        mTvCurrentDirectory.setText(mCurrentDirectory);

        mAdapter = new FileAdapter(mFileList);
        mRvShow.setAdapter(mAdapter);
        mRvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onDirectoryItemClick(FileEntity fileEntity, int position) {
                //点击事件，进入到子目录
                EventBus.getDefault().post(fileEntity);
            }

            @Override
            public void onDirectoryItemLongClick(final FileEntity fileEntity, final int position) {
                //长按事件，选项：重命名，删除
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String[] items = new String[]{"删除", "重命名"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            //删除文件夹
                            deleteDirectoryOrData(fileEntity, position);
                        } else if (i == 1) {
                            //重命名文件夹
                            changeName(fileEntity, "重命名该文件夹");
                        }
                    }
                });
                builder.create().show();

            }

            //默认点击一下的话，就是写操作了
            @Override
            public void onDataItemClick(FileEntity fileEntity, int position) {
                writeData(fileEntity);
            }

            @Override
            public void onDataItemLongClick(final FileEntity fileEntity, final int position) {
                //长按事件，选项：重命名，删除
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String[] items = new String[]{"删除", "重命名", "读", "写"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            //删除文件夹
                            deleteDirectoryOrData(fileEntity, position);
                        } else if (i == 1) {
                            //重命名文件夹
                            changeName(fileEntity, "重命名该文件");
                        } else if (i == 2) {
                            //读文件
                            readData(fileEntity);
                        } else if (i == 3) {
                            //写文件
                            writeData(fileEntity);
                        }
                    }
                });
                builder.create().show();
            }
        });

        return rootView;

    }


    public static FileFragment newInstance(ArrayList<FileEntity> fileList, String currentDirectory) {
        FileFragment fragment = new FileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("CurrentDirectory", currentDirectory);
        bundle.putParcelableArrayList("FileList", fileList);
        fragment.setArguments(bundle);
        return fragment;
    }


    private void initView() {
        mRvShow = (RecyclerView) rootView.findViewById(R.id.file_rv_show);
        mBtAddDirectory = (Button) rootView.findViewById(R.id.file_bt_add_directory);
        mBtAddData = (Button) rootView.findViewById(R.id.file_bt_add_data);
//        mBtSort = (Button) rootView.findViewById(R.id.file_bt_sort);
        mTvCurrentDirectory = (TextView) rootView.findViewById(R.id.file_tv_current_directory);

        mBtAddDirectory.setOnClickListener(this);
        mBtAddData.setOnClickListener(this);
//        mBtSort.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.file_bt_add_directory:
                createNewDirectory();
                break;
            case R.id.file_bt_add_data:
                createNewData();
                break;
//            case R.id.file_bt_sort:
//                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    //新建文件
    private void createNewData() {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        View view_dialog1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_data, null);
        builder1.setView(view_dialog1);
        final AlertDialog dialog1 = builder1.create();
        dialog1.show();

        Button btCancel1 = (Button) view_dialog1.findViewById(R.id.add_data_bt_cancel);
        Button btSure1 = (Button) view_dialog1.findViewById(R.id.add_data_bt_sure);
        final EditText etFileName1 = (EditText) view_dialog1.findViewById(R.id.add_data_et_fileName);
        final EditText etMaxLength = (EditText) view_dialog1.findViewById(R.id.add_data_et_maxLength);
        btCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        btSure1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新建的是数据文件，所以isDirectory设置为false
                String fileName = etFileName1.getText().toString();
                if (fileName.equals("")) {
                    Toast.makeText(getActivity(), "请输入文件的名字", Toast.LENGTH_SHORT).show();
                } else
                    //如果已经存在
                    if (isDataExist(fileName)) {
                        Toast.makeText(getActivity(), "已经有相同命名的文件存在", Toast.LENGTH_SHORT).show();
                    } else {
                        FileEntity fileEntity = new FileEntity(fileName, false);
                        String maxLength = etMaxLength.getText().toString();
                        if (maxLength.equals("")) {
                            fileEntity.setMaxLength(100);
                        } else {
                            fileEntity.setMaxLength(Integer.parseInt(maxLength));
                        }

                        mFileList.add(fileEntity);
                        mAdapter.notifyDataSetChanged();
                        dialog1.dismiss();
                    }

            }
        });
    }


    //新建文件夹
    private void createNewDirectory() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view_dialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_directory, null);
        builder.setView(view_dialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button btCancel = (Button) view_dialog.findViewById(R.id.add_directory_bt_cancel);
        Button btSure = (Button) view_dialog.findViewById(R.id.add_directory_bt_sure);
        final EditText etFileName = (EditText) view_dialog.findViewById(R.id.add_directory_et_fileName);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = etFileName.getText().toString();
                if (isDirectoryExist(fileName)) {
                    Toast.makeText(getActivity(), "文件夹已经存在", Toast.LENGTH_SHORT).show();
                } else {
                    //新建的是目录文件，所以isDirectory设置为true
                    FileEntity fileEntity = new FileEntity(fileName, true);
                    mFileList.add(fileEntity);
                    mAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });
    }

    //删除文件
    private void deleteDirectoryOrData(FileEntity fileEntity, int position) {
        /**
         * 这里不考虑文件夹是否为空的情况:如果是目录文件,要判断子目录是否为空,若子目录为空，直接删除，不为空的话
         *
         * 直接进行删除
         */

        mFileList.remove(position);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "删除文件成功", Toast.LENGTH_SHORT).show();

    }

    //修改目录文件或者数据文件的名字
    private void changeName(final FileEntity fileEntity, String title) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        View view_dialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_directory, null);
        builder1.setView(view_dialog);
        final AlertDialog dialog = builder1.create();

        TextView tv = (TextView) view_dialog.findViewById(R.id.add_directory_tv_title);
        final EditText edFileName = (EditText) view_dialog.findViewById(R.id.add_directory_et_fileName);
        Button btCancel = (Button) view_dialog.findViewById(R.id.add_directory_bt_cancel);
        Button btSure = (Button) view_dialog.findViewById(R.id.add_directory_bt_sure);

        tv.setText(title);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newFileName = edFileName.getText().toString();
                fileEntity.setName(newFileName);
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //写文件
    private void writeData(final FileEntity fileEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view_dialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_read_or_write, null);
        builder.setView(view_dialog);
        final AlertDialog dialog = builder.create();

        final EditText editText = (EditText) view_dialog.findViewById(R.id.read_edittext);
        Button btCancel = (Button) view_dialog.findViewById(R.id.read_bt_cancel);
        Button btSure = (Button) view_dialog.findViewById(R.id.read_bt_sure);

        //设置可编辑的最大长度，模拟文件的最大长度
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(fileEntity.getMaxLength())});

        editText.setText(fileEntity.getContent());
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newContent = editText.getText().toString();
                fileEntity.setContent(newContent);
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    //读文件,直接显示文件内容，并且无法对文件进行任何操作
    private void readData(FileEntity fileEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view_dialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_read_or_write, null);
        builder.setView(view_dialog);
        final AlertDialog dialog = builder.create();

        EditText editText = (EditText) view_dialog.findViewById(R.id.read_edittext);
        Button btCancel = (Button) view_dialog.findViewById(R.id.read_bt_cancel);
        Button btSure = (Button) view_dialog.findViewById(R.id.read_bt_sure);

        //设置edtiText不可编辑:
        //其实只需一行代码就能搞定et.setKeyListener(null);
        //注意, 这里不是setOnKeyListener, 而是setKeyListener. 此方法是TextView的成员, 调用后的效果完全符合预期, 并且获得焦点后不会弹出输入法.
//                editText.setFocusable(false);
        editText.setKeyListener(null);
        editText.setText(fileEntity.getContent());

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //判断该目录是否重复出现
    private boolean isDirectoryExist(String fileName) {
        for (int i = 0; i < mFileList.size(); i++) {
            if (mFileList.get(i).isDirectory() && mFileList.get(i).getName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    //判断该数据文件是否重复出现
    private boolean isDataExist(String fileName) {
        for (int i = 0; i < mFileList.size(); i++) {
            if (!mFileList.get(i).isDirectory() && mFileList.get(i).getName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }
}
