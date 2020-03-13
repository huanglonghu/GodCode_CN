package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.godcode.R;
import com.example.godcode.databinding.FragmentTxBinding;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.view.PsdPopupWindow;
import java.util.ArrayList;

public class TxFragment extends BaseFragment {

    private FragmentTxBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tx, container, false);
            view = binding.getRoot();
            binding.txToolbar.title.setText("提现");
            initView();
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.txToolbar.toolbar3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyDown();
            }
        });
    }


    private ArrayList<BaseFragment> fragments = new ArrayList<>();


    private String bank;
    private double money;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }


    public void initView() {
        Tx_firstFragment tx_firstFragment = new Tx_firstFragment();
        TxSuccessFragment txSuccessFragment = new TxSuccessFragment();
        fragments.add(tx_firstFragment);
        fragments.add(txSuccessFragment);
        toggle(0);
    }

    private int index;

    public void toggle(int index) {
        this.index = index;
        getChildFragmentManager().beginTransaction().replace(R.id.tx_container, fragments.get(index)).commit();
    }

    @Override
    protected void lazyLoad() {
    }


    @Override
    public void onKeyDown() {
        if (PsdPopupWindow.getInstance(activity) != null && PsdPopupWindow.getInstance(activity).isShowing()) {
            PsdPopupWindow.getInstance(activity).exit();
        } else {
            presenter.back();
        }
    }


}
