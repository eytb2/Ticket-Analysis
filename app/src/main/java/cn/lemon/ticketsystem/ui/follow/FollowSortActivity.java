package cn.lemon.ticketsystem.ui.follow;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.lemon.ticketsystem.R;
import cn.lemon.ticketsystem.adapter.TicketTypeSortAdapter;
import cn.lemon.ticketsystem.base.BaseTitleBar;
import cn.lemon.ticketsystem.base.BaseTitleBarActivity;
import cn.lemon.ticketsystem.bean.event.FollowDataChangeEvent;
import cn.lemon.ticketsystem.bean.TicketType;
import cn.lemon.ticketsystem.ui.widget.RecycleViewDivider;


/**
 * @author lemon
 * @version v1.0
 * @function <排序页>
 * @date 2016/8/9-14:10
 */
public class FollowSortActivity extends BaseTitleBarActivity {
    private TicketTypeSortAdapter ticketTypeAdapter;
    private List<TicketType> mTicketTypeList;

    public static Bundle buildBundle(ArrayList<TicketType> ticketTypeList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ticketTypes", ticketTypeList);
        return bundle;
    }

    @Override
    public void getExtra() {
        mTicketTypeList = (List<TicketType>) getIntent().getSerializableExtra("ticketTypes");
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_follow_sort;
    }

    @Override
    public void init() {
        RecyclerView rvFollowSort = findView(R.id.rvFollowSort);
        if (mTicketTypeList != null) {
            ticketTypeAdapter = new TicketTypeSortAdapter(this, mTicketTypeList);
            rvFollowSort.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvFollowSort.addItemDecoration(new RecycleViewDivider(this,
                    LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.main_divider_color)));
            rvFollowSort.setAdapter(ticketTypeAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(ticketTypeAdapter));
            itemTouchHelper.attachToRecyclerView(rvFollowSort);
        }

    }

    @Override
    public void setListener() {
    }

    @Override
    public void initTitleBar(BaseTitleBar titleBar) {
        titleBar.setTitleText("自定义关注页");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(new FollowDataChangeEvent(mTicketTypeList));
        super.onDestroy();
    }
}
