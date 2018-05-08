package cn.lemon.ticketsystem.presenter.view;

import cn.lemon.ticketsystem.base.ILoadingView;
import cn.lemon.ticketsystem.bean.TicketOpenData;
import cn.lemon.ticketsystem.bean.TicketRegular;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/8 15:02
 */

public interface IOpenResultView extends ILoadingView {
    void getSingleOpenResultSuccess(TicketOpenData ticketOpenData);

    void getRegularSuccess(TicketRegular ticketRegular);
}
