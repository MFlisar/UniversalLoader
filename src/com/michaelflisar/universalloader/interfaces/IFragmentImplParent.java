package com.michaelflisar.universalloader.interfaces;

import com.michaelflisar.universalloader.data.main.ULFragmentKey;
import com.michaelflisar.universalloader.data.main.ULKey;

public interface IFragmentImplParent
{
    public void onDataReceived(ULKey key, Object data);
    public ULFragmentKey createFragmentKey();
}
