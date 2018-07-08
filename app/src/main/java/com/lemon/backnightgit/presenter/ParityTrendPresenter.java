package com.lemon.backnightgit.presenter;

import android.app.Activity;

import com.standards.library.util.TimeUtils;

import com.lemon.backnightgit.api.DataManager;
import com.lemon.backnightgit.base.BasePresenter;
import com.lemon.backnightgit.presenter.view.IParityTrendView;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/14 14:58
 */

public class ParityTrendPresenter extends BasePresenter<IParityTrendView> {
    public ParityTrendPresenter(Activity activity) {
        super(activity);
    }

    public void getRecentOpenDatas(String ticketCode, String count) {
        addSubscribe(DataManager.getMutiPeriodCheck(ticketCode, count, TimeUtils.milliseconds2String(System.currentTimeMillis()))
                .subscribe(getSubscriber(ticketOpenDataListData -> {
                    mView.onGetHistoryRecentTicketListSuccess(ticketOpenDataListData.list);
                })));
    }
}
