package com.example.godcode.ui.fragment.dm;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.ContactBean;
import com.example.godcode.bean.ContactData;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.constant.Constant;
import com.example.godcode.databinding.ItemContactsBinding;
import com.example.godcode.databinding.ItemMcManagerBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.ClickSureListener;
import com.example.godcode.ui.view.widget.TipDialog;
import com.example.godcode.utils.ImagUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactAdapter extends BaseAdapter {
    private List<ContactData> contactDatas;
    private Context context;
    private LayoutInflater mLayoutInflater;
    private boolean isAuthorize;
    private HashMap<Integer, View> viewMap = new HashMap();

    public ContactAdapter(Context context, List<ContactData> contactDatas, boolean isAuthorize) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.contactDatas = contactDatas;
        this.isAuthorize = isAuthorize;
    }

    public void initData(int position, ItemContactsBinding binding) {
        ContactData contactData = contactDatas.get(position);
        binding.character.setText(contactData.getCharcter());
        ArrayList<ContactBean> contacts = contactData.getContacts();
        for (int i = 0; i < contacts.size(); i++) {
            ContactBean dataBean = contacts.get(i);
            String url = ImagUtil.handleUrl(dataBean.getFriendImgPath());
            ItemMcManagerBinding itemFriendBinding = DataBindingUtil.inflate(this.mLayoutInflater, R.layout.item_mc_manager, binding.friendContainer, false);
            itemFriendBinding.setContactBean(dataBean);
            if (TextUtils.isEmpty(url)) {
                itemFriendBinding.friendPhoto.setBackgroundResource(R.drawable.contact_normal);
            } else {
                RxImageLoader.with(context).getBitmap(url).subscribe(
                        imageBean -> {
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), imageBean.getBitmap());
                            itemFriendBinding.friendPhoto.setBackground(bitmapDrawable);
                        }
                );
            }
            View convertView = itemFriendBinding.getRoot();
            final int j = i;
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (isAuthorize) {
                        new TipDialog(context, new ClickSureListener() {
                            @Override
                            public void clickSure() {
                                HttpUtil.getInstance().addMcManager(Constant.userId, dataBean.getFK_UserID()).subscribe(
                                        str -> {
                                            Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();
                                            binding.friendContainer.removeView(convertView);
                                        }
                                );

                            }
                        }, "你确定要授权该用户吗?").show();

                    } else {

                        new TipDialog(context, new ClickSureListener() {
                            @Override
                            public void clickSure() {

                                HttpUtil.getInstance().deleteMcUser(dataBean.getToUserID()).subscribe(
                                        str -> {
                                            //取消成功 刷新数据
                                            Toast.makeText(context, "取消授权成功", Toast.LENGTH_SHORT).show();
                                            binding.friendContainer.removeView(convertView);
                                        }
                                );

                            }
                        }, "你确定要取消该用户的授权吗?").show();

                    }
                }
            });

            binding.friendContainer.addView(convertView);
        }
    }


    public int getCount() {
        return contactDatas == null ? 0 : contactDatas.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemContactsBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_contacts, parent, false);
            viewMap.put(position, binding.getRoot());
            initData(position, binding);
        }
        return viewMap.get(position);
    }

    public int getPositionByCharcter(String charcter) {
        for (int i = 0; i < contactDatas.size(); i++) {
            if ((contactDatas.get(i)).getCharcter().equals(charcter)) {
                return i;
            }
        }
        return -1;
    }

    public void clearView() {
        viewMap.clear();
        contactDatas.clear();
        notifyDataSetChanged();
    }
}
