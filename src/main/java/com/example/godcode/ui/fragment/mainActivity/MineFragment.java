package com.example.godcode.ui.fragment.mainActivity;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.godcode.R;
import com.example.godcode.bean.User;
import com.example.godcode.bean.WsHeart;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.FragmentMineBinding;
import com.example.godcode.greendao.option.UserOption;
import com.example.godcode.greendao.option.VersionMsgOption;
import com.example.godcode.observable.EventType;
import com.example.godcode.observable.RxBus;
import com.example.godcode.observable.RxEvent;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.base.GodCodeApplication;
import com.example.godcode.ui.fragment.deatailFragment.PresonalFragment;
import com.example.godcode.utils.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MineFragment extends BaseFragment {
    private boolean isPrepared;
    private View view;
    private FragmentMineBinding binding;
    private User.ResultBean result;
    private com.example.godcode.greendao.entity.User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.setTitle("我");
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.presenal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PresonalFragment presonalFragment = new PresonalFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("mine", result);
                presonalFragment.setArguments(bundle);
                presenter.step2Fragment(presonalFragment, "presonal");
            }
        });


        RxBus.getInstance().toObservable(RxEvent.class).subscribe(new Observer<RxEvent>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(RxEvent rxEvent) {
                switch (rxEvent.getEventType()) {
                    case EventType.EVENTTYPE_HEART:
                        WsHeart wsHeart = (WsHeart) rxEvent.getBundle().getSerializable("heart");
                        String androidVer = wsHeart.getData().getAndroidVer();
                        try {
                            String versionName = GodCodeApplication.getInstance().getPackageManager().getPackageInfo(GodCodeApplication.getInstance().getPackageName(), 0).versionName;
                            int version1 = Integer.parseInt(versionName.replace(".", ""));
                            int version2 = Integer.parseInt(androidVer.replace(".", ""));
                            LogUtil.log(version1 + "==========收到心跳============" + version2);
                            if (version2 > version1) {
                                String androidVerDes = wsHeart.getData().getAndroidVerDes();
                                VersionMsgOption.getInstance().updateVersion(androidVer, androidVerDes);
                                binding.versionMsg.setText("新版本 " + androidVer);
                                binding.newImg.setVisibility(View.VISIBLE);
                                MainFragment main = (MainFragment) getParentFragment();
                                main.getBinding().setUpdate(true);
                            }
                        } catch (PackageManager.NameNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        break;
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


    private boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()) {
            setHeadImage();
        }
    }

    @Override
    protected void lazyLoad() {

    }

    private void setHeadImage() {
        user = UserOption.getInstance().querryUser(Constant.userId);
        if (user != null) {
            binding.setUser(user);
            String headImageUrl = user.getHeadImageUrl();
            if (!TextUtils.isEmpty(headImageUrl)) {
                if (!headImageUrl.contains("http")) {
                    headImageUrl = Constant.baseUrl + headImageUrl;
                }
                RxImageLoader.with(activity).getBitmap(headImageUrl).subscribe(
                        imageBean -> {
                            if (imageBean.getBitmap() != null) {
                                LogUtil.log("======HHHHHHHHEEEEEEEEEEE==========");
                                Bitmap bitmap = imageBean.getBitmap();
                                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                                roundedBitmapDrawable.setCircular(true);
                                binding.ivUser.setImageDrawable(roundedBitmapDrawable);
                            }
                        }
                );
            } else {
                binding.ivUser.setImageResource(R.drawable.contact_normal);
            }
        }
    }

    public void refreshData() {
        setHeadImage();
    }


}
