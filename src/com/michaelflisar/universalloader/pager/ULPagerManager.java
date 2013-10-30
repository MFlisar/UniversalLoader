
package com.michaelflisar.universalloader.pager;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.michaelflisar.universalloader.interfaces.IPagerFragment;
import com.michaelflisar.universalloader.interfaces.IPagerAdapterCallback;

public class ULPagerManager<Frag extends Fragment & IPagerFragment, Adapter extends PagerAdapter> implements OnPageChangeListener
{
    public static interface OnPageSelectedCallback
    {
        public void onPageSelected(int pos);
    }

    private ViewPager mViewPager = null;
    private PagerSlidingTabStrip mPageIndicator = null;
    private Adapter UFragmentPagerAdapter = null;
    private OnPageSelectedCallback mOnPageSelectedCallback = null;

    public ULPagerManager(ViewPager pager, PagerSlidingTabStrip indicator, Adapter fragmentPagerAdapter)
    {
        updateViews(pager, indicator, fragmentPagerAdapter);
    }

    public void updateViews(ViewPager pager, PagerSlidingTabStrip indicator, Adapter fragmentPagerAdapter)
    {
        UFragmentPagerAdapter = fragmentPagerAdapter;
        mViewPager = pager;
        mPageIndicator = indicator;

        doInit();
    }

    private void doInit()
    {
        mViewPager.setAdapter(UFragmentPagerAdapter);
        if (mPageIndicator != null)
        {
            mPageIndicator.setViewPager(mViewPager);
            mPageIndicator.setOnPageChangeListener(this);
        }
        else
        {
            mViewPager.setOnPageChangeListener(this);
        }
    }
    
    public int getPageCount()
    {
        if (mViewPager != null)
            return mViewPager.getChildCount();
        return -1;
    }

    public int getCurrentPage()
    {
        if (mViewPager != null)
            return mViewPager.getCurrentItem();
        return -1;
    }

    public void setCurrentPage(int pos, boolean smoothScroll)
    {
        if (getCurrentPage() == pos)
            return;

        if (mViewPager != null)
            mViewPager.setCurrentItem(pos, smoothScroll);
    }

    public void notifyDataSetChanged()
    {
        if (mPageIndicator != null)
            mPageIndicator.notifyDataSetChanged();
        UFragmentPagerAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    public Frag getFragment(int pos)
    {
        if (IPagerAdapterCallback.class.isAssignableFrom(UFragmentPagerAdapter.getClass()))
            return ((IPagerAdapterCallback<Frag>) UFragmentPagerAdapter).tryGetFragment(pos);
        else
            throw new RuntimeException("Diese Funktion kann nur benutzt werden, wenn der Adapter UPagerAdapterHelperCallback implementiert!");
    }

    public void setOnPageSelectedCallback(OnPageSelectedCallback callback)
    {
        mOnPageSelectedCallback = callback;
    }

    public void setViewPagerOffscreenPageLimit(int limit)
    {
        mViewPager.setOffscreenPageLimit(limit);
    }

    @Override
    public void onPageSelected(int pos)
    {
        if (mOnPageSelectedCallback != null)
            mOnPageSelectedCallback.onPageSelected(pos);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {
    }

    @Override
    public void onPageScrollStateChanged(int arg0)
    {
    }
}
