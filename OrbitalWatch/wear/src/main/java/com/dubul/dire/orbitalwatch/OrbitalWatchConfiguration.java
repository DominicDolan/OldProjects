package com.dubul.dire.orbitalwatch;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by domin on 10 Aug 2016.
 */
public class OrbitalWatchConfiguration extends Activity implements WearableListView.ClickListener, WearableListView.OnScrollListener {

    private TextView mHeader;

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_layout);

        mHeader = (TextView) findViewById(R.id.header);
        WearableListView listView = (WearableListView) findViewById(R.id.config_picker);
        BoxInsetLayout content = (BoxInsetLayout) findViewById(R.id.configlayout);
        // BoxInsetLayout adds padding by default on round devices. Add some on square devices.
        content.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                if (!insets.isRound()) {
                    v.setPaddingRelative(
                            (int) getResources().getDimensionPixelSize(R.dimen.content_padding_start),
                            v.getPaddingTop(),
                            v.getPaddingEnd(),
                            v.getPaddingBottom());
                }
                return v.onApplyWindowInsets(insets);
            }
        });

        listView.setHasFixedSize(true);
        listView.setClickListener(this);
        listView.addOnScrollListener(this);

        String clockFormat;
        if (Orbital.settings.is24Hour()){
            clockFormat = "Set to 12 hour";
        } else {
            clockFormat = "Set to 24 hour";
        }

        String showFunctions;
        if (Orbital.settings.isShowFunctions()){
            showFunctions = "Hide functions";
        } else {
            showFunctions = "Show functions";
        }

        String showDate;
        if (Orbital.settings.isShowDate()){
            showDate = "Hide Date";
        } else {
            showDate = "Show Date";
        }

        String[] configItemNames = {clockFormat, showDate/*, showFunctions*/};
        int[] ids = {R.drawable.ic_access_time_white_24dp
                ,R.drawable.ic_today_white_24dp
               /*, R.drawable.ic_tune_white_24dp*/};
        listView.setAdapter(new SettingsListAdapter(configItemNames, ids));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override // WearableListView.ClickListener
    public void onClick(WearableListView.ViewHolder viewHolder) {
        SettingsViewHolder settingsViewHolder = (SettingsViewHolder) viewHolder;
        updateConfigDataItem(settingsViewHolder.configItem.getPosition());
        Orbital.setPrefsFromSettings();
        finish();
    }

    @Override // WearableListView.ClickListener
    public void onTopEmptyRegionClick() {}

    @Override // WearableListView.OnScrollListener
    public void onScroll(int scroll) {}

    @Override // WearableListView.OnScrollListener
    public void onAbsoluteScrollChange(int scroll) {
        mHeader.setTextColor(Color.argb(Math.min(Math.max(255 - 3*scroll,0),255),117,117,117));
    }

    @Override // WearableListView.OnScrollListener
    public void onScrollStateChanged(int scrollState) {}

    @Override // WearableListView.OnScrollListener
    public void onCentralPositionChanged(int centralPosition) {}

    private void updateConfigDataItem(final int configPosition) {
        switch (configPosition){
            case 0:
                Orbital.settings.setIs24Hour(!Orbital.settings.is24Hour());
                break;
            case 1:
                Orbital.settings.setShowDate(!Orbital.settings.isShowDate());
                break;
//            case 2:
//                Orbital.settings.setShowFunctions(!Orbital.settings.isShowFunctions());
//                break;
        }
    }

    private class SettingsListAdapter extends WearableListView.Adapter{
        private final String[] configItems;
        private final int[] ids;

        public SettingsListAdapter(String [] configs, int[] ids){
            this.configItems = configs;
            this.ids = ids;
        }

        @Override
        public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SettingsViewHolder(new ConfigItem(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            SettingsViewHolder viewHolder = (SettingsViewHolder) holder;
            String name = configItems[position];
            int id = ids[position];
            viewHolder.configItem.setName(name);
            viewHolder.configItem.setImage(ContextCompat.getDrawable(getApplicationContext(),id));
            viewHolder.configItem.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return configItems.length;
        }
    }

    private static class SettingsViewHolder extends WearableListView.ViewHolder{
        public ConfigItem configItem;
        public SettingsViewHolder(ConfigItem itemView) {
            super(itemView);
            configItem = itemView;
        }
    }

    private static class ConfigItem extends LinearLayout implements WearableListView.OnCenterProximityListener{

        private TextView label;
        private CircledImageView circleImage;
        private int position;

        public ConfigItem(Context context) {
            super(context);
            View.inflate(context, R.layout.config_picker_item, this);
            label = (TextView) findViewById(R.id.label);
            circleImage = (CircledImageView) findViewById(R.id.circle);
        }

        @Override
        public void onCenterPosition(boolean b) {

        }

        @Override
        public void onNonCenterPosition(boolean b) {

        }

        public void setName(String name){
            label.setText(name);
        }

        public void setImage(Drawable image){
            circleImage.setImageDrawable(image);
        }

        public void setPosition(int position){
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

//    private class ColorListAdapter extends WearableListView.Adapter {
//        private final String[] mColors;
//
//        public ColorListAdapter(String[] colors) {
//            mColors = colors;
//        }
//
//        @Override
//        public ColorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ColorItemViewHolder(new ColorItem(parent.getContext()));
//        }
//
//        @Override
//        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
//            ColorItemViewHolder colorItemViewHolder = (ColorItemViewHolder) holder;
//            String colorName = mColors[position];
//            colorItemViewHolder.mColorItem.setColor(colorName);
//
//            RecyclerView.LayoutParams layoutParams =
//                    new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT);
//            int colorPickerItemMargin = (int) getResources()
//                    .getDimension(R.dimen.digital_config_color_picker_item_margin);
//            // Add margins to first and last item to make it possible for user to tap on them.
//            if (position == 0) {
//                layoutParams.setMargins(0, colorPickerItemMargin, 0, 0);
//            } else if (position == mColors.length - 1) {
//                layoutParams.setMargins(0, 0, 0, colorPickerItemMargin);
//            } else {
//                layoutParams.setMargins(0, 0, 0, 0);
//            }
//            colorItemViewHolder.itemView.setLayoutParams(layoutParams);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mColors.length;
//        }
//    }

    /** The layout of a color item including image and label. */
//    private static class ColorItem extends LinearLayout implements
//            WearableListView.OnCenterProximityListener {
//        /** The duration of the expand/shrink animation. */
//        private static final int ANIMATION_DURATION_MS = 150;
//        /** The ratio for the size of a circle in shrink state. */
//        private static final float SHRINK_CIRCLE_RATIO = .75f;
//
//        private static final float SHRINK_LABEL_ALPHA = .5f;
//        private static final float EXPAND_LABEL_ALPHA = 1f;
//
//        private final TextView mLabel;
//        private final CircledImageView mColor;
//
//        private final float mExpandCircleRadius;
//        private final float mShrinkCircleRadius;
//
//        private final ObjectAnimator mExpandCircleAnimator;
//        private final ObjectAnimator mExpandLabelAnimator;
//        private final AnimatorSet mExpandAnimator;
//
//        private final ObjectAnimator mShrinkCircleAnimator;
//        private final ObjectAnimator mShrinkLabelAnimator;
//        private final AnimatorSet mShrinkAnimator;
//
//        public ColorItem(Context context) {
//            super(context);
//            View.inflate(context, R.layout.config_picker_item, this);
//
//            mLabel = (TextView) findViewById(R.id.label);
//            mColor = (CircledImageView) findViewById(R.id.circle);
//
//            mExpandCircleRadius = mColor.getCircleRadius();
//            mShrinkCircleRadius = mExpandCircleRadius * SHRINK_CIRCLE_RATIO;
//
//            mShrinkCircleAnimator = ObjectAnimator.ofFloat(mColor, "circleRadius",
//                    mExpandCircleRadius, mShrinkCircleRadius);
//            mShrinkLabelAnimator = ObjectAnimator.ofFloat(mLabel, "alpha",
//                    EXPAND_LABEL_ALPHA, SHRINK_LABEL_ALPHA);
//            mShrinkAnimator = new AnimatorSet().setDuration(ANIMATION_DURATION_MS);
//            mShrinkAnimator.playTogether(mShrinkCircleAnimator, mShrinkLabelAnimator);
//
//            mExpandCircleAnimator = ObjectAnimator.ofFloat(mColor, "circleRadius",
//                    mShrinkCircleRadius, mExpandCircleRadius);
//            mExpandLabelAnimator = ObjectAnimator.ofFloat(mLabel, "alpha",
//                    SHRINK_LABEL_ALPHA, EXPAND_LABEL_ALPHA);
//            mExpandAnimator = new AnimatorSet().setDuration(ANIMATION_DURATION_MS);
//            mExpandAnimator.playTogether(mExpandCircleAnimator, mExpandLabelAnimator);
//        }
//
//        @Override
//        public void onCenterPosition(boolean animate) {
//            if (animate) {
//                mShrinkAnimator.cancel();
//                if (!mExpandAnimator.isRunning()) {
//                    mExpandCircleAnimator.setFloatValues(mColor.getCircleRadius(), mExpandCircleRadius);
//                    mExpandLabelAnimator.setFloatValues(mLabel.getAlpha(), EXPAND_LABEL_ALPHA);
//                    mExpandAnimator.start();
//                }
//            } else {
//                mExpandAnimator.cancel();
//                mColor.setCircleRadius(mExpandCircleRadius);
//                mLabel.setAlpha(EXPAND_LABEL_ALPHA);
//            }
//        }
//
//        @Override
//        public void onNonCenterPosition(boolean animate) {
//            if (animate) {
//                mExpandAnimator.cancel();
//                if (!mShrinkAnimator.isRunning()) {
//                    mShrinkCircleAnimator.setFloatValues(mColor.getCircleRadius(), mShrinkCircleRadius);
//                    mShrinkLabelAnimator.setFloatValues(mLabel.getAlpha(), SHRINK_LABEL_ALPHA);
//                    mShrinkAnimator.start();
//                }
//            } else {
//                mShrinkAnimator.cancel();
//                mColor.setCircleRadius(mShrinkCircleRadius);
//                mLabel.setAlpha(SHRINK_LABEL_ALPHA);
//            }
//        }
//
//        private void setColor(String colorName) {
//            mLabel.setText(colorName);
//            mColor.setCircleColor(Color.parseColor(colorName));
//        }
//
//        private int getColor() {
//            return mColor.getDefaultCircleColor();
//        }
//    }

//    private static class ColorItemViewHolder extends WearableListView.ViewHolder {
//        private final ColorItem mColorItem;
//
//        public ColorItemViewHolder(ColorItem colorItem) {
//            super(colorItem);
//            mColorItem = colorItem;
//        }
//    }
}
