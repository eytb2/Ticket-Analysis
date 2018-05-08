package cn.lemon.ticketsystem.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.lemon.ticketsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {
    private View mView;
    TitleBar mTitle;

    DrawableTextView mRandomBet;
    //前区号码
    private MyBaseAdapter mAdapter;
    //后区号码
    private MyLatterBaseAdapter mLatterAdapter;

    private static final int MIN_NUMBERS = 5;
    private static final int LATTER_MIN_NUMBERS = 2;
    private static final int MAX_NUMBERS = 18;
    private static final int LATTER_MAX_NUMBERS = 12;
    private static final int SUM_NUMBERS = 35;
    private static final int LATTER_SUM_NUMBERS = 12;
    //机选次数
    private static final int SUM_SELECT = 10;

    //当前自动选到第几次
    private int currentSelectNum = 0;

    //保存前区选择的结果(存取不会重复,方便存删)
    private Set<Integer> mResult;
    //保存后区选择的结果(存取不会重复,方便存删)
    private Set<Integer> mLatterResult;

    //机选的类型
    private int randomType = -1;
    //机选三分之一
    private static final int TYPE_ONE_THIRD = 1;
    //机选二分之一
    private static final int TYPE_TWO_THIRD = 2;
    //机选全部金额
    private static final int TYPE_ALL = 3;
    //机选一注（摇一摇）
    private static final int TYPE_ONLY_ONE = 4;
    private String mPeriodId;
    GridView mNumbersContainer;
    GridView mLatterNumbersContainer;

    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_one, container, false);
        // Inflate the layout for this fragment
        mTitle= (TitleBar) mView.findViewById(R.id.title);
        mTitle.setRightButtonEnable(false);
        mNumbersContainer= (GridView) mView.findViewById(R.id.numbersContainer);
        mLatterNumbersContainer= (GridView) mView.findViewById(R.id.latterNumbersContainer);
        mRandomBet= (DrawableTextView) mView.findViewById(R.id.randomBet);
        init();
        getData();


        mView.findViewById(R.id.randomBet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomSelectNumber();
            }
        });

        mView.findViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认选择
                if (mResult.size() < MIN_NUMBERS || mLatterResult.size() < LATTER_MIN_NUMBERS) {
                    Toast.makeText(getActivity(), "前区至少选" + MIN_NUMBERS + "个号码," + "后区至少选" + LATTER_MIN_NUMBERS + "个号码哦~", Toast.LENGTH_SHORT).show();
                    return;
                }
                fakeHttpRequest();
            }
        });
        return mView;
    }

    private void fakeHttpRequest() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(getActivity(), "");
        progressDialog.show();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
               getActivity(). runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(TheApplication.getContext(), "请前往当地彩票店购买", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 1000);
    }


    public void clearAlreadySelect() {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            LinearLayout layout = (LinearLayout) mNumbersContainer.getChildAt(i);
            layout.getChildAt(0).setSelected(false);
        }
        mResult.clear();
        for (int i = 0; i < mLatterAdapter.getCount(); i++) {
            LinearLayout layout = (LinearLayout) mLatterNumbersContainer.getChildAt(i);
            layout.getChildAt(0).setSelected(false);
        }
        mLatterResult.clear();
    }

      private void randomSelectNumber() {
        //防止同一时间多次点击
        mRandomBet.setEnabled(false);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentSelectNum > SUM_SELECT) {
                            timer.cancel();
                            currentSelectNum = 0;
                            mRandomBet.setEnabled(true);
                        }
                        //先清空已选的
                        clearAlreadySelect();
                        //按照最少个数去选前区号
                        int i = 0;
                        while (i < MIN_NUMBERS) {
                            int position = new Random().nextInt(SUM_NUMBERS);
                            LinearLayout layout = (LinearLayout) mNumbersContainer.getChildAt(position);
                            TextView number = (TextView) layout.getChildAt(0);
                            if (number.isSelected()) {
                                continue;
                            }
                            number.setSelected(true);
                            int ballInt = Integer.parseInt(number.getText().toString());
                            mResult.add(ballInt);
                            i++;
                        }
                        //按照最少个数去选后区号
                        int j = 0;
                        while (j < LATTER_MIN_NUMBERS) {
                            int position = new Random().nextInt(LATTER_SUM_NUMBERS);
                            LinearLayout layout = (LinearLayout) mLatterNumbersContainer.getChildAt(position);
                            TextView number = (TextView) layout.getChildAt(0);
                            if (number.isSelected()) {
                                continue;
                            }
                            number.setSelected(true);
                            int ballInt = Integer.parseInt(number.getText().toString());
                            mLatterResult.add(ballInt);
                            j++;
                        }
                        currentSelectNum++;
                    }
                });
            }
        }, 0, 100);

    }

    private void getData() {
        final Dialog progressDialog = TheApplication.createLoadingDialog(getActivity(), "");
        progressDialog.show();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 1000);
    }

    public static OneFragment newInstance() {
        OneFragment fragment = new OneFragment();
        return fragment;
    }

    private void init() {

        mResult = new HashSet();
        mLatterResult = new HashSet();
        mAdapter = new MyBaseAdapter();
        mNumbersContainer.setAdapter(mAdapter);

        mLatterAdapter = new MyLatterBaseAdapter();
        mLatterNumbersContainer.setAdapter(mLatterAdapter);

    }

    class MyBaseAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return SUM_NUMBERS;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.number_item, null);
            final TextView number = (TextView) view.findViewById(R.id.text);
            int index = position + 1;
            if (index < 10) {
                number.setText("0" + index);
            } else {
                number.setText("" + index);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //当点击的是未选中的时候判断是否超出最大个数
                    if (mResult.size() >= MAX_NUMBERS && !number.isSelected()) {
                        Toast.makeText(getActivity(), "最多只能选" + MAX_NUMBERS + "个号码哦~", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //添加点击事件
                    if (number.isSelected()) {
                        number.setSelected(false);
                        int ballInt = Integer.parseInt(number.getText().toString());
                        mResult.remove(ballInt);
                    } else {
                        number.setSelected(true);
                        int ballInt = Integer.parseInt(number.getText().toString());
                        mResult.add(ballInt);
                    }

                }
            });

            return view;
        }

    }

    private class MyLatterBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return LATTER_SUM_NUMBERS;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.latter_number_item, null);
            final TextView number = (TextView) view.findViewById(R.id.text);
            int index = position + 1;
            if (index < 10) {
                number.setText("0" + index);
            } else {
                number.setText("" + index);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //当点击的是未选中的时候判断是否超出最大个数
                    if (mLatterResult.size() >= LATTER_MAX_NUMBERS && !number.isSelected()) {
                        Toast.makeText(getActivity(), "最多只能选" + LATTER_MAX_NUMBERS + "个号码哦~", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //添加点击事件
                    if (number.isSelected()) {
                        number.setSelected(false);
                        int ballInt = Integer.parseInt(number.getText().toString());
                        mLatterResult.remove(ballInt);
                    } else {
                        number.setSelected(true);
                        int ballInt = Integer.parseInt(number.getText().toString());
                        mLatterResult.add(ballInt);

                    }
                }
            });

            return view;
        }

    }

}
