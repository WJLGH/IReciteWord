package com.sdut.soft.ireciteword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.sdut.soft.ireciteword.activity.base.BaseActivity;

import com.sdut.soft.ireciteword.adapter.StrangeWordAdapter;
import com.sdut.soft.ireciteword.bean.Word;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StrangeWord extends BaseActivity {

    @BindView(R.id.word_recycler_view)
     RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strange_word);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

    }

    private void fillView(List<Word> data
    ) {
        StrangeWordAdapter stangeWordAdapter = new StrangeWordAdapter(R.id.word_recycler_view,data);
        recyclerView.setAdapter(stangeWordAdapter);
    }
}
