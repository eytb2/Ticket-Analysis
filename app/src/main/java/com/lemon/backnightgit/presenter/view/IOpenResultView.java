package com.lemon.backnightgit.presenter.view;

import com.lemon.backnightgit.base.ILoadingView;
import com.lemon.backnightgit.bean.TicketOpenData;
import com.lemon.backnightgit.bean.TicketRegular;

import com.lemon.backnightgit.base.ILoadingView;
import com.lemon.backnightgit.bean.TicketOpenData;
import com.lemon.backnightgit.bean.TicketRegular;

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
