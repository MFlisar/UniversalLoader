
package com.michaelflisar.universalloader.data.fragments;

import java.util.concurrent.Callable;

public class ULFragmentLoaderData
{
    public enum ULLoaderType
    {
        OnViewCreated, // needs the view to be created, i.e. if you want to call view update functions if finished
        OnStart,
        Manual // will be started on demand only
    }

    private boolean mWasStarted;
    private Callable<Object> mCallable;
    private ULLoaderType mType;

    public ULFragmentLoaderData(Callable<Object> callable, ULLoaderType type)
    {
        mWasStarted = false;
        mCallable = callable;
        mType = type;
    }

    public Callable<Object> getCallable()
    {
        return mCallable;
    }

    public ULLoaderType getType()
    {
        return mType;
    }

    public boolean wasStarted()
    {
        return mWasStarted;
    }

    public void setWasStarted()
    {
        mWasStarted = true;
    }
}
