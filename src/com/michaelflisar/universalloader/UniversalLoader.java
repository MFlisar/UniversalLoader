
package com.michaelflisar.universalloader;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;

import android.support.v4.app.Fragment;

import com.michaelflisar.universalloader.data.fragments.ULFragmentLoaderData;
import com.michaelflisar.universalloader.data.fragments.ULFragmentLoaderData.ULLoaderType;
import com.michaelflisar.universalloader.data.fragments.ULFragmentLoaders;
import com.michaelflisar.universalloader.data.main.ULFragmentKey;
import com.michaelflisar.universalloader.data.main.ULKey;
import com.michaelflisar.universalloader.data.main.ULLoaderDataManager;
import com.michaelflisar.universalloader.data.main.ULResult;
import com.michaelflisar.universalloader.data.main.ULResultManager;
import com.michaelflisar.universalloader.data.main.ULTask;
import com.michaelflisar.universalloader.data.main.ULTaskManager;
import com.michaelflisar.universalloader.helper.ULHelper;
import com.michaelflisar.universalloader.interfaces.IUniversalLoaderListener;

public class UniversalLoader extends Fragment
{
    private static final Object mLock = new Object();

    private ULTaskManager mTasks = new ULTaskManager();
    private ULLoaderDataManager mLoaderData = new ULLoaderDataManager();
    private ULResultManager mResult = new ULResultManager();

    private Set<IUniversalLoaderListener> mLoaderFinishedListeners = new HashSet<IUniversalLoaderListener>();

    private Executor mExecutor = null;

    public UniversalLoader()
    {
        super();
        setRetainInstance(true);
        mExecutor = ULHelper.getExecutor(true);
    }

    @Override
    public void onDestroy()
    {
        mLoaderFinishedListeners.clear();
        Iterator<Entry<ULKey, ULTask>> it = mTasks.iterator();
        while (it.hasNext())
            it.next().getValue().cancel(true);
        mResult.clear();
        mLoaderData.clear();
        super.onDestroy();
    }

    public void disableMulitThread()
    {
        mExecutor = ULHelper.getExecutor(false);
    }

    // -----------------------------
    // data functions
    // -----------------------------

    public void putResult(ULKey key, ULResult result)
    {
        synchronized (mLock)
        {
            removeTask(key);
            mResult.put(key, result);
            notifyListeners(key, result);
        }
    }

    public ULResult getResult(ULKey key)
    {
        return mResult.get(key);
    }

    public ULResult getUndeliveredData(ULKey key)
    {
        if (mResult.setDelivered(key))
            return mResult.get(key);
        return null;
    }

    public boolean isAllDataLoaded(ULFragmentKey fKey)
    {
        return isAllDataLoaded(mLoaderData.getLoaders(fKey));
    }

    public boolean isAllDataLoaded(ULFragmentLoaders loaders)
    {
        synchronized (mLock)
        {
            Iterator<Entry<ULKey, ULFragmentLoaderData>> it = loaders.iterator();
            while (it.hasNext())
            {
                if (mResult.get(it.next().getKey()) == null)
                    return false;
            }
            return true;
        }
    }

    public void clearData(ULKey key)
    {
        synchronized (mLock)
        {
            ULTask t = mTasks.remove(key);
            if (t != null)
                t.cancel(true);
            mResult.clear(key);
        }
    }

    public void clearAllSubData(ULKey parentKey)
    {
        synchronized (mLock)
        {
            Iterator<Entry<ULKey, ULResult>> it = mResult.iterator();
            while (it.hasNext())
            {
                Entry<ULKey, ULResult> entry = it.next();
                if (parentKey.isSubKey(entry.getKey()))
                {
                    ULTask t = mTasks.remove(entry.getKey());
                    if (t != null)
                        t.cancel(true);
                    it.remove();
                }
            }
        }
    }

    // -----------------------------
    // data functions
    // -----------------------------

