package com.example.godcode.ui.fragment.deatailFragment;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;

import com.example.godcode.R;
import com.example.godcode.bean.Teansantion;
import com.example.godcode.bean.TeansantionType;
import com.example.godcode.databinding.FragmentTransactionrecordBinding;
import com.example.godcode.greendao.option.TransationOption;
import com.example.godcode.greendao.option.TransationTypeOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.adapter.TransationRecordListAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.view.widget.BottomDialog;
import com.example.godcode.ui.view.MyDatePickerDialog;
import com.example.godcode.ui.view.MyListView;
import com.example.godcode.utils.StringUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TransationRecordFragment extends BaseFragment implements BottomDialog.TypeSelect, MyListView.RefreshData {
    private FragmentTransactionrecordBinding binding;
    private View view;
    private List<TeansantionType.ResultBean> teansantionTypeList;
    private List<Teansantion.DataBean> data;
    private TransationRecordListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transactionrecord, container, false);
            binding.setPresenter(presenter);
            binding.lvTransationrecord.setRefreshData(this);
            view = binding.getRoot();
            String title = StringUtil.getString(activity, R.string.transactionsDetail);
            String sx = StringUtil.getString(activity, R.string.sx);
            binding.transationrecordToolbar.title.setText(title);
            binding.transationrecordToolbar.tvOption.setText(sx);
            data = new ArrayList<>();
            typeList = new ArrayList<>();
            querryTeansationType();
            initView();
            initListener();
        } else {
            refreshData(1);
        }
        data.clear();
        return view;
    }


    private void initListener() {
        binding.lvTransationrecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransationRecordDetailFragment trdf = new TransationRecordDetailFragment();
                Bundle bundle = new Bundle();
                Teansantion.DataBean dataBean = data.get(position);
                bundle.putInt("id", dataBean.getId());
                bundle.putInt("relatedKey", dataBean.getRelatedKey());
                bundle.putInt("transactionType", dataBean.getTransactionType());
                trdf.setArguments(bundle);
                presenter.step2Fragment(trdf, "trdf");
            }
        });

    }

    private ArrayList<String> typeList;

    private void querryTeansationType() {
        HttpUtil.getInstance().getTeansantionTypeList().subscribe(
                teanstantionTypeStr -> {
                    TeansantionType teansantionType = new Gson().fromJson(teanstantionTypeStr, TeansantionType.class);
                    teansantionTypeList = teansantionType.getResult();
                    for (int i = 0; i < teansantionTypeList.size(); i++) {
                        TeansantionType.ResultBean resultBean = teansantionTypeList.get(i);
                        TransationTypeOption.getInstance().insertTransationType(resultBean.getKey(), resultBean.getValue());
                        typeList.add(resultBean.getKey());
                    }
                    type = teansantionTypeList.get(0).getValue();
                    date = binding.tvDate.getText().toString();
                    querryTeansationName();
                }
        );
    }


    private String type;
    private String date;


    private void querryTeansationName() {
        HttpUtil.getInstance().getTypeList2().subscribe(
                typeStr -> {
                    TeansantionType teansantionType = new Gson().fromJson(typeStr, TeansantionType.class);
                    List<TeansantionType.ResultBean> result = teansantionType.getResult();
                    for (int i = 0; i < result.size(); i++) {
                        TeansantionType.ResultBean resultBean = result.get(i);
                        String value = resultBean.getValue();
                        String key = resultBean.getKey();
                        TransationOption.getInstance().insertTransationName(key, value);
                    }
                    refreshData(1);
                }
        );


    }

    public void initView() {
        initTime();
        adapter = new TransationRecordListAdapter(activity, data, R.layout.item_lv_transationrecord);
        binding.lvTransationrecord.setAdapter(adapter);
        binding.transationrecordToolbar.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(type);
                BottomDialog bottomDialog = new BottomDialog(activity, typeList, position, "请选择交易类型");
                bottomDialog.setTypeSelect(TransationRecordFragment.this);
                bottomDialog.show();
            }
        });
    }

    private void initTime() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        binding.tvDate.setText(y + "-" + (m + 1));
        binding.recordDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        adapter.clearView();
                        data.clear();
                        date = String.format("%d-%d", year, month + 1);
                        binding.tvDate.setText(date);
                        binding.lvTransationrecord.setState(2);
                    }
                }, y, m + 1, 0);
                ((ViewGroup) ((ViewGroup) myDatePickerDialog.getDatePicker().
                        getChildAt(0)).
                        getChildAt(0)).
                        getChildAt(2).
                        setVisibility(View.GONE);
                myDatePickerDialog.show();
            }
        });
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void selectType(int position) {
        data.clear();
        date = binding.tvDate.getText().toString();
        type = teansantionTypeList.get(position).getValue();
        adapter.clearView();
        binding.lvTransationrecord.setState(2);
    }

    @Override
    public void refreshData(int page) {
        HashMap<String, String> urlMap = new HashMap<>();
        urlMap.put("UserId", String.valueOf(Constant.userId));
        urlMap.put("type", type);
        urlMap.put("time", date);
        urlMap.put("page", page + "");
        urlMap.put("limit", "20");
        HttpUtil.getInstance().getTeansantion(urlMap).subscribe(
                teansantionStr -> {
                    Teansantion teansantion = new Gson().fromJson(teansantionStr, Teansantion.class);
                    List<Teansantion.DataBean> list = teansantion.getData();
                    if (list != null && list.size() > 0) {
                        data.addAll(list);
                        adapter.notifyDataSetChanged();
                        binding.setTeansation(teansantion);
                        binding.lvTransationrecord.setState(0);
                    } else {
                        binding.lvTransationrecord.setState(1);
                    }

                }
        );
    }
}
