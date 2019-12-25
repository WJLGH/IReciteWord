package com.sdut.soft.ireciteword.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdut.soft.ireciteword.R;

import java.io.File;
import java.util.List;

public class FileChooseAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

    public FileChooseAdapter(int layoutResId, @Nullable List<File> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, File item) {
        helper.setText(R.id.file_item_name_txt, item.getName());

        double length =  item.length();
        String[] unit = new String[]{"B","K","M","G"};
        int size = 0;
        for(size = 0 ;size < unit.length;size++) {
            if(1024 > length) {
                break;
            }
            length = length/1024;
        }

        helper.setText(R.id.file_item_size_txt, String.format("%.2f %s",length,unit[size]) );

    }
}