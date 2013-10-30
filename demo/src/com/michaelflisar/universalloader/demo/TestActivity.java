
package com.michaelflisar.universalloader.demo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.michaelflisar.universalloader.ULActivity;
import com.michaelflisar.universalloader.helper.ULDebugger;
import com.michaelflisar.universalloader.helper.ULDebugger.DEBUG_MODE;

public class TestActivity extends ULActivity implements OnClickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        ULDebugger.setDebugger(true, DEBUG_MODE.DETAILED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        Button b = (Button) findViewById(R.id.btReload);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        getUniversalLoader().restartLoader(TestLoaderFragment.KEY, null);
    }
}
