package com.yl.youthlive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by TBX on 11/22/2017.
 */

public class CoverImage extends Fragment {

    String url;
    ImageView image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cober_image_layout , container , false);

        url = getArguments().getString("url");
        image = (ImageView)view.findViewById(R.id.image);

        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(url , image);


        return  view;
    }

}
