package cn.lemon.ticketsystem.presenter;

import android.app.Activity;

import com.google.gson.Gson;
import com.standards.library.cache.SPHelp;

import java.util.List;

import cn.lemon.ticketsystem.BuildConfig;
import cn.lemon.ticketsystem.base.BasePresenter;
import cn.lemon.ticketsystem.bean.TicketRegular;
import cn.lemon.ticketsystem.bean.TicketType;
import cn.lemon.ticketsystem.manager.TicketTypeDataManager;
import cn.lemon.ticketsystem.presenter.view.IFollowAddView;
import cn.lemon.ticketsystem.presenter.view.INumberGenerateView;
import cn.lemon.ticketsystem.utils.NumberGenerateHelper;

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
