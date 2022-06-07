package com.whyphy.tvgridmodule;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.leanback.system.Settings;
import androidx.leanback.transition.TransitionHelper;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ItemBridgeAdapterShadowOverlayWrapper;
import androidx.leanback.widget.OnChildLaidOutListener;
import androidx.leanback.widget.OnChildSelectedListener;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.ShadowOverlayHelper;
import androidx.leanback.widget.VerticalGridPresenter;
import androidx.leanback.widget.VerticalGridView;
import com.rs.leanbacknative.presenters.CardPresenterSelector;

import com.whyphy.tvgridmodule.model.Movie;

public class CustomGridView extends RelativeLayout {

    private static final int COLUMNS = 4;
    private static final int ZOOM_FACTOR = FocusHighlight.ZOOM_FACTOR_MEDIUM;

    private OnItemViewClickedListener mOnItemViewClickedListener;
    OnItemViewSelectedListener mOnItemViewSelectedListener;

    ShadowOverlayHelper mShadowOverlayHelper;
    ItemBridgeAdapter itemBridgeAdapter;
    private boolean mUseFocusDimmer = true;
    private boolean mShadowEnabled = true;
    private boolean mKeepChildForeground = true;
    private boolean mRoundedCornersEnabled = true;
    private ItemBridgeAdapter.Wrapper mShadowOverlayWrapper;
    private int mFocusZoomFactor;
    VerticalGridView verticalGridView;
    VerticalGridPresenter gridPresenter;

    ArrayObjectAdapter adapter;

    private int mSelectedPosition = -1;

    private static final String TAG = "CustomGridView";

    final private OnItemViewSelectedListener mViewSelectedListener =
            new OnItemViewSelectedListener() {
                @Override
                public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                           RowPresenter.ViewHolder rowViewHolder, Row row) {
                    int position = verticalGridView.getSelectedPosition();
                    gridOnItemSelected(position);
                    if (mOnItemViewSelectedListener != null) {
                        mOnItemViewSelectedListener.onItemSelected(itemViewHolder, item,
                                rowViewHolder, row);
                    }
                }
            };


    final private OnChildLaidOutListener mChildLaidOutListener =
            new OnChildLaidOutListener() {
                @Override
                public void onChildLaidOut(ViewGroup parent, View view, int position, long id) {
                    if (position == 0) {
                        showOrHideTitle();
                    }
                }
            };



    void showOrHideTitle() {
        if (verticalGridView.findViewHolderForAdapterPosition(mSelectedPosition)
                == null) {
            return;
        }
        if (!verticalGridView.hasPreviousViewInSameRow(mSelectedPosition)) {
            //    showTitle(true);
        } else {
            //  showTitle(false);
        }
    }


    class VerticalGridItemBridgeAdapter extends ItemBridgeAdapter {
        @Override
        protected void onCreate(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof ViewGroup) {
                TransitionHelper.setTransitionGroup((ViewGroup) viewHolder.itemView,
                        true);
            }
            if (mShadowOverlayHelper != null) {
                mShadowOverlayHelper.onViewCreated(viewHolder.itemView);
            }
        }

        @Override
        public void onBind(final ItemBridgeAdapter.ViewHolder itemViewHolder) {
            // Only when having an OnItemClickListener, we attach t    he OnClickListener.
            if (getOnItemViewClickedListener() != null) {
                final View itemView = itemViewHolder.getViewHolder().view;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getOnItemViewClickedListener() != null) {
                            // Row is always null
                            getOnItemViewClickedListener().onItemClicked(
                                    itemViewHolder.getViewHolder(), itemViewHolder.getItem(), null, null);
                        }
                    }
                });
            }
        }

        @Override
        public void onUnbind(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (getOnItemViewClickedListener() != null) {
                viewHolder.getViewHolder().view.setOnClickListener(null);
            }
        }

        @Override
        public void onAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            viewHolder.itemView.setActivated(true);
        }
    }


    public CustomGridView(Context context) {
        super(context);
        //this(context,null);
        Log.e(TAG, "CustomGridView: 1");
    }

    public CustomGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        Log.e(TAG, "CustomGridView: 2");
    }

