package cn.lemon.ticketsystem.presenter.view;

import java.util.List;

import cn.lemon.ticketsystem.base.ILoadingView;
import cn.lemon.ticketsystem.bean.TicketOpenData;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/14 14:59
 */

public interface IParityTrendView extends ILoadingView {
    void onGetHistoryRecentTicketListSuccess(List<TicketOpenData> list);
}
