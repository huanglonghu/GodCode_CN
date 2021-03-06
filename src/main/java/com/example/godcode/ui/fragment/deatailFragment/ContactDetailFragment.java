package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.FragmentContacterDetailBinding;
import com.example.godcode.greendao.entity.Friend;
import com.example.godcode.greendao.option.FriendOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.ClickSureListener;
import com.example.godcode.interface_.Strategy;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.view.widget.DeleteDialog;

public class ContactDetailFragment extends BaseFragment implements RemarkSettingFragment.RemarkCallBack {
    private FragmentContacterDetailBinding binding;
    private View view;
    private Friend friend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contacter_detail, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.contacterDetailToolbar.title.setText("详细资料");
            initView();
            initListener();
        }
        return view;
    }


    private int id;

    public void initData(int id) {
        this.id = id;
    }


    private void initListener() {
        binding.deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog deleteDialog = new DeleteDialog(activity, "确定要删除" + friend.getUserName() + "吗？", new ClickSureListener() {
                    @Override
                    public void clickSure() {
                        HttpUtil.getInstance().deleteFriend(Constant.userId, id)
                                .subscribe(
                                        deleteStr -> {
                                            FriendOption.getInstance(activity).deleteFriend(id);
                                            Toast.makeText(activity, "删除成功", Toast.LENGTH_SHORT).show();
                                            presenter.back();
                                        }
                                );
                    }
                });
                deleteDialog.show();
            }
        });

        binding.remarkSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemarkSettingFragment remarkSettingFragment = new RemarkSettingFragment();
                remarkSettingFragment.setRemarkCallBack(ContactDetailFragment.this);
                remarkSettingFragment.initData(friend);
                presenter.step2Fragment(remarkSettingFragment, "remarksetting");
            }
        });
    }


    public void initView() {
        friend = FriendOption.getInstance(activity).querryFriend(id);
        if (friend == null) {
            FriendOption.getInstance(activity).querryFriendList(1,true);
            FriendOption.getInstance(activity).friendUpdateListener().subscribe(
                    friendUpdate -> {
                        if(friendUpdate){
                            friend = FriendOption.getInstance(activity).querryFriend(id);
                            if(friend!=null){
                                initFriend();
                            }
                        }

                    }
            );
        } else {
            initFriend();
        }


    }

    private void initFriend() {
        binding.setFriend(friend);
        String headImageUrl = friend.getHeadImageUrl();
        if (!TextUtils.isEmpty(headImageUrl)) {
            if (!headImageUrl.contains("http")) {
                headImageUrl = Constant.baseUrl + headImageUrl;
            }
            RxImageLoader.with(activity).load(headImageUrl).into(binding.friendPhoto);
        }
    }

    @Override
    protected void lazyLoad() {
    }


    @Override
    public void remarkUpdate(String remark) {
        binding.tvUserName.setText(remark);
    }



}
