package com.michaelflisar.universalloader.pager;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;

import com.michaelflisar.universalloader.interfaces.IPagerFragment;
import com.michaelflisar.universalloader.interfaces.IPagerAdapterCallback;

public class ULPagerAdapterHelper<Frag extends Fragment & IPagerFragment>
{
    private IPagerAdapterCallback<Frag> mCallback = null;
    private SparseArray<Frag> mPageFragments = new SparseArray<Frag>();

    public ULPagerAdapterHelper(IPagerAdapterCallback<Frag> callback)
    {
        mCallback = callback;
    }

    public Frag getItem(int index)
    {
        Frag myFragment = tryGetFragment(index);
        if (myFragment == null)
        {
            myFragment = mCallback.createFragment(index);
            mPageFragments.put(index, myFragment);
        }
        return myFragment;
    }

    public void instantiateItem(int position, Frag frag)
    {
        mPageFragments.append(position, frag);
    }

    public void destroyItem(int position)
    {
        mPageFragments.remove(position);
    }

    @SuppressWarnings("unchecked")
    public int getItemPosition(Object item)
    {
        int pos = ((Frag) item).getPagerIndex();
        if (tryGetFragment(pos) != null)
            return pos;
        // sonst neues Fragment liefern
        return PagerAdapter.POSITION_NONE;
    }

    public Frag tryGetFragment(int pos)
    {
        return mPageFragments.get(pos);
    }
}
