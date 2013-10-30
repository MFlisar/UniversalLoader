
package com.michaelflisar.universalloader.demo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.michaelflisar.pagermanager.IPagerFragment;
import com.michaelflisar.pagermanager.MFragmentPagerStateAdapter;
import com.michaelflisar.pagermanager.MPagerManager;
import com.michaelflisar.universalloader.ULFragment;
import com.michaelflisar.universalloader.data.ULFragmentLoaders;
import com.michaelflisar.universalloader.data.ULKey;

public class TestLoaderFragment extends ULFragment
{
    public static final ULKey KEY = new ULKey(TestLoaderFragment.class);
    
    @Override
    public ULFragmentLoaders createLoaders()
    {
        ULFragmentLoaders loaders = new ULFragmentLoaders();
        loaders.add(KEY, Helper.getCallable(2));
        return loaders;
    }

    @Override
    public View onCreateUserView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.view_pager, null);

        PagerSlidingTabStrip indicator = (PagerSlidingTabStrip) v.findViewById(R.id.indicator);
        ViewPager pager = (ViewPager) v.findViewById(R.id.pager);

        new MPagerManager<TestPageFragment, MFragmentPagerStateAdapter<TestPageFragment>>(pager, indicator, new MFragmentPagerStateAdapter<TestPageFragment>(getChildFragmentManager())
        {
            @Override
            public TestPageFragment createFragment(int pos)
            {
                return TestPageFragment.newInstance(pos);
            }

            @Override
            public int getCount()
            {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int pos)
            {
                return "Page " + String.valueOf((pos + 1));
            }
        });

        return v;
    }

    public static class TestPageFragment extends ULFragment implements IPagerFragment, OnClickListener
    {
        private int mPos;
        private int count1 = 0;
        private int count2 = 0;

        ULKey key1 = null;
        ULKey key2 = null;

        static TestPageFragment newInstance(int pos)
        {
            TestPageFragment f = new TestPageFragment();
            Bundle args = new Bundle();
            args.putInt("pos", pos);
            f.setArguments(args);
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        { 
            mPos = getArguments() != null ? getArguments().getInt("pos") : 0;
            
            // create a unique key for every loader task
            // do this ALWAYS BEFORE onCreate, the loader may be loading data and wants to put it's result
            // to the retained fragment
            key1 = new ULKey(getClass().getName().toString() + "|" + 1 + "|" + mPos);
            key2 = new ULKey(getClass().getName().toString() + "|" + 2 + "|" + mPos);
            
            super.onCreate(savedInstanceState);

            if (savedInstanceState != null)
            {
                count1 = savedInstanceState.getInt("count1");
                count2 = savedInstanceState.getInt("count2");
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState)
        {
            super.onSaveInstanceState(outState);
            outState.putInt("count1", count1);
            outState.putInt("count2", count2);
        }
        
        @Override
        public ULFragmentLoaders createLoaders()
        {
            ULFragmentLoaders loaders = new ULFragmentLoaders();
            loaders.add(key1, Helper.getCallable(3));
            loaders.add(key2, Helper.getCallable(4));
            return loaders;
        }
        
        @Override
        public void onLoaderFinished(View v, ULKey key, Object data)
        {
            if (v == null)
                return;

            // use this functions to update UI incrementally

            if (key.equals(key1))
            {
                TextView tv1 = (TextView) v.findViewById(android.R.id.text1);
                tv1.setText(((String) data) + " (count: " + count1 + ")");
            }
            else if (key.equals(key2))
            {
                TextView tv2 = (TextView) v.findViewById(android.R.id.text2);
                tv2.setText(((String) data) + " (count: " + count2 + ")");
            }
        }

        @Override
        public View onCreateUserView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            return Helper.getTestFragmentView(getActivity(), this);
        }

        @Override
        public int getPagerIndex()
        {
            return mPos;
        }

        @Override
        public void onClick(View v)
        {
            ULKey key = null;
            if (v.getId() == 1)
            {
                count1++;
                key = key1;
            }
            else if (v.getId() == 2)
            {
                count2++;
                key = key2;
            }
            else
            {
                count1++;
                count2++;
                getUniversalLoader().restartLoader(key1, null);
                getUniversalLoader().restartLoader(key2, null);
                return;
            }

            getUniversalLoader().restartLoader(key, null);
        }

        
    }    
}
