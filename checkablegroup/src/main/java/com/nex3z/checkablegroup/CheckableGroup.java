package com.nex3z.checkablegroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.concurrent.atomic.AtomicInteger;

abstract class CheckableGroup extends LinearLayout {
    private static final String LOG_TAG = CheckableGroup.class.getSimpleName();
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    private OnClickListener mOnClickListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;
    protected int mInitialCheckedId = View.NO_ID;

    protected abstract void onChildClick(View child);

    public CheckableGroup(Context context) {
        this(context, null);
    }

    public CheckableGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CheckableGroup, 0, 0);
        try {
            int value = a.getResourceId(R.styleable.CheckableGroup_cgCheckedButton, View.NO_ID);
            if (value != View.NO_ID) {
                mInitialCheckedId = value;
            }
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mOnClickListener = new OnChildClickListener();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    protected void setCheckedStateForView(int viewId, boolean checked) {
        View target = findViewById(viewId);
        if (target != null && target instanceof Checkable) {
            ((Checkable) target).setChecked(checked);
        }
    }

    protected void toggleCheckedStateForView(int viewId) {
        View target = findViewById(viewId);
        if (target != null && target instanceof Checkable) {
            ((Checkable) target).toggle();
        }
    }

    private class OnChildClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof Checkable) {
                onChildClick(v);
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
                    id = generateIdForView();
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

    protected int generateIdForView() {
        return android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1
                ? supportGenerateViewId()
                : generateViewId();
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

}
