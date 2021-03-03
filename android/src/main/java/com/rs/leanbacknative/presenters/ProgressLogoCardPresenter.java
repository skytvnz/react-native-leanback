package com.rs.leanbacknative.presenters;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.react.bridge.ReadableMap;
import com.rs.leanbacknative.cardViews.ProgressLogoCardViewView;
import com.rs.leanbacknative.models.Card;

public class ProgressLogoCardPresenter extends AbstractCardPresenter<ProgressLogoCardViewView> {
    public ProgressLogoCardPresenter(ReadableMap attributes) {
        initializeAttributes(attributes);
    }

    @Override
    protected ProgressLogoCardViewView onCreateView(Context context) {
        ProgressLogoCardViewView cardView = new ProgressLogoCardViewView(context);
        cardView.buildImageCardView();

        return cardView;
    }

    @Override
    public void onBindViewHolder(Card card, ProgressLogoCardViewView cardView) {
        cardView.updateUI(card, mBorderRadius, mCardWidth, mCardHeight);

        if (!card.getOverlayImageUrl().isEmpty()) {
            Glide.with(cardView.getContext())
                    .load(card.getOverlayImageUrl())
                    .apply(RequestOptions.fitCenterTransform())
                    .into(cardView.getOverlayImageView());
        }

        loadMainImage(cardView.getMainImageView(), card);
    }
}
