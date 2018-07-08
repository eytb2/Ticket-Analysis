package com.lemon.backnightgit.presenter.view;

import com.lemon.backnightgit.base.ILoadingView;
import com.lemon.backnightgit.bean.TicketOpenData;

import java.util.List;

import com.lemon.backnightgit.base.ILoadingView;
import com.lemon.backnightgit.bean.TicketOpenData;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/14 14:59
 */

public interface IParityTrendView extends ILoadingView {
    void onGetHistoryRecentTicketListSuccess(List<TicketOpenData> list);
}
