package com.nex3z.checkablegroup.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.nex3z.checkablegroup.CheckedStateObservable;
import com.nex3z.checkablegroup.OnCheckedChangeListener;

public class ActiveCheckableLabel extends AppCompatButton implements Checkable, CheckedStateObservable {
    private boolean mChecked;
    private int mCheckedTextColor;
    private int mUnchekcedTextColor;
    private Drawable mCheckedDrawable;
    private Drawable mUncheckedDrawable;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public ActiveCheckableLabel(Context context) {
        this(context, null);
    }

    public ActiveCheckableLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCheckedTextColor = ContextCompat.getColor(getContext(), R.color.colorChecked);
        mUnchekcedTextColor = ContextCompat.getColor(getContext(), R.color.colorUnchecked);
        mCheckedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_round_rect_checked);
        mUncheckedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_round_rect_unchecked);

        mChecked = false;
        setTextColor(mUnchekcedTextColor);
        setBackgroundDrawable(mUncheckedDrawable);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;

            if (mChecked) {
                setTextColor(mCheckedTextColor);
                setBackgroundDrawable(mCheckedDrawable);
            } else {
                setTextColor(mUnchekcedTextColor);
                setBackgroundDrawable(mUncheckedDrawable);
            }

            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }
}