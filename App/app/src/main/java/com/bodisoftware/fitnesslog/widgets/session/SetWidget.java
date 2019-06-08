package com.bodisoftware.fitnesslog.widgets.session;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bodisoftware.fitnesslog.R;


/**
 * Created by dvukman on 8/3/2017.
 */

public class SetWidget extends LinearLayout {

    private TextView mTxtHeader = null;
    private TextView mTxtWeight = null;
    private TextView mTxtReps = null;
    private ImageButton mBtnPlusWeight = null;
    private ImageButton mBtnMinusWeight = null;
    private ImageButton mBtnPlusReps = null;
    private ImageButton mBtnMinusReps = null;
    private ToggleButton mToggleDone = null;

    private Context mContext = null;
    private int mSetNumber = 0;


    public SetWidget(Context context) {
        super(context);
        initializeViews(context);
    }

    public SetWidget(Context context, AttributeSet attrs) {
        super(context,attrs);
        initializeViews(context);
    }

    public SetWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.set_widget, this);
        //view.setOrientation(LinearLayout.VERTICAL);

        mTxtHeader = (TextView) view.findViewById(R.id.txtTitle);
        mTxtWeight = (TextView) view.findViewById(R.id.txtWeight);
        mTxtReps = (TextView) view.findViewById(R.id.txtReps);
        mBtnPlusWeight = (ImageButton) view.findViewById(R.id.btnPlusWeight);
        mBtnMinusWeight = (ImageButton) view.findViewById(R.id.btnMinusWeight);
        mBtnPlusReps = (ImageButton) view.findViewById(R.id.btnPlusReps);
        mBtnMinusReps = (ImageButton) view.findViewById(R.id.btnMinusReps);
        mToggleDone = (ToggleButton) view.findViewById(R.id.toggleDone);

        mBtnPlusWeight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                float weight = getWeight();
                weight +=1;
                setWeight(weight);
            }
        });

        mBtnMinusWeight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                float weight = getWeight();
                weight -=1;
                setWeight(weight);
            }
        });

        mBtnPlusReps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int reps = getReps();
                reps++;
                setReps(reps);
            }
        });

        mBtnMinusReps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int reps = getReps();
                reps--;
                if (reps > 0) {
                    setReps(reps);
                }
            }
        });

        mToggleDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean setDone = mToggleDone.isChecked() ? true : false;
                setPlusMinusButtonsEnabled(!setDone); // If set is done, disable plus and minus buttons
            }
        });
    }

    public boolean isDone() {
        return mToggleDone.isChecked();
    }

    public void setName(final String title) {
        mTxtHeader.setText(title);
    }

    public String getName() {
        return mTxtHeader.getText().toString().trim();
    }

    public void setWeight(final float weight) {
        mTxtWeight.setText(String.valueOf(weight));
    }

    public float getWeight() {
        return Float.valueOf(mTxtWeight.getText().toString()).floatValue();
    }

    public void setReps(final int reps) {
        mTxtReps.setText(String.valueOf(reps));
    }

    public int getReps() {
        return Integer.valueOf(mTxtReps.getText().toString()).intValue();
    }

    public void setSetNumber(final int number) {
        mSetNumber = number;
    }

    public int getmSetNumber() {
        return mSetNumber;
    }

    /**
     * Finalizes the exercise and starts the appropriate animations.
     */
    public void markSetFinished(final boolean finished) {
        if (finished) {
            if (this.isDone()) {
                startCompletedSuccessfullyAnimation();
            } else {
                startNotCompletedAnimation();
            }
        } else {
            if (this.isDone()) {
                revertCompletedSuccessfullyAnimation();
            } else {
                revertNotCompletedAnimation();
            }
        }
    }

    private void startCompletedSuccessfullyAnimation() {
        final Animation animationFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fade_out_10_to_00);
        final Animation animationZoomIn = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
        final Animation animationRotateZoom = AnimationUtils.loadAnimation(mContext, R.anim.together_rotate_zoom);

        mToggleDone.setEnabled(false);

        mToggleDone.startAnimation(animationRotateZoom);
        mBtnMinusWeight.startAnimation(animationFadeOut);
        mBtnPlusWeight.startAnimation(animationFadeOut);
        mBtnMinusReps.startAnimation(animationFadeOut);
        mBtnPlusReps.startAnimation(animationFadeOut);
        mTxtWeight.startAnimation(animationZoomIn);
        mTxtReps.startAnimation(animationZoomIn);
    }

    private void revertCompletedSuccessfullyAnimation() {
        final Animation animationFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_0_to_05);
        final Animation animationZoomOut = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out);
        final Animation animationRotateUnZoom = AnimationUtils.loadAnimation(mContext, R.anim.together_rotate_unzoom);

        animationRotateUnZoom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mToggleDone.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mToggleDone.startAnimation(animationRotateUnZoom);

        mBtnMinusWeight.startAnimation(animationFadeIn);
        mBtnPlusWeight.startAnimation(animationFadeIn);
        mBtnMinusReps.startAnimation(animationFadeIn);
        mBtnPlusReps.startAnimation(animationFadeIn);

        mTxtWeight.startAnimation(animationZoomOut);
        mTxtReps.startAnimation(animationZoomOut);
    }

    private void startNotCompletedAnimation() {
        setPlusMinusButtonsEnabled(false);
        setToggleButtonEnabled(false);
        setWeightRepsEnabled(false);
    }

    private void revertNotCompletedAnimation() {
        setPlusMinusButtonsEnabled(true);
        setToggleButtonEnabled(true);
        setWeightRepsEnabled(true);
    }

    private void setPlusMinusButtonsEnabled(final boolean enabled) {
        final Animation animationFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fade_out_1_to_05);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_05_to_10);
        final Animation animation = enabled ? animationFadeIn : animationFadeOut;

        mBtnPlusWeight.setEnabled(enabled);
        mBtnMinusWeight.setEnabled(enabled);
        mBtnPlusReps.setEnabled(enabled);
        mBtnMinusReps.setEnabled(enabled);

        mBtnPlusWeight.startAnimation(animation);
        mBtnMinusWeight.startAnimation(animation);
        mBtnPlusReps.startAnimation(animation);
        mBtnMinusReps.startAnimation(animation);
    }

    private void setToggleButtonEnabled(final boolean enabled) {
        final Animation animationFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fade_out_1_to_05);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_05_to_10);
        final Animation animation = enabled ? animationFadeIn : animationFadeOut;

        mToggleDone.setEnabled(enabled);
        mToggleDone.startAnimation(animation);
    }

    private void setWeightRepsEnabled(final boolean enabled) {
        final Animation animationFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fade_out_1_to_05);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_05_to_10);
        final Animation animation = enabled ? animationFadeIn : animationFadeOut;

        mTxtWeight.startAnimation(animation);
        mTxtReps.startAnimation(animation);
    }

}
