package com.lemon.backnightgit.presenter;

import android.app.Activity;

import com.lemon.backnightgit.api.DataManager;
import com.lemon.backnightgit.base.BasePresenter;
import com.lemon.backnightgit.bean.TicketRegular;
import com.lemon.backnightgit.manager.TicketRegularManager;
import com.lemon.backnightgit.presenter.view.IOpenResultView;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/8 15:01
 */

public class OpenResultPresenter extends BasePresenter<IOpenResultView> {
    private TicketRegular mTicketRegular;

    public OpenResultPresenter(Activity activity) {
        super(activity);
    }

    public void getSingleOpenResult(String code, String issue) {
        addSubscribe(DataManager.getSinglePeroidCheck(code, issue).subscribe(getSubscriber(ticketOpenDataTicketInfo ->
                mView.getSingleOpenResultSuccess(ticketOpenDataTicketInfo.list.get(0))
        )));
    }

    public void getRegularCache(String code) {
        if (mTicketRegular != null) {
            mView.getRegularSuccess(mTicketRegular);
            return;
        }
        addSubscribe(TicketRegularManager.getTicketDataManager().getTicketRegularList().subscribe(getSubscriberNoProgress(ticketRegulars -> {
            for (TicketRegular ticketRegular : ticketRegulars) {
                if (ticketRegular.code.equals(code)) {
                    mTicketRegular = ticketRegular;
                    mView.getRegularSuccess(mTicketRegular);
                    break;
                }
            }
        })));
    }
}
