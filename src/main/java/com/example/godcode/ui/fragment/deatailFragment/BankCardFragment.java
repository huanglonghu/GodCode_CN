package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.BankCard;
import com.example.godcode.bean.BindBankCard;
import com.example.godcode.constant.Constant;
import com.example.godcode.databinding.FragmentBankcardBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.ClickSureListener;
import com.example.godcode.interface_.EtStrategy;
import com.example.godcode.observable.RxBus;
import com.example.godcode.observable.RxEvent;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.adapter.BankCardListAdaPter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.fragment.pwd.CheckPayPsdFragment;
import com.example.godcode.ui.fragment.pwd.SetPayPsdFragment;
import com.example.godcode.ui.view.widget.BankConfigDialog;
import com.example.godcode.ui.view.widget.EtItemDialog;
import com.example.godcode.utils.PayPwdSetting;
import com.example.godcode.utils.StringUtil;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BankCardFragment extends BaseFragment {
    private FragmentBankcardBinding binding;
    private View view;
    private List<BankCard.ResultBean> result;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initData();
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bankcard, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            String title = StringUtil.getString(activity, R.string.bankCard);
            binding.bankcardToolbar.title.setText(title);
            initView();
            lazyLoad();
            initListener();
        }
        return view;
    }

    private void initData() {
        querryBankCard();
    }

    private void querryBankCard() {
        HttpUtil.getInstance().getBankCardsByUserID(Constant.userId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                bankCardStr -> {
                    BankCard bankCard = new Gson().fromJson(bankCardStr, BankCard.class);
                    result = bankCard.getResult();
                    BankCardListAdaPter bankCardListAdaPter = new BankCardListAdaPter(activity, result, this);
                    binding.lvBankcard.setAdapter(bankCardListAdaPter);
                }, throwable -> {
                }
        );
    }

    private void initListener() {
        binding.addBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证是否有支付密码
                PayPwdSetting.getInstance().verifyPwd(new ClickSureListener() {
                    @Override
                    public void isPwdExit(boolean isPwdExit) {
                        if (isPwdExit) {
                            CheckPayPsdFragment checkPayPsdFragment = new CheckPayPsdFragment();
                            presenter.step2Fragment(checkPayPsdFragment, "checkPsd");
                        }
                    }
                });
            }
        });


        RxBus.getInstance().toObservable(RxEvent.class).subscribe(new Observer<RxEvent>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(RxEvent rxEvent) {
                //处理事件
                if (rxEvent.getEventType() == 2) {
                    AddBankCardFragment addBankCardFragment = new AddBankCardFragment();
                    presenter.step2Fragment(addBankCardFragment);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    public void initView() {
    }

    @Override
    protected void lazyLoad() {

    }


    public void refreshData() {
        querryBankCard();
    }


    public void clickBank(int bindType, int position) {
        switch (bindType) {
            case 1://待审核
                break;
            case 2://已审核
                EtItemDialog etItemDialog = new EtItemDialog.Builder().
                        context(activity).
                        etStragety(new EtBankCardStrategy()).
                        title("请输入该银行卡绑定手机号收取的信息金额").
                        hint("金额").
                        position(position).
                        type(2).
                        build();
                etItemDialog.show();
                break;
            case 3://已绑定
                deleteBank(position);
                break;
            case 4://审核失败
                //点击重新添加
                deleteBank(position);
                break;
        }

    }

    private void deleteBank(int position) {
        BankCard.ResultBean resultBean = result.get(position);
        BankConfigDialog deleteBank = new BankConfigDialog(activity, resultBean, this);
        deleteBank.show();
    }


    public class EtBankCardStrategy extends EtStrategy {

        @Override
        public void etComplete(String str, int position) {
            double value = Double.parseDouble(str);
            BindBankCard bindBankCard = new BindBankCard();
            bindBankCard.setMoney(value);
            bindBankCard.setBankCardId(result.get(position).getId());
            HttpUtil.getInstance().bindBankCard(bindBankCard).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    bindBankCardStr -> {
                        Toast.makeText(activity, "绑定成功", Toast.LENGTH_SHORT).show();
                        querryBankCard();
                    }
            );
        }
    }
}
