package com.lemon.backnightgit.bean;

import com.google.gson.annotations.SerializedName;
import com.standards.library.model.BaseInfo;

import java.util.List;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/7 17:31
 */

public class TicketList<T> extends com.standards.library.model.ListData {
    @SerializedName("ret_code")
    public int ret_code;
}
