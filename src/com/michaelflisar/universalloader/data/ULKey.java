
package com.michaelflisar.universalloader.data;


public class ULKey
{
    private String mKey = null;
    private String mSubKey = null;

    // -----------------
    // constructors
    // -----------------

    public ULKey(String key)
    {
        mKey = key;
    }
    
    public ULKey(String key, String subKey)
    {
        mKey = key;
        mSubKey = subKey;
    }

    public ULKey(Class<?> c)
    {
        mKey = c.getName();
    }
    
    public ULKey getSubKey(String subKey)
    {
        return new ULKey(mKey, subKey);
    }
    
    public boolean isSubKey(ULKey key)
    {
        if (key.mKey.equals(mKey))
            return true;
        return false;
    }

    // -----------------
    // functions
    // -----------------

    public String toString()
    {
        return mKey + (mSubKey != null ? "|" + mSubKey : "");
    }

    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ULKey other = (ULKey) obj;

        return toString().equals(other.toString());
    }

}
