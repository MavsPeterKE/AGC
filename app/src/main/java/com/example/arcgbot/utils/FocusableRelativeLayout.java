package com.example.arcgbot.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class FocusableRelativeLayout extends RelativeLayout {

    public FocusableRelativeLayout(Context context) {
        super(context);
    }

    public FocusableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FocusableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // true if you do not want the children to be clickable.
        return true;
    }
}
