package com.lemon.backnightgit.presenter;

import android.app.Activity;

import com.google.gson.Gson;
import com.lemon.backnightgit.presenter.view.IFollowAddView;
import com.standards.library.cache.SPHelp;

import java.util.List;

import com.lemon.backnightgit.BuildConfig;
import com.lemon.backnightgit.api.DataManager;
import com.lemon.backnightgit.base.BasePresenter;
import com.lemon.backnightgit.bean.TicketType;
import com.lemon.backnightgit.manager.TicketTypeDataManager;
import com.lemon.backnightgit.presenter.view.IFollowAddView;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/09/11 11:10:50
 */

public class FollowAddPresenter extends BasePresenter<IFollowAddView> {

    public FollowAddPresenter(Activity activity) {
        super(activity);
    }

    public void getMyFollowList() {
        addSubscribe(TicketTypeDataManager.getTicketDataManager().getMyFollowData().subscribe(getSubscriber(ticketTypeList ->
                mView.onGetTicketListSuccess(ticketTypeList)
        )));
    }

    public void cacheList(List<TicketType> ticketTypes) {
        SPHelp.setUserParam(BuildConfig.KEY_MY_FOLLOW, new Gson().toJson(ticketTypes));
    }
}
