package com.sdut.soft.ireciteword.adapter;

import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdut.soft.ireciteword.R;
import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.Word;

import java.util.List;

public class UnitAdapter extends BaseQuickAdapter<Unit,BaseViewHolder> {


    public UnitAdapter(int layoutResId, @Nullable List<Unit> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Unit item) {
        helper.setText(R.id.unit_name, item.getName())
                .setText(R.id.unit_cnt_txt, ""+item.getCnt())
                .setProgress(R.id.unit_progressbar,item.getProgress(),item.getCnt());

    }
}