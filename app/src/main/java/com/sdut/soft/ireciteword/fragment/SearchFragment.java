package com.sdut.soft.ireciteword.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdut.soft.ireciteword.AboutActivity;
import com.sdut.soft.ireciteword.BaseSettingActivity;
import com.sdut.soft.ireciteword.R;
import com.sdut.soft.ireciteword.WordSpecificActivity;
import com.sdut.soft.ireciteword.adapter.SearchWordAdapter;
import com.sdut.soft.ireciteword.bean.Word;
import com.sdut.soft.ireciteword.searchWord.SearchWordService;
import com.sdut.soft.ireciteword.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * add toolbar
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "查词";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.et_word)
    EditText etWord;
    //todo  add toolbar,tvTitle
    @BindView(R.id.toolbar_menu)
    Toolbar toolbar;
    @BindView(R.id.tv_tb_title)
    TextView tvTitle;


    SearchWordService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MenuActivity activity = (MenuActivity)getActivity();
//        activity.mTvTitle.setText("Search");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
      //  setToolBar();
        tvTitle.setText("Search");
        initView();
        return view;
    }

    private void initView() {
        service = new SearchWordService(getContext());
    }

    @OnClick(R.id.btn_search)
    public void searchWord() {
        String key = etWord.getText().toString().trim();
        if(StringUtils.isEmpty(key)) {
            Toast.makeText(getContext(), "输入单词不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final List<Word> words = service.findWords(key);
        Log.i(TAG, "searchWord: " + words);
        SearchWordAdapter adapter = new SearchWordAdapter(R.layout.search_word_item, words);
        // TODO add searchHistory

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), WordSpecificActivity.class);
                intent.putExtra("id", words.get(position).getId());
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    /**
     * TODO 添加 toolbar 重新布局 测试一
     * 设置toolbar
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
                    Log.i(TAG, "onMenuItemClick: searchItem======================");
                    SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                    //设置搜索栏的默认提示
                    searchView.setQueryHint("请输入商品名称");

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
                        public boolean onQueryTextSubmit(String query) {
                            if (TextUtils.isEmpty(query)) {
                                Toast.makeText(getActivity(), "请输入查找内容111！==========", Toast.LENGTH_SHORT).show();
                                Log.i("search", "onQueryTextSubmit: 请输入查找内容111！");
                            } else {
                                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                                Log.i("search", "onQueryTextSubmit: ========" + query);
                            }
                            return true;
                        }

                        //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
                        public boolean onQueryTextChange(String newText) {
                            if (TextUtils.isEmpty(newText)) {
                                Toast.makeText(getActivity(), "请输入查找内容222！", Toast.LENGTH_SHORT).show();
                                Log.i("search", "onQueryTextSubmit: " + newText);

                            } else {
                                Toast.makeText(getActivity(), newText, Toast.LENGTH_SHORT).show();
                                Log.i("search", "onQueryTextSubmit: " + newText);


                            }
                            return true;
                        }
                    });


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
     * 页面跳转， 前往 About页面
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
