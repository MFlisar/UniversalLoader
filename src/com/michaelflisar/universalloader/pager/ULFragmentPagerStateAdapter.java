
package com.michaelflisar.universalloader.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.universalloader.interfaces.IPagerFragment;
import com.michaelflisar.universalloader.interfaces.IPagerAdapterCallback;

public abstract class ULFragmentPagerStateAdapter<Frag extends Fragment & IPagerFragment> extends FragmentStatePagerAdapter implements IPagerAdapterCallback<Frag>
{
    private ULPagerAdapterHelper<Frag> mHelper;

    public ULFragmentPagerStateAdapter(FragmentManager fm)
    {
        super(fm);
        mHelper = new ULPagerAdapterHelper<Frag>(this);
    }

    @Override
    public Fragment getItem(int index)
    {
        return (Fragment)mHelper.getItem(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        Object frag = super.instantiateItem(container, position);
        mHelper.instantiateItem(position, (Frag) frag);
        return frag;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        super.destroyItem(container, position, object);
        mHelper.destroyItem(position);
    }

    @Override
    public int getItemPosition(Object item)
    {
        return mHelper.getItemPosition(item);
    }

    @Override
    public Frag tryGetFragment(int key)
    {
        return mHelper.tryGetFragment(key);
    }

    @Override
    public View getPageTabCustomView(int position)
    {
        return null;
    }

    @Override
    public View getPageTabBackground(int position)
    {
        return null;
    }
}
