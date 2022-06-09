package com.rs.leanbacknative.presenters;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.rs.leanbacknative.R;
import com.rs.leanbacknative.models.Data;


public class ImageCardViewPresenter extends Presenter {

    private Drawable mDefaultCardImage;
    private static final String TAG = "ImageCardViewPresenter";

   public int imgHeight = 100;
    public int imgWidth = 100;


    public ImageCardViewPresenter(int imgHeight, int imgWidth) {
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mDefaultCardImage = parent.getResources().getDrawable(R.drawable.photo,null);

        ImageCardView cardView = new ImageCardView(parent.getContext()){
            @Override
            public void setSelected(boolean selected) {
                //Log.e(TAG, "setSelected: "+selected);
                super.setSelected(selected);
            }
        };

        cardView.setMainImageDimensions(100,100);
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Data movie = (Data) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        cardView.setTitleText(movie.getTitle());
       // cardView.setLayoutParams(new FrameLayout.LayoutParams(100,100));

        cardView.setMainImage(mDefaultCardImage);

//        Glide.with(cardView.getContext())
//                .load(movie.getThumbUrl())
//                .apply(RequestOptions.errorOf(mDefaultCardImage))
//                .into(cardView.getMainImageView());

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
