package com.michaelflisar.universalloader.data.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.michaelflisar.universalloader.data.fragments.ULFragmentLoaderData;
import com.michaelflisar.universalloader.data.fragments.ULFragmentLoaders;

public class ULLoaderDataManager
{
    private Map<ULFragmentKey, ULFragmentLoaders> mLoaders;
    
    public ULLoaderDataManager()
    {
        mLoaders = new HashMap<ULFragmentKey, ULFragmentLoaders>();
    }
    
    public void put(ULFragmentKey fKey, ULFragmentLoaders loaders)
    {
        mLoaders.put(fKey, loaders);
    }
    
    public ULFragmentLoaders getLoaders(ULFragmentKey fkey)
    {
        return mLoaders.get(fkey);
    }
    
    public ULFragmentLoaderData getLoaderData(ULFragmentKey fkey, ULKey key)
    {
        return mLoaders.get(fkey).get(key);
    }
    
    public Iterator<Entry<ULKey, ULFragmentLoaderData>> iterator(ULFragmentKey fKey)
    {
        return mLoaders.get(fKey).iterator();
    }
    
    public void clear()
    {
        mLoaders.clear();
    }
}
