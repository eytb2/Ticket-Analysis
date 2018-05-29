package cn.lemon.ticketsystem.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.lemon.ticketsystem.BaseFragment;
import cn.lemon.ticketsystem.R;
import cn.lemon.ticketsystem.model.ItemModel;
import cn.lemon.ticketsystem.model.LotteryModel;
import cn.lemon.ticketsystem.model.ResultModel;
import cn.lemon.ticketsystem.utils.CommonUtils;
import cn.lemon.ticketsystem.utils.FileUtil;
import cn.lemon.ticketsystem.utils.KV;
import cn.lemon.ticketsystem.utils.LocalStorageKeys;
import cn.lemon.ticketsystem.utils.RecognizeService;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment2 extends BaseFragment {
    //以下代码为纯java实现，并未依赖第三方框架,具体传入参数请参看接口描述详情页.
    protected android.os.Handler mHandler = new android.os.Handler();
    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
    //解析json数据
    private static com.google.gson.Gson gson;

    private android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;
    private android.support.v7.widget.RecyclerView recyclerview;

    private ListAdapter adapter;

    //保存当前所有Item数据
    private java.util.List<ItemModel> list;

    //当前点击的彩票号所在的位置
    private int currIndex;
    //当前点击的彩票号所在的recyclerView中的位置
    private int currPosition;
    TitleBarUtil mTitleBarUtil;
    //提示视图
    private android.widget.LinearLayout ll_bianhao;
    private android.widget.TextView tvStep2;
    View inflate;

    public BlankFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_blank_fragment2, container, false);

        mTitleBarUtil = (TitleBarUtil) inflate.findViewById(R.id.mTitleBarUtil);
        mTitleBarUtil.setTitle("拍照识别");
        mTitleBarUtil.setTitleColor(Color.parseColor("#ffffff"));
        swipeRefreshLayout = (android.support.v4.widget.SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setEnabled(false);
        recyclerview = (android.support.v7.widget.RecyclerView) inflate.findViewById(R.id.recyclerview);

        initData();
        return inflate;
    }

    public static BlankFragment2 newInstance() {
        BlankFragment2 fragment = new BlankFragment2();
        return fragment;
    }

    private void initData() {
        list = new java.util.ArrayList<>();
        recyclerview.setHasFixedSize(true);
        android.support.v7.widget.LinearLayoutManager linearLayoutManager = new android.support.v7.widget.LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new ListAdapter(getActivity(), list);
        adapter.openLoadAnimation(com.chad.library.adapter.base.BaseQuickAdapter.ALPHAIN);
        adapter.setNotDoAnimationCount(2);
        adapter.setOnItemChildClickListener(new com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(com.chad.library.adapter.base.BaseQuickAdapter adapter, View view, int position) {
                //获取到当前点击的视图的数据
                currPosition = position;
                int index = Integer.parseInt(((android.widget.TextView) view).getText().toString());
                if (view.getId() != R.id.tv_no7) {
                    initDatePickerRed(index);
                } else {
                    initDatePickerBlue(index);
                }
                switch (view.getId()) {
                    case R.id.tv_no1:
                        currIndex = 1;
                        break;
                    case R.id.tv_no2:
                        currIndex = 2;
                        break;
                    case R.id.tv_no3:
                        currIndex = 3;
                        break;
                    case R.id.tv_no4:
                        currIndex = 4;
                        break;
                    case R.id.tv_no5:
                        currIndex = 5;
                        break;
                    case R.id.tv_no6:
                        currIndex = 6;
                        break;
                    case R.id.tv_no7:
                        currIndex = 7;
                        break;
                }
            }
        });

        initHeaderView();

        recyclerview.setAdapter(adapter);
    }

    private void initHeaderView() {
        View view = View.inflate(getActivity(), R.layout.header_recycler, null);
        //第一步：点击按钮将您的彩票拍成照片后进行分析，拍完后请对范围进行截取，只需要包含您购买的号码信息和开奖期，请尽量不要包含其他无用信息，演示示例："
        String step1 = "第一步：点击按钮将您的彩票拍成照片后进行分析，拍完后请对范围进行截取，只需要包含您购买的号码信息和开奖期，请尽量不要包含其他无用信息，演示示例：";
        android.text.SpannableString spannableString = new android.text.SpannableString(step1);
        spannableString.setSpan(new android.text.style.ForegroundColorSpan(android.graphics.Color.parseColor("#ef0000")), step1.length() - 5, step1.length(), android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new android.text.style.ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showCustomDialog();
            }
        }, step1.length() - 5, step1.length(), android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        android.widget.TextView tv_step1 = (android.widget.TextView) view.findViewById(R.id.tv_step1);
        tv_step1.setText(spannableString);
        tv_step1.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

        ll_bianhao = (android.widget.LinearLayout) view.findViewById(R.id.ll_bianhao);
        tvStep2 = (android.widget.TextView) view.findViewById(R.id.tv_step2);
        view.findViewById(R.id.tv_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KV.get(LocalStorageKeys.IS_FIRST, true)) {
                    //第一次的话先显示说明
                    showCustomDialog();
                    KV.put(LocalStorageKeys.IS_FIRST, false);
                } else {
                    ll_bianhao.setVisibility(View.GONE);
                    tvStep2.setVisibility(View.GONE);
                    list.clear();
                    adapter.notifyDataSetChanged();
                    openCamera();
                }
            }
        });
        adapter.setHeaderView(view);
    }

    //打开相机
    private void openCamera() {
        if (!checkTokenStatus()) {
            return;
        }
        android.content.Intent intent = new android.content.Intent(getActivity(), com.baidu.ocr.ui.camera.CameraActivity.class);
        intent.putExtra(com.baidu.ocr.ui.camera.CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getActivity()).getAbsolutePath());
        intent.putExtra(com.baidu.ocr.ui.camera.CameraActivity.KEY_CONTENT_TYPE, com.baidu.ocr.ui.camera.CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
    }

    class ListAdapter extends com.chad.library.adapter.base.BaseMultiItemQuickAdapter<ItemModel, com.chad.library.adapter.base.BaseViewHolder> {

        public ListAdapter(android.content.Context context, java.util.List<ItemModel> data) {
            super(data);
            addItemType(ItemModel.TYPE_NO, R.layout.item_reclcyerview_no);
            addItemType(ItemModel.TYPE_DONE, R.layout.item_reclcyerview_done);
            addItemType(ItemModel.TYPE_STEP3, R.layout.item_reclcyerview_step3);
            addItemType(ItemModel.TYPE_STEP4, R.layout.item_reclcyerview_step4);
        }

        @Override
        protected void convert(final com.chad.library.adapter.base.BaseViewHolder helper, final ItemModel item) {
            if (item.getType() == ItemModel.TYPE_NO) {
                //正常号码
                helper.setText(R.id.tv_bianhao, item.getIndex());
                helper.setText(R.id.tv_no1, item.getNo1());
                helper.setText(R.id.tv_no2, item.getNo2());
                helper.setText(R.id.tv_no3, item.getNo3());
                helper.setText(R.id.tv_no4, item.getNo4());
                helper.setText(R.id.tv_no5, item.getNo5());
                helper.setText(R.id.tv_no6, item.getNo6());
                helper.setText(R.id.tv_no7, item.getNo7());
                helper.setText(R.id.tv_num, item.getNum());

                helper.addOnClickListener(R.id.tv_no1);
                helper.addOnClickListener(R.id.tv_no2);
                helper.addOnClickListener(R.id.tv_no3);
                helper.addOnClickListener(R.id.tv_no4);
                helper.addOnClickListener(R.id.tv_no5);
                helper.addOnClickListener(R.id.tv_no6);
                helper.addOnClickListener(R.id.tv_no7);

            } else if (item.getType() == ItemModel.TYPE_STEP3) {
                //第三步
                if (!android.text.TextUtils.isEmpty(item.getKjq())) {
                    helper.setText(R.id.tv_kjq, item.getKjq());
                }

                //最新一期
                helper.getView(R.id.tv_newest).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doPost("", new PostResultListener() {
                            @Override
                            public void result(LotteryModel result) {
                                if (result.getShowapi_res_body() != null) {
                                    final LotteryModel.ShowapiResBodyBean.ResultBean result1 = result.getShowapi_res_body().getResult();
                                    showMsgDialog("最新一期是" + result1.getExpect() + ",填入？", new MyAlertDialog.OnPositiveClickListener() {
                                        @Override
                                        public void onPositiveClickListener(View v) {
                                            helper.setText(R.id.tv_kjq, result1.getExpect());
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

                //分析
                helper.getView(R.id.tv_fenxi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String kjq = ((android.widget.EditText) helper.getView(R.id.tv_kjq)).getText().toString();
                        if (!android.text.TextUtils.isEmpty(kjq) && kjq.length() == 7) {
                            //删除之前的数据
                            java.util.Iterator<ItemModel> it = list.iterator();
                            while (it.hasNext()) {
                                ItemModel x = it.next();
                                if (x.getType() == ItemModel.TYPE_DONE || x.getType() == ItemModel.TYPE_STEP4) {
                                    it.remove();
                                }
                            }
                            CommonUtils.resetData(list);
                            adapter.notifyDataSetChanged();

                            analyse(kjq);
                        } else {
                            android.widget.Toast.makeText(mContext, "开奖期号格式有误，格式:2018027", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (item.getType() == ItemModel.TYPE_DONE) {
                helper.setText(R.id.tv_bianhao, item.getIndex());
                helper.setText(R.id.tv_no1, item.getNo1());
                helper.setText(R.id.tv_no2, item.getNo2());
                helper.setText(R.id.tv_no3, item.getNo3());
                helper.setText(R.id.tv_no4, item.getNo4());
                helper.setText(R.id.tv_no5, item.getNo5());
                helper.setText(R.id.tv_no6, item.getNo6());
                helper.setText(R.id.tv_no7, item.getNo7());
                helper.setText(R.id.tv_num, item.getNum());
                helper.setText(R.id.tv_result, "分析结果：" + item.getResult());

                //显示
                setViewShow(item.getIndexs(), helper);
            } else if (item.getType() == ItemModel.TYPE_STEP4) {
                //第四步
                helper.setText(R.id.tv_tips_qihao, item.getKjq() + "期中奖号码为");
                helper.setText(R.id.tv_haoma, item.getOpenCode().replaceAll(",", "、"));
            }
        }

        private void setViewShow(java.util.List<Integer> index, com.chad.library.adapter.base.BaseViewHolder helper) {
            //先隐藏所有勾
            helper.setVisible(R.id.iv_1, false);
            helper.setVisible(R.id.iv_2, false);
            helper.setVisible(R.id.iv_3, false);
            helper.setVisible(R.id.iv_4, false);
            helper.setVisible(R.id.iv_5, false);
            helper.setVisible(R.id.iv_6, false);
            helper.setVisible(R.id.iv_7, false);
            for (int i : index) {
                switch (i + 1) {
                    case 1:
                        helper.setVisible(R.id.iv_1, true);
                        break;
                    case 2:
                        helper.setVisible(R.id.iv_2, true);
                        break;
                    case 3:
                        helper.setVisible(R.id.iv_3, true);
                        break;
                    case 4:
                        helper.setVisible(R.id.iv_4, true);
                        break;
                    case 5:
                        helper.setVisible(R.id.iv_5, true);
                        break;
                    case 6:
                        helper.setVisible(R.id.iv_6, true);
                        break;
                    case 7:
                        helper.setVisible(R.id.iv_7, true);
                        break;
                }
            }

        }

    }

    //分析结果
    private void analyse(final String kjq) {
        doPost(kjq, new PostResultListener() {
            @Override
            public void result(LotteryModel result) {
                if (result.getShowapi_res_body() != null) {
                    LotteryModel.ShowapiResBodyBean.ResultBean result1 = result.getShowapi_res_body().getResult();
                    if (result1 != null) {
                        //有数据
                        if (kjq.equals(result1.getExpect())) {
                            //分析中奖号码 02,07,09,14,18,28+05
                            String openCode = result1.getOpenCode();
                            String[] split = openCode.split(",");
                            java.util.List<String> aims = new java.util.ArrayList<>();
                            String lanHao = "";
                            for (int i = 0; i < split.length; i++) {
                                if (i < split.length - 1) {
                                    aims.add(split[i]);
                                } else {
                                    //最后一个
                                    aims.add(split[i].split("\\+")[0]);
                                    lanHao = split[i].split("\\+")[1];
                                }
                            }
                            //计算结果
                            calcu(aims, lanHao, openCode, kjq);
                            return;
                        }
                    }
                }
                android.widget.Toast.makeText(getActivity(), "未查到该期彩票，请检查期号", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    //计算最终结果
    private void calcu(java.util.List<String> aims, String lanHao, String openCode, String kjq) {
        list.add(new ItemModel(ItemModel.TYPE_STEP4, openCode, kjq));
        //获取public static final int TYPE_NO = 0的集合
        java.util.List<ItemModel> temp = new java.util.ArrayList<>();
        ItemModel tempItem = null;
        for (ItemModel itemModel : list) {
            if (itemModel.getType() == ItemModel.TYPE_NO) {
                tempItem = new ItemModel();
                CommonUtils.copyProperties(tempItem, itemModel);
                tempItem.setType(ItemModel.TYPE_DONE);
                temp.add(tempItem);
            }
        }
        int[] nums = new int[temp.size()];
        int[] results = new int[temp.size()];
        java.util.List<Integer>[] indexs = new java.util.ArrayList[temp.size()];
        for (int i = 0; i < indexs.length; i++) {
            indexs[i] = new java.util.ArrayList<Integer>();
        }
        for (int i = 0; i < aims.size(); i++) {
            for (int k = 0; k < temp.size(); k++) {
                ItemModel itemModel = temp.get(k);
                HH:
                for (int j = 0; j < itemModel.getList().size(); j++) {
                    if (itemModel.getList().get(j).equals(aims.get(i))) {
                        nums[k]++;
                        indexs[k].add(j);
                        break HH;
                    }
                }
                if (lanHao.equals(itemModel.getNo7())) {
                    //蓝号正确
                    results[k] = 1;
                    indexs[k].add(6);
                } else {
                    //蓝号不正确
                    results[k] = 0;
                }
            }
        }
        for (int i = 0; i < results.length; i++) {
            String result = nums[i] + "+" + results[i] + "(" + CommonUtils.lotteryResult(nums[i], results[i]) + ")";
            ItemModel itemModel = temp.get(i);
            itemModel.setIndexs(indexs[i]);
            itemModel.setResult(result);
        }
        list.addAll(temp);
        adapter.notifyDataSetChanged();

        recyclerview.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 识别成功回调，通用文字识别（高精度版）
        if (requestCode == REQUEST_CODE_ACCURATE_BASIC && resultCode == android.app.Activity.RESULT_OK) {
            times = 0;
            getPicText();
        }
    }

    private int times;

    private void getPicText() {
        showDialog();
        RecognizeService.recAccurateBasic(FileUtil.getSaveFile(getActivity()).getAbsolutePath(), new RecognizeService.ServiceListener<ResultModel>() {
                    @Override
                    public void onResult(ResultModel result) {
                        times = 0;
                        formatData(result);
                        stopDialog();
                    }

                    @Override
                    public void onError(String result) {
                        times++;
                        if (times < 3) {
                            getPicText();
                        } else {
                            android.widget.Toast.makeText(getActivity(), "网络错误，请重试", android.widget.Toast.LENGTH_SHORT).show();
                            stopDialog();
                        }
                    }
                }

        );
    }

    //解析数据
    private void formatData(ResultModel result) {
        java.util.List<String> listNums = new java.util.ArrayList<>();
        //在这里我需要获取到开奖期 和 购买的几组号码
        //找到开奖期:
        String kjq = null;
        for (ResultModel.WordsResultBean wordsResultBean : result.getWords_result()) {
//            Log.e("HHHHHHHH", wordsResultBean.getWords());
            if (matcher(wordsResultBean.getWords()) != null) {
                //匹配到了
                listNums.add(wordsResultBean.getWords());
            } else {
                if (!android.text.TextUtils.isEmpty(wordsResultBean.getWords())) {
                    if (wordsResultBean.getWords().startsWith("开奖期")) {
                        kjq = matcher(wordsResultBean.getWords(), 1);
                    }
                }
            }
        }

        //构造数据
        for (int i = 0; i < listNums.size(); i++) {
            String content = listNums.get(i);
            //先获取各个号码
            String nums = "";
            if (content.contains("x")) {
                nums = content.substring(content.indexOf("x") + 1);
            } else if (content.contains("X")) {
                nums = content.substring(content.indexOf("X") + 1);
            } else {
                if (content.contains("+")) {
                    nums = content.substring(17, content.length());
                } else {
                    nums = content.substring(16, content.length());
                }
            }

            list.add(i, new ItemModel(content.substring(2, 4), content.substring(4, 6), content.substring(6, 8), content.substring(8, 10), content.substring(10, 12), content.substring(12, 14), content.contains("+") ? content.substring(15, 17) : content.substring(14, 16), content.substring(0, 2), nums));
        }
        //显示第二步视图
        ll_bianhao.setVisibility(View.VISIBLE);
        tvStep2.setVisibility(View.VISIBLE);

        list.add(new ItemModel(ItemModel.TYPE_STEP3, kjq));
        //添加开奖期这个
        adapter.notifyDataSetChanged();
    }

 /*   @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        android.view.MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.option_normal_1:
                android.content.Intent intent1 = new android.content.Intent(android.content.Intent.ACTION_SEND);
                intent1.putExtra(android.content.Intent.EXTRA_TEXT, "给你分享一个免费好用的福彩对号助手，复制链接去浏览器打开下载使用吧！链接：" +
                        "http://jokesimg.cretinzp.com/common/lotteryhelper/apk/fcdhzs.apk");
                intent1.setType("text/plain");
                startActivity(android.content.Intent.createChooser(intent1, "share"));
                return true;
            case R.id.option_normal_2:
                showMsgDialog("使用中遇到什么问题，请联系\nmxnzp_life@163.com寻求帮助\n希望你早日中大奖", new MyAlertDialog.OnPositiveClickListener() {
                    @Override
                    public void onPositiveClickListener(View v) {

                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    /**
     * 正则表达式获取购买的号码
     *
     * @param content
     * @param type    0 购买的号 1 获取开奖期
     * @return
     */
    private String matcher(String content, int type) {
        content = content.replaceAll(" ", "");
        String pattern = "^[A-Z].[0-9]{12}\\+?[0-9]{2}x?[0-9]+$";
        if (type == 1) {
            pattern = "[0-9]{7}";
        }
        java.util.regex.Pattern test_ = java.util.regex.Pattern.compile(pattern, java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = test_.matcher(content);
        if (matcher.find()) {
            if (type == 1) {
                return matcher.group();
            }
            //找到了
            return content;
        } else {
            return null;
        }
    }

    private String matcher(String content) {
        return matcher(content, 0);
    }


    private boolean checkTokenStatus() {
        if (!TheApplication.hasGotToken) {
            if (TheApplication.times < 3) {
                android.widget.Toast.makeText(getActivity(), "token还未成功获取,请稍后", android.widget.Toast.LENGTH_LONG).show();
            } else {
                android.widget.Toast.makeText(getActivity(), "token未能成功获取,请杀掉APP后重启APP", android.widget.Toast.LENGTH_LONG).show();
            }
        }
        return TheApplication.hasGotToken;
    }

/*    @Override
    private void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        if ( com.baidu.ocr.sdk.OCR.getInstance() != null )
            com.baidu.ocr.sdk.OCR.getInstance().release();
    }*/


    private void doPost(final String expect, final PostResultListener listener) {
        showDialog();
        new Thread() {
            //在新线程中发送网络请求
            public void run() {
                final String res = new com.show.api.ShowApiRequest("http://route.showapi.com/44-3", Console.APP_ID_API, Console.APP_SECRET_API).addTextPara("code", "ssq").addTextPara("expect", expect).post();
                //把返回内容通过handler对象更新到界面
                mHandler.post(new Thread() {
                    public void run() {
                        if (gson == null) {
                            gson = new com.google.gson.Gson();
                        }
                        listener.result(gson.fromJson(res, LotteryModel.class));
                        stopDialog();
                    }
                });
            }
        }.start();
    }

    interface PostResultListener {
        void result(LotteryModel result);
    }

    public void showCustomDialog() {
        View view = View.inflate(getActivity(), R.layout.layout_shuoming, null);
        final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity(), view);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertDialog.dismiss();
            }
        });
        myAlertDialog.show();
        android.view.WindowManager windowManager = getActivity().getWindowManager();
        android.view.Display display = windowManager.getDefaultDisplay();
        android.view.WindowManager.LayoutParams lp = myAlertDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() - 200); //设置宽度
        myAlertDialog.getWindow().setAttributes(lp);
    }

    public void showMsgDialog(String msg, MyAlertDialog.OnPositiveClickListener listener) {
        final MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity(), "提示", msg);
        myAlertDialog.setOnClickListener(listener);
        myAlertDialog.show();
        android.view.WindowManager windowManager = getActivity().getWindowManager();
        android.view.Display display = windowManager.getDefaultDisplay();
        android.view.WindowManager.LayoutParams lp = myAlertDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() - 200); //设置宽度
        myAlertDialog.getWindow().setAttributes(lp);
    }

    //开启红号的滑动选择器
    private void initDatePickerRed(int index) {
        final java.util.ArrayList<String> listStr = new java.util.ArrayList<>();
        for (int i = 1; i <= 33; i++) {
            String s = "";
            if (i < 10) {
                s = "0" + i;
            } else {
                s = i + "";
            }
            listStr.add(s);
        }
        cn.addapp.pickers.picker.SinglePicker<String> pickerRed = new cn.addapp.pickers.picker.SinglePicker<>(getActivity(), listStr);
        pickerRed.setCanLoop(false);//不禁用循环
        pickerRed.setLineVisible(true);
        pickerRed.setTextSize(18);
        pickerRed.setTitleText("原始:" + (index < 10 ? ("0" + index) : index));
        pickerRed.setSelectedIndex(index - 1);
        pickerRed.setWheelModeEnable(false);
        //启用权重 setWeightWidth 才起作用
        pickerRed.setWeightEnable(true);
        pickerRed.setWeightWidth(1);
        pickerRed.setSelectedTextColor(android.graphics.Color.GREEN);//前四位值是透明度
        pickerRed.setUnSelectedTextColor(android.graphics.Color.GRAY);
        pickerRed.setOnItemPickListener(new cn.addapp.pickers.listeners.OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                ItemModel itemModel = list.get(currPosition);
                CommonUtils.setData(currIndex, itemModel, item);
                adapter.notifyDataSetChanged();
            }
        });
        pickerRed.show();
    }

    //开启蓝号的滑动选择器
    private void initDatePickerBlue(int index) {
        java.util.ArrayList<String> list1 = new java.util.ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            String s = "";
            if (i < 10) {
                s = "0" + i;
            } else {
                s = i + "";
            }
            list1.add(s);
        }
        cn.addapp.pickers.picker.SinglePicker<String> pickerBlue = new cn.addapp.pickers.picker.SinglePicker<>(getActivity(), list1);
        pickerBlue.setCanLoop(false);//不禁用循环
        pickerBlue.setLineVisible(true);
        pickerBlue.setTextSize(18);
        pickerBlue.setSelectedIndex(index - 1);
        pickerBlue.setTitleText("原始:" + (index < 10 ? ("0" + index) : index));
        pickerBlue.setWheelModeEnable(false);
        //启用权重 setWeightWidth 才起作用
        pickerBlue.setWeightEnable(true);
        pickerBlue.setWeightWidth(1);
        pickerBlue.setSelectedTextColor(android.graphics.Color.GREEN);//前四位值是透明度
        pickerBlue.setUnSelectedTextColor(android.graphics.Color.GRAY);
        pickerBlue.setOnItemPickListener(new cn.addapp.pickers.listeners.OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                ItemModel itemModel = list.get(currPosition);
                CommonUtils.setData(currIndex, itemModel, item);
                adapter.notifyDataSetChanged();
            }
        });
        pickerBlue.show();
    }

}
