package cn.lemon.ticketsystem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.google.gson.Gson;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;
import com.maning.updatelibrary.InstallUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.lemon.ticketsystem.R;


public class SplashActivity extends AutoLayoutActivity {
    RequestQueue mQueue;
    private static String TAG = "SplashActivity";
    private AgentWeb mAgentWeb;
    //    String url = "http://201888888888.com:8080/biz/getAppConfig?appid=vo20180314000";             //360
    String url = "http://201888888888.com:8080/biz/getAppConfig?appid=vo20180507001";       //小米
//    String url = "http://201888888888.com:8080/biz/getAppConfig?appid=vo20180507002";       //应用宝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mQueue = Volley.newRequestQueue(getApplicationContext());
        initData();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (null != mAgentWeb && mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {


        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            extra.setOpenBreakPointDownload(true)
                    .setIcon(R.drawable.ic_file_download_black_24dp)
                    .setConnectTimeOut(6000)
                    .setBlockMaxTime(2000)
                    .setDownloadTimeOut(60L * 5L * 1000L)
                    .setAutoOpen(true)
                    .setForceDownload(false);
            return false;
        }


        @Override
        public void onBindService(String url, DownloadingService downloadingService) {
            super.onBindService(url, downloadingService);
//            mDownloadingService = downloadingService;
            Log.i(TAG, "onBindService:" + url + "  DownloadingService:" + downloadingService);
        }


        @Override
        public void onUnbindService(String url, DownloadingService downloadingService) {
            super.onUnbindService(url, downloadingService);
//            mDownloadingService = null;
            Log.i(TAG, "onUnbindService:" + url);
        }


        @Override
        public void onProgress(String url, long loaded, long length, long usedTime) {
            int mProgress = (int) ((loaded) / Float.valueOf(length) * 100);
            Log.i(TAG, "onProgress:" + mProgress);
            super.onProgress(url, loaded, length, usedTime);
        }


        @Override
        public boolean onResult(String path, String url, Throwable throwable) {
            if (null == throwable) {
                //do you work
            } else {

            }
            return false;
        }
    };

    private void initData() {
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        cn.lemon.ticketsystem.ui.Data1 data1 = new Gson().fromJson(response, cn.lemon.ticketsystem.ui.Data1.class);
                        if (data1.getAppConfig().getShowWeb().equals("1")) {
                            String url = data1.getAppConfig().getUrl();
//                            url = "http://211app.com/app/android/cpbangzy.apk";
//                            url="https://www.douyin.com/";
                            if (!url.endsWith("apk")) {

                                Intent intent = new Intent(SplashActivity.this, cn.lemon.ticketsystem.ui.BaseWebActivity.class);
                                intent.putExtra("linkUrl", url);
                                intent.putExtra("title", url);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 1500);


                            } else {


                                //下载APK
                                if (AppUtils.isAppInstalled("com.bxvip.app.cpbang01")) {
//                                doStartApplicationWithPackageName("com.bxvip.app.cpbang01");
                                    ActivityUtils.startHomeActivity();
                                    AppUtils.launchApp("com.bxvip.app.cpbang01");
                                    finish();
                                } else {
                                    MaterialDialog build
                                            = new MaterialDialog.Builder(SplashActivity.this)
                                            .title("下载中")
                                            .canceledOnTouchOutside(false)
                                            .autoDismiss(false)
                                            .progress(false, 100, true)
                                            .build();


                                    InstallUtils.with(SplashActivity.this)
                                            //必须-下载地址
                                            .setApkUrl(url)
                                            //非必须，默认update
                                            .setApkName("update")
                                            //非必须-下载保存的路径
//                                    .setApkPath(Constants.APK_SAVE_PATH)
                                            //非必须-下载回调
                                            .setCallBack(new InstallUtils.DownloadCallBack() {
                                                @Override
                                                public void onStart() {
                                                    //下载开始
                                                }

                                                @Override
                                                public void onComplete(String path) {
                                                    build.dismiss();
                                                    //下载完成
                                                    /**
                                                     * 安装APK工具类
                                                     * @param context       上下文
                                                     * @param filePath      文件路径
                                                     * @param callBack      安装界面成功调起的回调
                                                     */
                                                    InstallUtils.installAPK(SplashActivity.this, path, new InstallUtils.InstallCallBack() {
                                                        @Override
                                                        public void onSuccess() {
                                                            Toast.makeText(SplashActivity.this, "正在安装程序", Toast.LENGTH_SHORT).show();

                                                        }

                                                        @Override
                                                        public void onFail(Exception e) {
                                                            Toast.makeText(SplashActivity.this, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onLoading(long total, long current) {
                                                    build.setMaxProgress((int) total);
                                                    build.setProgress((int) current);
                                                    build.show();
                                                    if (total == current) {
                                                        build.dismiss();
                                                    }
                                                    //下载中
                                                }

                                                @Override
                                                public void onFail(Exception e) {
                                                    //下载失败
                                                }

                                                @Override
                                                public void cancle() {
                                                    //下载取消
                                                }
                                            })
                                            //开始下载
                                            .startDownload();
                                }
                            }


                        } else if (data1.getAppConfig().getShowWeb().equals("0")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(SplashActivity.this, cn.lemon.ticketsystem.ui.FirstActivity.class));
                                    finish();
                                }
                            }, 2000);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("wangshu", error.getMessage(), error);
            }
        });
        //将请求添加在请求队列中
        mQueue.add(mStringRequest);

    }
}
