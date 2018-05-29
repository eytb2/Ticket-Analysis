package cn.lemon.ticketsystem.ui;



import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;


import java.util.ArrayList;
import java.util.List;

import cn.lemon.ticketsystem.R;
import cn.lemon.ticketsystem.adapter.TabFragmentAdapter;
import cn.lemon.ticketsystem.base.BaseFuncFragment;
import cn.lemon.ticketsystem.bean.type.TicketTypeEnum;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends BaseFuncFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_blank;
    }

    @Override
    public void init() {
        initTabTop(getFragmentTitle(), getFragmentList());
    }

    private void initTabTop(List<String> fragmentTitles, List<android.support.v4.app.Fragment> fragmentList) {
        TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(fragmentList, fragmentTitles, getChildFragmentManager(), getActivity());
        ViewPager pagerContent = findView(R.id.pageContent);
        pagerContent.setAdapter(tabFragmentAdapter);
        TabLayout tlMainTop = findView(R.id.tlMainTop);
        tlMainTop.setupWithViewPager(pagerContent);
    }

    private List<android.support.v4.app.Fragment> getFragmentList() {
        List<android.support.v4.app.Fragment> fragmentList = new ArrayList<>();
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

    @Override
    public void setListener() {

    }


    public static BlankFragment newInstance() {
        BlankFragment fragment = new BlankFragment();
        return fragment;
    }






}
