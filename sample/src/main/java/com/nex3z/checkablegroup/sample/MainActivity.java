package com.nex3z.checkablegroup.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.nex3z.checkablegroup.CheckableGroup;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCheckableButtonGroup();
    }

    private void initCheckableButtonGroup() {
        CheckableGroup group = (CheckableGroup) findViewById(R.id.cg_button_group);
        group.setOnCheckedChangeListener(new CheckableGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CheckableGroup group, int checkedId) {
                Log.v(LOG_TAG, "onCheckedChanged(): checkedId = " + checkedId);
            }
        });

        ToggleButton tb = new ToggleButton(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        tb.setLayoutParams(params);
        tb.setChecked(true);
        group.addView(tb);
    }
}
