package com.lemon.backnightgit.asd;

import android.support.v4.app.Fragment;

/**
 * Created by gaofei on 2018/1/31.
 * 发彩网走势页面
 */

public class FcTrendsInfoActivity extends SingleFragmentActivity{
    @Override
    public Fragment createFragment() {
        return new FcTrendsInfoFragment();
    }
}
