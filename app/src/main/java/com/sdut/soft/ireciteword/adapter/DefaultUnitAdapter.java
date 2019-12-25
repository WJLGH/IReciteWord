package com.sdut.soft.ireciteword.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdut.soft.ireciteword.R;
import com.sdut.soft.ireciteword.bean.Unit;

import java.util.ArrayList;
import java.util.List;

public class DefaultUnitAdapter extends BaseQuickAdapter<Unit, BaseViewHolder> {

    public DefaultUnitAdapter(int layoutResId, @Nullable List<Unit> data) {

        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Unit item) {

        helper.setText(R.id.unit_name, item.getName())
                .setText(R.id.unit_cnt_txt, "" + item.getCnt())
                .setImageResource(R.id.checked,item.isChecked()? R.drawable.check:R.drawable.check_no);
//                .setProgress(R.id.unit_progressbar,item.getProgress(),item.getCnt());

    }
}