package com.yl.youthlive;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class VerticalFragment extends Fragment {


    List<liveBean> mainList = new ArrayList<>();
    //List<liveBean> list = new ArrayList<>();


    CustomViewPager pager;

    int pos;

    public void setList(List<liveBean> mainList , int pos) {
        this.mainList = mainList;
        /*for (int i = 0; i < mainList.size(); i++) {
            if (mainList.get(i).getType().equals("live")) {
                list.add(mainList.get(i));
            }
        }*/

        this.pos = pos;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vertical_fragment_layout, container, false);

        pager = view.findViewById(R.id.pager);

        pager.setOffscreenPageLimit(0);

        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), mainList);
        pager.setAdapter(adapter);

        pager.setCurrentItem(pos);

        return view;

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        List<liveBean> ll = new ArrayList<>();

        public PagerAdapter(FragmentManager fm, List<liveBean> ll) {
            super(fm);
            this.ll = ll;
        }

        @Override
        public Fragment getItem(int position) {
            final bean b1 = (bean) getContext().getApplicationContext();


            if (ll.get(position).getType().equals("live"))
            {

                VideoPlayerFragment frag = new VideoPlayerFragment();
                Bundle b = new Bundle();
                b.putString("uri", ll.get(position).getLiveId());
                b.putString("pic", b1.BASE_URL + ll.get(position).getUserImage());
                frag.setArguments(b);
                return frag;
            }
            else
            {
                YoutubePlayerFragment frag = new YoutubePlayerFragment();
                Bundle b = new Bundle();
                b.putString("uri", ll.get(position).getChannelUrl());
                b.putString("liveId", ll.get(position).getLiveId());
                b.putString("pic", b1.BASE_URL + ll.get(position).getUserImage());
                frag.setArguments(b);
                return frag;
            }


        }

        @Override
        public int getCount() {
            return ll.size();
        }
    }

}
