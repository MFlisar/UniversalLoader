
package com.michaelflisar.universalloader.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import android.content.Context;
import android.util.SparseArray;

import com.michaelflisar.universalloader.UniversalLoader;

public class ULLoaderManager
{
    private Map<ULKey, Callable<Object>> mLoaders = new HashMap<ULKey, Callable<Object>>();
    private Map<ULKey, Integer> mIDs = new HashMap<ULKey, Integer>();
    private SparseArray<ULKey> mKeys = new SparseArray<ULKey>();

    public ULLoaderManager()
    {
    }

    public void addLoader(int loaderID, ULKey key, Callable<Object> callable)
    {
        if (key == null)
            throw new RuntimeException("ULKey == null");
        mLoaders.put(key, callable);
        mIDs.put(key, loaderID);
        mKeys.put(loaderID, key);
    }

    public ULTask createLoader(Context context, UniversalLoader universalLoader, int id)
    {
        ULKey key = mKeys.get(id);
        return new ULTask(context, key, universalLoader, mLoaders.get(key));
    }

    public ULKey[] getKeys()
    {
        return mLoaders.keySet().toArray(new ULKey[mLoaders.keySet().size()]);
    }

    public int getLoaderID(ULKey key)
    {
        return mIDs.get(key);
    }

    public ULKey getLoaderKey(int id)
    {
        return mKeys.get(id);
    }

    public void releaseAllResources()
    {
        mLoaders.clear();
        mIDs.clear();
        mKeys.clear();
    }

}
