package com.rs.leanbacknative.cardViews;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rs.leanbacknative.R;
import com.rs.leanbacknative.models.Card;
import com.rs.leanbacknative.utils.Utils;

public abstract class AbstractCardView extends DefaultImageCardView {
    protected FrameLayout layout;
    protected RelativeLayout overlayImageWrapper;
    protected ImageView overlayImage;
    protected ProgressBar progressBar;
    protected TextView overlayTitleView;
    protected TextView overlaySubtitleView;
    // protected TextView liveBadge;
    protected View gradient;
    protected TextView overlayRemainingTimeView;
    protected FrameLayout deleteLayout;
    protected TextView serviceCardTitleView;
    protected ImageView serviceCardImageView;
    protected TextView overlaySizeView;

    public AbstractCardView(Context context) {
        super(context);
    }

    public void buildImageCardView() {
        setBackgroundColor(Color.TRANSPARENT);
        setFocusable(true);
        setFocusableInTouchMode(true);

        buildCardView();

        layout = findViewById(R.id.overlay_container);
        mImageView = findViewById(com.rs.leanbacknative.R.id.main_image);
        progressBar = findViewById(R.id.progress_bar);
        overlayTitleView = findViewById(R.id.overlay_title);
        overlaySubtitleView = findViewById(R.id.overlay_subtitle);
        // liveBadge = findViewById(R.id.live_badge);
        gradient = findViewById(R.id.gradient);
        overlayRemainingTimeView = findViewById(R.id.overlay_remaining_time);
        deleteLayout = findViewById(R.id.delete_icon_layout);
        overlaySizeView = findViewById(R.id.overlay_size_title);

        if (mImageView != null) {
            mFadeInAnimator = ObjectAnimator.ofFloat(mImageView, ALPHA, 1f);
            mFadeInAnimator.setDuration(
                    mImageView.getResources().getInteger(android.R.integer.config_shortAnimTime));
        }

        serviceCardTitleView = findViewById(R.id.service_text);
        serviceCardImageView = findViewById(R.id.service_icon);
    }

    public abstract void buildCardView();

    public void setLayoutDimensions(int width, int height) {
        ViewGroup.LayoutParams lp = layout.getLayoutParams();
        lp.width = width;
        lp.height = height;
        layout.setLayoutParams(lp);
    }

    public View getGradientView() {
        return gradient;
    }

    public TextView getOverlayTitleView() {
        return overlayTitleView;
    }

    public TextView getOverlaySubtitleView() {
        return overlaySubtitleView;
    }

    public TextView getOverlayRemainingTimeView() {
        return overlayRemainingTimeView;
    }

    protected void setGradientCornerRadius(int borderRadius) {
//        GradientDrawable drawable = (GradientDrawable) gradient.getBackground();
//        drawable.setCornerRadius(borderRadius);
    }

    protected void setProgressBar(Card card) {
        long startTimestamp = card.getProgramStartTimestamp();
        long endTimestamp = card.getProgramEndTimestamp();
        int progress = card.getProgress();
        // Boolean displayLiveBadge = card.getDisplayLiveBadge();
        // String badgeColor = card.getLiveBadgeColor();
        // String progressBarColor = card.getLiveProgressBarColor();

        if ((startTimestamp != 0 && endTimestamp != 0) || progress != -1) {
            // GradientDrawable drawable = (GradientDrawable) liveBadge.getBackground();

            // if (!displayLiveBadge) {
            //     liveBadge.setVisibility(View.INVISIBLE);
            // }

            // if (!badgeColor.isEmpty()) {
            //     drawable.setColor(Color.parseColor(badgeColor));
            // }

            // if (!progressBarColor.isEmpty()) {
            //     progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(progressBarColor)));
            // }

            if (progress > 0 && progress < 100) {
                progressBar.setProgress(progress);
            } else if (startTimestamp != 0 && endTimestamp != 0) {
                progressBar.setProgress(Utils.livePercentageLeft(card.getProgramStartTimestamp(), card.getProgramEndTimestamp()));
            }
        }
    }
}
