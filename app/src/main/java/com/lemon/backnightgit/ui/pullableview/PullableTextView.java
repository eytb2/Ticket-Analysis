package com.lemon.backnightgit.ui.pullableview;


import android.content.Context;
import android.util.AttributeSet;

import com.lemon.backnightgit.ui.Pullable;

public class PullableTextView extends android.support.v7.widget.AppCompatTextView implements Pullable
{

	public PullableTextView(Context context)
	{
		super(context);
	}

	public PullableTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		return true;
	}

	@Override
	public boolean canPullUp()
	{
		return true;
	}

}
