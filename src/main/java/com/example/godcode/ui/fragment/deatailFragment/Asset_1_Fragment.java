package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.R;
import com.example.godcode.bean.GroupMsg;
import com.example.godcode.bean.WebSocketNews1;
import com.example.godcode.databinding.FragmentMyassetBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.observable.EventType;
import com.example.godcode.observable.RxBus;
import com.example.godcode.observable.RxEvent;
import com.example.godcode.ui.activity.MainActivity;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.fragment.asset.Asset_DetailFragment;
import com.example.godcode.ui.fragment.asset.Asset_GroupFragment;
import com.example.godcode.utils.FormatUtil;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Asset_1_Fragment extends BaseFragment {
    private FragmentMyassetBinding binding;
    private View root;
    private String[] incomeType = {"今日", "昨日", "本周", "本月", "总"};
    private Asset_GroupFragment asset_groupFragment;
    private Asset_DetailFragment asset_detailFragment;
    private ArrayList<BaseFragment> fragments;

    public FragmentMyassetBinding getBinding() {
        return binding;
    }

    public void setBinding(FragmentMyassetBinding binding) {
        this.binding = binding;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myasset, container, false);
            binding.setFragment(this);
            binding.setIntcomeType(incomeType);
            binding.setPresenter(presenter);
            binding.asset1Toolbar.ivOption.setImageResource(R.drawable.config);
            root = binding.getRoot();
            initListener();
            initView();
            initData();
            RxBus.getInstance().toObservable(RxEvent.class).subscribe(new Observer<RxEvent>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(RxEvent rxEvent) {
                    if (rxEvent.getEventType() == EventType.EVENTTYPE_DIVIDE_MSG) {
                        WebSocketNews1.DataBean dataBean = (WebSocketNews1.DataBean) rxEvent.getBundle().getSerializable("dataBean");
                        refreshData(dataBean);
                    }
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            });

        }
        toggleFragment(index);
        return root;
    }

    private void initData() {

    }

    public void querryByStatus(int status) {
        if (index == 1) {
            asset_detailFragment.initParameter(status);
            asset_detailFragment.refreshData(1);
        }
    }

    private void initView() {
        fragments = new ArrayList<>();
        asset_groupFragment = new Asset_GroupFragment();
        asset_detailFragment = new Asset_DetailFragment();
        fragments.add(asset_groupFragment);
        fragments.add(asset_detailFragment);
    }

    public ArrayList<BaseFragment> getFragments() {
        return fragments;
    }

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void toggleFragment(int index) {
        this.index = index;
        if (index == 0) {
            selectDate(1);
            binding.asset1Toolbar.option.setVisibility(View.GONE);
        } else {
            binding.asset1Toolbar.option.setVisibility(View.VISIBLE);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.asset_pager, fragments.get(index)).commit();
    }

    private void initListener() {
        binding.asset1Toolbar.toolbar4Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyDown();
            }
        });
    }


    private int periodType;

    public int getPeriodType() {
        return periodType;
    }

    public void selectDate(int periodType) {
        this.periodType = periodType;
        binding.setSelectPosition(periodType);
        if (index == 0) {
            HttpUtil.getInstance().getGroup(periodType).subscribe(
                    groupStr -> {
                        GroupMsg groupMsg = GsonUtil.fromJson(groupStr, GroupMsg.class);
                        GroupMsg.ResultBean result = groupMsg.getResult();
                        int normalCount = result.getNormalCount();
                        int errorCount = result.getErrorCount();
                        int sumAwardCount = result.getSumAwardCount();
                        refreshAsset(result.getCoinCount(), normalCount + errorCount, normalCount, result.getErrorCount(), result.getDividedMoney(), sumAwardCount);
                        asset_groupFragment.refreshGroup(result.getData(), periodType);
                    }
            );
        } else {
            asset_detailFragment.initParameter(2);
            asset_detailFragment.refreshData(1);
        }

    }

    public void refreshAsset(double coinCount, int count, int normalCount, int errorCount, String divideMoney, int awardCount) {
        binding.setCoinCount(coinCount);
        binding.setCount(count);
        binding.setNormalCount(normalCount);
        binding.setErrorCount(errorCount);
        binding.setDivideMoney(divideMoney);
        binding.setAwardCount(awardCount);
    }


    @Override
    protected void lazyLoad() {

    }


    public void refreshData(WebSocketNews1.DataBean data) {
        if (periodType != 2) {
            double divedeMoney = data.getDivedeMoney();
            int awardCount = 0;
            if (!TextUtils.isEmpty(data.getAwardCount())) {
                awardCount = Integer.parseInt(data.getAwardCount());
            }
            int coinCount = data.getCoinCount();
            String s = binding.getDivideMoney();
            double v = Double.parseDouble(s);
            if (index == 0) {
                asset_groupFragment.refreshData(data);
                binding.setCoinCount(binding.getCoinCount() + coinCount);
                binding.setAwardCount(binding.getAwardCount() + awardCount);
                binding.setDivideMoney(FormatUtil.getInstance().get2double(v + divedeMoney));
            } else if (index == 1) {
                //改分组是否有该机器
                HashMap<String, Integer> assetMap = asset_detailFragment.getAssetMap();
                LogUtil.log(assetMap+"==============gfd================"+assetMap.containsKey(data.getProductNumber()));
                if (assetMap != null && assetMap.containsKey(data.getProductNumber())) {
                    binding.setCoinCount(binding.getCoinCount() + coinCount);
                    binding.setAwardCount(binding.getAwardCount() + awardCount);
                    binding.setDivideMoney(FormatUtil.getInstance().get2double(v + divedeMoney));
                    asset_detailFragment.refreshDivide(data);
                }
            }
        }
    }


    @Override
    public void onKeyDown() {
        if (index == 0) {
            presenter.back();
        } else if (index == 1) {
            toggleFragment(0);
        }
    }

    public void setTitle(String title) {
        binding.asset1Toolbar.title.setText(title);
    }


}