//    public CustomGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        Log.e(TAG, "CustomGridView: 3");
//    }
//
//    public CustomGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        Log.e(TAG, "CustomGridView: 4");
//    }


    public void init(Context context){
        Log.e(TAG, "init: ");
        verticalGridView = new VerticalGridView(context);
        verticalGridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        itemBridgeAdapter = new VerticalGridItemBridgeAdapter();
        mShadowOverlayHelper = new ShadowOverlayHelper.Builder()
                .needsOverlay(mUseFocusDimmer)
                .needsShadow(needsDefaultShadow())
                .needsRoundedCorner(mRoundedCornersEnabled)
                .preferZOrder(isUsingZOrder(context))
                .keepForegroundDrawable(mKeepChildForeground)
                .options(createShadowOverlayOptions())
                .build(context);

        if (mShadowOverlayHelper.needsWrapper()) {
            mShadowOverlayWrapper = new ItemBridgeAdapterShadowOverlayWrapper(
                    mShadowOverlayHelper);
        }


        itemBridgeAdapter.setWrapper(mShadowOverlayWrapper);
        mShadowOverlayHelper.prepareParentForShadow(verticalGridView);
        verticalGridView.setFocusDrawingOrderEnabled(mShadowOverlayHelper.getShadowType() != ShadowOverlayHelper.SHADOW_DYNAMIC);
        FocusHighlightHelper.setupBrowseItemFocusHighlight(itemBridgeAdapter,
                mFocusZoomFactor, mUseFocusDimmer);

        verticalGridView.setOnChildSelectedListener(new OnChildSelectedListener() {
            @Override
            public void onChildSelected(ViewGroup parent, View view, int position, long id) {
                selectChildView(view);
            }
        });

        verticalGridView.setNumColumns(5);
        gridPresenter = new VerticalGridPresenter(ZOOM_FACTOR);
        gridPresenter.setNumberOfColumns(COLUMNS);
        setGridPresenter();

        PresenterSelector cardPresenterSelector = new CardPresenterSelector(context);
        adapter = new ArrayObjectAdapter(cardPresenterSelector);
        verticalGridView.setOnChildLaidOutListener(mChildLaidOutListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        setDummyData();


        super.onDraw(canvas);
    }

    public final void setDummyData(){
        for (int i = 0; i <20 ; i++) {
            String url = "https://m.media-amazon.com/images/M/MV5BMWEwNjhkYzYtNjgzYy00YTY2LThjYWYtYzViMGJkZTI4Y2MyXkEyXkFqcGdeQXVyNTM0OTY1OQ@@._V1_FMjpg_UX1000_.jpg";
            adapter.add(new Movie("url","test"+i));
        }
        itemBridgeAdapter.setAdapter(adapter);
        Log.e(TAG, "setDummyData: "+itemBridgeAdapter.getItemCount());

        verticalGridView.setAdapter(itemBridgeAdapter);

        this.addView(verticalGridView);

    }

    void gridOnItemSelected(int position) {
        if (position != mSelectedPosition) {
            mSelectedPosition = position;
            showOrHideTitle();
        }
    }

    public void setGridPresenter() {
        if (gridPresenter == null) {
            throw new IllegalArgumentException("Grid presenter may not be null");
        }
        gridPresenter.setOnItemViewSelectedListener(mViewSelectedListener);
        if (mOnItemViewClickedListener != null) {
            gridPresenter.setOnItemViewClickedListener(mOnItemViewClickedListener);
        }
    }

    public final OnItemViewClickedListener getOnItemViewClickedListener() {
        return mOnItemViewClickedListener;
    }

    final boolean needsDefaultShadow() {
        return isUsingDefaultShadow() && getShadowEnabled();
    }

    public boolean isUsingDefaultShadow() {
        return ShadowOverlayHelper.supportsShadow();
    }

    public final boolean getShadowEnabled() {
        return mShadowEnabled;
    }

    public boolean isUsingZOrder(Context context) {
        return !Settings.getInstance(context).preferStaticShadows();
    }

    protected ShadowOverlayHelper.Options createShadowOverlayOptions() {
        return ShadowOverlayHelper.Options.DEFAULT;
    }

    void selectChildView(View view) {
        if (mOnItemViewSelectedListener != null) {
            ItemBridgeAdapter.ViewHolder ibh = (view == null) ? null :
                    (ItemBridgeAdapter.ViewHolder) verticalGridView.getChildViewHolder(view);
            if (ibh == null) {
                mOnItemViewSelectedListener.onItemSelected(null, null, null, null);
            } else {
                mOnItemViewSelectedListener.onItemSelected(ibh.getViewHolder(), ibh.getItem(), null, null);
            }
        }
    }
}
