
package com.michaelflisar.universalloader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.universalloader.data.fragments.ULFragmentLoaderData.ULLoaderType;
import com.michaelflisar.universalloader.data.fragments.ULFragmentLoaders;
import com.michaelflisar.universalloader.data.main.ULFragmentKey;
import com.michaelflisar.universalloader.data.main.ULKey;
import com.michaelflisar.universalloader.helper.ULDebugger;
import com.michaelflisar.universalloader.helper.ULDebugger.DebugMode;
import com.michaelflisar.universalloader.interfaces.IFragmentImplParent;
import com.michaelflisar.universalloader.interfaces.IUniversalLoaderListener;
import com.michaelflisar.universalloader.interfaces.IUniversalLoaderProvider;
import com.michaelflisar.universalloaderfragment.R;

public class ULFragmentImpl implements IUniversalLoaderListener
{
    private IFragmentImplParent mParent = null;
    private ULFragmentKey mFKey = null;
    private IUniversalLoaderProvider mProvider = null;

    private ViewGroup mMainView = null;
    private View mLoadingView = null;

    private boolean mAutomaticLoadingOverlayEnabled = true;

    public ULFragmentImpl(IFragmentImplParent parent)
    {
        mParent = parent;
        mFKey = mParent.createFragmentKey();
    }

    // -----------------------------
    // handling fragment functions
    // ordered by lifecycle
    // -----------------------------

    public void onAttach()
    {

    }

    public void onCreate()
    {

    }

    public ViewGroup onCreateView(LayoutInflater inflater, ViewGroup container, View userView)
    {
        mMainView = (ViewGroup) inflater.inflate(R.layout.loader_fragment, container, false);
        mLoadingView = mMainView.findViewById(R.id.loading_overlay);
        mMainView.addView(userView, 0);
        return mMainView;
    }

    public void onActivityCreated(IUniversalLoaderProvider provider, ULFragmentLoaders loaders)
    {
        mProvider = provider;
        mProvider.getUniversalLoader().putLoaderData(mFKey, loaders);
        mProvider.getUniversalLoader().register(this, true, ULLoaderType.OnViewCreated);
        mProvider.getUniversalLoader().deliverAlreadyLoadedData(this, ULLoaderType.OnViewCreated);
        mProvider.getUniversalLoader().startAllLoadersAutomatically(mFKey, ULLoaderType.OnViewCreated);
    }

    public void onStart()
    {
        mProvider.getUniversalLoader().deliverAlreadyLoadedData(this, ULLoaderType.OnStart);
        mProvider.getUniversalLoader().startAllLoadersAutomatically(mFKey, ULLoaderType.OnStart);
    }

    public void onResume()
    {
        if (mAutomaticLoadingOverlayEnabled && mProvider.getUniversalLoader().isAllDataLoaded(mFKey))
            setLoadingState(false);
    }

    public void onPause()
    {

    }

    public void onStop()
    {

    }

    public void onDestroyView()
    {
        mProvider.getUniversalLoader().unregister(this);
        mMainView = null;
        mLoadingView = null;
    }

    public void onDestroy()
    {
        mParent = null;
        mProvider = null;
    }

    public void onDetach()
    {

    }

    // -----------------------------
    // functions
    // -----------------------------

    public void disableAutomaticLoadingOverlay()
    {
        mAutomaticLoadingOverlayEnabled = false;
    }

    public void setLoadingState(boolean enabled)
    {
        if (mLoadingView != null)
            mLoadingView.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    // ------------------
    // IUniversalLoader callbacks
    // ------------------

    @Override
    public ULFragmentKey getFragmentKey()
    {
        return mFKey;
    }

    @Override
    public void onLoaderStarted()
    {
        if (mAutomaticLoadingOverlayEnabled)
            setLoadingState(true);
    }

    @Override
    public void onDataReceived(ULKey key, Object data)
    {
        ULDebugger.debug(DebugMode.SIMPLE, getClass(), "onDataReceived: " + key);
        if (mAutomaticLoadingOverlayEnabled && mProvider != null)
        {
            // check if ALL loaders have loaded their data
            if (mProvider.getUniversalLoader().isAllDataLoaded(mFKey))
                setLoadingState(false);
        }
        mParent.onDataReceived(key, data);
    }
}