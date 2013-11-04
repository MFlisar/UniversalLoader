
package com.michaelflisar.universalloader.data.main;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import android.os.AsyncTask;

import com.michaelflisar.universalloader.UniversalLoader;
import com.michaelflisar.universalloader.helper.ULDebugger;
import com.michaelflisar.universalloader.helper.ULDebugger.DebugMode;

public class ULTask extends AsyncTask<Void, Void, Object>
{
    private ULKey mKey;
    private WeakReference<UniversalLoader> mUniversalLoader;
    private Callable<Object> mCallable;

    public ULTask(ULKey key, UniversalLoader universalLoader, Callable<Object> callable)
    {
        mKey = key;
        mUniversalLoader = new WeakReference<UniversalLoader>(universalLoader);
        mCallable = callable;
    }

    @Override
    protected void onPreExecute()
    {

    }

    @Override
    protected Object doInBackground(Void... params)
    {
        try
        {
            ULDebugger.debug(DebugMode.DETAILED, getClass(), "doInBackground: " + mKey);
            return mCallable.call();
        }
        catch (Exception e)
        {
            ULDebugger.debug(DebugMode.DETAILED, getClass(), "doInBackground EXCEPTION: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onCancelled()
    {
        ULDebugger.debug(DebugMode.DETAILED, getClass(), "onCancelled: " + mKey);
        UniversalLoader universalLoader = mUniversalLoader.get();
        if (universalLoader != null)
            universalLoader.removeTask(mKey);
    }

    @Override
    protected void onPostExecute(Object result)
    {
        ULDebugger.debug(DebugMode.DETAILED, getClass(), "onPostExecute: " + mKey);
        UniversalLoader universalLoader = mUniversalLoader.get();
        if (universalLoader != null && result != null)
            universalLoader.putResult(mKey, result);
    }
}
