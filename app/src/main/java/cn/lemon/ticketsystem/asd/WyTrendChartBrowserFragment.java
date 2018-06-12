package cn.lemon.ticketsystem.asd;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.lemon.ticketsystem.R;
import cn.lemon.ticketsystem.ui.TitleBarUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017\9\3 0003.
 * 网页浏览页面
 */

@ContentView(R.layout.fragment_browser)
public class WyTrendChartBrowserFragment extends AppBaseFragment {

    @ViewInject(R.id.web_info)
    private WebView mWebInfo;
    @ViewInject(R.id.progress_load)
    ProgressBar mProgressLoad;

    @ViewInject(R.id.mTitleBarUtil)
    TitleBarUtil mTitleBarUtil;
    private String mLoadUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }


    @Override
    public void init() {
        super.init();


        mWebInfo.setFocusable(true);
        mWebInfo.setFocusableInTouchMode(true);
        mWebInfo.requestFocus();
        WebSettings webSettings = mWebInfo.getSettings();
        webSettings.setDomStorageEnabled(true);
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(false);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        //支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setUserAgentString("Mozilla/5.0");
        webSettings.setLoadWithOverviewMode(true);
        updateWebSettings(webSettings);
        //加载需要显示的网页
        //mWebInfo.loadUrl(mLoadUrl);
        //设置Web视图
        mWebInfo.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //ToastUtils.showToast("shouldOverrideUrlLoading1");
                mWebInfo.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressLoad.setVisibility(View.VISIBLE);
                pageStarted(view, url, favicon);
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressLoad.setVisibility(View.GONE);
                pageFinished(view, url);
                super.onPageFinished(view, url);

            }
        });

        mLoadUrl = getActivity().getIntent().getStringExtra(ConstData.IntentKey.WEB_LOAD_URL);
        mTitleBarUtil.setTitle(getActivity().getIntent().getStringExtra(ConstData.IntentKey.TARGET_ACTIVITY_LABEL));
        mTitleBarUtil.setTitleColor(Color.parseColor("#ffffff"));
        //boolean isInformationUrl = getActivity().getIntent().getBooleanExtra(ConstData.IntentKey.IS_INFORMATION_URL, false);
        //if(isInformationUrl)
        //    mLoadUrl += "?from=client";
        //mWebInfo.loadUrl(mLoadUrl);
        mProgressLoad.setVisibility(View.VISIBLE);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> e) {
                e.onNext( LottyDataGetUtils.getTrendChardHtmlByWy(mLoadUrl));
            }


        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s)   {
                mProgressLoad.setVisibility(View.GONE);
                mWebInfo.loadDataWithBaseURL("http://trend.caipiao.163.com/ssq/", s, "text/html;charset=UTF-8", null, null);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable)   {
                mProgressLoad.setVisibility(View.GONE);
//                ToastUtils.showToast("发生错误，请重试");
            }
        });
        mWebInfo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK ) {
                    if(mWebInfo.canGoBack()){
                        mWebInfo.goBack();
                        return true;
                    }
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    public void pageStarted(WebView view, String url, Bitmap favicon){

    }

    public void pageFinished(WebView view, String url){

    }

    public void updateWebSettings(WebSettings settings){

    }
}
