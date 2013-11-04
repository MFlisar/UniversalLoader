
package com.michaelflisar.universalloader.fragments;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.universalloader.ULFragmentImpl;
import com.michaelflisar.universalloader.UniversalLoader;
import com.michaelflisar.universalloader.data.fragments.ULFragmentLoaders;
import com.michaelflisar.universalloader.data.main.ULFragmentKey;
import com.michaelflisar.universalloader.data.main.ULKey;
import com.michaelflisar.universalloader.interfaces.IFragmentImplParent;
import com.michaelflisar.universalloader.interfaces.IUniversalLoaderProvider;

public abstract class ULDialogFragment extends DialogFragment implements IFragmentImplParent
{
    private ULFragmentImpl mFragmentImpl = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mFragmentImpl = new ULFragmentImpl(this);
        mFragmentImpl.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public final Dialog onCreateDialog(final Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        onPrepareBuilder(builder);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View userView = onCreateUserView(inflater, null, savedInstanceState);
        View v = mFragmentImpl.onCreateView(inflater, null, userView);
        builder.setView(v);
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (!(getActivity() instanceof IUniversalLoaderProvider))
            throw new RuntimeException("the parent activity of an ULFragment MUST implement IUniversalLoaderProvider!");
        mFragmentImpl.onActivityCreated((IUniversalLoaderProvider) getActivity(), createLoaders());
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mFragmentImpl.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mFragmentImpl.onResume();
    }

    @Override
    public void onPause()
    {
        mFragmentImpl.onPause();
        super.onPause();
    }

    @Override
    public void onStop()
    {
        mFragmentImpl.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView()
    {
        mFragmentImpl.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy()
    {
        mFragmentImpl.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDetach()
    {
        mFragmentImpl.onDetach();
        super.onDetach();
    }

    // -----------------------------
    // forward functions
    // -----------------------------

    public UniversalLoader getUniversalLoader()
    {
        return ((IUniversalLoaderProvider) getActivity()).getUniversalLoader();
    }

    public ULFragmentKey getFragmentKey()
    {
        return mFragmentImpl.getFragmentKey();
    }

    public void disableAutomaticLoadingOverlay()
    {
        mFragmentImpl.disableAutomaticLoadingOverlay();
    }

    // -----------------------------
    // abstract functions
    // -----------------------------

    public abstract void onPrepareBuilder(Builder builder);

    public abstract void onDataReceived(ULKey key, Object data);

    public abstract View onCreateUserView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract ULFragmentLoaders createLoaders();
}
