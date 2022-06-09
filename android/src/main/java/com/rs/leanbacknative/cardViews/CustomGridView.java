package com.rs.leanbacknative.cardViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
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

import com.customgridcomponent.thegird.listeners.KeyListener;
import com.customgridcomponent.thegird.views.SimpleVerticleGrid;
import com.google.gson.Gson;
import com.rs.leanbacknative.models.Data;
import com.rs.leanbacknative.presenters.CardPresenterSelector1;

import java.util.Arrays;
import java.util.List;

public class CustomGridView extends RelativeLayout implements KeyListener {

    //private static final int COLUMNS = 4;
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
    SimpleVerticleGrid verticalGridView;
    VerticalGridPresenter gridPresenter;

    ArrayObjectAdapter adapter;

    private int COLUMNS = 5;
    private int mSelectedPosition = -1;

    private static final String TAG = "CustomGridView";

    Context mContext;


    boolean isReachedEnd = false;
    boolean isReachedTop = false;

    int imgHeight,imgWidth;

    PresenterSelector cardPresenterSelector;

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
        protected void onCreate(ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof ViewGroup) {
                TransitionHelper.setTransitionGroup((ViewGroup) viewHolder.itemView,
                        true);
            }
            if (mShadowOverlayHelper != null) {
                mShadowOverlayHelper.onViewCreated(viewHolder.itemView);
            }
        }

        @Override
        public void onBind(final ViewHolder itemViewHolder) {
            // Only when having an OnItemClickListener, we attach t    he OnClickListener.
            if (getOnItemViewClickedListener() != null) {
                final View itemView = itemViewHolder.getViewHolder().view;
                itemView.setOnClickListener(new OnClickListener() {
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
        public void onUnbind(ViewHolder viewHolder) {
            if (getOnItemViewClickedListener() != null) {
                viewHolder.getViewHolder().view.setOnClickListener(null);
            }
        }

        @Override
        public void onAttachedToWindow(ViewHolder viewHolder) {
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


    public void setImgHeightWidth(Context context,int h,int w){
        this.imgHeight = h;
        this.imgWidth = w;
        init(context);
    }




    public void init(Context context) {
        this.mContext = context;
        verticalGridView = new SimpleVerticleGrid(context);
        verticalGridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        verticalGridView.setKeyListener(this);

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

        //   verticalGridView.setNumColumns(5);
        gridPresenter = new VerticalGridPresenter(ZOOM_FACTOR);
        gridPresenter.setNumberOfColumns(COLUMNS);
        setGridPresenter();


    }

    public void setImgHeight(Context context,int h){
        this.imgHeight = h;
        cardPresenterSelector = new CardPresenterSelector1(context,imgHeight,imgWidth);
        adapter = new ArrayObjectAdapter(cardPresenterSelector);
        verticalGridView.setOnChildLaidOutListener(mChildLaidOutListener);
    }

    public void setImgWidth(Context context,int w){
        this.imgWidth = w;
        cardPresenterSelector = new CardPresenterSelector1(context,imgHeight,imgWidth);
        adapter = new ArrayObjectAdapter(cardPresenterSelector);
        verticalGridView.setOnChildLaidOutListener(mChildLaidOutListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        setDummyData();


        super.onDraw(canvas);
    }

    public final void setNumColumns(int cols) {
        this.COLUMNS = cols;
        verticalGridView.setNumColumns(COLUMNS);
    }

    public final void setData(String data) {
        List<Data> dataList = Arrays.asList(new Gson().fromJson(data,Data[].class));
        for (Data d:dataList) {
            adapter.add(d);
        }

        itemBridgeAdapter.setAdapter(adapter);
        Log.e(TAG, "setDummyData: " + itemBridgeAdapter.getItemCount());

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

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        Log.e(TAG, "onRequestFocusInDescendants: " + direction);
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    public void onKeyEvent(KeyEvent event) {
        Log.e(TAG, "onKeyEvent: " + event);
        verticalGridView.getSelectedPosition();
        int size = verticalGridView.getAdapter().getItemCount();
        Log.e(TAG, "onKeyEvent: pos " + verticalGridView.getSelectedPosition() + " last: " + (size - COLUMNS));


        int selectedPos = verticalGridView.getSelectedPosition();
        if (selectedPos > (size - COLUMNS - 1)) {
            isReachedTop = false;
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {

                    if (!isReachedEnd) {
                        isReachedEnd = true;
                    } else {
                        getRootView().findViewById(getNextFocusDownId()).requestFocus();
                    }

                    Log.e(TAG, "onKeyEvent: focus down");
                }
            }
        } else if (selectedPos < COLUMNS) {
            isReachedEnd = false;
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                    if (!isReachedTop){
                        isReachedTop = true;
                    } else {
                        getRootView().findViewById(getNextFocusUpId()).requestFocus();
                    }
                }

                Log.e(TAG, "onKeyEvent: focus up");
            }
        } else if (selectedPos % COLUMNS == 0) {
            isReachedEnd = false;
            isReachedTop = false;
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                    //     getRootView().findViewById(getNextFocusLeftId()).requestFocus();
                    Log.e(TAG, "onKeyEvent: focus left");
                }
            }
        }else {
            isReachedTop = false;
            isReachedEnd = false;
        }
    }
}
