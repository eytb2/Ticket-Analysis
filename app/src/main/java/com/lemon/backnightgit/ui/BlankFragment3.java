package com.lemon.backnightgit.ui;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lemon.backnightgit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment3 extends Fragment {
    private WebView mWebView;
    View inflate;
    SwipeRefreshLayout msrf;
    TitleBarUtil mTitleBarUtil;
    public BlankFragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_blank_fragment3, container, false);
        // Inflate the layout for this fragment
        mTitleBarUtil= (TitleBarUtil) inflate.findViewById(R.id.mTitleBarUtil);

        mWebView= (WebView) inflate.findViewById(R.id.webview);
        msrf= (SwipeRefreshLayout) inflate.findViewById(R.id.srf);
        WebSettings webSettings = mWebView .getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        //设置 缓存模式
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.loadUrl("http://m.zhcw.com/kaijiang/");


        msrf.setEnabled(false);
        msrf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrf.setEnabled(true);

                mWebView.loadUrl("http://m.zhcw.com/kaijiang/");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        msrf.setEnabled(false);
                    }
                },2000);

            }

        });
        return inflate;}

    public static BlankFragment3 newInstance() {
        BlankFragment3 fragment = new BlankFragment3();
        return fragment;
    }

}
