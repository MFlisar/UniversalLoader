
package com.michaelflisar.universalloader.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class ULData
{
    private Map<ULKey, Object> mData = new HashMap<ULKey, Object>();

    public Object get(ULKey key)
    {
        return mData.get(key);
    }

    public void set(ULKey key, Object data)
    {
        // don't do this!!!
        if (key == null)
            throw new RuntimeException("ULKey == NULL");
        mData.put(key, data);
    }

    public void clear(ULKey key)
    {
        mData.remove(key);
    }
    
    public void clearAllSubData(ULKey parentKey)
    {
        Iterator<Entry<ULKey, Object>> it = mData.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<ULKey, Object> entry = it.next();
            if (parentKey.isSubKey(entry.getKey()))
                it.remove();
        }
    }

//    public boolean isAllLoaded(ULLoaderManager loaderWrapper)
//    {
//        if (loaderWrapper == null)
//            return true;
//        ULKey[] keys = loaderWrapper.getKeys();
//        for (int i = 0; i < keys.length; i++)
//        {
//            if (mData.get(keys[i]) == null)
//                return false;
//        }
//        return true;
//    }
}
