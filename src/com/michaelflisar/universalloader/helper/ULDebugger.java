
package com.michaelflisar.universalloader.helper;

import android.util.Log;

import com.michaelflisar.universalloader.UniversalLoader;

public class ULDebugger
{
    public static enum DebugMode
    {
        SIMPLE,
        DETAILED
    }

    private static boolean mDebugEnabled = false;
    private static DebugMode mDebugMode = DebugMode.SIMPLE;
    
    public static void setDebugger(boolean enabled, DebugMode mode)
    {
        setDebug(enabled);
        setMode(mode);
    }

    protected static void setDebug(boolean enabled)
    {
        mDebugEnabled = enabled;
    }

    protected static void setMode(DebugMode mode)
    {
        mDebugMode = mode;
    }

    private static boolean isDebugRelevant(DebugMode mode)
    {
        if (mDebugEnabled && mode.ordinal() <= mDebugMode.ordinal())
            return true;
        return false;
    }

    public static void debug(DebugMode mode, Class<?> source, String message)
    {
        if (isDebugRelevant(mode))
            debug(source.getSimpleName() + ": " + message);
    }

    public static void debug(DebugMode mode, Object source, String message)
    {
        if (isDebugRelevant(mode))
            debug(source.getClass().getSimpleName() + "@" + Integer.toHexString(source.hashCode()) + ": " + message);
    }

    private static void debug(String message)
    {
        Log.d(UniversalLoader.class.getSimpleName(), message);
    }
}
