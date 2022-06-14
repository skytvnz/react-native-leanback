package com.rs.leanbacknative.managers;

import androidx.annotation.NonNull;
import androidx.leanback.app.VerticalGridFragment;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.rs.leanbacknative.layouts.LeanbackGridLayout;

public class LeanbackGrid5ColManager extends BaseGridManager {
    private static final String REACT_CLASS = "LeanbackGrid5Col";

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @Override
    protected LeanbackGridLayout createViewInstance(@NonNull ThemedReactContext reactContext) {
        VerticalGridFragment gridFragment = new VerticalGridFragment();
        LeanbackGridLayout customGrid = new LeanbackGridLayout(reactContext, gridFragment, 5);

        addView(customGrid, gridFragment.getView(), 0);

        return customGrid;
    }
}

