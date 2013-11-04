
package com.michaelflisar.universalloader.interfaces;

import com.michaelflisar.universalloader.data.main.ULFragmentKey;
import com.michaelflisar.universalloader.data.main.ULKey;

public interface IUniversalLoaderListener
{
    public ULFragmentKey getFragmentKey();

    public void onLoaderStarted();

    public void onDataReceived(ULKey key, Object data);
}
