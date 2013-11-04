
package com.michaelflisar.universalloader.data.main;


public class ULKey
{
    protected String key = null;
    protected String subKey = null;

    // -----------------
    // constructors
    // -----------------

    public ULKey(String key)
    {
        this.key = key;
    }
    
    public ULKey(String key, String subKey)
    {
        this.key = key;
        this.subKey = subKey;
    }

    public ULKey(Class<?> c)
    {
        key = c.getName();
    }
    
    public ULKey getSubKey(String subKey)
    {
        return new ULKey(key, (this.subKey != null ? this.subKey : "") + subKey);
    }
    
    public boolean isSubKey(ULKey key)
    {
        if (key.key.equals(key))
            return true;
        return false;
    }

    // -----------------
    // functions
    // -----------------

    public String toString()
    {
        return key + (subKey != null ? "|" + subKey : "");
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