    public void putLoaderData(ULFragmentKey fKey, ULFragmentLoaders loaders)
    {
        synchronized (mLock)
        {
            mLoaderData.put(fKey, loaders);
        }
    }

    // -----------------------------
    // task functions
    // -----------------------------

    private void startTask(ULKey key, ULFragmentLoaderData loaderData)
    {
        synchronized (mLock)
        {
            // 1) check if data already exists
            if (mResult.get(key) != null)
                return;

            // 2) report that task is started/working
            notifyListenersAboutStart(key);

            // 3) check if data is already loading
            if (mTasks.get(key) != null)
                return;
            // 4) create new task and start it
            else
            {
                ULTask task = new ULTask(key, this, loaderData.getCallable());
                mTasks.put(key, task);
                task.executeOnExecutor(mExecutor);
            }
        }
    }

    public void removeTask(ULKey key)
    {
        synchronized (mLock)
        {
            mTasks.remove(key);
        }
    }

    // -----------------------------
    // loader functions
    // -----------------------------

    public void startAllLoadersAutomatically(ULFragmentKey fKey, ULLoaderType type)
    {
        Iterator<Entry<ULKey, ULFragmentLoaderData>> it = mLoaderData.iterator(fKey);
        while (it.hasNext())
        {
            Entry<ULKey, ULFragmentLoaderData> entry = it.next();
            if (entry.getValue().getType() == type && !entry.getValue().wasStarted())
            {
                entry.getValue().setWasStarted();
                startLoader(entry.getKey(), entry.getValue());
            }
        }
    }

    public void startLoader(ULFragmentKey fKey, ULKey key)
    {
        startLoader(key, mLoaderData.getLoaderData(fKey, key));
    }

    public void restartLoader(ULFragmentKey fKey, ULKey key)
    {
        clearData(key);
        startLoader(fKey, key);
    }

    private void startLoader(ULKey key, ULFragmentLoaderData data)
    {
        startTask(key, data);
    }

    // -----------------------------
    // listener functions
    // -----------------------------

    private void notifyListenersAboutStart(ULKey key)
    {
        Iterator<IUniversalLoaderListener> iterator = mLoaderFinishedListeners.iterator();
        while (iterator.hasNext())
        {
            IUniversalLoaderListener listener = iterator.next();
            if (listener != null)
            {
                if (mLoaderData.getLoaders(listener.getFragmentKey()).contains(key))
                {
                    listener.onLoaderStarted();
                }
            }
        }
    }

    private void notifyListeners(ULKey key, ULResult result)
    {
        Iterator<IUniversalLoaderListener> iterator = mLoaderFinishedListeners.iterator();
        while (iterator.hasNext())
        {
            IUniversalLoaderListener listener = iterator.next();
            if (listener != null)
            {
                if (mLoaderData.getLoaders(listener.getFragmentKey()).contains(key))
                {
                    mResult.setDelivered(key);
                    listener.onDataReceived(key, result);
                }
            }
        }
    }

    public void register(IUniversalLoaderListener listener, boolean deliverAlreadyLoadedData, ULLoaderType type)
    {
        synchronized (mLock)
        {
            mLoaderFinishedListeners.add(listener);
        }
    }

    public void deliverAlreadyLoadedData(IUniversalLoaderListener listener, ULLoaderType type)
    {
        synchronized (mLock)
        {
            Iterator<Entry<ULKey, ULFragmentLoaderData>> it = mLoaderData.getLoaders(listener.getFragmentKey()).iterator();
            while (it.hasNext())
            {
                Entry<ULKey, ULFragmentLoaderData> entry = it.next();
                ULResult result = mResult.get(entry.getKey());
                if (entry.getValue().getType() == type && result != null)
                {
                    mResult.setDelivered(entry.getKey());
                    listener.onDataReceived(entry.getKey(), result);
                }
            }
        }
    }

    public void unregister(IUniversalLoaderListener listener)
    {
        synchronized (mLock)
        {
            mLoaderFinishedListeners.remove(listener);
        }
    }
}
