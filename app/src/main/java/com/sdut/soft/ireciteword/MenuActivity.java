package com.sdut.soft.ireciteword;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sdut.soft.ireciteword.adapter.FragmentAdapter;
import com.sdut.soft.ireciteword.fragment.ReciteFragment;
import com.sdut.soft.ireciteword.fragment.SearchFragment;
import com.sdut.soft.ireciteword.fragment.SettingsFragment;
import com.sdut.soft.ireciteword.fragment.TranslateFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity {
    @BindView(R.id.tabs)
    TabLayout mTabLayout ;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        initViewPager();
    }
    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("词典");
        titles.add("学习");
        titles.add("翻译");
        titles.add("工具");


        List<Fragment> fragments = new ArrayList<>();
        for (String title : titles) {
            mTabLayout.addTab(mTabLayout.newTab().setText(title));

        }
        fragments.add(new SearchFragment());
        fragments.add(new ReciteFragment());
        fragments.add(new TranslateFragment());
        fragments.add(new SettingsFragment());

        FragmentAdapter mFragmentAdapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mFragmentAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapter);
    }
}
