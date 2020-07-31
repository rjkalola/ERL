package com.app.erl.util;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


public class PagerImageViewByWidth extends AppCompatImageView {

    public PagerImageViewByWidth(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerImageViewByWidth(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int) ((float) width * 0.65));
    }

}
