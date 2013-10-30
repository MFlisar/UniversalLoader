
package com.michaelflisar.universalloader;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.michaelflisar.universalloader.data.ULData;
import com.michaelflisar.universalloader.data.ULFragmentLoaders;
import com.michaelflisar.universalloader.data.ULKey;
import com.michaelflisar.universalloader.data.ULLoaderManager;
import com.michaelflisar.universalloader.helper.ULDebugger;
import com.michaelflisar.universalloader.helper.ULDebugger.DEBUG_MODE;
import com.michaelflisar.universalloader.interfaces.ILoaderFinishedListener;

public class UniversalLoader extends Fragment implements LoaderCallbacks<Object>
{
    private FragmentActivity mParent = null;

    private ULData mData = new ULData();
    private int mLastLoaderID = 0;

    private ULLoaderManager mLoaderManager = null;

    private WeakHashMap<ILoaderFinishedListener, Void> mLoaderFinishedListeners = new WeakHashMap<ILoaderFinishedListener, Void>();

    public UniversalLoader()
    {
        super();
        setRetainInstance(true);

        mLoaderManager = new ULLoaderManager();
        mLoaderFinishedListeners = new WeakHashMap<ILoaderFinishedListener, Void>();
    }

    public void updateParent(FragmentActivity parent)
    {
        mParent = parent;
    }

    @Override
    public void onDestroy()
    {
        mParent = null;
        mLoaderManager.releaseAllResources();
        mLoaderManager = null;
        mLoaderFinishedListeners.clear();
        mLoaderFinishedListeners = null;
        super.onDestroy();
    }

    // ----------------------------------
    // loader relevant functions
    // ----------------------------------

    public synchronized int getNewLoaderID()
    {
        mLastLoaderID++;
        return mLastLoaderID;
    }

    public void register(ILoaderFinishedListener listener)
    {
        mLoaderFinishedListeners.put(listener, null);
    }

    public void addCallable(ULKey key, Callable<Object> callable)
    {
        int id = getNewLoaderID();
        mLoaderManager.addLoader(id, key, callable);
        initLoader(id, null);
    }

    public synchronized boolean isAllDataLoaded(ULFragmentLoaders loaders)
    {
        Iterator<Entry<ULKey, Callable<Object>>> it = loaders.getIterator();
        while (it.hasNext())
        {
            if (mData.get(it.next().getKey()) == null)
                return false;
        }
        return true;
    }

    private void initLoader(int id, Bundle bundle)
    {
        mParent.getSupportLoaderManager().initLoader(id, bundle, this);
    }

    public void restartLoader(ULKey key, Bundle bundle)
    {
        mData.clear(key);
        int id = mLoaderManager.getLoaderID(key);
        onLoaderRestarted(id);
        mParent.getSupportLoaderManager().restartLoader(id, bundle, this);
    }

    // ----------------------------------
    // loader callbacks implementation
    // ----------------------------------

    @Override
    public void onLoaderReset(Loader<Object> loader)
    {
        ULDebugger.debug(DEBUG_MODE.SIMPLE, getClass(), "onLoaderReset: " + getDebugIdAndKey(loader.getId()));
        // mRetainedFragment.clearData(loader.getId());
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data)
    {
        ULDebugger.debug(DEBUG_MODE.SIMPLE, getClass(), "onLoadFinished: " + getDebugIdAndKey(loader.getId()));
        mData.set(mLoaderManager.getLoaderKey(loader.getId()), data);
        onLoaderFinishedAndDataIsAvailable(loader.getId());
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args)
    {
        ULDebugger.debug(DEBUG_MODE.SIMPLE, getClass(), "onCreateLoader: " + getDebugIdAndKey(id));
        return mLoaderManager.createLoader(mParent, this, id);
    }

    public void onLoaderRestarted(int id)
    {
        ULKey key = mLoaderManager.getLoaderKey(id);
        Iterator<ILoaderFinishedListener> iterator = mLoaderFinishedListeners.keySet().iterator();
        while (iterator.hasNext())
        {
            ILoaderFinishedListener listener = iterator.next();
            if (listener != null)
            {
                if (listener.containsKey(key))
                    listener.onLoaderRestarted(key);
            }
        }
    }

    private void onLoaderFinishedAndDataIsAvailable(int id)
    {
        ULKey key = mLoaderManager.getLoaderKey(id);
        onLoaderFinishedAndDataIsAvailable(key);
    }
    
    public void onLoaderFinishedAndDataIsAvailable(ULKey key)
    {
        Iterator<ILoaderFinishedListener> iterator = mLoaderFinishedListeners.keySet().iterator();
        while (iterator.hasNext())
        {
            Object data = mData.get(key);
            ILoaderFinishedListener listener = iterator.next();
            if (listener != null)
            {
                if (listener.containsKey(key))
                    listener.onLoaderFinished(key, data);
            }
        }
    }
    
    // ----------------------------------
    // helper functions
    // ----------------------------------
    
    private String getDebugIdAndKey(int id)
    {
        if (mLoaderManager == null)
            return "NULL";
        ULKey key = mLoaderManager.getLoaderKey(id);
        if (key != null)
            return id + "|" + key.toString();
        return id + "|NULL";
    }

    // ----------------------------------
    // getter/setter
    // ----------------------------------

    public ULData getData()
    {
        return mData;
    }

    public Object getData(ULKey key)
    {
        return mData.get(key);
    }
    
    public void setData(ULKey key, Object data)
    {
        mData.set(key, data);
    }
}
