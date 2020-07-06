package com.itsaky.colorpicker;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.view.View.OnClickListener;
import android.text.TextWatcher;
import android.text.Editable;

/**
* Custom Dialog for ColorPicker
* @author Akash
*/

public class ColorPickerDialog extends Dialog implements SeekBar.OnSeekBarChangeListener, 
														OnClickListener,
														TextWatcher
{
	/**
	* Fields to store current color values
	*/
	private int alpha;
	private int red;
	private int green;
	private int blue;
	private boolean withAlpha = false;
	private boolean closeOnPick = true;
	
	private ColorPickerSeekbar mAlphaSeek, 
					mRedSeek, 
					mGreenSeek, 
					mBlueSeek;			
	private LinearLayout mColorView;
	private EditText mHexField;
	private Button mPickButton;
	
	/**
	* onColorPicked Callback
	*/
	private ColorPickerCallback mCallback;
	
	/**
	* Initiate with black color with full alpha
	* It will not show the Alpha Slider
	* @param c Activity
	*/
	public ColorPickerDialog(Activity c)
	{
		super(c);
		alpha = 255;
		withAlpha = false;
		red = green = blue = 0;
	}
	
	/**
	 * Initiate with provided color with alpha
	 * Shows the alpha slider
	 * @param c Activity
	 * @param alpha => alpha value
	 * @param red => red value
	 * @param green => green value
	 * @param blue => blue value
	 */
	public ColorPickerDialog(Activity c, int alpha, int red, int green, int blue)
	{
		super(c);
		withAlpha = true;
		this.alpha = validateColorValue(alpha);
		this.red = validateColorValue(red);
		this.green = validateColorValue(green);
		this.blue = validateColorValue(blue);
	}
	
	/**
	 * Initiate with provided color with full alpha
	 * Does not shows the alpha slider
	 * @param c Activity
	 * @param red => red value
	 * @param green => green value
	 * @param blue => blue value
	 */
	public ColorPickerDialog(Activity c, int red, int green, int blue)
	{
		super(c);
		withAlpha = false;
		this.alpha = 255;
		this.red = validateColorValue(red);
		this.green = validateColorValue(green);
		this.blue = validateColorValue(blue);
	}
	
	/**
	 * Initiate with color provided
	 * Shows the alpha slider
	 * @param c Activity
	 * @param hexColor Hex Color String
	 */
	public ColorPickerDialog(Activity c, String hexColor)
	{
		super(c);
		try
		{
			int col = Color.parseColor(hexColor);
			withAlpha = true;
			this.alpha = Color.alpha(col);
			this.red = Color.red(col);
			this.green = Color.green(col);
			this.blue = Color.blue(col);
		} catch(Exception e)
		{
			throw new IllegalArgumentException("Invalid hex color string provided to ColorPickerDialog");
		}
	}
	
	/**
	* Set the callback for the ColorPicker
	* @param listener Listener to attach
	*/
	public void setColorPickerCallback(ColorPickerCallback callback)
	{
		this.mCallback = callback;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		/**
		* Disable title for the dialog
		* Don't know why it don't work :)
		* Maybe you can figure it out..
		*/
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
		
		setContentView(R.layout.layout_colorpickerview);
		
		mAlphaSeek = findViewById(R.id.colorpickerview_alphaSeek);
		mRedSeek = findViewById(R.id.colorpickerview_redSeek);
		mGreenSeek = findViewById(R.id.colorpickerview_greenSeek);
		mBlueSeek = findViewById(R.id.colorpickerview_blueSeek);
		mColorView = findViewById(R.id.colorpickerview_colorView);
		mHexField = findViewById(R.id.colorpickerview_hexCode);
		mPickButton = findViewById(R.id.colorpickerview_pickButton);
		
		mAlphaSeek.setVisibility(withAlpha ? View.VISIBLE : View.GONE);
		
		mPickButton.setOnClickListener(this);
		
		int max = 255;
		mAlphaSeek.setMax(max);
		mRedSeek.setMax(max);
		mGreenSeek.setMax(max);
		mBlueSeek.setMax(max);
		
		mAlphaSeek.addOnSeekbarChangeListener(this);
		mRedSeek.addOnSeekbarChangeListener(this);
		mGreenSeek.addOnSeekbarChangeListener(this);
		mBlueSeek.addOnSeekbarChangeListener(this);
		
		mAlphaSeek.setThumbColor(Color.parseColor("#9E9E9E"));
		mRedSeek.setThumbColor(Color.parseColor("#F44336"));
		mGreenSeek.setThumbColor(Color.parseColor("#4CAF50"));
		mBlueSeek.setThumbColor(Color.parseColor("#2196F3"));
		
		mAlphaSeek.setThumbTextColor(Color.parseColor("#EEEEEE"));
		mRedSeek.setThumbTextColor(Color.parseColor("#EF9A9A"));
		mGreenSeek.setThumbTextColor(Color.parseColor("#A5D6A7"));
		mBlueSeek.setThumbTextColor(Color.parseColor("#90CAF9"));
		
		mAlphaSeek.setProgressColor(Color.parseColor("#BDBDBD"));
		mRedSeek.setProgressColor(Color.parseColor("#EF5350"));
		mGreenSeek.setProgressColor(Color.parseColor("#66BB6A"));
		mBlueSeek.setProgressColor(Color.parseColor("#42A5F5"));
		
		mHexField.addTextChangedListener(this);
	}
	
	/**
	* get current color of the color picker
	* @return int value of color
	*/
	public int getColor()
	{
		this.alpha = validateColorValue(alpha);
		this.red = validateColorValue(red);
		this.green = validateColorValue(green);
		this.blue = validateColorValue(blue);
		return withAlpha ? Color.argb(alpha, red, green, blue) : Color.rgb(red, green, blue);
	}
	
	/**
	* Get hex color code of current color
	* @return if alphaEnabled then with alpha hex value else without it
	*/
	public String getHexColorCode()
	{
		return withAlpha ? getHexWithAlpha() : getHexWithoutAlpha();
	}
	
	/**
	* Get hex color code without alpha hex value
	* @return hex string
	*/
	public String getHexWithoutAlpha()
	{
		return String.format("#%02X%02X%02X", red, green, blue);
	}
	
	/**
	 * Get hex color code with alpha hex value
	 * @return hex string
	 */
	public String getHexWithAlpha()
	{
		alpha = validateColorValue(alpha);
		return String.format("#%02X%02X%02X%02X", alpha, red, green, blue);
	}
	
	/**
	* check if color value is valid or not (between 0 and 255)
	* if not valid, set it to 255
	* @param color => color value to validate
	* @return validated value
	*/
	private int validateColorValue(int color)
	{
		return ((color >= 0) && (color <= 255)) ? color : 255;
	}
	
	/**
	* toggle alpha slider visibility
	* @param withAlpha enable/disable alpha
	*/
	public void withAlpha(boolean withAlpha)
	{
		this.withAlpha = withAlpha;
	}
	
	/**
	* initiate/re-initiate view
	*/
	private void initUI()
	{
		mAlphaSeek.setVisibility(
								withAlpha 
								? View.VISIBLE 
								: View.GONE);
		
		mColorView.setBackgroundColor(getColor());
		
		mAlphaSeek.setProgress(alpha);
		mRedSeek.setProgress(red);
	 	mGreenSeek.setProgress(green);
		mBlueSeek.setProgress(blue);
	}
	
	/**
	* enable/disable auto close for dialog
	* @param closeOnPicked boolean value
	*/
	public void setCloseOnPicked(boolean closeOnPicked)
	{
		this.closeOnPick = closeOnPicked;
	}
	
	@Override
	public void onClick(View p1)
	{
		if(p1.getId() == mPickButton.getId())
		{
			if(mCallback != null)
				mCallback.onColorPicked(getColor(), getHexColorCode());
			if(closeOnPick)
				dismiss();
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar p1, int p2, boolean p3)
	{
		int id = p1.getId();
		if(id == mAlphaSeek.getId())
		{
			alpha = p2;
		} else if(id == mRedSeek.getId())
		{
			red = p2;
		} else if(id == mGreenSeek.getId())
		{
			green = p2;
		} else if(id == mBlueSeek.getId())
		{
			blue = p2;
		}
		
		mColorView.setBackgroundColor(getColor());
		/**
		* If progress is changed by user, set hex code to EditText
		*/
		if(p3)
			mHexField.setText(getHexColorCode().replace("#", ""));
	}
	
	@Override public void onStartTrackingTouch(SeekBar p1){}
	@Override public void onStopTrackingTouch(SeekBar p1){}
	
	@Override public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){}
	@Override 
	public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		/**
		 * Try to parse the color entered in EditText
		 * Text entered in EditText may not always be a Hex Color Code
		 */
		try
		{
			int c = Color.parseColor("#" + mHexField.getText().toString());
			alpha = Color.alpha(c);
			red = Color.red(c);
			green = Color.green(c);
			blue = Color.blue(c);
			initUI();
		} catch(Exception e){}
	}
	@Override public void afterTextChanged(Editable p1){}

	@Override
	public void show()
	{
		super.show();
		initUI();
		mHexField.setText(getHexColorCode().replace("#", ""));
	}
	
	public Button getPickButton()
	{
		return mPickButton;
	}
	
	public ColorPickerSeekbar getAlphaSeekbar()
	{
		return mAlphaSeek;
	}
	
	public ColorPickerSeekbar getRedSeekbar()
	{
		return mRedSeek;
	}
	
	public ColorPickerSeekbar getGreenSeekbar()
	{
		return mGreenSeek;
	}
	
	public ColorPickerSeekbar getBlueSeekbar()
	{
		return mBlueSeek;
	}
	
	public EditText getHexEditText()
	{
		return mHexField;
	}
	
	/**
	* setters and getters for color values
	*/
	public void setAlpha(int alpha)
	{
		this.alpha = alpha;
	}

	public int getAlpha()
	{
		return alpha;
	}

	public void setRed(int red)
	{
		this.red = red;
	}

	public int getRed()
	{
		return red;
	}

	public void setGreen(int green)
	{
		this.green = green;
	}

	public int getGreen()
	{
		return green;
	}

	public void setBlue(int blue)
	{
		this.blue = blue;
	}

	public int getBlue()
	{
		return blue;
	}
	
	/**
	* Interface for onColorPicked Callback
	*/
	public interface ColorPickerCallback
	{
		public void onColorPicked(int color, String hexColorCode);
	}
	
}
