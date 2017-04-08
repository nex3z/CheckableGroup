package com.nex3z.checkablegroup.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.nex3z.checkablegroup.MultiCheckableGroup;
import com.nex3z.checkablegroup.SingleCheckableGroup;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCheckableButtonGroup();
    }

    private void initCheckableButtonGroup() {
        SingleCheckableGroup single = (SingleCheckableGroup) findViewById(R.id.single_select_group);
        single.setOnCheckedChangeListener(new SingleCheckableGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleCheckableGroup group, int checkedId) {
                Log.v(LOG_TAG, "onCheckedChanged(): checkedId = " + checkedId);
            }
        });

        ToggleButton tb = new ToggleButton(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        tb.setLayoutParams(params);
        tb.setChecked(true);
        single.addView(tb);

        MultiCheckableGroup multi = (MultiCheckableGroup) findViewById(R.id.multi_select_group);
        multi.setOnCheckedChangeListener(new MultiCheckableGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedStateChanged(MultiCheckableGroup group, int checkedId,
                                              boolean checked) {
                Log.v(LOG_TAG, "onCheckedStateChanged(): checkId = " + checkedId
                        + ", checked  = " + checked);
            }
        });
    }
}
