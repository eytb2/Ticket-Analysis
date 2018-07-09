package com.lemon.backnightgit.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.lemon.backnightgit.R;
import com.lemon.backnightgit.ui.web.X5WebView;
import com.maning.updatelibrary.InstallUtils;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

public class BrowserActivity extends Activity {
	/**
	 * 作为一个浏览器的示例展示出来，采用android+web的模式
	 */
	private X5WebView mWebView;
	private ViewGroup mViewParent;
	TitleBarUtil mTitleBarUtil;

	private static final String mHomeUrl = "baidu.com";
	private static final String TAG = "SdkDemo";
	private static final int MAX_LENGTH = 14;
	private boolean mNeedTestPage = false;

	private final int disable = 120;
	private final int enable = 255;

	private ProgressBar mPageLoadingProgressBar = null;

	private ValueCallback<Uri> uploadFile;

	private String mIntentUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);

		Intent intent = getIntent();
		if (intent != null) {
			mIntentUrl=intent.getStringExtra("linkUrl");
		}
		//
		try {
			if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
				getWindow()
						.setFlags(
								android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
								android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			}
		} catch (Exception e) {
		}

		/*
		 * getWindow().addFlags(
		 * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		setContentView(R.layout.activity_webview);
		mViewParent = (ViewGroup) findViewById(R.id.webView1);

		mTitleBarUtil = (TitleBarUtil) findViewById(R.id.mTitleBarUtil);
		mTitleBarUtil.setTitle(intent.getStringExtra("title"));
		mTitleBarUtil.setTitleColor(Color.parseColor("#ffffff"));

		mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);

	}

	private void changGoForwardButton(WebView view) {

	}

	private void initProgressBar() {
		mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);// new
																				// ProgressBar(getApplicationContext(),
																				// null,
																				// android.R.attr.progressBarStyleHorizontal);
		mPageLoadingProgressBar.setMax(100);
		mPageLoadingProgressBar.setProgressDrawable(this.getResources()
				.getDrawable(R.drawable.color_progressbar));
	}

	private void init() {

		mWebView = new X5WebView(this, null);

		mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT));

		initProgressBar();

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
				mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
				if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
					changGoForwardButton(view);
				/* mWebView.showLog("test Log"); */
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
					JsResult arg3) {
				return super.onJsConfirm(arg0, arg1, arg2, arg3);
			}

			View myVideoView;
			View myNormalView;
			CustomViewCallback callback;

			// /////////////////////////////////////////////////////////
			//
			/**
			 * 全屏播放配置
			 */
			@Override
			public void onShowCustomView(View view,
					CustomViewCallback customViewCallback) {
				FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
				ViewGroup viewGroup = (ViewGroup) normalView.getParent();
				viewGroup.removeView(normalView);
				viewGroup.addView(view);
				myVideoView = view;
				myNormalView = normalView;
				callback = customViewCallback;
			}

			@Override
			public void onHideCustomView() {
				if (callback != null) {
					callback.onCustomViewHidden();
					callback = null;
				}
				if (myVideoView != null) {
					ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
					viewGroup.removeView(myVideoView);
					viewGroup.addView(myNormalView);
				}
			}

			@Override
			public boolean onJsAlert(WebView arg0, String arg1, String arg2,
					JsResult arg3) {
				/**
				 * 这里写入你自定义的window alert
				 */
				return super.onJsAlert(null, arg1, arg2, arg3);
			}
		});

		mWebView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String arg0, String arg1, String arg2,
					String arg3, long arg4) {
				TbsLog.d(TAG, "url: " + arg0);
			/*	new AlertDialog.Builder(BrowserActivity.this)
						.setTitle("allow to download？")
						.setPositiveButton("yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {*/

										MaterialDialog build
												= new MaterialDialog.Builder(BrowserActivity.this)
												.title("下载中")
												.canceledOnTouchOutside(false)
												.backgroundColor(Color.parseColor("#00000000"))
												.autoDismiss(false)
												.progress(false, 100, true)
												.build();

										InstallUtils.with(BrowserActivity.this)
												//必须-下载地址
												.setApkUrl(arg0)
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
//														mProgressBar.setVisibility(View.GONE);
														//下载完成
														/**
														 * 安装APK工具类
														 * @param context       上下文
														 * @param filePath      文件路径
														 * @param callBack      安装界面成功调起的回调
														 */
														InstallUtils.installAPK(BrowserActivity.this, path, new InstallUtils.InstallCallBack() {
															@Override
															public void onSuccess() {
																Toast.makeText(BrowserActivity.this, "正在安装程序", Toast.LENGTH_SHORT).show();

															}

															@Override
															public void onFail(Exception e) {
																Toast.makeText(BrowserActivity.this, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
															}
														});
													}

													@Override
													public void onLoading(long total, long current) {
//                                                    build.setMaxProgress((int) total);
//                                                    build.setProgress((int) current);
														LogUtils.i(total+"...."+current);
														build.setProgress((int) ((current) / Float.valueOf(total) * 100));
														build.setMaxProgress(100);
														build.show();

//                                                    build.show();
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
								});
						/*.setNegativeButton("no",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								})
						.setOnCancelListener(
								new DialogInterface.OnCancelListener() {

									@Override
									public void onCancel(DialogInterface dialog) {
										// TODO Auto-generated method stub
										Toast.makeText(
												BrowserActivity.this,
												"fake message: refuse download...",
												Toast.LENGTH_SHORT).show();
									}
								}).show();
			}
		});*/

		WebSettings webSetting = mWebView.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		// webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
		webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
		webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
				.getPath());
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// webSetting.setPreFectch(true);
		long time = System.currentTimeMillis();
		if (mIntentUrl == null) {
			mWebView.loadUrl(mHomeUrl);
		} else {
			mWebView.loadUrl(mIntentUrl);
		}
		TbsLog.d("time-cost", "cost time: "
				+ (System.currentTimeMillis() - time));
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().sync();
	}



	boolean[] m_selected = new boolean[] { true, true, true, true, false,
			false, true };

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
				if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
					changGoForwardButton(mWebView);
				return true;
			} else
				return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
				+ ",resultCode:" + resultCode);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				if (null != uploadFile) {
					Uri result = data == null || resultCode != RESULT_OK ? null
							: data.getData();
					uploadFile.onReceiveValue(result);
					uploadFile = null;
				}
				break;
			default:
				break;
			}
		} else if (resultCode == RESULT_CANCELED) {
			if (null != uploadFile) {
				uploadFile.onReceiveValue(null);
				uploadFile = null;
			}

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent == null || mWebView == null || intent.getData() == null)
			return;
		mWebView.loadUrl(intent.getData().toString());
	}

	@Override
	protected void onDestroy() {
		if (mTestHandler != null)
			mTestHandler.removeCallbacksAndMessages(null);
		if (mWebView != null)
			mWebView.destroy();
		super.onDestroy();
	}

	public static final int MSG_OPEN_TEST_URL = 0;
	public static final int MSG_INIT_UI = 1;
	private final int mUrlStartNum = 0;
	private int mCurrentUrl = mUrlStartNum;
	private Handler mTestHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_OPEN_TEST_URL:
				if (!mNeedTestPage) {
					return;
				}

				String testUrl = "file:///sdcard/outputHtml/html/"
						+ Integer.toString(mCurrentUrl) + ".html";
				if (mWebView != null) {
					mWebView.loadUrl(testUrl);
				}

				mCurrentUrl++;
				break;
			case MSG_INIT_UI:
				init();
				break;
			}
			super.handleMessage(msg);
		}
	};

}
