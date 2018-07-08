package com.lemon.backnightgit.presenter.view;

import com.lemon.backnightgit.base.ILoadingView;

import java.util.List;

import com.lemon.backnightgit.base.ILoadingView;
import com.lemon.backnightgit.bean.TicketType;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/11 11:17
 */

public interface INumberGenerateView extends ILoadingView {
    void onGenerateDataSuccess(String codeGroup);
}
