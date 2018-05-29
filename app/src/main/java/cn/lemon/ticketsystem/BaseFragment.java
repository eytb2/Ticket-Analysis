package cn.lemon.ticketsystem;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    private cn.lemon.ticketsystem.ui.CustomProgressDialog dialog;

    /**
     * 显示加载对话框
     *
     * @param msg
     */
    public void showDialog(String msg, boolean cancelable) {
        if ( dialog == null ) {
            dialog = cn.lemon.ticketsystem.ui.CustomProgressDialog.createDialog(getActivity());
        }
        if ( msg != null && !msg.equals("") ) {
            dialog.setMessage(msg);
        }
        dialog.setCancelable(cancelable);
        dialog.show();
    }

    /**
     * 显示加载对话框
     *
     * @param msg
     */
    public void showDialog(String msg) {
        showDialog(msg, true);
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        showDialog("正在加载中...", true);
    }

    /**
     * 关闭对话框
     */
    public void stopDialog() {
        if ( dialog != null && dialog.isShowing() ) {
            dialog.dismiss();
        }
    }
}
