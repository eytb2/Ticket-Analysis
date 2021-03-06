package com.lemon.backnightgit.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.lemon.backnightgit.adapter.TabFragmentAdapter;
import com.lemon.backnightgit.base.BaseFuncActivity;
import com.lemon.backnightgit.bean.type.TicketTypeEnum;

import java.util.ArrayList;
import java.util.List;

import com.lemon.backnightgit.R;
import com.lemon.backnightgit.adapter.TabFragmentAdapter;
import com.lemon.backnightgit.base.BaseFuncActivity;
import com.lemon.backnightgit.bean.type.TicketTypeEnum;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/7 14:45
 */

public class MainActivity extends BaseFuncActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        initTabTop(getFragmentTitle(), getFragmentList());

    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(MyFollowFragment.getNewInstance(TicketTypeEnum.Follow));
        for (int i = 1; i < TicketTypeEnum.values().length; i++) {
            fragmentList.add(TicketTypeFragment.getNewInstance(TicketTypeEnum.values()[i]));
        }
        return fragmentList;
    }

    private List<String> getFragmentTitle() {
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < TicketTypeEnum.values().length; i++) {
            titles.add(TicketTypeEnum.values()[i].getValue());
        }
        return titles;
    }

    private void initTabTop(List<String> fragmentTitles, List<Fragment> fragmentList) {
        TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(fragmentList, fragmentTitles, getSupportFragmentManager(), this);
        ViewPager pagerContent = findView(R.id.pageContent);
        pagerContent.setAdapter(tabFragmentAdapter);
        TabLayout tlMainTop = findView(R.id.tlMainTop);
        tlMainTop.setupWithViewPager(pagerContent);
    }

    @Override
    protected void setListener() {

    }

}
