package com.nex3z.checkablegroup.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.Checkable;

public class CheckableLabel extends AppCompatTextView implements Checkable {
    private boolean mChecked;
    private int mCheckedTextColor;
    private int mUnchekcedTextColor;
    private Drawable mCheckedDrawable;
    private Drawable mUncheckedDrawable;

    public CheckableLabel(Context context) {
        super(context);
        init();
    }

    public CheckableLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCheckedTextColor = ContextCompat.getColor(getContext(), R.color.colorChecked);
        mUnchekcedTextColor = ContextCompat.getColor(getContext(), R.color.colorUnchecked);
        mCheckedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_round_rect_checked);
        mUncheckedDrawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_round_rect_unchecked);
        setChecked(false);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;

        if (mChecked) {
            setTextColor(mCheckedTextColor);
            setBackgroundDrawable(mCheckedDrawable);
        } else {
            setTextColor(mUnchekcedTextColor);
            setBackgroundDrawable(mUncheckedDrawable);
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
}
