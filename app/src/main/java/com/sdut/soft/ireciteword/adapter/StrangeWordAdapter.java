package com.sdut.soft.ireciteword.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdut.soft.ireciteword.R;
import com.sdut.soft.ireciteword.StrangeWord;
import com.sdut.soft.ireciteword.bean.Word;

import java.util.List;

public class StrangeWordAdapter extends BaseQuickAdapter<Word,BaseViewHolder> {


    public StrangeWordAdapter(int layoutResId, @Nullable List<Word> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Word item) {
        helper.setText(R.id.key, item.getKey())
                .setText(R.id.trans, item.getTrans());
    }
}
