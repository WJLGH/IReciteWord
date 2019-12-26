package com.sdut.soft.ireciteword;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdut.soft.ireciteword.bean.User;
import com.sdut.soft.ireciteword.bean.UserVo;
import com.sdut.soft.ireciteword.user.UserService;
import com.sdut.soft.ireciteword.utils.Const;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PwdActivity extends AppCompatActivity {

    @BindView(R.id.et_o_pwd)
    EditText oPwd;
    @BindView(R.id.et_o_pwd_e)
    TextView  oPwdE;
    @BindView(R.id.et_n_pwd)
    EditText nPwd;
    @BindView(R.id.et_n_pwd_e)
    TextView  nPwdE;
    @BindView(R.id.et_r_pwd)
    EditText rPwd;
    @BindView(R.id.et_r_pwd_e)
    TextView  rPwdE;
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);
        ButterKnife.bind(this);
        userService = new UserService(PwdActivity.this);
    }
    @OnClick(R.id.btn_submit)
    public void submit() {
        clearAllError();
        User currentUser = userService.currentUser();
        String oldPwd = oPwd.getText().toString();
        String newPwd = nPwd.getText().toString();
        String repeatPwd = rPwd.getText().toString();
        if(isEmpty(oldPwd) ) {
            oPwdE.setVisibility(View.VISIBLE);
            oPwdE.setText("内容不能为空");
            return ;
        }
        if(isEmpty(newPwd) ) {
            nPwdE.setVisibility(View.VISIBLE);
            nPwdE.setText("内容不能为空");
            return ;
        }
        if(isEmpty(repeatPwd) ) {
            rPwdE.setVisibility(View.VISIBLE);
            rPwdE.setText("内容不能为空");
            return ;
        }
        if(!newPwd.equals(repeatPwd)) {
            rPwdE.setVisibility(View.VISIBLE);
            rPwdE.setText("密码不一致，请重新输入");
            return;
        }
        UserVo userVo = new UserVo();
        userVo.setOldPwd(oldPwd);
        userVo.setPassword(newPwd);
        userVo.setId(currentUser.getId());
        userService.commitProgress(userVo,handler);

    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.PWD_UPDATE_SUCCESS:
                    Toast.makeText(PwdActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                break;
                case Const.PWD_UPDATE_FAILURE:
                    Toast.makeText(PwdActivity.this, "密码修改失败", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    private void clearAllError() {
        oPwdE.setVisibility(View.INVISIBLE);
        nPwdE.setVisibility(View.INVISIBLE);
        rPwdE.setVisibility(View.INVISIBLE);
    }

    private boolean isEmpty(String s) {
        if( null == s  || "".equals(s)) {
            return true;
        }
        return false;
    }
}
