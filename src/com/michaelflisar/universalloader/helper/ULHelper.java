
package com.michaelflisar.universalloader.helper;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import com.michaelflisar.universalloader.UniversalLoader;

public class ULHelper
{
    public static UniversalLoader getUniversalLoader(FragmentActivity activity, boolean createIfNotexists)
    {
        UniversalLoader universalLoader = (UniversalLoader) activity.getSupportFragmentManager().findFragmentByTag(UniversalLoader.class.getName());
        if (universalLoader == null && createIfNotexists)
        {
            universalLoader = new UniversalLoader();
            activity.getSupportFragmentManager().beginTransaction().add(universalLoader, UniversalLoader.class.getName()).commitAllowingStateLoss();
        }
        return universalLoader;
    }

    // ------------------------------------------------
    // copied executors for backwards compatibility
    // ------------------------------------------------

    private static class SerialExecutor implements Executor
    {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r)
        {
            mTasks.offer(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        r.run();
                    }
                    finally
                    {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null)
            {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext()
        {
            if ((mActive = mTasks.poll()) != null)
            {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;

    private static ThreadFactory sThreadFactory = null;
    private static BlockingQueue<Runnable> sPoolWorkQueue = null;

    private static Executor THREAD_POOL_EXECUTOR = null;
    private static Executor SERIAL_EXECUTOR = null;

    public static Executor getExecutor(boolean multiThreadEnabled)
    {
        if (Build.VERSION.SDK_INT >= 11)
        {
            if (multiThreadEnabled)
                return AsyncTask.THREAD_POOL_EXECUTOR;
            return AsyncTask.SERIAL_EXECUTOR;
        }
        else
        {
            if (multiThreadEnabled)
            {
                createExecutorIfNecessary(multiThreadEnabled);
                return THREAD_POOL_EXECUTOR;
            }
            createExecutorIfNecessary(multiThreadEnabled);
            return SERIAL_EXECUTOR;
        }
    }

    private static void createExecutorIfNecessary(boolean multiThreadEnabled)
    {
        if (multiThreadEnabled)
        {
            if (THREAD_POOL_EXECUTOR == null)
            {
                sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);
                sThreadFactory = new ThreadFactory()
                {
                    private final AtomicInteger mCount = new AtomicInteger(1);

                    public Thread newThread(Runnable r)
                    {
                        return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
                    }
                };
                THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
            }
        }
        else
        {
            if (SERIAL_EXECUTOR == null)
                SERIAL_EXECUTOR = new SerialExecutor();
        }
    }
}
