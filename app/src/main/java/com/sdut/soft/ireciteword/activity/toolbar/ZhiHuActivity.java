package com.sdut.soft.ireciteword.activity.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sdut.soft.ireciteword.AboutActivity;
import com.sdut.soft.ireciteword.R;
import com.sdut.soft.ireciteword.activity.base.BaseActivity;

/**
 * 仿知乎主界面Toolbar的应用
 *
 * @author Clock
 * @since 2016-02-18
 */
public class ZhiHuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_hu);

        setToolBar();

    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_drawer_home);

        toolbar.setTitle(R.string.home_page);
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

                } else if (menuItemId == R.id.action_about) {
                    // TODO  goto About Activity
                    //  Toast.makeText(ToolBarActivity.this, R.string.item_02, Toast.LENGTH_SHORT).show();
                    gotoAbout();
                }
                return true;
            }
        });
    }


    public void gotoAbout() {
        Intent intent;
        intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }


}
