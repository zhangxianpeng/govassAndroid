package com.lihang.selfmvvm.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lihang.selfmvvm.R;

import androidx.fragment.app.Fragment;

/**
 * created by zhangxianpeng
 * 横向滑动卡片fragment
 */
public class ShowIndustryListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_industry_list, container, false);
    }
}
