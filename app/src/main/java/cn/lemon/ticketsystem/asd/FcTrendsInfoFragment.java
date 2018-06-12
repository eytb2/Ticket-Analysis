package cn.lemon.ticketsystem.asd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.lemon.ticketsystem.R;
import cn.lemon.ticketsystem.ui.TitleBarUtil;
import momo.cn.edu.fjnu.androidutils.utils.SizeUtils;
import momo.cn.edu.fjnu.androidutils.utils.ToastUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by gaofei on 2018/1/31.
 * 发彩网走势页面
 */
@ContentView(R.layout.fragment_trends_info)
public class FcTrendsInfoFragment extends AppBaseFragment implements View.OnClickListener{

    @ViewInject(R.id.container_lotty_title)
    private LinearLayout mContainerLottyTitle;
    @ViewInject(R.id.list_trendinfos)
    private ListView mListTrendInfos;

    @ViewInject(R.id.mTitleBarUtil)
    TitleBarUtil mTitleBarUtil;
    private TextView mTextSelectTitle;
    private List<TrendInfo> mTrendInfos;
    @ViewInject(R.id.progress_load)
    ProgressBar mProgressLoad;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void init() {
        super.init();
        initView();
        loadData();
    }

    @Override
    public void onClick(View v) {
        int selectIndex = mContainerLottyTitle.indexOfChild(v);
        if(selectIndex >= 0){
            if(mTextSelectTitle != null){
                mTextSelectTitle.setTextColor(getResources().getColor(R.color.black));
                mTextSelectTitle.setTextSize(18);
            }
            TextView clickView = (TextView)v;
            clickView.setTextColor(getResources().getColor(R.color.red));
            clickView.setTextSize(20);
            mTextSelectTitle = clickView;
            update(mTrendInfos.get(selectIndex));
        }
    }

    private void update(final TrendInfo info){
        //走势名称
        final List<String> trendNames = new ArrayList<>();
        //走势URL
        final List<String> trendUrls = new ArrayList<>();
        Map<String, String> urls =info.getTrendUrl();
        Set<String> keys = urls.keySet();
        for(String key : keys){
            trendNames.add(key);
            trendUrls.add(urls.get(key));
        }
        ArrayAdapter<String> trendAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, trendNames);
        mListTrendInfos.setAdapter(trendAdapter);
        mListTrendInfos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), WyTrendChartActivity.class);
                intent.putExtra(ConstData.IntentKey.TARGET_ACTIVITY_LABEL, info.getName() + "-" + trendNames.get(position));
                intent.putExtra(ConstData.IntentKey.WEB_LOAD_URL, trendUrls.get(position));
                startActivity(intent);
            }
        });
    }

    private void initView(){
        mTitleBarUtil.setTitle("走势图");
        mTitleBarUtil.setTitleColor(Color.parseColor("#ffffff"));
    }

    private void loadData(){
        mProgressLoad.setVisibility(View.VISIBLE);
        Observable.create(new Observable.OnSubscribe<List<TrendInfo>>() {
            @Override
            public void call(Subscriber<? super List<TrendInfo>> e) {
                List<TrendInfo> trendInfos = LottyDataGetUtils.getAllTrendInfoByWy();
                e.onNext(trendInfos);
            }

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<TrendInfo>>() {
            @Override
            public void call(List<TrendInfo> trendInfos)   {
                mProgressLoad.setVisibility(View.GONE);
                if (trendInfos == null || trendInfos.size() == 0) {
                    //检查网络
                    ToastUtils.showToast("请检查网络");
                } else {
                    //获取当前选中Title
                    mTrendInfos = trendInfos;
                    String selectTitle = getActivity().getIntent().getStringExtra(ConstData.IntentKey.LOTTERY_NAME);
                    boolean isFind = false;
                    for (TrendInfo info : trendInfos) {
                        TextView lottyTitleTextView = new TextView(getContext());
                        lottyTitleTextView.setTextSize(18);
                        lottyTitleTextView.setTextColor(getResources().getColor(R.color.black));
                        lottyTitleTextView.setGravity(Gravity.CENTER);
                        lottyTitleTextView.setPadding(SizeUtils.dp2px(10), 0, SizeUtils.dp2px(10), 0);
                        lottyTitleTextView.setText(info.getName());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        mContainerLottyTitle.addView(lottyTitleTextView, layoutParams);
                        lottyTitleTextView.setOnClickListener(FcTrendsInfoFragment.this);
                        if (info.getName().equals(selectTitle)) {
                            isFind = true;
                            mTextSelectTitle = lottyTitleTextView;
                            lottyTitleTextView.setTextSize(20);
                            lottyTitleTextView.setTextColor(getResources().getColor(R.color.red));
                            update(info);
                        }
                    }
                    //设置选中第一项
                    if(!isFind){
                        mTextSelectTitle =  (TextView) mContainerLottyTitle.getChildAt(0);
                        mTextSelectTitle.setTextSize(20);
                        mTextSelectTitle.setTextColor(getResources().getColor(R.color.red));
                        update(trendInfos.get(0));
                    }

                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable)   {
                mProgressLoad.setVisibility(View.GONE);
//                ToastUtils.showToast("发生错误，请重试");
            }
        });
    }
}
