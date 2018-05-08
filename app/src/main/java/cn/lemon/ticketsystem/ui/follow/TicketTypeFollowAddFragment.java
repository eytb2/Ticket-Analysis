package cn.lemon.ticketsystem.ui.follow;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.standards.library.listview.ListGroupPresenter;
import com.standards.library.listview.listview.RecycleListViewImpl;
import com.standards.library.listview.manager.BaseGroupListManager;
import com.standards.library.model.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.lemon.ticketsystem.R;
import cn.lemon.ticketsystem.adapter.TicketTypeFollowAdapter;
import cn.lemon.ticketsystem.base.BaseFuncFragment;
import cn.lemon.ticketsystem.bean.TicketType;
import cn.lemon.ticketsystem.bean.event.FollowDataChangeEvent;
import cn.lemon.ticketsystem.bean.type.TicketTypeEnum;
import cn.lemon.ticketsystem.group.LoadingPage;
import cn.lemon.ticketsystem.group.Scene;
import cn.lemon.ticketsystem.manager.TicketTypeManager;
import cn.lemon.ticketsystem.ui.OpenResultActivity;
import cn.lemon.ticketsystem.ui.widget.RecycleViewDivider;
import cn.lemon.ticketsystem.utils.LaunchUtil;


/**
 * @author lemon
 * @version v1.0
 * @function <三个可供选择的彩票种类关注Fragment>
 * @date 2016/8/9-14:10
 */
public class TicketTypeFollowAddFragment extends BaseFuncFragment {
    private TicketTypeFollowAdapter ticketTypeAdapter;
    private ListGroupPresenter presenter;
    private BaseGroupListManager manager;
    private RecycleListViewImpl recycleListView;
    private TicketTypeEnum mTicketTypeEnum;
    private List<TicketType> mTicketTypes;

    public static TicketTypeFollowAddFragment getNewInstance(TicketTypeEnum ticketTypeEnum, ArrayList<TicketType> ticketTypes) {
        TicketTypeFollowAddFragment fragment = new TicketTypeFollowAddFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ticketListType", ticketTypeEnum);
        bundle.putSerializable("ticketTypes", ticketTypes);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void getExtra() {
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mTicketTypeEnum = (TicketTypeEnum) getArguments().getSerializable("ticketListType");
            mTicketTypes = (List<TicketType>) getArguments().getSerializable("ticketTypes");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ticket_type;
    }

    @Override
    public void init() {
        recycleListView = new RecycleListViewImpl(true, false, false);
        RelativeLayout rlContent = findView(R.id.rlContent);
        LoadingPage loadingPage = new LoadingPage(getActivity(), Scene.DEFAULT);
        ticketTypeAdapter = new TicketTypeFollowAdapter(getActivity(), mTicketTypes);
        manager = new TicketTypeManager(mTicketTypeEnum);
        presenter = ListGroupPresenter.create(getActivity(), recycleListView, manager, ticketTypeAdapter, loadingPage);
        recycleListView.getRecyclerView().addItemDecoration(new RecycleViewDivider(getActivity(),
                LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.main_divider_color)));
        rlContent.addView(presenter.getRootView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void setListener() {
        ticketTypeAdapter.setOnItemClickListener(view ->
                LaunchUtil.launchActivity(getActivity(), OpenResultActivity.class,
                        OpenResultActivity.buildBundle((TicketType) view.getTag())));
    }

    @Subscribe
    public void refresh(FollowDataChangeEvent event) {
        ticketTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
