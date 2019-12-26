package com.sdut.soft.ireciteword;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.sdut.soft.ireciteword.activity.base.BaseActivity;
import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.user.UserService;
import com.sdut.soft.ireciteword.utils.Const;
import com.sdut.soft.ireciteword.utils.DBFileUtils;
import com.sdut.soft.ireciteword.utils.PermissionsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};
    MediaPlayer mediaPlayer;


    @BindView(R.id.et_user)
    EditText etUser;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initAPP();
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void initAPP() {
        DBFileUtils.copyDBData(this);

        PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
            @Override
            public void passPermissons() {
//                Toast.makeText(MainActivity.this, "权限通过，可以开始背诵单词了!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void forbitPermissons() {
                Toast.makeText(MainActivity.this, "本应用必须获得相应权限才能启动，请设置!", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        };
        //请求应用权限
        PermissionsUtils.showSystemSetting = true;//是否支持显示系统设置权限设置窗口跳转
        //这里的this不是上下文，是Activity对象！
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);

    }

    private void initView() {
        userService = new UserService(this);
        if (checkIsLogin()) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkIsLogin() {
        return userService.currentUser() != null;
    }

    @OnClick(R.id.btn_login)
    public void login() {
        User u = new User(etUser.getText().toString(), etPwd.getText().toString());
        if (userService.login(u) != null) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            userService.saveUser(u);
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_register)
    public void register() {
        String name = etUser.getText().toString();
        String password = etPwd.getText().toString();
        int perDay = Const.PER_DAY;

        User u = new User(null, name, password, 0L, 0L, perDay);
        UserService userService = new UserService(this);
        if (userService.login(u) != null) {
            Toast.makeText(this, "该用户已注册，请登录！！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userService.register(u)) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isExit;

    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                this.finish();
                System.exit(0);

            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit= false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void webView() {
        WebView webView = null;
        webView.getSettings().setJavaScriptEnabled(true);//getSettiongs()用于设置一些浏览器属性，这里让WebView支持JavaScript脚本
        webView.setWebViewClient(new WebViewClient());//当需要从一个网页跳转到另一个网页是，希望目标网页仍然在当前WebView显示，而不是打开浏览器
        webView.loadUrl("http://www.baidu.com");
    }


}
