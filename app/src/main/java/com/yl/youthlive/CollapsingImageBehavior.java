package com.yl.youthlive;


import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

public class CollapsingImageBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return dependency instanceof Toolbar;
    }


    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView avatar, View dependency) {
        modifyAvatarDependingDependencyState(avatar, dependency);
        return true;
    }

    private void modifyAvatarDependingDependencyState(
            CircleImageView avatar, View dependency) {
        //  avatar.setY(dependency.getY());
        //  avatar.setBlahBlah(dependency.blah / blah);
    }
}
