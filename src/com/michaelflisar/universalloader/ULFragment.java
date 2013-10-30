
package com.michaelflisar.universalloader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.universalloader.data.ULFragmentLoaders;
import com.michaelflisar.universalloader.data.ULKey;
import com.michaelflisar.universalloader.interfaces.ILoaderFinishedListener;
import com.michaelflisar.universalloader.interfaces.IUniversalLoaderProvider;

public abstract class ULFragment extends Fragment implements ILoaderFinishedListener
{
    private ULFragmentImpl mFragmentImpl = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mFragmentImpl = new ULFragmentImpl(createLoaders());
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View userView = onCreateUserView(inflater, container, savedInstanceState);
        View v = mFragmentImpl.onCreateMainView(inflater, container, isAutomaticLoadingOverlayEnabled(), userView);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mFragmentImpl.onViewCreated(getUniversalLoader(), this);
    }

    @Override
    public void onDestroyView()
    {
        mFragmentImpl.onDestroyView(getUniversalLoader(), this);
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mFragmentImpl.onActivityCreated(getUniversalLoader(), this, isAutomaticLoadingOverlayEnabled(), savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        UniversalLoader universalLoader = getUniversalLoader();
        if (universalLoader != null)
            mFragmentImpl.onResume(universalLoader);
    }

    @Override
    public void onDestroy()
    {
        mFragmentImpl.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLoaderRestarted(ULKey key)
    {
        if (isAutomaticLoadingOverlayEnabled())
            mFragmentImpl.setLoadingState(true);
    }

    @Override
    public final void onLoaderFinished(ULKey key, Object data)
    {
        UniversalLoader universalLoader = getUniversalLoader();
        if (isAutomaticLoadingOverlayEnabled() && universalLoader != null)
            mFragmentImpl.onLoaderFinished(universalLoader, key, data, isAutomaticLoadingOverlayEnabled());
        onLoaderFinished(mFragmentImpl.existsView() ? getView() : null, key, data);
    }

    @Override
    public boolean containsKey(ULKey key)
    {
        return mFragmentImpl.containsKey(key);
    }

    public void onLoaderFinished(View v, ULKey key, Object data)
    {

    }

    public boolean existsView()
    {
        return mFragmentImpl.existsView();
    }

    public boolean isAllLoaded()
    {
        return getUniversalLoader().isAllDataLoaded(mFragmentImpl.getLoaders());
    }

    public boolean isAutomaticLoadingOverlayEnabled()
    {
        return true;
    }

    public UniversalLoader getUniversalLoader()
    {
        IUniversalLoaderProvider provider = (IUniversalLoaderProvider) getActivity();
        if (provider != null)
            return provider.getUniversalLoader();
        return null;
    }

    public abstract View onCreateUserView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract ULFragmentLoaders createLoaders();
}
