
package com.michaelflisar.universalloader.data;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.michaelflisar.universalloader.UniversalLoader;
import com.michaelflisar.universalloader.helper.ULDebugger;
import com.michaelflisar.universalloader.helper.ULDebugger.DEBUG_MODE;

public class ULTask extends AsyncTaskLoader<Object>
{
    private ULKey mKey = null;
    private boolean mCancellationEnabled = false;
    private Callable<Object> mCallable = null;
    private WeakReference<UniversalLoader> mUniversalLoader = null;

    public ULTask(Context context, ULKey key, UniversalLoader universalLoader, Callable<Object> callable)
    {
        super(context);
        mKey = key;
        mCallable = callable;
        mUniversalLoader = new WeakReference<UniversalLoader>(universalLoader);
    }

    public ULTask enableCancellation()
    {
        return this;
    }

    @Override
    public Object loadInBackground()
    {
        Object result = null;
        try
        {
            result = mCallable.call();
            ULDebugger.debug(DEBUG_MODE.SIMPLE, getClass(), "loadInBackground " + getId());
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void deliverResult(Object object)
    {
        ULDebugger.debug(DEBUG_MODE.SIMPLE, getClass(), "deliverResult " + getId() + ": key=" + mKey + ", object=" + object + ", reset=" + isReset());

        UniversalLoader universalLoader = mUniversalLoader.get();
        Object oldObject = null;
        if (universalLoader != null)
        {
            oldObject = universalLoader.getData().get(mKey);
            universalLoader.getData().set(mKey, object);
        }

        if (isReset())
        {
            releaseResources(object);
            return;
        }

        if (isStarted())
            super.deliverResult(object);

        if (oldObject != null && oldObject != object)
            releaseResources(oldObject);
    }

    @Override
    protected void onStartLoading()
    {
        ULDebugger.debug(DEBUG_MODE.SIMPLE, getClass(), "onStartLoading " + getId());

        UniversalLoader universalLoader = mUniversalLoader.get();
        Object data = null;
        if (universalLoader != null)
        {
            data = universalLoader.getData().get(mKey);
            if (data != null)
                deliverResult(data);
            else
                forceLoad();
        }
    }

    @Override
    protected void onStopLoading()
    {
        ULDebugger.debug(DEBUG_MODE.SIMPLE, getClass(), "onStopLoading " + getId());
        if (mCancellationEnabled)
            cancelLoad();
    }

    @Override
    protected void onReset()
    {
        ULDebugger.debug(DEBUG_MODE.SIMPLE, getClass(), "onReset " + getId());

        onStopLoading();
        UniversalLoader universalLoader = mUniversalLoader.get();
        Object data = null;
        if (universalLoader != null)
        {
            data = universalLoader.getData().get(mKey);
            if (data != null)
                releaseResources(data);
        }
    }

    @Override
    public void onCanceled(Object object)
    {
        super.onCanceled(object);
        if (mCancellationEnabled)
            releaseResources(object);
    }

    private void releaseResources(Object object)
    {
        object = null;
    }
}
