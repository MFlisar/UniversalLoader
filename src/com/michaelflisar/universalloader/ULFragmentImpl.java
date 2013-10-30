
package com.michaelflisar.universalloader;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.universalloader.data.ULFragmentLoaders;
import com.michaelflisar.universalloader.data.ULKey;
import com.michaelflisar.universalloader.interfaces.ILoaderFinishedListener;
import com.michaelflisar.universalloaderfragment.R;

public class ULFragmentImpl
{
    private boolean mViewExists = false;
    private ViewGroup mMainView = null;
    private View mLoadingView = null;
    private ULFragmentLoaders mLoaders = null;

    public ULFragmentImpl(ULFragmentLoaders loaders)
    {
        this.mLoaders = loaders;
    }

    public ViewGroup onCreateMainView(LayoutInflater inflater, ViewGroup container, boolean showLoadingOverlayAutomatically, View userView)
    {
        mMainView = (ViewGroup) inflater.inflate(R.layout.loader_fragment, container, false);
        mLoadingView = mMainView.findViewById(R.id.loading_overlay);
        if (!showLoadingOverlayAutomatically)
            setLoadingState(false);
        mMainView.addView(userView, 0);
        return mMainView;
    }

    public void onViewCreated(UniversalLoader universalLoader, ILoaderFinishedListener fragment)
    {
        mViewExists = true;
        if (universalLoader != null)
            universalLoader.register(fragment);
    }

    public void onDestroyView(UniversalLoader universalLoader, ILoaderFinishedListener fragment)
    {
        mViewExists = false;
        mMainView = null;
        mLoadingView = null;
    }

    public void onDestroy()
    {
        mLoaders = null;
    }

    public void onActivityCreated(UniversalLoader universalLoader, ILoaderFinishedListener fragment, boolean automaticLoadingOverlayEnabled, Bundle savedInstanceState)
    {
        // // 1) start a new loader or get already loaded data and send it to the listener
        // Iterator<Entry<ULKey, Callable<Object>>> it = mLoaders.getIterator();
        // while (it.hasNext())
        // {
        // Entry<ULKey, Callable<Object>> entry = it.next();
        // Object data = universalLoader.getData(entry.getKey());
        // if (data == null)
        // universalLoader.addCallable(entry.getKey(), entry.getValue());
        // else
        // universalLoader.onLoaderFinishedAndDataIsAvailable(entry.getKey());
        // }
    }

    public void onResume(UniversalLoader universalLoader)
    {
        // Prepare the loader - either reconnect with an existing one or start a new one
        Iterator<Entry<ULKey, Callable<Object>>> it = mLoaders.getIterator();
        while (it.hasNext())
        {
            Entry<ULKey, Callable<Object>> entry = it.next();
            Object data = universalLoader.getData(entry.getKey());
            if (data == null)
                universalLoader.addCallable(entry.getKey(), entry.getValue());
            else
                universalLoader.onLoaderFinishedAndDataIsAvailable(entry.getKey());
        }
    }

    public void setLoadingState(boolean enabled)
    {
        if (mLoadingView != null)
            mLoadingView.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    public boolean containsKey(ULKey key)
    {
        if (mLoaders != null && mLoaders.contains(key))
            return true;
        return false;
    }

    public ULFragmentLoaders getLoaders()
    {
        return mLoaders;
    }

    public boolean existsView()
    {
        return mViewExists;
    }

    // ------------------
    // loader functions
    // ------------------

    public void onLoaderFinished(UniversalLoader universalLoader, ULKey key, Object data, boolean automaticLoadingOverlayEnabled)
    {
        if (automaticLoadingOverlayEnabled && universalLoader != null)
        {
            // check if ALL loaders have loaded their data
            if (universalLoader != null && universalLoader.isAllDataLoaded(mLoaders))
                setLoadingState(false);
        }
    }
}
