package cn.lemon.ticketsystem.presenter;

import android.app.Activity;

import cn.lemon.ticketsystem.api.DataManager;
import cn.lemon.ticketsystem.base.BasePresenter;
import cn.lemon.ticketsystem.bean.TicketRegular;
import cn.lemon.ticketsystem.manager.TicketRegularManager;
import cn.lemon.ticketsystem.presenter.view.IOpenResultView;

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
