package com.michaelflisar.universalloader.interfaces;

import android.support.v4.app.Fragment;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip.TabBackgroundProvider;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip.TabCustomViewProvider;

public interface IPagerAdapterCallback<Frag extends Fragment & IPagerFragment> extends TabCustomViewProvider, TabBackgroundProvider
{
    public Frag createFragment(int pos);
    public Frag tryGetFragment(int pos);
}
