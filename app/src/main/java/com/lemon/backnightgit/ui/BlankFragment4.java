package com.lemon.backnightgit.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.lemon.backnightgit.R;
import devlight.io.library.ntb.NavigationTabBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment4 extends Fragment {
    NavigationTabBar navigationTabBar;
    ViewPager mBannerViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    TitleBarUtil mTitleBarUtil;
    public BlankFragment4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_blank_fragment4, container, false);
        navigationTabBar= (NavigationTabBar)inflate. findViewById(R.id.ntb);
        mBannerViewPager= (ViewPager) inflate.findViewById(R.id.bannerViewPager);
        mTitleBarUtil= (TitleBarUtil) inflate.findViewById(R.id.mTitleBarUtil);
        mTitleBarUtil.setTitle("其他");
        mTitleBarUtil.setTitleColor(Color.parseColor("#ffffff"));

        initView();
        initIndic();
        return inflate;

    }

    public static BlankFragment4 newInstance() {
        BlankFragment4 fragment = new BlankFragment4();
        return fragment;
    }

    private void initView() {

        mFragmentList.add(OneFragment.newInstance());
        mFragmentList.add(TwoFragment.newInstance());


    }

    private void initIndic() {
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.logo),
                        Color.parseColor("#11cccccc")
                ).title("模拟选号")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.logo),
                        Color.parseColor("#11cccccc")
                ).title("资讯")
                        .build()
        );


        navigationTabBar.setModels(models);
        FragmentManager fragmentManager = getChildFragmentManager();
        ViewpagerAdapter mAdapter = new ViewpagerAdapter(fragmentManager, mFragmentList);
        mBannerViewPager.setAdapter(mAdapter);
        navigationTabBar.setViewPager(mBannerViewPager, 0);
    }


}
