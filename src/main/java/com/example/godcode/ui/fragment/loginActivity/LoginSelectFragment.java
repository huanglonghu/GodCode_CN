package com.example.godcode.ui.fragment.loginActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.R;
import com.example.godcode.bean.RegisterBody;
import com.example.godcode.databinding.FragmentLoginselectBinding;
import com.example.godcode.ui.base.BaseFragment;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class LoginSelectFragment extends BaseFragment {

    private FragmentLoginselectBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loginselect, container, false);
            binding.setPresenter(presenter);
            initListener();
        }
        return binding.getRoot();
    }

    private void initListener() {
        binding.zhdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                presenter.step2Fragment(loginFragment, "login");
            }
        });

        binding.wxdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wxdl();
            }
        });

        binding.step2regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                RegisterBody registerBody = new RegisterBody();
                registerFragment.initData(registerBody);
                presenter.step2Fragment(registerFragment,"register");
            }
        });

    }

    public void wxdl() {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, "wxf145d2ce67346702", true);
        boolean a = msgApi.registerApp("wxf145d2ce67346702");
        SendAuth.Req req = new SendAuth.Req();
        //授权域 获取用户个人信息则填写snsapi_userinfo
        req.scope = "snsapi_userinfo";
        //用于保持请求和回调的状态 可以任意填写
        req.state = "test_login";
        msgApi.sendReq(req);
    }

    @Override
    protected void lazyLoad() {

    }

}
