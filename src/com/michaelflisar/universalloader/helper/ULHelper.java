
package com.michaelflisar.universalloader.helper;

import android.support.v4.app.FragmentActivity;

import com.michaelflisar.universalloader.UniversalLoader;

public class ULHelper
{
    public static UniversalLoader getUniversalLoader(FragmentActivity activity, boolean createIfNotexists)
    {
        UniversalLoader universalLoader = (UniversalLoader) activity.getSupportFragmentManager().findFragmentByTag(UniversalLoader.class.getName());
        if (universalLoader == null && createIfNotexists)
        {
            universalLoader = new UniversalLoader();
            activity.getSupportFragmentManager().beginTransaction().add(universalLoader, UniversalLoader.class.getName()).commitAllowingStateLoss();
        }
        return universalLoader;
    }
}
