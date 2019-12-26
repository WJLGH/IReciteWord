package com.sdut.soft.ireciteword.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sdut.soft.ireciteword.AboutActivity;
import com.sdut.soft.ireciteword.BaseSettingActivity;
import com.sdut.soft.ireciteword.R;
import com.sdut.soft.ireciteword.translate.YouDaoTranslateService;
import com.sdut.soft.ireciteword.utils.Const;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * add toolbar
 */
public class TranslateFragment extends android.support.v4.app.Fragment {
    @BindView(R.id.et_src)
    EditText eTSrc;
    @BindView(R.id.tv_tgt)
    TextView tvTgt;
    //todo  add toolbar,tvTitle
    @BindView(R.id.toolbar_menu)
    Toolbar toolbar;
    @BindView(R.id.tv_tb_title)
    TextView tvTitle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);
        ButterKnife.bind(this, view);
       // setToolBar();
        initView();
        return view;
    }

    private void initView() {

        tvTitle.setText("Translation");
    }

    @OnClick(R.id.btn_search)
    public void search() {
        YouDaoTranslateService.translateString(eTSrc.getText().toString().trim(), handler);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const
                        .TRA_SUCCESS:
                    tvTgt.setText((String) msg.obj);
                    tvTgt.setTextColor(Color.BLACK);
                    break;
                case Const.TRA_FAIL:
                    tvTgt.setText((String) msg.obj);
                    tvTgt.setTextColor(Color.RED);
                    break;
            }
        }
    };

    /**
     * TODO 添加 toolbar 重新布局 测试一
     *  设置toolbar
     */
    private void setToolBar() {

        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);
        toolbar.setTitle("");
        tvTitle.setText("Search");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        toolbar.inflateMenu(R.menu.zhihu_toolbar_menu); // 关联 mmenu 菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_search) {
                    // TODO  跳转到 search 界面
                    // Toast.makeText(ToolBarActivity.this, R.string.menu_search, Toast.LENGTH_SHORT).show();

                } else if (menuItemId == R.id.action_settings) {
                    //  TODO  goto Settings
                    // Toast.makeText(ToolBarActivity.this, R.string.item_01, Toast.LENGTH_SHORT).show();
                    gotoBaseSettings();
                } else if (menuItemId == R.id.action_about) {
                    // TODO  goto About Activity
                    //  Toast.makeText(ToolBarActivity.this, R.string.item_02, Toast.LENGTH_SHORT).show();
                    gotoAbout();
                }
                return true;
            }
        });
    }

    /**
     *  页面跳转， 前往 About页面
     */
    public void gotoAbout() {
        Intent intent;
        intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
    }
    public void gotoBaseSettings() {
        Intent intent;
        intent = new Intent(getActivity(), BaseSettingActivity.class);
        startActivity(intent);
    }


}
