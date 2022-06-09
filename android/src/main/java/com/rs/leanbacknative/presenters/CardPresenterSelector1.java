package com.rs.leanbacknative.presenters;

import android.content.Context;

import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

public class CardPresenterSelector1 extends PresenterSelector {

    private final Context mContext;
    int imgWidth, imgHeight;
    Presenter presenter;

    public CardPresenterSelector1(Context mContext,int h,int w) {
        this.mContext = mContext;
        this.imgHeight = h;
        this.imgWidth  = w;
    }

    @Override
    public Presenter getPresenter(Object item) {
        presenter = new ImageCardViewPresenter(imgHeight,imgWidth);

        return presenter;
    }

    @Override
    public Presenter[] getPresenters() {
        return super.getPresenters();
    }

}
