package com.bova.security.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewTemplate extends SurfaceView{

    private SurfaceHolder.Callback callback;

    public SurfaceViewTemplate(Context context) {
        this(context, null);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView((SurfaceHolder.Callback) context);
    }

    /**
     * 初始化View
     */
    private void initView(SurfaceHolder.Callback callback){
        SurfaceHolder mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(callback);
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);
    }

    public SurfaceHolder.Callback getCallback() {
        return callback;
    }

    public void setCallback(SurfaceHolder.Callback callback) {
        this.callback = callback;
    }
}