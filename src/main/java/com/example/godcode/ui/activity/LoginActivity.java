package com.example.godcode.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.view.View;
import com.example.godcode.R;
import com.example.godcode.databinding.ActivityLoginBinding;
import com.example.godcode.greendao.entity.LoginResult;
import com.example.godcode.greendao.option.LoginResultOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.fragment.loginActivity.LoginSelectFragment;



public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private LoginSelectFragment loginSelectFragment;
    private FragmentManager supportFragmentManager;


    @Override
    public void init() {
        HttpUtil.getInstance().init(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        supportFragmentManager = getSupportFragmentManager();
        Presenter.getInstance().initFragmentManager(LoginActivity.this, supportFragmentManager, 0);
        LoginResult loginResult = LoginResultOption.getInstance().getLoginResult();
        if (loginResult==null) {
            loginSelectFragment = new LoginSelectFragment();
            supportFragmentManager.beginTransaction().replace(R.id.login_fragmentContainer, loginSelectFragment).commit();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            Constant.userId=loginResult.getUserId();
            Constant.payUrl=loginResult.getPayUrl();
            Constant.uniquenessToken=loginResult.getUniquenessToken();
            startActivity(intent);
        }
    }


    public void exit(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.login_fragmentContainer, loginSelectFragment).commit();
    }



}
