package com.michaelflisar.universalloader.interfaces;

import com.michaelflisar.universalloader.data.main.ULFragmentKey;
import com.michaelflisar.universalloader.data.main.ULKey;
import com.michaelflisar.universalloader.data.main.ULResult;

public interface IFragmentImplParent
{
    public void onLoaderStarted();
    public void onDataReceived(ULKey key, ULResult result);
    public ULFragmentKey createFragmentKey();
}
