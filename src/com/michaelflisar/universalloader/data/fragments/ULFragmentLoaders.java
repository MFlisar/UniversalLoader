
package com.michaelflisar.universalloader.data.fragments;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import com.michaelflisar.universalloader.data.fragments.ULFragmentLoaderData.ULLoaderType;
import com.michaelflisar.universalloader.data.main.ULKey;

public class ULFragmentLoaders
{
    private Map<ULKey, ULFragmentLoaderData> mLoaders;

    public ULFragmentLoaders()
    {
        mLoaders = new HashMap<ULKey, ULFragmentLoaderData>();
    }
    
    public void add(ULKey key, Callable<Object> callable, ULLoaderType type)
    {
        mLoaders.put(key, new ULFragmentLoaderData(callable, type));
    }
    
    public ULFragmentLoaderData get(ULKey key)
    {
        return mLoaders.get(key);
    }

    public boolean contains(ULKey key)
    {
        return mLoaders.containsKey(key);
    }

    public Iterator<Entry<ULKey, ULFragmentLoaderData>> iterator()
    {
        return mLoaders.entrySet().iterator();
    }
}
