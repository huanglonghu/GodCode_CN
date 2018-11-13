package com.example.godcode.ui.fragment.loginActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.GetVerification;
import com.example.godcode.bean.LoginBody;
import com.example.godcode.bean.LoginResponse;
import com.example.godcode.bean.RegisterBody;
import com.example.godcode.bean.YZM;
import com.example.godcode.databinding.FragmentRegisterBinding;
import com.example.godcode.greendao.entity.LoginResult;
import com.example.godcode.greendao.option.LoginResultOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.activity.MainActivity;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.SharepreferenceUtil;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class RegisterFragment extends BaseFragment {

    private FragmentRegisterBinding binding;
    private RegisterBody registerBody;
    private boolean isEmail;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
            binding.setFragment(this);
            binding.setIsEmail(isEmail);
            binding.setPresenter(presenter);
            initListener();
        }
        binding.setRegisterBody(registerBody);
        return binding.getRoot();
    }


    private void initListener() {

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        binding.cbEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isEmail = isChecked;
                binding.setIsEmail(isEmail);
            }
        });
    }

    public void getYzCode() {
        String phoneNumber = registerBody.getPhoneNumber();
        if (isEmail) {
            if (TextUtils.isEmpty(registerBody.getEmailAddress())) {
                Toast.makeText(activity, "请输入邮箱", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (!registerBody.getEmailAddress().contains("@")) {
                    Toast.makeText(activity, "邮箱格式有误", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        if (TextUtils.isEmpty(registerBody.getPhoneNumber())) {
            Toast.makeText(activity, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        GetVerification getVerification = new GetVerification();
        getVerification.setType("1");
        if (isEmail) {
            getVerification.setEmailAddress(registerBody.getEmailAddress());
        }
        getVerification.setPhoneNumber(phoneNumber);
        HttpUtil.getInstance().getVerificationCode(getVerification).subscribe(
                yzmStr -> {
                    YZM yzm = GsonUtil.getInstance().fromJson(yzmStr, YZM.class);
                    String result = yzm.getResult();
                    if (result.equals("OK")) {
                        yzmCountDoownTime();
                        binding.yzm2.setEnabled(false);
                    } else if (result.equals("触发小时级流控Permits:5")) {
                        Toast.makeText(activity, "一小时内只能发5次验证码", Toast.LENGTH_SHORT).show();
                        binding.yzm2.setEnabled(false);
                    } else if (result.equals("触发天级流控Permits:10")) {
                        Toast.makeText(activity, "一天内只能发10次验证码", Toast.LENGTH_SHORT).show();
                        binding.yzm2.setEnabled(false);
                    } else if (result.equals("True")) {
                        LogUtil.log("----------YZ-------" + result);
                        yzmCountDoownTime();
                        binding.yzm1.setEnabled(false);
                    }
                }
        );

    }


    public void yzmCountDoownTime() {
        Observable.interval(0, 1, TimeUnit.SECONDS, Schedulers.io())
                .take(60).observeOn(AndroidSchedulers.mainThread()).map(new Function<Long, Long>() {
            @Override
            public Long apply(Long aLong) throws Exception {
                return 59 - aLong;
            }
        }).subscribe(
                time -> {
                    if (isEmail) {
                        binding.yzm1.setText(time + "秒后重发");
                    } else {
                        binding.yzm2.setText(time + "秒后重发");
                    }

                    if (time == 0) {
                        if (isEmail) {
                            binding.yzm1.setEnabled(true);
                            binding.yzm1.setText("获取验证码");
                        } else {
                            binding.yzm2.setEnabled(true);
                            binding.yzm2.setText("获取验证码");
                        }

                    }
                }
        );
    }


    public void register() {
        if (isEmail) {
            if (TextUtils.isEmpty(registerBody.getEmailAddress())) {
                Toast.makeText(activity, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (TextUtils.isEmpty(registerBody.getPhoneNumber())) {
            Toast.makeText(activity, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(registerBody.getVerificationCode())) {
            Toast.makeText(activity, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(registerBody.getNickName())) {
            registerBody.setNickName(registerBody.getPhoneNumber());
        }
        HttpUtil.getInstance().register(registerBody).subscribe(
                registerStr -> {
                    Toast.makeText(activity, "注册成功", Toast.LENGTH_SHORT).show();
                    LogUtil.log("-------openId------" + registerBody.getOpenID());
                    if (registerBody.getOpenID() == null) {
                        LoginFragment loginFragment = new LoginFragment();
                        presenter.step2Fragment(loginFragment, "login");
                    } else {
                        LoginBody loginBody = new LoginBody();
                        loginBody.setOpenID(registerBody.getOpenID());
                        loginBody.setDeviceToken(Constant.diviceToken);
                        HttpUtil.getInstance().login(loginBody).subscribe(
                                loginStr -> {
                                    LoginResponse loginResponse = new Gson().fromJson(loginStr, LoginResponse.class);
                                    LoginResponse.ResultBean result = loginResponse.getResult();
                                    LoginResult loginResult = new LoginResult(Constant.uniquenessToken, result.getPayServerUrl(), result.getUserId());
                                    Constant.uniquenessToken = result.getUniquenessToken();
                                    loginResult.setUniquenessToken(Constant.uniquenessToken);
                                    LoginResultOption.getInstance().insertLoginResult(loginResult);
                                    Constant.userId = result.getUserId();
                                    Constant.expireInSeconds = result.getExpireInSeconds();
                                    long time = System.currentTimeMillis();
                                    SharepreferenceUtil.getInstance().saveAccessToken(result.getAccessToken(), Constant.uniquenessToken, time);
                                    Constant.payUrl = result.getPayServerUrl();
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    activity.startActivity(intent);
                                }
                        );
                    }
                }
        );
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    public void refreshData() {
    }

    public void initData(RegisterBody registerBody) {
        this.registerBody = registerBody;
    }


}
