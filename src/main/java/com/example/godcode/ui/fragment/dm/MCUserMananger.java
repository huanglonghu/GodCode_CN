package com.example.godcode.ui.fragment.dm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.R;
import com.example.godcode.bean.ContactBean;
import com.example.godcode.bean.ContactData;
import com.example.godcode.bean.McUserResponse;
import com.example.godcode.databinding.McUserManagerBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MCUserMananger extends BaseFragment {

    private McUserManagerBinding binding;
    private ContactAdapter contactAdapter;
    private boolean isAuthorize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.mc_user_manager, container, false);
            binding.setPresenter(Presenter.getInstance());
            initView();
            initData();
        }

        return binding.getRoot();
    }


    private ArrayList<ContactData> datas;

    private void initView() {
        isAuthorize = getArguments().getBoolean("isAuthorize");
        binding.setIsAuthorize(isAuthorize);
        datas = new ArrayList<>();
        contactAdapter = new ContactAdapter(getContext(), datas, isAuthorize);
        binding.lvContacts.setAdapter(contactAdapter);

    }

    private void initData() {
        HashMap<String, ContactData> contactMap = new HashMap();

        HttpUtil.getInstance().getUserList(isAuthorize).subscribe(
                str -> {
                    McUserResponse mcUserResponse = GsonUtil.fromJson(str, McUserResponse.class);
                    McUserResponse.ResultBean result = mcUserResponse.getResult();
                    if (result != null) {
                        List<ContactBean> items = result.getItems();
                        if (items != null && items.size() > 0) {
                            for (int i = 0; i < items.size(); i++) {
                                ContactBean itemsBean = items.get(i);
                                String name = itemsBean.getNickName();
                                if (!TextUtils.isEmpty(name)) {
                                    String firstChar = StringUtil.getPingYin(name).substring(0, 1);
                                    if (contactMap.containsKey(firstChar)) {
                                        ((ContactData) contactMap.get(firstChar)).getContacts().add(itemsBean);
                                    } else {
                                        ContactData contactData = new ContactData();
                                        ArrayList<ContactBean> contactList = new ArrayList();
                                        contactList.add(itemsBean);
                                        contactData.setCharcter(firstChar);
                                        contactData.setContacts(contactList);
                                        datas.add(contactData);
                                        contactMap.put(firstChar, contactData);
                                    }
                                }
                            }
                            contactAdapter.notifyDataSetChanged();
                        }
                    }


                }
        );


    }


    @Override
    protected void lazyLoad() {

    }
}
