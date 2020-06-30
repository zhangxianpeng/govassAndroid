package com.lihang.selfmvvm.bean.basebean;

import com.lihang.selfmvvm.bean.HomeBean;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeFatherBean implements Serializable {
    private ArrayList<HomeBean> datas;

    public ArrayList<HomeBean> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<HomeBean> datas) {
        this.datas = datas;
    }
}
