package com.sdut.soft.ireciteword;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdut.soft.ireciteword.adapter.UnitAdapter;
import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.dao.UnitDao;
import com.sdut.soft.ireciteword.dao.WordDao;
import com.sdut.soft.ireciteword.utils.MyExcelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BackUpActivity extends AppCompatActivity {
    @BindView(R.id.unit_recycler)
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        UnitDao unitDao = new UnitDao(this);
        final WordDao wordDao = new WordDao(this);
        final List<Unit> units = unitDao.getUnits();
        UnitAdapter unitAdapter = new UnitAdapter(R.layout.unit_item,units);
        unitAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BackUpActivity.this);
                builder.setTitle("数据备份");
                builder.setMessage("是否将词汇表备份为excel文件");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    private static final String TAG ="单词文件" ;

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Unit unit = units.get(position);
                        String filePath = Environment.getExternalStorageDirectory() + "/AndroidExcelDemo";
                        File file = new File(filePath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }


                        String excelFileName = "/nnndemo.xls";


                        String[] title = {"单词", "音标", "解释","例句"};
                        String sheetName = "demoSheetName";


//                        List<Word>  wordList = wordDao.getAllWord(unit.getName());
                        List<Word>  wordList = wordDao.getNewWordsFromUnit(unit,22);
                        filePath = filePath + excelFileName;


                        MyExcelUtils.initExcel(filePath, sheetName, title);


                        MyExcelUtils.writeObjListToExcel(wordList, filePath, BackUpActivity.this);
                        Toast.makeText(BackUpActivity.this,"excel已导出至：" + filePath,Toast.LENGTH_SHORT).show();

                        List<Word> words = MyExcelUtils.readExcel(filePath);
                        Log.i(TAG, "onClick: "+words);
                    }
                });
                //消极的选择
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(BackUpActivity.this,"点击了取消",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();;

            }
        });
        recyclerView.setAdapter(unitAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
