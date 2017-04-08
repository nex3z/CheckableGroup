package com.nex3z.checkablegroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

public class MultiCheckableGroup extends CheckableGroup {
    private static final String LOG_TAG = MultiCheckableGroup.class.getSimpleName();

    private OnCheckedStateChangeListener mOnCheckedStateChangeListener;
    private boolean mManageChildState = true;

    public MultiCheckableGroup(Context context) {
        super(context);
    }

    public MultiCheckableGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mInitialCheckedId != View.NO_ID) {
            setCheckedStateForView(mInitialCheckedId, true);
        }
    }

    @Override
    protected void onChildClick(View child) {
        if (mManageChildState) {
            ((Checkable) child).toggle();
        }
        notifyCheckedStateChange(child.getId(), ((Checkable) child).isChecked());
    }

    public void check(int id) {
        setCheckedStateForView(id, true);
        notifyCheckedStateChange(id, true);
    }

    public void uncheck(int id) {
        setCheckedStateForView(id, false);
        notifyCheckedStateChange(id, false);
    }

    public void toggle(int id) {
        toggleCheckedStateForView(id);
        View target = findViewById(id);
        if (target instanceof Checkable) {
            notifyCheckedStateChange(id, ((Checkable) target).isChecked());
        }
    }

    public void setOnCheckedChangeListener(OnCheckedStateChangeListener listener) {
        mOnCheckedStateChangeListener = listener;
    }

    private void notifyCheckedStateChange(int id, boolean checked) {
        if (mOnCheckedStateChangeListener != null) {
            mOnCheckedStateChangeListener.onCheckedStateChanged(this, id, checked);
        }
    }

    public interface OnCheckedStateChangeListener {
        void onCheckedStateChanged(MultiCheckableGroup group, int checkedId, boolean checked);
    }
}
