package com.example.godcode.ui.fragment.deatailFragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.BindProduct;
import com.example.godcode.bean.EditProduct;
import com.example.godcode.bean.EditProductPrice;
import com.example.godcode.bean.EditProductSetting;
import com.example.godcode.bean.MyAssetList;
import com.example.godcode.bean.ProductSetting;
import com.example.godcode.bean.ReturnEquity;
import com.example.godcode.bean.UploadResponse;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.FragmentMyassetConfigBinding;
import com.example.godcode.handler.ActivityResultHandler;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.ClickSureListener;
import com.example.godcode.interface_.EtStrategy;
import com.example.godcode.interface_.HandlerStrategy;
import com.example.godcode.interface_.ProductSettingStrategy;
import com.example.godcode.observable.EventType;
import com.example.godcode.observable.RxBus;
import com.example.godcode.observable.RxEvent;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.fragment.bindproduct.HuoDaoDetailFrgment;
import com.example.godcode.ui.fragment.bindproduct.JtcsConfigFragment;
import com.example.godcode.ui.view.widget.DeleteDialog;
import com.example.godcode.ui.view.widget.EtItemDialog;
import com.example.godcode.ui.view.widget.AirkissConfigDialog;
import com.example.godcode.ui.view.widget.ProductSettingDialog;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.StringUtil;
import com.example.godcode.utils.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

