
package com.michaelflisar.universalloader.interfaces;

import com.michaelflisar.universalloader.data.ULKey;

public interface ILoaderFinishedListener
{
    public void onLoaderFinished(ULKey key, Object data);

    public void onLoaderRestarted(ULKey key);

    public boolean containsKey(ULKey key);
}
