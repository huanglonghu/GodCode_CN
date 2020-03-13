package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.godcode.R;
import com.example.godcode.bean.MyAssetList;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.ItemLvMyassetBinding;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.constant.Constant;
import com.example.godcode.utils.FormatUtil;
import com.example.godcode.utils.LogUtil;

import java.util.HashMap;
import java.util.List;

public class AssetListAdapter extends BaseAdapter {

    private HashMap<Integer, View> viewMap = new HashMap<>();
    private LayoutInflater layoutInflater;
    private List<MyAssetList.ResultBean.DataBean> assetList;
    private Context context;
    private HashMap<Integer, Integer> categoryMap;
    private String[] incomeType = {"今日", "昨日", "本周", "本月", "总"};


    public AssetListAdapter(Context context, List<MyAssetList.ResultBean.DataBean> assetList, HashMap<Integer, Integer> categoryMap) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.assetList = assetList;
        this.categoryMap = categoryMap;
    }

    @Override
    public int getCount() {
        return assetList == null ? 0 : assetList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemLvMyassetBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lv_myasset, parent, false);
            binding.setPresenter(Presenter.getInstance());
            MyAssetList.ResultBean.DataBean dataBean = assetList.get(position);
            if (dataBean.getFK_UserID() == Constant.userId) {
                binding.setIsMaster(true);
            } else {
                binding.setIsMaster(false);
            }
            String productImgUrl = dataBean.getProductImgUrl();
            if (!TextUtils.isEmpty(productImgUrl)) {
                if (!productImgUrl.contains("http")) {
                    productImgUrl = Constant.baseUrl + productImgUrl;
                }
                RxImageLoader.with(context).load(productImgUrl).into(binding.ivZc);
            }
            initData(binding, dataBean);
            initDivideData(binding, dataBean);


            convertView = binding.getRoot();
            convertView.setTag(binding);
            viewMap.put(position, convertView);
        }

        return viewMap.get(position);
    }

    private void initDivideData(ItemLvMyassetBinding binding, MyAssetList.ResultBean.DataBean dataBean) {
        binding.container1.removeAllViews();
        binding.container2.removeAllViews();
        String periodType = incomeType[this.periodType - 1];
        Integer purView = categoryMap.get(dataBean.getProductCategoryID());
        TextView v2 = (TextView) layoutInflater.inflate(R.layout.item_assetdetail_tv, binding.container1, false);
        v2.setText(periodType + "扫码:" + dataBean.getScanCodeIncome());
        binding.container1.addView(v2);

        TextView v5 = (TextView) layoutInflater.inflate(R.layout.item_assetdetail_tv, binding.container2, false);
        v5.setText("我的分成:" + dataBean.getDivideIncome());
        binding.container2.addView(v5);


        TextView v6 = (TextView) layoutInflater.inflate(R.layout.item_assetdetail_tv, binding.container1, false);
        v6.setText("广告收入:" + dataBean.getAdIncome());
        binding.container1.addView(v6);


        if ((1 & purView) != 0) {
            TextView v3 = (TextView) layoutInflater.inflate(R.layout.item_assetdetail_tv, binding.container2, false);
            v3.setText(periodType + "投币:" + dataBean.getTodayCoin());
            binding.container2.addView(v3);
        }


        if ((1 << 1 & purView) != 0) {
            TextView v1 = (TextView) layoutInflater.inflate(R.layout.item_assetdetail_tv, binding.container1, false);
            v1.setText(periodType + "纸币:" + dataBean.getTodayBanknote());
            if (binding.container2.getChildCount() == 1) {
                binding.container2.addView(v1);
            } else {
                binding.container1.addView(v1);
            }
        }


        if ((1 << 2 & purView) != 0) {
            TextView v4 = (TextView) layoutInflater.inflate(R.layout.item_assetdetail_tv, binding.container1, false);
            v4.setText(periodType + "退礼:" + dataBean.getTodayAwardCount());
            if (binding.container1.getChildCount() + binding.container2.getChildCount() == 3) {
                binding.container2.addView(v4);
            }

            if (binding.container1.getChildCount() + binding.container2.getChildCount() == 4) {
                binding.container1.addView(v4);
            }

            if (binding.container1.getChildCount() + binding.container2.getChildCount() == 5) {
                binding.container2.addView(v4);
            }

        }
    }

    private void initData(ItemLvMyassetBinding binding, MyAssetList.ResultBean.DataBean dataBean) {
        double price = dataBean.getPrice();
        String priceStr = FormatUtil.getInstance().get2double(price);
        binding.price.setText("¥" + priceStr);
        binding.setAssetBean(dataBean);
    }


    private int periodType;

    public void clearView(int periodType) {
        this.periodType = periodType;
        viewMap.clear();
        assetList.clear();
        notifyDataSetChanged();
    }


    public void refreshData(int position, MyAssetList.ResultBean.DataBean dataBean) {
        View view = getView(position, null, null);
        ItemLvMyassetBinding binding = (ItemLvMyassetBinding) view.getTag();
        LogUtil.log("===========dataBean.getTodayBanknote()============="+dataBean.getTodayBanknote());
        initDivideData(binding, dataBean);
    }

}
