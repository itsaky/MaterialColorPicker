package com.itsaky.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.SeekBar;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IlToro on 06/12/2017
 * Modified by Akash for ColorPicker ;)
 */

public class ColorPickerSeekbar extends SeekBar {
    private TextPaint mTextPaint;
    private boolean isTextVisible;
    private int multiplier = 1;
    private String progressText = "0";
    private String formatter = "1";
    private AnimatedVectorDrawableCompat seekBarThumb;
    private AnimatedVectorDrawableCompat seekBarThumbBackwards;
    private VectorDrawableCompat seekBarThumbNotAnimated;
    private final float scale = getContext().getResources().getDisplayMetrics().density;
	
	private List<OnSeekBarChangeListener> listeners = new ArrayList<>();
	private int thumbColor = -1;
	
    public ColorPickerSeekbar(Context context) {
        super(context);
        method(context);
    }
	
    public ColorPickerSeekbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        method(context);
    }

    public ColorPickerSeekbar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        method(context);
    }
	
	private void method(Context context) throws Resources.NotFoundException
	{
		isTextVisible = false;

        mTextPaint = new TextPaint();
        mTextPaint.setColor(getResources().getColor(R.color.pink_800));
        mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.seekbar_thumb_text));
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        final SeekBar thisSeekbar = this;
		
		if(thumbColor != -1)
			getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);

        seekBarThumbNotAnimated = VectorDrawableCompat.create(getResources(), R.drawable.seekbar_thumb_vector, null);

        seekBarThumb = AnimatedVectorDrawableCompat.create(context, R.drawable.seekbar_thumb_animation);
        seekBarThumb.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
				@Override
				public void onAnimationEnd(Drawable drawable)
				{
					super.onAnimationEnd(drawable);
					if(thumbColor != -1)
						getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
					if (thisSeekbar.getThumb() == seekBarThumb)
					{
						isTextVisible = true;
						Log.i("CATs", "AnimationForwards:TRUE");
					}
				}
			});
        seekBarThumbBackwards = AnimatedVectorDrawableCompat.create(context, R.drawable.seekbar_thumb_animation_backward);
        seekBarThumbBackwards.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
				@Override
				public void onAnimationStart(Drawable drawable)
				{
					super.onAnimationStart(drawable);
					if(thumbColor != -1)
						getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
					isTextVisible = false;
					Log.i("CATs", "AnimationBackwards:FALSE");
				}
				@Override
				public void onAnimationEnd(Drawable drawable)
				{
					super.onAnimationEnd(drawable);
					if(thumbColor != -1)
						getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
					thisSeekbar.setThumb(seekBarThumbNotAnimated);
					isTextVisible = false;
					Log.i("CATs", "AnimationBackwards:FALSE");
					//set height to 20dp
					thisSeekbar.getLayoutParams().height = (int)(20 * scale + 0.5f);
					//set padding to n 0 n 0
					thisSeekbar.setPadding(thisSeekbar.getPaddingLeft(), 0, thisSeekbar.getPaddingRight(), 0);
					//set margin top to 0
					ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) thisSeekbar.getLayoutParams();
					params.topMargin = 0;
					thisSeekbar.requestLayout();
				}
			});

        this.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
				{
					progressText = progress + "";
					
					//Notify
					for(OnSeekBarChangeListener l : listeners)
					{
						l.onProgressChanged(seekBar, progress, b);
					}
					
					if(thumbColor != -1)
						getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
					for(OnSeekBarChangeListener l : listeners)
					{
						l.onStartTrackingTouch(seekBar);
					}
					
					thisSeekbar.setThumb(seekBarThumb);
					seekBarThumb.start();
					//set height to 120dp
					thisSeekbar.getLayoutParams().height = (int)(120 * scale + 0.5f);
					//set padding to n 100dp n 0
					thisSeekbar.setPadding(thisSeekbar.getPaddingLeft(), (int)(100 * scale + 0.5f), thisSeekbar.getPaddingRight(), 0);
					ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) thisSeekbar.getLayoutParams();
					params.topMargin = -(int)(100 * scale + 0.5f);
					thisSeekbar.requestLayout();
					//set margin top -100dp: do not bother me, i know it's a bad practice, if you have a
					//suggetion just open an issue, but i do not promise i will reply :)git
					Log.i("CATs", "StartTrackingTouch:");
					
					if(thumbColor != -1)
						getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
					for(OnSeekBarChangeListener l : listeners)
					{
						l.onStopTrackingTouch(seekBar);
					}
					
					thisSeekbar.setThumb(seekBarThumbBackwards);
					seekBarThumbBackwards.start();
					isTextVisible = false;
					Log.i("CATs", "StopTrackingTouch:FALSE");
					
					if(thumbColor != -1)
						getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
				}
			});
	}
	
    /*
	 Sets the number you want to multiply the progress to: this will NOT affect progress itself
	 (no use of setProgress() method) but it will just multiply the value getProgress() just before it
	 is formatted (see onDraw())
     */
    public ColorPickerSeekbar setMultiplier(int m) {
        multiplier = m;
        return this;
    }
    /*
	 Gets the number you want to multiply your progress to;
     */
    public int getMultiplier() {
        return multiplier;
    }
    /*
	 Sets the type of formatter you want, so that you can apply your logic in onDraw() with some
	 if-else statements
     */
    public ColorPickerSeekbar setFormatter(String f) {
        formatter = f;
        return this;
    }
    /*
	 Gets the type of formatter you set
     */
    public String getFormatter() {
        return formatter;
    }
	
	/*
	
	Seekbar can only have one listener, 
	so I added this method to add more than one listener
	
	Make sure you do not add too much listeners
	
	@param listener The listener to attach
	
	*/
	public void addOnSeekbarChangeListener(OnSeekBarChangeListener listener)
	{
		this.listeners.add(listener);
	}
	
	public void setThumbColor(int color)
	{
		this.thumbColor = color;
	}
	
	public void setThumbTextColor(int color)
	{
		if(mTextPaint != null)
			mTextPaint.setColor(color);
	}
	
	public void setProgressColor(int color)
	{
		getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
	}
	
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
		 Write your own logic here, you can change progressText to whatever you want be it $, %,
		 h-m-s, etc.

		 NOTE: there is no TimeValueFormatter here, these five lines below just show you how I used
		 it
		 */
//        if (formatter == "1") {
//            //no formatting needed
//            progressText = Integer.toString(getProgress()*multiplier);
//        } else if (formatter == "TimeValueFormatter") {
//            //timevalueformatting XXh YYm ZZs (not included here)
//            progressText = new TimeValueFormatter().getHHMMSS(getProgress()*multiplier);
//        }


        Rect bounds = new Rect();
        mTextPaint.getTextBounds(progressText, 0, progressText.length(), bounds);
        int width = getWidth()-getPaddingLeft()-getPaddingRight();
        float normalizedProgress = (float) getProgress()/getMax();

        float thumbX = getPaddingLeft()+width*normalizedProgress;
        //Mmmh...this assumes the drawable is 200x200dp, the seekbar is 20dp and its padding/negative margin is 100dp
        float thumbY = this.getHeight()-(int)(75*scale+0.5f);
        /* Don't really know what size you need: here's what I used, 18dp-(1dp*progressText.lenght())
		 so that the text will become smaller if text is longer
		 */
        mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.seekbar_thumb_text)-getResources().getDimensionPixelSize(R.dimen.dp1)*(progressText.length()));
        if (isTextVisible) {
            canvas.drawText(progressText, thumbX, thumbY, mTextPaint);
        }
    }
}
