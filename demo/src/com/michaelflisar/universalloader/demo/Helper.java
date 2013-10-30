package com.michaelflisar.universalloader.demo;

import java.util.concurrent.Callable;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Helper
{
    public static View getTestFragmentView(Activity activity, OnClickListener clickListener)
    {
        LinearLayout v = new LinearLayout(activity);
        v.setBackgroundColor(Color.BLUE);
        v.setOrientation(LinearLayout.VERTICAL);
        TextView tv1 = new TextView(activity);
        TextView tv2 = new TextView(activity);
        Button b1 = new Button(activity);
        Button b2 = new Button(activity);
        Button b3 = new Button(activity);

        b1.setText("reload 1");
        b2.setText("reload 2");
        b3.setText("reload all");

        b1.setId(1);
        b2.setId(2);
        b3.setId(3);

        tv1.setId(android.R.id.text1);
        tv2.setId(android.R.id.text2);

        b1.setOnClickListener(clickListener);
        b2.setOnClickListener(clickListener);
        b3.setOnClickListener(clickListener);

        v.addView(tv1);
        v.addView(tv2);
        v.addView(b1);
        v.addView(b2);
        v.addView(b3);
        
        return v;
    }
    
    public static Callable<Object> getCallable(final int seconds)
    {
        return new Callable<Object>()
        {
            @Override
            public Object call() throws Exception
            {
                Thread.sleep(seconds * 1000);
                return seconds + "s pause finished";
            }
        };
    }
}
