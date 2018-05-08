package cn.lemon.ticketsystem.presenter.view;

import com.standards.library.model.ListData;

import java.util.List;

import cn.lemon.ticketsystem.base.ILoadingView;
import cn.lemon.ticketsystem.bean.TicketType;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/11 11:17
 */

public interface IFollowAddView extends ILoadingView {
    void onGetTicketListSuccess(List<TicketType> ticketTypeList);
}
