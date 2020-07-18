package com.lihang.selfmvvm.ui.senddocument;

import android.view.View;

import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.base.BaseActivity;
import com.lihang.selfmvvm.bean.ProjectBean;
import com.lihang.selfmvvm.databinding.ActivitySendDocumentctivityBinding;
import com.lihang.selfmvvm.ui.declaredetail.DeclareDetailActivity;
import com.lihang.selfmvvm.ui.documentdetail.DocumentDetailActivity;
import com.lihang.selfmvvm.utils.ActivityUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 已发公文
 */
public class SendDocumentctivity extends BaseActivity<SendDocumentViewModel, ActivitySendDocumentctivityBinding> {


    private ArrayList<ProjectBean> issuedDocumentList = new ArrayList<>();


    @Override
    protected int getContentViewId() {
        return R.layout.activity_send_documentctivity;
    }

    @Override
    protected void processLogic() {
        getIssuedDocumentList();
    }

    private void getIssuedDocumentList() {
        ProjectBean testBean1 = new ProjectBean();
        testBean1.setProjectTitle("2020年南宁服务机构能力提升研修班项目（第八期）");
        testBean1.setProjectTime("2020年6月17日");
        issuedDocumentList.add(testBean1);

        ProjectBean testBean2 = new ProjectBean();
        testBean2.setProjectTitle("2020年创客南宁大赛项目");
        testBean2.setProjectTime("2020年6月17日");
        issuedDocumentList.add(testBean2);

        ProjectBean testBean3 = new ProjectBean();
        testBean3.setProjectTitle("高考加油！为芊芊学子助力！");
        testBean3.setProjectTime("2020年6月17日");
        issuedDocumentList.add(testBean3);

        ProjectBean testBean4 = new ProjectBean();
        testBean4.setProjectTitle("在变革中求发展 在发展中求突破项目");
        testBean4.setProjectTime("2020年6月17日");
        issuedDocumentList.add(testBean4);

        ProjectBean testBean5 = new ProjectBean();
        testBean5.setProjectTitle("关于疫情下外贸营销的困难与机遇");
        testBean5.setProjectTime("2020年6月17日");
        issuedDocumentList.add(testBean5);

        ProjectBean testBean6 = new ProjectBean();
        testBean6.setProjectTitle("直播营销“带货南宁”项目");
        testBean6.setProjectTime("2020年6月17日");
        issuedDocumentList.add(testBean6);

        binding.rvIssuedDocuments.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIssuedDocuments.setAdapter(new CommonAdapter<ProjectBean>(getContext(), R.layout.issued_document_list_item, issuedDocumentList) {

            @Override
            protected void convert(ViewHolder holder, ProjectBean projectBean, int position) {
                holder.setText(R.id.tv_title, issuedDocumentList.get(position).getProjectTitle());
                holder.setText(R.id.tv_time, issuedDocumentList.get(position).getProjectTime());
                holder.setOnClickListener(R.id.rl_container, (view -> {
                    ActivityUtils.startActivity(getContext(), DocumentDetailActivity.class);
                }));
            }
        });
    }

    @Override
    protected void setListener() {
        binding.ivTitleBarBack.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
            default:
                break;
        }
    }
}