package com.acomlib.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.acomlib.BaseApp;


/**
 * 字体图标
 *
 * <com.xxxxx.xxxxx.TextViewIcon
 *  // android:text="&#xe643;"
 *     android:textColor="#ff0000"
 *     android:textSize="50sp"/>
 */
public class TextViewIcon extends AppCompatTextView {
    public TextViewIcon(Context context) {
        super(context);
        init(context);
    }

    public TextViewIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(BaseApp.getIconfont(context));
    }
}

