package com.example.godcode.ui.fragment.loginActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.GetVerification;
import com.example.godcode.bean.YZM;
import com.example.godcode.databinding.FragmentResetpwdBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.utils.GsonUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ResetPwd extends BaseFragment {

    private FragmentResetpwdBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_resetpwd, container, false);
            binding.setPresenter(presenter);
            initListener();
        }
        return binding.getRoot();
    }


    private void initListener() {


        binding.getYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.account.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(activity, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                GetVerification getVerification = new GetVerification();
                if (phoneNumber.contains("@")) {
                    getVerification.setEmailAddress(phoneNumber);
                } else {
                    getVerification.setPhoneNumber(phoneNumber);
                }
                getVerification.setType("2");
                HttpUtil.getInstance().getVerificationCode(getVerification).subscribe(
                        yzmStr -> {
                            YZM yzm = GsonUtil.getInstance().fromJson(yzmStr, YZM.class);
                            String result = yzm.getResult();
                            if (result.equals("OK")||result.equals("True")) {
                                binding.getYzm.setEnabled(false);
                                yzmCountDownTime(binding.getYzm);
                            } else {
                                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });

        binding.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.account.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String code = binding.yzm.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(getContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwd = binding.password.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                HttpUtil.getInstance().resetPwd(phone, code, pwd).subscribe(
                        str -> {
                            Toast.makeText(getContext(), "密码重置成功", Toast.LENGTH_SHORT).show();
                            Presenter.getInstance().back();
                        }
                );


            }
        });


    }


    public void yzmCountDownTime(TextView tv) {
        Observable.interval(0, 1, TimeUnit.SECONDS, Schedulers.io())
                .take(60).observeOn(AndroidSchedulers.mainThread()).map(new Function<Long, Long>() {
            @Override
            public Long apply(Long aLong) throws Exception {
                return 59 - aLong;
            }
        }).subscribe(
                time -> {
                    tv.setText(time + "s");
                    if (time == 0) {
                        tv.setEnabled(true);
                        tv.setText("");
                    }
                }
        );
    }


    @Override
    protected void lazyLoad() {

    }

}
