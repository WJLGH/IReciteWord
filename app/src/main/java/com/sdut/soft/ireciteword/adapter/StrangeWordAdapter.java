package com.sdut.soft.ireciteword.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdut.soft.ireciteword.R;
import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.dao.WordDao;

import java.util.List;

public class StrangeWordAdapter extends BaseQuickAdapter<Word,BaseViewHolder> {


    private final List<Word> list;


    public StrangeWordAdapter(int layoutResId, @Nullable List<Word> data) {
        super(layoutResId, data);
        this.list = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, Word item) {
        helper.setText(R.id.key, item.getKey())
                .setText(R.id.trans, item.getTrans());
    }

    public boolean deleteWord(Context context, int position, String userScb) {
        WordDao wordDao = new WordDao(context);
        Unit unit = new Unit(userScb);
        boolean ret =  wordDao.deleteWordFromUnit(list.get(position),unit);
        if (ret) {
            list.remove(position);
            notifyDataSetChanged();
        }
        return ret;
    }
}
