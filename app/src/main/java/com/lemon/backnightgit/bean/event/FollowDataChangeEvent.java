package com.lemon.backnightgit.bean.event;

import com.standards.library.model.Event;

/**
 * @author lemon
 * @version v1.0
 * @function <描述功能>
 * @date: 2017/9/12 16:52
 */

public class FollowDataChangeEvent<T> extends Event<T> {
    public FollowDataChangeEvent(T data) {
        super(data);
    }

    public FollowDataChangeEvent(int code, T data) {
        super(code, data);
    }
}
