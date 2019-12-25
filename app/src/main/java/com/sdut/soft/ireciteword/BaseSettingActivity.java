package com.sdut.soft.ireciteword;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdut.soft.ireciteword.adapter.DefaultUnitAdapter;
import com.sdut.soft.ireciteword.adapter.MetaOptionAdapter;
import com.sdut.soft.ireciteword.bean.MetaOption;
import com.sdut.soft.ireciteword.bean.Unit;
import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.dao.UnitDao;
import com.sdut.soft.ireciteword.user.UserService;
import com.sdut.soft.ireciteword.utils.Const;
import com.sdut.soft.ireciteword.utils.SettingUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.color.white;

public class BaseSettingActivity extends AppCompatActivity {
    private static final String TAG = "BaseSettingActivity";

    @BindView(R.id.default_unit_recycler)
   RecyclerView recyclerView;
    DefaultUnitAdapter adapter;
    @BindView(R.id.et_perday)
    EditText etPerday;
    List<Unit> units;
    int lastIndex = -1;
    Integer perDay = Const.PER_DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_setting);
        ButterKnife.bind(this);
        setToolBar();
        initView();
    }

    @OnClick(R.id.btn_save)
    public void save() {
        new AlertDialog.Builder(BaseSettingActivity.this)
                .setTitle("配置")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("是否保存配置？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!checkPerday()) {
                            finish();
                            return;
                        }
                        SettingUtils.setDefaultSearchUnit(getApplicationContext(),units.get(lastIndex).getName());
                        SettingUtils.setPerDay(getApplicationContext(),perDay);
                        finish();
                    }
                })
                .create()
                .show();
    }

    private boolean checkPerday() {
        String str = etPerday.getText().toString();
        if (str == null || "".equals(str)) {
            return false;
        }
        int t = Integer.parseInt(str);
        if (t <= 0) {
            return false;
        }
        perDay = t;
        return true;
    }

    private void initView() {

        units  = new UnitDao(this).getUnits();
        adapter = new DefaultUnitAdapter(R.layout.unit_check_item,units);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(lastIndex == -1 ) {
                    lastIndex = position;
                    units.get(position).setChecked(true);
                } else if(lastIndex != position) {
                    units.get(position).setChecked(true);
                    units.get(lastIndex).setChecked(false);
                    lastIndex = position;
                }
                adapter.notifyDataSetChanged();

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(BaseSettingActivity.this));
        recyclerView.setAdapter(adapter);
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Settings");//设置主标题
        toolbar.setTitleTextColor(getResources().getColor(white));//设置主标题颜色

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        StatusBarUtil.setColor(this, R.color.color_main_blue);
        //  set status transparent
//        StatusBarUtil.setTransparent(this);
    }

    /**
     * use back button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
