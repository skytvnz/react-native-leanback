package com.rs.leanbacknative.managers;

import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.rs.leanbacknative.cardViews.CustomGridView;

public class MyComponentManager extends SimpleViewManager {
    @NonNull
    @Override
    public String getName() {
        return "CustomGridView";
    }

    @NonNull
    @Override
    protected View createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new CustomGridView(reactContext);
    }

    @ReactProp(name = "gridData")
    public void setgridData(CustomGridView customGridView,String data){
        customGridView.setData(data);
    }

    @ReactProp(name = "gridColumns")
    public void setGridColumns(CustomGridView customGridView,int cols){
        customGridView.setNumColumns(cols);
    }

    @ReactProp(name = "imgHeight" )
    public void setImgHeight(CustomGridView customGridView,int h){
        customGridView.setImgHeight(customGridView.getContext(),h);
    }

    @ReactProp(name = "imgWidth")
    public void setImageWidth(CustomGridView customGridView,int w){
        customGridView.setImgWidth(customGridView.getContext(),w);
    }
}
