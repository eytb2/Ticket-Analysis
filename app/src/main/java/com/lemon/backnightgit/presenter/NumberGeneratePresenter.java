package com.lemon.backnightgit.presenter;

import android.app.Activity;

import java.util.List;

import com.lemon.backnightgit.base.BasePresenter;
import com.lemon.backnightgit.bean.TicketRegular;
import com.lemon.backnightgit.bean.TicketType;
import com.lemon.backnightgit.manager.TicketTypeDataManager;
import com.lemon.backnightgit.presenter.view.IFollowAddView;
import com.lemon.backnightgit.presenter.view.INumberGenerateView;
import com.lemon.backnightgit.utils.NumberGenerateHelper;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/09/11 11:10:50
 */

public class NumberGeneratePresenter extends BasePresenter<INumberGenerateView> {
    private NumberGenerateHelper numberGenerateHelper;

    public NumberGeneratePresenter(Activity activity, TicketRegular regular) {
        super(activity);
        numberGenerateHelper = new NumberGenerateHelper(regular);
    }

    public void generaterNumber(List<List<String>> numberBase) {
        addSubscribe(numberGenerateHelper.generateNumberGroup(numberBase).subscribe(getSubscriberNoProgress(t ->
                mView.onGenerateDataSuccess(t)
        )));
    }

}
