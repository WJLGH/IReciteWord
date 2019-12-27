package com.sdut.soft.ireciteword;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdut.soft.ireciteword.activity.base.BaseActivity;

import com.sdut.soft.ireciteword.adapter.StrangeWordAdapter;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.dao.WordDao;
import com.sdut.soft.ireciteword.user.UserService;
import com.sdut.soft.ireciteword.utils.Const;
import com.sdut.soft.ireciteword.utils.MyExcelUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StrangeWord extends BaseActivity {

    private static final String TAG = "StrangWord";
    @BindView(R.id.unit_name)
    TextView unitName;
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

        UserService userService = new UserService(this);
        final String userScb = userService.getUserScb();
        unitName.setText(userScb);
        WordDao wordDao = new WordDao(this);
        final List<Word> wordList = wordDao.getAllWord(userScb);
        final StrangeWordAdapter stangeWordAdapter = new StrangeWordAdapter(R.layout.search_word_item,wordList);
        stangeWordAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                new AlertDialog.Builder(StrangeWord.this)
                        .setTitle("单词删除")
                        .setMessage("是否将单词移出生词本：")
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean ret = stangeWordAdapter.deleteWord(StrangeWord.this, position, userScb);
                                Toast.makeText(StrangeWord.this, ret?"删除成功":"删除失败"  , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create()
                        .show();
                return false;
            }
        });
        stangeWordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(StrangeWord.this, WordSpecificActivity.class);
                intent.putExtra("id", wordList.get(position).getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(stangeWordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
