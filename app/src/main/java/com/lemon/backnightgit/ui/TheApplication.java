package com.lemon.backnightgit.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.lemon.backnightgit.BuildConfig;
import com.lemon.backnightgit.R;
import com.lemon.backnightgit.asd.ConstData;
import com.standards.library.app.AppContext;
import com.standards.library.app.ReturnCode;
import com.standards.library.app.ReturnCodeConfig;
import com.standards.library.cache.DataProvider;
import com.standards.library.network.NetworkConfig;
import com.standards.library.util.LogUtil;
import com.tencent.bugly.Bugly;

import org.xutils.x;

import java.io.File;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;
import momo.cn.edu.fjnu.androidutils.base.BaseApplication;
import momo.cn.edu.fjnu.androidutils.utils.StorageUtils;


/**
 * @author 马鹏昊
 * @date {2016-10-28}
 * @des 获取全局对象
 * @updateAuthor
 * @updateDate
 * @updateDes
 */
public class TheApplication extends BaseApplication {
    public static boolean hasGotToken;
    //尝试次数
    public static int times = 0;


    //全局Context
    private static Context mContext;

    //屏幕宽度
    public static int screenWidth;
    //屏幕高度
    public static int screenHeight;

    //SharedPreference
    private static SharedPreferences sSharedPreferences;

    //此App一些信息的存储文件夹路径
    private static final String appDirectoryPath = File.separator + "mnt" + File.separator + "sdcard" + File.separator + "lottery";
    //此App一些信息的存储文件夹
    private static File appRootDirectory;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

           initAccessTokenWithAkSk();

                com.orhanobut.hawk.Hawk.init(this)
                .setEncryptionMethod(com.orhanobut.hawk.HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setStorage(com.orhanobut.hawk.HawkBuilder.newSqliteStorage(this))
                .setLogLevel(com.orhanobut.hawk.LogLevel.FULL)
                .build();


        x.Ext.init(this);
        //写入APK第一次打开时间
        if(TextUtils.isEmpty(StorageUtils.getDataFromSharedPreference(ConstData.SharedKey.INSTALL_TIME)))
            StorageUtils.saveDataToSharedPreference(ConstData.SharedKey.INSTALL_TIME, "" + new Date().getTime());

        AppContext.getInstance().init(this);
        mContext = getContext();
        LogUtil.init(BuildConfig.DEBUG_LOG, "lucky");
        DataProvider.init(this);
        NetworkConfig.setBaseUrl(BuildConfig.HOST_URL);
        ReturnCodeConfig.getInstance().initReturnCode(ReturnCode.CODE_SUCCESS, ReturnCode.CODE_EMPTY);


        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Utils.init(this);
        AppContext.getInstance().init(this);
        Bugly.init(getApplicationContext(),"9c2225c0d8",false);
        LogUtil.init(BuildConfig.DEBUG_LOG, "lucky");
        DataProvider.init(this);
        NetworkConfig.setBaseUrl(BuildConfig.HOST_URL);
        ReturnCodeConfig.getInstance().initReturnCode(ReturnCode.CODE_SUCCESS, ReturnCode.CODE_EMPTY);

        mContext = getApplicationContext();
        sSharedPreferences = mContext.getSharedPreferences("loginInfo", MODE_PRIVATE);



        appRootDirectory = new File(appDirectoryPath);
        if (appRootDirectory.exists() && appRootDirectory.isDirectory()) {
            appRootDirectory.delete();
        }
        appRootDirectory.mkdirs();
    }


    //外部得到全局context的接口
    public static Context getContext() {
        return mContext;
    }

    //外部得到SharedPreference的接口
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    //外部得到App根目录文件夹路径
    public static String getAppRootDirectory() {
        return appDirectoryPath;
    }

    /*
        得到dp转化成的px
    */
    public static int getPxFromDp(float dip) {
        float result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, mContext.getResources().getDisplayMetrics());
        return (int) result;
    }
    /*
        得到dp转化成的px
    */
    public static float getPxFromSp(float sp) {
        float result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mContext.getResources().getDisplayMetrics());
        return result;
    }

        private void initAccessTokenWithAkSk() {
        com.baidu.ocr.sdk.OCR.getInstance().initAccessTokenWithAkSk(new com.baidu.ocr.sdk.OnResultListener<com.baidu.ocr.sdk.model.AccessToken>() {
            @Override
            public void onResult(com.baidu.ocr.sdk.model.AccessToken result) {
                hasGotToken = true;
            }

            @Override
            public void onError(com.baidu.ocr.sdk.exception.OCRError error) {
                error.printStackTrace();
                if ( times++ < 3 ) {
                    initAccessTokenWithAkSk();
                } else {
//                    android.widget.Toast.makeText(TheApplication.this, "AK，SK方式获取token失败", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        }, getApplicationContext(), Console.APP_KEY, Console.APP_SECRET);
    }

    /**
     * 得到自定义的progressDialog
     *
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg/*int parentWidth,int parentHeight*/) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        //        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(parentWidth,parentHeight);
        //        v.setLayoutParams(params);
        //        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if (!TextUtils.isEmpty(msg)) {
            tipTextView.setText(msg);// 设置加载信息
        }

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        //        loadingDialog.getWindow().setLayout(parentWidth,parentHeight);
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }

}
