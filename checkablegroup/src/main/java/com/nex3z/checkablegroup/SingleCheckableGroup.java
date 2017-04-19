package com.nex3z.checkablegroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

public class SingleCheckableGroup extends CheckableGroup {
    private static final String LOG_TAG = SingleCheckableGroup.class.getSimpleName();

    protected OnCheckedChangeListener mOnCheckedChangeListener;
    private int mCheckedId = View.NO_ID;

    public SingleCheckableGroup(Context context) {
        super(context);
    }

    public SingleCheckableGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mInitialCheckedId != View.NO_ID) {
            setCheckedStateForView(mInitialCheckedId, true);
            setCheckedId(mInitialCheckedId);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof Checkable) {
            final Checkable checkable = (Checkable) child;
            if (checkable.isChecked()) {
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                if (child.getId() == View.NO_ID) {
                    child.setId(generateIdForView(child));
                }
                setCheckedId(child.getId());
            }
        }

        super.addView(child, index, params);
    }

    @Override
    protected void onChildClick(View child) {
        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }
        ((Checkable) child).setChecked(true);
        setCheckedId(child.getId());

    }

    @Override
    protected <T extends View & Checkable> void onChildCheckedChange(T child, boolean isChecked) {
        if (isChecked) {
            if (mCheckedId != View.NO_ID && mCheckedId != child.getId()) {
                setCheckedStateForView(mCheckedId, false);
            }
            int id = child.getId();
            setCheckedId(id);
        }
    }

    public void check(int id) {
        if (id == mCheckedId) {
            return;
        }
        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }
        setCheckedStateForView(id, true);
        setCheckedId(id);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    private void setCheckedId(int id) {
        mCheckedId = id;
        notifyCheckedChange(mCheckedId);
    }

    private void notifyCheckedChange(int id) {
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, id);
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(SingleCheckableGroup group, int checkedId);
    }
}
