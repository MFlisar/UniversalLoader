
package com.michaelflisar.universalloader.data.main;

public class ULResult
{
    private Exception mException;
    private Object mResult;

    public ULResult(Object result)
    {
        mResult = result;
        mException = null;
    }
    
    public ULResult(Exception exception)
    {
        mResult = null;
        mException = exception;
    }

    @SuppressWarnings("unchecked")
    public <T> T get()
    {
        return (T) mResult;
    }
    
    public Exception getException()
    {
        return mException;
    }
    
    public boolean isValid()
    {
        return mException == null;
    }
}
