package com.lemon.backnightgit.asd;

import android.support.v4.app.Fragment;


/**
 * Created by gaofei on 2018/1/31.
 * 图表走势页面
 */

public class WyTrendChartActivity extends SingleFragmentActivity{
    @Override
    public Fragment createFragment() {
        return new WyTrendChartBrowserFragment();
    }
}
