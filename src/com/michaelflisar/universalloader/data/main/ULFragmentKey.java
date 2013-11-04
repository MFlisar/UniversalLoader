
package com.michaelflisar.universalloader.data.main;

public class ULFragmentKey extends ULKey
{
    public ULFragmentKey(Class<?> c)
    {
        super(c);
    }

    public ULFragmentKey(String key)
    {
        super(key);
    }
    
    public ULFragmentKey(String key, String subKey)
    {
        super(key, subKey);
    }
    
    @Override
    public ULFragmentKey getSubKey(String subKey)
    {
        return new ULFragmentKey(key, (this.subKey != null ? this.subKey : "") + subKey);
    }
}
