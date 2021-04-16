package com.lihang.selfmvvm.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lihang.selfmvvm.R;
import com.lihang.selfmvvm.ui.msgdetail.MsgDetailActivity;
import com.lihang.selfmvvm.vo.model.CommunicateMsgVo;
import com.lihang.selfmvvm.vo.res.AttachmentResVo;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static com.lihang.selfmvvm.base.BaseConstant.DEFAULT_FILE_SERVER;
import static com.lihang.selfmvvm.common.SystemConst.DEFAULT_SERVER;

/**
 * created by zhangxianpeng
 */
public class CommunicateMsgAdapter extends RecyclerView.Adapter<CommunicateMsgAdapter.ViewHolder> {
    private List<CommunicateMsgVo> mMsgList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        ImageView leftImageView;
        ImageView rightImageView;
        TextView leftMsg;
        TextView leftCreater;
        TextView rightMsg;
        TextView answer;
        ImageView crateImageView;
        WebView replayWebView;

        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            leftCreater = (TextView) view.findViewById(R.id.tv_creater);
            answer = (TextView) view.findViewById(R.id.tv_answer);
            leftImageView = view.findViewById(R.id.left_head);
            rightImageView = view.findViewById(R.id.right_head);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
            crateImageView = (ImageView) view.findViewById(R.id.craetePicIv);
            replayWebView = (WebView) view.findViewById(R.id.replayWebView);
        }
    }

    public CommunicateMsgAdapter(Context context, List<CommunicateMsgVo> msgList) {
        this.mMsgList = msgList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_communicate, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CommunicateMsgVo msg = mMsgList.get(i);
        String userName = msg.getUsername();
        String answer = msg.getAnswer();
        List<AttachmentResVo> attachmentResVos = msg.getAttachmentList();
        if (!msg.isSend()) {
            if (msg.getAnswer() != null) {
                viewHolder.leftLayout.setVisibility(View.VISIBLE);
                viewHolder.rightLayout.setVisibility(View.GONE);
                viewHolder.replayWebView.setVisibility(View.VISIBLE);
                loadHtml(StringEscapeUtils.unescapeHtml4(answer),viewHolder.replayWebView);
            }
        } else {
            if (msg.getContent() != null) {
                viewHolder.leftLayout.setVisibility(View.GONE);
                viewHolder.rightLayout.setVisibility(View.VISIBLE);
                viewHolder.rightMsg.setText(msg.getContent());
                viewHolder.leftCreater.setText(userName);

                if (attachmentResVos != null && !attachmentResVos.isEmpty()) {
                    viewHolder.crateImageView.setVisibility(View.VISIBLE);
                    Glide.with(context).load(DEFAULT_SERVER + DEFAULT_FILE_SERVER + attachmentResVos.get(0).getUrl()).placeholder(R.mipmap.default_img)
                            .error(R.mipmap.default_img).into(viewHolder.crateImageView);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    private void loadHtml(String content,WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true); // 支持缩放
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        webView.loadData(content, "text/html;charset=utf-8", "utf-8");
    }

}
