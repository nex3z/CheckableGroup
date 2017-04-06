package com.nex3z.checkablegroup;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.concurrent.atomic.AtomicInteger;

public class CheckableGroup extends LinearLayout {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private OnClickListener mOnClickListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private int mCheckedId = -1;

    public CheckableGroup(Context context) {
        super(context);
        init();
    }

    public CheckableGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mOnClickListener = new OnChildClickListener();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof Checkable) {
            final Checkable checkable = (Checkable) child;
            if (checkable.isChecked()) {
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                setCheckedId(child.getId());
            }
        }

        super.addView(child, index, params);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    public void check(int id) {
        if (id == mCheckedId || findViewById(id) == null) {
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
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View target = findViewById(viewId);
        if (target != null && target instanceof Checkable) {
            ((Checkable) target).setChecked(checked);
        }
    }

    private class OnChildClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof Checkable) {
                if (mCheckedId != -1 && v.getId() != NO_ID) {
                    setCheckedStateForView(mCheckedId, false);
                }
                ((Checkable) v).setChecked(true);
                setCheckedId(v.getId());
            }
        }
    }

    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        public void onChildViewAdded(View parent, View child) {
            if (parent == CheckableGroup.this && child instanceof Checkable) {
                int id = child.getId();
                if (id == View.NO_ID) {
                    if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        id = supportGenerateViewId();
                    } else {
                        id = generateViewId();
                    }
                    child.setId(id);
                }
                child.setOnClickListener(mOnClickListener);
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(View parent, View child) {
            if (parent == CheckableGroup.this && child instanceof Checkable) {
                child.setOnClickListener(null);
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    private static int supportGenerateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CheckableGroup group, int checkedId);
    }

}
