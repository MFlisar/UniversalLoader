
package com.michaelflisar.universalloader.helper;

import android.util.Log;

import com.michaelflisar.universalloader.UniversalLoader;

public class ULDebugger
{
    public static enum DEBUG_MODE
    {
        SIMPLE,
        DETAILED
    }

    private static boolean mDebugEnabled = false;
    private static DEBUG_MODE mDebugMode = DEBUG_MODE.SIMPLE;
    
    public static void setDebugger(boolean enabled, DEBUG_MODE mode)
    {
        setDebug(enabled);
        setMode(mode);
    }

    protected static void setDebug(boolean enabled)
    {
        mDebugEnabled = enabled;
    }

    protected static void setMode(DEBUG_MODE mode)
    {
        mDebugMode = mode;
    }

    private static boolean isDebugRelevant(DEBUG_MODE mode)
    {
        if (mDebugEnabled && mode.ordinal() <= mDebugMode.ordinal())
            return true;
        return false;
    }

    public static void debug(DEBUG_MODE mode, Class<?> source, String message)
    {
        if (isDebugRelevant(mode))
            debug(source.getSimpleName() + ": " + message);
    }

    public static void debug(DEBUG_MODE mode, Object source, String message)
    {
        if (isDebugRelevant(mode))
            debug(source.getClass().getSimpleName() + "@" + Integer.toHexString(source.hashCode()) + ": " + message);
    }

    private static void debug(String message)
    {
        Log.d(UniversalLoader.class.getSimpleName(), message);
    }
}