public class Asset_2_Fragment extends BaseFragment implements EditAssetFragment.AssetUpdate {
    private FragmentMyassetConfigBinding binding;
    private View view;
    private MyAssetList.ResultBean.DataBean bean;
    private ProductSetting productSetting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myasset_config, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.asset2Toolbar.title.setText("我的设备");
            binding.setFragment(this);
            initListener();
            Bundle bundle = getArguments();
            if (bundle != null) {
                bean = (MyAssetList.ResultBean.DataBean) bundle.getSerializable("dataBean");
                if (bean.getFK_UserID() == Constant.userId) {
                    binding.setIsMaster(true);
                }
            }
        }
        initView();
        initData();
        return view;
    }


    public void back() {
        presenter.back();
    }

    private void initData() {
        HttpUtil.getInstance().getProductSettingMsg(bean.getFK_ProductID()).subscribe(
                productSettingStr -> {
                    productSetting = GsonUtil.fromJson(productSettingStr, ProductSetting.class);
                }
        );
    }


    private void initListener() {
        binding.relieveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getFK_UserID() == Constant.userId) {
                    String title = null;
                    if (type == 1) {
                        title = "是否解除资产绑定?";
                        DeleteDialog deleteDialog = new DeleteDialog(activity, title, new ClickSureListener() {
                            @Override
                            public void clickSure() {
                                BindProduct body = new BindProduct();
                                body.setFK_UserID(bean.getFK_UserID());
                                body.setIsBind(false);
                                body.setProductName(bean.getProductName());
                                body.setProductNumber(bean.getProductNumber());
                                body.setMachineAddress(bean.getMachineAddress());
                                body.setFK_ProductCategoryID(bean.getProductCategoryID());
                                body.setPrice(bean.getPrice() + "");
                                HttpUtil.getInstance().bindProduct(body).subscribe(
                                        bindProductStr -> {
                                            Toast.makeText(activity, "解绑成功", Toast.LENGTH_SHORT).show();
                                            presenter.back();
                                        }
                                );
                            }
                        });
                        deleteDialog.show();
                    } else if (type == 2) {
                        title = "是否返还股权?";
                        DeleteDialog deleteDialog = new DeleteDialog(activity, title, new ClickSureListener() {
                            @Override
                            public void clickSure() {
                                ReturnEquity returnEquity = new ReturnEquity();
                                returnEquity.setFK_UserID(bean.getFK_UserID());
                                returnEquity.setId(bean.getId());
                                HttpUtil.getInstance().returnEquity(returnEquity).subscribe(
                                        returnEquityStr -> {
                                            Toast.makeText(activity, "产权返还成功", Toast.LENGTH_SHORT).show();
                                            presenter.back();
                                        }
                                );
                            }
                        });
                        deleteDialog.show();
                    }
                }
            }
        });

        binding.ivZc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                new ActivityResultHandler.Builder().hadlerStrategy(new HandlerStrategy() {
                    @Override
                    public void onActivityResult(MultipartBody.Part filePart, Bitmap bitmap) {
                        upload(filePart, bitmap);
                    }
                }).requestCode(ActivityResultHandler.REQUEST_SELECT_PHOTO).intent(intent).activity(activity).build().startActivityForResult();

            }
        });
        RxBus.getInstance().toObservable(RxEvent.class).subscribe(new Observer<RxEvent>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(RxEvent rxEvent) {
                if (rxEvent.getEventType() == EventType.EVENTTYPE_CURRENSSTOCK_CHANGE) {
                    Bundle bundle = rxEvent.getBundle();
                    int kc = bundle.getInt("kc");
                    bean.setCurrentStock(kc);
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


    public void assetConfig(int type) {
        switch (type) {
            case 1:
                RevenueConfigFragment revenueConfigFragment = new RevenueConfigFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                revenueConfigFragment.setArguments(bundle);
                presenter.step2Fragment(revenueConfigFragment, "revenue");
                break;
            case 2:
                EtItemDialog dialog = new EtItemDialog.Builder().
                        context(activity).
                        etStragety(new EtProductPriceSt()).
                        title("修改价格").
                        hint("价格").
                        type(1).
                        build();
                dialog.show();
                break;
            case 3:
                EditAssetFragment editAssetFragment = new EditAssetFragment();
                editAssetFragment.initData(bean);
                editAssetFragment.setAssetUpdate(Asset_2_Fragment.this);
                presenter.step2Fragment(editAssetFragment, "etAsset");
                break;
            case 4:
                new ProductSettingDialog.Builder().
                        context(activity)
                        .strategy(new VolumeStrategy())
                        .title("音量设置")
                        .type(1)
                        .build().show();
                break;
            case 5:
                new ProductSettingDialog.Builder().
                        context(activity)
                        .strategy(new JvStrategy())
                        .title("机率调整")
                        .type(2)
                        .build().show();
                break;
            case 6:
                new ProductSettingDialog.Builder().
                        context(activity)
                        .strategy(new CoinStrategy())
                        .title("几币一玩")
                        .type(3)
                        .build().show();
                break;
            case 7:
                AirkissConfigDialog airkissConfigDialog = new AirkissConfigDialog(activity);
                airkissConfigDialog.show();
                break;
            case 8:
                JtcsConfigFragment jtcsConfigFragment = new JtcsConfigFragment();
                Bundle b2 = new Bundle();
                b2.putSerializable("DataBean", bean);
                jtcsConfigFragment.setArguments(b2);
                presenter.step2Fragment(jtcsConfigFragment, "jtcsConfig");
                break;
            case 9:
                HuoDaoDetailFrgment huoDaoDetailFrgment = new HuoDaoDetailFrgment();
                Bundle b = new Bundle();
                b.putSerializable("DataBean", bean);
                b.putBoolean("isMaster", binding.getIsMaster());
                huoDaoDetailFrgment.setArguments(b);
                presenter.step2Fragment(huoDaoDetailFrgment, "hdDetail");
                break;
            case 10: {
                PackageFragment packageFragment = new PackageFragment();
                int productCategoryID = bean.getProductCategoryID();
                int fk_productID = bean.getFK_ProductID();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("productCategoryID", productCategoryID);
                bundle1.putInt("productId", fk_productID);
                ProductSetting.ResultBean result = productSetting.getResult();
                String productSettingId = null;
                String isFreePlay = null;
                if (result != null) {
                    productSettingId = result.getId() + "";
                }
                bundle1.putString("productSettingId", productSettingId);
                bundle1.putString("isFreeplay", isFreePlay);
                packageFragment.setArguments(bundle1);
                presenter.step2Fragment(packageFragment, "package");
            }

            break;
            case 11: {
                FreeModeSetting freeModeSetting = new FreeModeSetting();
                Bundle bundle2 = new Bundle();
                ProductSetting.ResultBean result = productSetting.getResult();
                int freePlayType = 0;
                int freePlayCount = 0;
                String productSettingId = null;
                if (result != null) {
                    productSettingId = result.getId() + "";
                    freePlayType = result.getFreePlayType();
                    freePlayCount = result.getFreePlayCount();
                }
                bundle2.putInt("productId", bean.getFK_ProductID());
                bundle2.putString("productSettingId", productSettingId);
                bundle2.putInt("freePlayType", freePlayType);
                bundle2.putInt("freePlayCount", freePlayCount);
                freeModeSetting.setArguments(bundle2);
                Presenter.getInstance().step2Fragment(freeModeSetting, "freeModeSetting");
            }

            break;

        }

    }

    private int type;

    public void initView() {
        if (bean.getPrimaevalUserID() == 0) {
            type = 1;
            String str = StringUtil.getString(activity, R.string.unbindAsset);
            binding.relieveProduct.setText(str);
        } else {
            if (bean.getPrimaevalUserID() == Constant.userId) {
                type = 1;
                String str = StringUtil.getString(activity, R.string.unbindAsset);
                binding.relieveProduct.setText(str);
            } else {
                type = 2;
                binding.relieveProduct.setText("返还股权");
            }
        }
        String productImgUrl = bean.getProductImgUrl();
        if (!TextUtils.isEmpty(productImgUrl)) {
            if (!productImgUrl.contains("http")) {
                productImgUrl = Constant.baseUrl + productImgUrl;
            }
            RxImageLoader.with(activity).load(productImgUrl).into(binding.ivZc);
        }
        binding.setAssetBean(bean);
    }

    @Override
    protected void lazyLoad() {
    }


    @Override
    public void assetUpdate(String productName, String adress) {
        bean.setProductName(productName);
        bean.setMachineAddress(adress);
        binding.setAssetBean(bean);
    }


    public void upload(MultipartBody.Part filePart, Bitmap bitmap) {
        binding.ivZc.setBackground(new BitmapDrawable(bitmap));
        HttpUtil.getInstance().upload(filePart, 1).subscribe(
                uploadStr -> {
                    UploadResponse uploadResponse = GsonUtil.getInstance().fromJson(uploadStr, UploadResponse.class);
                    String picturePath = uploadResponse.getResult().get(0);
                    EditProduct editProduct = new EditProduct();
                    EditProduct.ProductBean productBean = new EditProduct.ProductBean();
                    productBean.setId(bean.getFK_ProductID());
                    productBean.setFK_ProductCategoryID(bean.getProductCategoryID());
                    productBean.setProductName(bean.getProductName());
                    productBean.setProductNumber(bean.getProductNumber());
                    productBean.setMachineAddress(bean.getMachineAddress());
                    productBean.setIsValid(true);
                    productBean.setThumbnailImgPath(picturePath);
                    editProduct.setProduct(productBean);
                    HttpUtil.getInstance().editProduct(editProduct).subscribe(
                    );
                }
        );
    }

    class EtProductPriceSt extends EtStrategy {
        @Override
        public void etComplete(String str, int position) {
            int price = Integer.parseInt(str);
            EditProductPrice editProductPrice = new EditProductPrice();
            EditProductPrice.ProductPriceBean productPriceBean = new EditProductPrice.ProductPriceBean();
            productPriceBean.setPrice(price);
            productPriceBean.setFK_ProductID(bean.getFK_ProductID());
            productPriceBean.setFK_UserID(bean.getFK_UserID());
            productPriceBean.setId(bean.getFK_PriceID());
            productPriceBean.setIsValid(true);
            editProductPrice.setProductPrice(productPriceBean);
            HttpUtil.getInstance().editProductPrice(editProductPrice).subscribe(
                    editPriceStr -> {
                        if (editPriceStr.contains("\"success\":true")) {
                            Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
                            bean.setPrice(price);
                            binding.setAssetBean(bean);
                        } else {
                            Toast.makeText(activity, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
    }

    class VolumeStrategy implements ProductSettingStrategy {

        @Override
        public void valueSetting(int value) {
            EditProductSetting.ProductSettingBean psb = getProductSettingBean();
            psb.setProductSettingType(1 + "");
            psb.setVolume(value + "");
            HttpUtil.getInstance().editProductSetting(psb).subscribe(
                    epsStr -> {
                        productSetting = GsonUtil.fromJson(epsStr, ProductSetting.class);
                        ToastUtil.getInstance().showToast("修改成功", 1000, activity);
                    }
            );
        }
    }

    class JvStrategy implements ProductSettingStrategy {
        @Override
        public void valueSetting(int value) {
            EditProductSetting.ProductSettingBean psb = getProductSettingBean();
            psb.setProductSettingType(2 + "");
            psb.setAward(value + "");
            HttpUtil.getInstance().editProductSetting(psb).subscribe(
                    epsStr -> {
                        productSetting = GsonUtil.fromJson(epsStr, ProductSetting.class);
                        ToastUtil.getInstance().showToast("修改成功", 1000, activity);
                    }
            );
        }
    }

    class CoinStrategy implements ProductSettingStrategy {
        @Override
        public void valueSetting(int value) {
            EditProductSetting.ProductSettingBean psb = getProductSettingBean();
            psb.setProductSettingType(3 + "");
            psb.setCoinPlay(value + "");
            HttpUtil.getInstance().editProductSetting(psb).subscribe(
                    epsStr -> {
                        productSetting = GsonUtil.fromJson(epsStr, ProductSetting.class);
                        ToastUtil.getInstance().showToast("修改成功", 1000, activity);
                    }
            );

        }
    }

    @NonNull
    private EditProductSetting.ProductSettingBean getProductSettingBean() {
        EditProductSetting.ProductSettingBean psb = new EditProductSetting.ProductSettingBean();
        psb.setFK_ProductID(bean.getFK_ProductID());
        ProductSetting.ResultBean result = productSetting.getResult();
        if (result != null) {
            psb.setId(result.getId());
        }
        return psb;
    }

}
