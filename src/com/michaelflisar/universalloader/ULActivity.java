
package com.michaelflisar.universalloader;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.michaelflisar.universalloader.helper.ULHelper;
import com.michaelflisar.universalloader.interfaces.IUniversalLoaderProvider;

public class ULActivity extends FragmentActivity implements IUniversalLoaderProvider
{
    private UniversalLoader mUniversalLoader = null;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mUniversalLoader = ULHelper.getUniversalLoader(this, true);
    }

    // ------------------
    // IUniversalLoaderProvider
    // ------------------

    public UniversalLoader getUniversalLoader()
    {
        return mUniversalLoader;
    }
}
