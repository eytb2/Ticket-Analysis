package cn.lemon.ticketsystem.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.standards.library.app.AppContext;
import com.standards.library.cache.SPHelp;

import java.util.List;

import cn.lemon.ticketsystem.BuildConfig;
import cn.lemon.ticketsystem.bean.TicketRegular;
import cn.lemon.ticketsystem.bean.TicketType;
import cn.lemon.ticketsystem.utils.JsonUtil;
import rx.Observable;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/19 16:18
 */

public class TicketRegularManager {
    public static TicketRegularManager sTicketRegularManager;
    public static List<TicketRegular> sTicketRegular;

    public static TicketRegularManager getTicketDataManager() {
        if (sTicketRegularManager == null) {
            synchronized (TicketRegularManager.class) {
                if (sTicketRegularManager == null) {
                    sTicketRegularManager = new TicketRegularManager();
                }
            }
        }
        return sTicketRegularManager;
    }

    public Observable<List<TicketRegular>> getTicketRegularList() {
        return sTicketRegular == null ? getDataFromAsset().doOnNext(ticketRegulars -> sTicketRegular = ticketRegulars) : Observable.just(sTicketRegular);
    }


    private Observable<List<TicketRegular>> getDataFromAsset() {
        return Observable.create(subscriber -> {
            List<TicketRegular> ticketRegulars = new Gson().fromJson(JsonUtil.getJson(AppContext.getContext(), "regular.json"), new TypeToken<List<TicketRegular>>() {
            }.getType());
            subscriber.onNext(ticketRegulars);
            subscriber.onCompleted();
        });
    }
}
