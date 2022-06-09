package com.customgridcomponent.thegird.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.leanback.widget.VerticalGridView;

import com.customgridcomponent.thegird.listeners.KeyListener;


public class SimpleVerticleGrid extends VerticalGridView {
    KeyListener keyListener;
    private  final String TAG="SimpleVerticleGrid";



    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public SimpleVerticleGrid(Context context) {
        super(context);
    }

    public SimpleVerticleGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleVerticleGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int size = getAdapter().getItemCount();

        keyListener.onKeyEvent(event);
        return super.dispatchKeyEvent(event);
    }
}
