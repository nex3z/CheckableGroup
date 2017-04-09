package com.nex3z.checkablegroup.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
        SingleCheckableGroup passiveSingleCheckableGroup = (SingleCheckableGroup) findViewById(R.id.single_select_group_passive);
        passiveSingleCheckableGroup.setOnCheckedChangeListener(new SingleCheckableGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleCheckableGroup group, int checkedId) {
                Log.v(LOG_TAG, "passiveSingleCheckableGroup: " + checkedId);
            }
        });

        SingleCheckableGroup activeSingleCheckableGroup = (SingleCheckableGroup) findViewById(R.id.single_select_group_active);
        activeSingleCheckableGroup.setOnCheckedChangeListener(new SingleCheckableGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleCheckableGroup group, int checkedId) {
                Log.v(LOG_TAG, "activeSingleCheckableGroup: " + checkedId);
            }
        });

        MultiCheckableGroup passiveMultiCheckableGroup = (MultiCheckableGroup) findViewById(R.id.multi_select_group_passive);
        passiveMultiCheckableGroup.setOnCheckedChangeListener(new MultiCheckableGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedStateChanged(MultiCheckableGroup group, int checkedId, boolean isChecked) {
                Log.v(LOG_TAG, "passiveMultiCheckableGroup: checkedId = " + checkedId + ", isChecked = " + isChecked);
            }
        });

        MultiCheckableGroup activeMultiCheckableGroup = (MultiCheckableGroup) findViewById(R.id.multi_select_group_active);
        activeMultiCheckableGroup.setOnCheckedChangeListener(new MultiCheckableGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedStateChanged(MultiCheckableGroup group, int checkedId, boolean isChecked) {
                Log.v(LOG_TAG, "activeMultiCheckableGroup: checkedId = " + checkedId + ", isChecked = " + isChecked);
            }
        });
    }
}
