package com.sdut.soft.ireciteword;

import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdut.soft.ireciteword.adapter.FileChooseAdapter;
import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.dao.UnitDao;
import com.sdut.soft.ireciteword.dao.WordDao;
import com.sdut.soft.ireciteword.utils.Const;
import com.sdut.soft.ireciteword.utils.MyExcelUtils;
import com.sdut.soft.ireciteword.utils.SettingUtils;
import com.sdut.soft.ireciteword.utils.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecoveryActivity extends AppCompatActivity {

    private static final String TAG = "还原";

    @BindView(R.id.choose_file_recyclerview)
    RecyclerView chooseFileRecyclerView;
    @BindView(R.id.data_import_progressbar)
    ProgressBar progressBar;
    Handler recoveryHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.FAILURE:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RecoveryActivity.this, "数据导入失败", Toast.LENGTH_SHORT).show();
                    break;
                case Const.FINISH:
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RecoveryActivity.this, "数据导入成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        final WordDao wordDao = new WordDao(this);
        UnitDao unitDao = new UnitDao(this);
        List<Unit> units = unitDao.getUnits();
        List<String> uNameList = new ArrayList<>(units.size());
        for (Unit unit : units) {
            uNameList.add(unit.getName());
        }
        final ArrayAdapter<String> unitAutoAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, uNameList);

        final String filePath = Environment.getExternalStorageDirectory() + "/IReciteWordExcel";
        final File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !StringUtils.isEmpty(pathname.getName()) && pathname.getName().endsWith(".xls");
            }
        });
        final List<File> fileList = Arrays.asList(files);

        FileChooseAdapter adapter = new FileChooseAdapter(R.layout.file_item, fileList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {

                final AutoCompleteTextView et = new AutoCompleteTextView(RecoveryActivity.this);
                et.setAdapter(unitAutoAdapter);
                String fileName = fileList.get(position).getName();
                String name = fileName.substring(0, fileName.lastIndexOf(".xls"));
                et.setText(name);
                new AlertDialog.Builder(RecoveryActivity.this)
                        .setTitle("数据恢复")
                        .setMessage("导入数据到：")
                        .setView(et)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String excelPath = filePath + "/" + fileList.get(position).getName();
                                final List<Word> words = MyExcelUtils.readExcel(excelPath);
                                final String unitName = et.getText().toString().trim();
                                progressBar.setVisibility(View.VISIBLE);
                                new Thread() {
                                    @Override
                                    public void run() {
                                       try {
                                           wordDao.insertWordListToUnit(words, unitName);
                                       } catch (Exception e) {
                                           Message message = new Message();
                                            message.what = Const.FAILURE;
                                           recoveryHandler.sendMessage(message);
                                           return;
                                       }
                                        Message message = new Message();
                                        message.what = Const.FINISH;
                                        recoveryHandler.sendMessage(message);
                                    }
                                }.start();

                            }
                        })
                        .create()
                        .show();
            }
        });
        chooseFileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chooseFileRecyclerView.setAdapter(adapter);
    }
}
