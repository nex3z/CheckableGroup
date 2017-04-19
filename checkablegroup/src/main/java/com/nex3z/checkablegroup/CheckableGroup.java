package com.nex3z.checkablegroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

abstract class CheckableGroup extends LinearLayout {
    private static final String LOG_TAG = CheckableGroup.class.getSimpleName();

    private OnClickListener mOnClickListener;
    private OnCheckedChangeListener mCheckedStateListener;
    private CompoundButton.OnCheckedChangeListener mCompoundButtonStateListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;
    protected int mInitialCheckedId = View.NO_ID;
    protected boolean mPassiveMode = false;

    protected abstract void onChildClick(View child);
    protected abstract <T extends View & Checkable> void onChildCheckedChange(T child,
                                                                              boolean isChecked);

    public CheckableGroup(Context context) {
        this(context, null);
    }

    public CheckableGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CheckableGroup, 0, 0);
        try {
            mInitialCheckedId = a.getResourceId(R.styleable.CheckableGroup_cgCheckedButton, View.NO_ID);
            mPassiveMode = a.getBoolean(R.styleable.CheckableGroup_cgPassiveMode, false);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
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

    private class CheckedStateTracker implements OnCheckedChangeListener {
        @Override
        public <T extends View & Checkable> void onCheckedChanged(T view, boolean isChecked) {
            onChildCheckedChange(view, isChecked);
        }
    }

    private class CompoundButtonStateTracker implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            onChildCheckedChange(buttonView, isChecked);
        }
    }

    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        public void onChildViewAdded(View parent, View child) {
            if (parent == CheckableGroup.this && child instanceof Checkable) {
                if (child.getId() == View.NO_ID) {
                    child.setId(generateIdForView(child));
                };
                if (!mPassiveMode) {
                    if (mOnClickListener == null) {
                        mOnClickListener = new OnChildClickListener();
                    }
                    child.setOnClickListener(mOnClickListener);
                } else if (child instanceof CheckedStateObservable) {
                    if (mCheckedStateListener == null) {
                        mCheckedStateListener = new CheckedStateTracker();
                    }
                    ((CheckedStateObservable) child)
                            .setOnCheckedChangeListener(mCheckedStateListener);
                } else if (child instanceof CompoundButton) {
                    if (mCompoundButtonStateListener == null) {
                        mCompoundButtonStateListener = new CompoundButtonStateTracker();
                    }
                    ((CompoundButton) child)
                            .setOnCheckedChangeListener(mCompoundButtonStateListener);
                }
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(View parent, View child) {
            if (parent == CheckableGroup.this && child instanceof Checkable) {
                if (!mPassiveMode) {
                    child.setOnClickListener(null);
                } else if (child instanceof CheckedStateObservable) {
                    ((CheckedStateObservable) child)
                            .setOnCheckedChangeListener(null);
                } else if (child instanceof CompoundButton) {
                    ((CompoundButton) child).setOnCheckedChangeListener(null);
                }
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    protected int generateIdForView(View view) {
        return android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1
                ? view.hashCode()
                : generateViewId();
    }

}
