package com.michaelflisar.universalloader.data.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ULTaskManager
{
    private Map<ULKey, ULTask> mTasks;

    public ULTaskManager()
    {
        mTasks = new HashMap<ULKey, ULTask>();
    }
    
    public ULTask get(ULKey key)
    {
        return mTasks.get(key);
    }
    
    public void put(ULKey key, ULTask task)
    {
        mTasks.put(key, task);
    }
    
    public ULTask remove(ULKey key)
    {
        return mTasks.remove(key);
    }
    
    public Iterator<Entry<ULKey, ULTask>> iterator()
    {
        return mTasks.entrySet().iterator();
    }
    
    
}
