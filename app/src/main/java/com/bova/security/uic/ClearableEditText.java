package com.bova.security.uic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.bova.security.R;


/**
 * 带有删除按钮的EditText
 * Created by LZC on 2018/6/26.
 */

public class ClearableEditText extends AppCompatEditText implements TextWatcher {
    private static final int DRAWABLE_LEFT = 0;
    private static final int DRAWABLE_TOP = 1;
    private static final int DRAWABLE_RIGHT = 2;
    private static final int DRAWABLE_BOTTOM = 3;
    private Drawable mClearDrawable;

    private int maxLength;

    private OnTextChangedListener onTextChangedListener;

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearableEditText);
        //第一个参数为属性集合里面的属性，R文件名称：R.styleable+属性集合名称+下划线+属性名称
        //第二个参数为，如果没有设置这个属性，则设置的默认的值
        maxLength = typedArray.getInteger(R.styleable.ClearableEditText_maxLength, -1);
        typedArray.recycle();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mClearDrawable = getResources().getDrawable(R.mipmap.ic_edit_input_clear);
        addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setClearIconVisible(hasFocus() && s.length() > 0);
        if (null != onTextChangedListener) {
            onTextChangedListener.onTextChanged(s.length());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (maxLength == -1)
            return;

        String inputStr = editable.toString().trim();
        byte[] bytes = inputStr.getBytes();
        Log.i("afterTextChanged", "currentLen = " + bytes.length + ", maxLength = " + maxLength);
        if (bytes.length > maxLength) {
            //取前maxLength个字节
            byte[] newBytes = new byte[maxLength];
            for (int i = 0; i < maxLength; i++) {
                newBytes[i] = bytes[i];
            }
            String newStr = new String(newBytes);
            setText(newStr);
            //将光标定位到最后
            Selection.setSelection(getEditableText(), newStr.length());
        }
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setClearIconVisible(focused && length() > 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Drawable drawable = getCompoundDrawables()[DRAWABLE_RIGHT];
                if (drawable != null && event.getX() <= (getWidth() - getPaddingRight())
                        && event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
                    setText("");
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void setClearIconVisible(boolean visible) {
        setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[DRAWABLE_LEFT], getCompoundDrawables()[DRAWABLE_TOP],
                visible ? mClearDrawable : null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
    }

    public void setOnTextChangedListener(OnTextChangedListener listener) {
        this.onTextChangedListener = listener;
    }

    public interface OnTextChangedListener {
        void onTextChanged(int length);
    }
}
