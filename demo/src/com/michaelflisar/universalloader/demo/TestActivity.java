
package com.michaelflisar.universalloader.demo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.michaelflisar.universalloader.ULActivity;
import com.michaelflisar.universalloader.helper.ULDebugger;
import com.michaelflisar.universalloader.helper.ULDebugger.DebugMode;

public class TestActivity extends ULActivity implements OnClickListener
{
    private TestLoaderFragment mFragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        ULDebugger.setDebugger(true, DebugMode.DETAILED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        mFragment = (TestLoaderFragment) getSupportFragmentManager().findFragmentById(R.id.test_fragment);

        Button b = (Button) findViewById(R.id.btReload);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        mFragment.reload();
    }
}
