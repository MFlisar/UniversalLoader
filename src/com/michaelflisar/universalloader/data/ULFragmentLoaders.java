
package com.michaelflisar.universalloader.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

public class ULFragmentLoaders
{
    private Map<ULKey, Callable<Object>> mLoaders = new HashMap<ULKey, Callable<Object>>();

    public ULFragmentLoaders()
    {

    }
    
    public boolean contains(ULKey key)
    {
        return mLoaders.containsKey(key);
    }

    public void add(ULKey key, Callable<Object> callable)
    {
        mLoaders.put(key, callable);
    }

    public Iterator<Entry<ULKey, Callable<Object>>> getIterator()
    {
        return mLoaders.entrySet().iterator();
    }
}
