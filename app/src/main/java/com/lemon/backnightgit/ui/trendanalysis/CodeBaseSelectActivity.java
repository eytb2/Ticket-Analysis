package com.lemon.backnightgit.ui.trendanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.lemon.backnightgit.adapter.CodeBaseAdapter;
import com.lemon.backnightgit.base.BaseTitleBar;
import com.lemon.backnightgit.base.BaseTitleBarActivity;
import com.lemon.backnightgit.bean.TicketRegular;

import java.io.Serializable;
import java.util.List;

import com.lemon.backnightgit.R;
import com.lemon.backnightgit.adapter.CodeBaseAdapter;
import com.lemon.backnightgit.base.BaseTitleBar;
import com.lemon.backnightgit.base.BaseTitleBarActivity;
import com.lemon.backnightgit.bean.TicketRegular;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/19 17:29
 */

public class CodeBaseSelectActivity extends BaseTitleBarActivity {
    public static final int RESULT_NUMBER_BASE = 0x123456;
    private TicketRegular mTicketRegular;
    private RecyclerView rvBaseList;
    private TextView tvClear;
    private TextView tvConfirm;
    private CodeBaseAdapter codeBaseAdapter;
    private List<List<String>> numberBaseList;

    public static Bundle buildBundle(TicketRegular ticketRegular, List<List<String>> numberBase) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ticketRegular", ticketRegular);
        bundle.putSerializable("codeBase", (Serializable) numberBase);
        return bundle;
    }

    @Override
    public void getExtra() {
        super.getExtra();
        mTicketRegular = (TicketRegular) getIntent().getSerializableExtra("ticketRegular");
        numberBaseList = (List<List<String>>) getIntent().getSerializableExtra("codeBase");
    }

    @Override
    public void initTitleBar(BaseTitleBar titleBar) {
        titleBar.setTitleText("胆码选择");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_number_base;
    }

    @Override
    protected void init() {
        rvBaseList = findView(R.id.rvBaseList);
        codeBaseAdapter = new CodeBaseAdapter(this, mTicketRegular, numberBaseList);
        rvBaseList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvBaseList.setAdapter(codeBaseAdapter);
        tvClear = findView(R.id.tvClear);
        tvConfirm = findView(R.id.tvConfirm);

    }

    @Override
    protected void setListener() {
        tvClear.setOnClickListener(v -> codeBaseAdapter.getSelectedList().clear());
        tvConfirm.setOnClickListener(v -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("codeBase", (Serializable) codeBaseAdapter.getSelectedList());
            intent.putExtras(bundle);
            setResult(RESULT_NUMBER_BASE, intent);
            finish();
        });
    }

}
