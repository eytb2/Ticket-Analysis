package com.lemon.backnightgit.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import com.lemon.backnightgit.R;
import com.lemon.backnightgit.asd.FCTrendsTypeFramgnet;
import devlight.io.library.ntb.NavigationTabBar;

public class FirstActivity extends AppCompatActivity {

    NavigationTabBar navigationTabBar;
    ViewPager mBannerViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        navigationTabBar= (NavigationTabBar) findViewById(R.id.ntb);
        mBannerViewPager= (ViewPager) findViewById(R.id.bannerViewPager);
        initView();
        initIndic();
    }

        public void initView() {

            mFragmentList.add(com.lemon.backnightgit.ui.BlankFragment.newInstance());
            mFragmentList.add(com.lemon.backnightgit.ui.BlankFragment2.newInstance());
            mFragmentList.add(FCTrendsTypeFramgnet.newInstance());
            mFragmentList.add(com.lemon.backnightgit.ui.BlankFragment4.newInstance());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //设置对话框标题，该标题会显示在标题区域中
            builder.setTitle("注意")
                    //设置对话框图标，该标题会显示在标题区域中
                    .setIcon(null)
                    //setMessage方法中的内容会显示在内容区域中
                    .setMessage("应主管部门要求，当前各彩票网站均暂停售彩，已售出彩票兑奖不受影响。购买彩票建议您查询附近的实体网点。就此给您带来的不便，敬请谅解！")
                    /*以下三个setXXXButton(CharSequence text, final OnClickListener listener)方法
                       都向对话框的按钮区域添加了一个按钮，方法的第一个参数是按钮文本，第二个是按钮点击监听器。
                       注意按钮的顺序和代码的添加顺序无关，按钮的位置是固定的(如图1)，只有调用了对应的setXXXButton()
                       方法该按钮才显示。
                    */
                    .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })

                    //真正实例化AlertDialog对象
                    .create()
                    //显示对话框
                    .show();

        }

        private void initIndic() {
            final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.mipmap.logo),
                            Color.parseColor("#11cccccc")
                    ).title("首页")
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.mipmap.logo),
                            Color.parseColor("#11cccccc")
                    ).title("拍照识别")
                            .build()
            );
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.mipmap.logo),
                            Color.parseColor("#11cccccc")
                    ).title("彩票走势")
                            .badgeTitle("icon")
                            .build()
            );    models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(R.mipmap.logo),
                            Color.parseColor("#11cccccc")
                    ).title("其他")
                            .badgeTitle("icon")
                            .build()
            );

            navigationTabBar.setModels(models);
            FragmentManager fragmentManager = getSupportFragmentManager();
            com.lemon.backnightgit.ui.ViewpagerAdapter mAdapter = new com.lemon.backnightgit.ui.ViewpagerAdapter(fragmentManager, mFragmentList);
            mBannerViewPager.setAdapter(mAdapter);
            navigationTabBar.setViewPager(mBannerViewPager, 0);
        }

}
