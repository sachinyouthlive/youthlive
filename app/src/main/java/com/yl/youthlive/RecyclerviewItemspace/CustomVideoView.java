package com.yl.youthlive.RecyclerviewItemspace;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by USER on 11/13/2017.
 */

public class CustomVideoView extends android.widget.VideoView {
    private int width;
    private int height;
    private Context context;
    private VideoSizeChangeListener listener;
    private boolean isFullscreen;

    public CustomVideoView(Context context) {
        super(context);
        init(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * get video screen width and height for calculate size
     *
     * @param context Context
     */
    private void init(Context context) {
        this.context = context;
        setScreenSize();
    }

    /**
     * calculate real screen size
     */
    private void setScreenSize() {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();

        //new pleasant way to get real metrics
        DisplayMetrics realMetrics = new DisplayMetrics();
        display.getRealMetrics(realMetrics);
        width = realMetrics.widthPixels;
        height = realMetrics.heightPixels;

        // when landscape w > h, swap it
        if (width > height) {
            int temp = width;
            width = height;
            height = temp;
        }
    }

    /**
     * set video size change listener
     */
    public void setVideoSizeChangeListener(VideoSizeChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // full screen when landscape
            setSize(height, width);
            if (listener != null) listener.onFullScreen();
            isFullscreen = true;
        } else {
            // height = width * 9/16
            setSize(width, width * 9 / 16);
            if (listener != null) listener.onNormalSize();
            isFullscreen = false;
        }
    }

    /**
     * @return true: fullscreen
     */
    public boolean isFullscreen() {
        return isFullscreen;
    }

    /**
     * set video sie
     *
     * @param w Width
     * @param h Height
     */
    private void setSize(int w, int h) {
        setMeasuredDimension(w, h);
        getHolder().setFixedSize(w, h);
    }

    public interface VideoSizeChangeListener {
        /**
         * when landscape
         */
        void onFullScreen();

        /**
         * when portrait
         */
        void onNormalSize();
    }
}