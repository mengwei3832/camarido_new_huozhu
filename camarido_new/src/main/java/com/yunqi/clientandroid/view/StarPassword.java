package com.yunqi.clientandroid.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;

import com.yunqi.clientandroid.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class StarPassword extends EditText {
	private static final int defaultContMargin = 5;
	private static final int defaultSplitLineWidth = 3;
	private int passwordBorderColor;
	private float passwordBorderRadius;
	private float passwordBorderWidth;
	private Drawable passwordBeforeDrawable;
	private Bitmap passwordBeforeBitmap;
	private Bitmap passwordAfterBitmap;
	private Drawable passwordAfterDrawable;
	private int passwordLength;
	private int currentPasswordLength;

	private Paint passwordPaint = new Paint(ANTI_ALIAS_FLAG);
	private Paint borderPaint = new Paint(ANTI_ALIAS_FLAG);

	public StarPassword(Context context, AttributeSet attrs) {
		super(context, attrs);
		SetDefault();
		init(context, attrs);
	}

	private void SetDefault() {
		passwordBorderColor = Color.parseColor("#00000000");
		passwordBeforeBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.zhanwei);
		passwordAfterBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.xinghao);
		passwordBorderRadius = 0;
		passwordBorderWidth = 0;
		passwordLength = 6;
		currentPasswordLength = 0;
	}

	private void init(Context context, AttributeSet attrs) {

		DisplayMetrics dm = getResources().getDisplayMetrics();
		passwordBorderRadius = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, passwordBorderRadius, dm);
		passwordBorderWidth = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, passwordBorderWidth, dm);

		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.StartPassword);
		passwordBorderColor = ta.getColor(
				R.styleable.StartPassword_start_password_border_color,
				passwordBorderColor);
		passwordBorderWidth = ta.getDimension(
				R.styleable.StartPassword_start_password_border_width,
				passwordBorderWidth);
		passwordBorderRadius = ta.getDimension(
				R.styleable.StartPassword_start_password_border_radius,
				passwordBorderRadius);
		passwordBeforeDrawable = ta
				.getDrawable(R.styleable.StartPassword_start_password_before_drawable);
		passwordAfterDrawable = ta
				.getDrawable(R.styleable.StartPassword_start_password_after_drawable);
		passwordLength = ta
				.getInt(R.styleable.StartPassword_start_password_length,
						passwordLength);
		if (passwordBeforeDrawable == null) {
			passwordBeforeBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.zhanwei);
		} else {
			passwordBeforeBitmap = ((BitmapDrawable) passwordBeforeDrawable)
					.getBitmap();
		}
		if (passwordAfterDrawable == null) {
			passwordAfterBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.xinghao);
		} else {
			passwordAfterBitmap = ((BitmapDrawable) passwordAfterDrawable)
					.getBitmap();
		}
		ta.recycle();

		borderPaint.setStrokeWidth(passwordBorderWidth);
		borderPaint.setColor(passwordBorderColor);
		passwordPaint.setStyle(Paint.Style.FILL);
		passwordPaint.setFilterBitmap(true);
		passwordPaint.setDither(true);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		// 外边框
		RectF rect = new RectF(0, 0, width, height);
		borderPaint.setColor(passwordBorderColor);
		canvas.drawRoundRect(rect, passwordBorderRadius, passwordBorderRadius,
				borderPaint);

		// 内容区
		RectF rectIn = new RectF(rect.left + defaultContMargin, rect.top
				+ defaultContMargin, rect.right - defaultContMargin,
				rect.bottom - defaultContMargin);
		borderPaint.setColor(Color.parseColor("#00000000"));
		canvas.drawRoundRect(rectIn, passwordBorderRadius,
				passwordBorderRadius, borderPaint);

		// 分割线
		borderPaint.setColor(passwordBorderColor);
		borderPaint.setStrokeWidth(defaultSplitLineWidth);
		for (int i = 1; i < passwordLength; i++) {
			float x = width * i / passwordLength;
			canvas.drawLine(x, 0, x, height, borderPaint);
		}

		// 密码
		float cx, cy;
		float half = width / passwordLength / 2;
		for (int i = 0; i < passwordLength; i++) {
			if (i < currentPasswordLength) {
				cx = width * i / passwordLength + half
						- passwordAfterBitmap.getWidth() / 2;
				cy = height / 2 - passwordAfterBitmap.getHeight() / 2;
				canvas.drawBitmap(passwordAfterBitmap, cx, cy, passwordPaint);
			} else {
				cx = width * i / passwordLength + half
						- passwordBeforeBitmap.getWidth() / 2;
				cy = height / 2 - passwordBeforeBitmap.getHeight() / 2;
				canvas.drawBitmap(passwordBeforeBitmap, cx, cy, passwordPaint);
			}
		}
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		this.currentPasswordLength = text.toString().length();
		invalidate();
	}

	public int getPasswordBorderColor() {
		return passwordBorderColor;
	}

	public void setPasswordBorderColor(int passwordBorderColor) {
		this.passwordBorderColor = passwordBorderColor;
	}

	public float getPasswordBorderRadius() {
		return passwordBorderRadius;
	}

	public void setPasswordBorderRadius(float passwordBorderRadius) {
		this.passwordBorderRadius = passwordBorderRadius;
	}

	public float getPasswordBorderWidth() {
		return passwordBorderWidth;
	}

	public void setPasswordBorderWidth(float passwordBorderWidth) {
		this.passwordBorderWidth = passwordBorderWidth;
	}

	public int getPasswordLength() {
		return passwordLength;
	}

	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
	}

	public Bitmap getPasswordBeforeBitmap() {
		return passwordBeforeBitmap;
	}

	public void setPasswordBeforeDrawable(Drawable passwordBeforeDrawable) {
		this.passwordBeforeBitmap = ((BitmapDrawable) passwordBeforeDrawable)
				.getBitmap();
	}

	public Bitmap getPasswordAfterBitmap() {
		return passwordAfterBitmap;
	}

	public void setPasswordAfterDrawable(Drawable passwordAfterDrawable) {
		this.passwordAfterBitmap = ((BitmapDrawable) passwordAfterDrawable)
				.getBitmap();
		;
	}

	public Drawable getPasswordBeforeDrawable() {
		return passwordBeforeDrawable;
	}

	public Drawable getPasswordAfterDrawable() {
		return passwordAfterDrawable;
	}

	public void setPasswordBeforeBitmap(Bitmap passwordBeforeBitmap) {
		this.passwordBeforeBitmap = passwordBeforeBitmap;
	}

	public void setPasswordAfterBitmap(Bitmap passwordAfterBitmap) {
		this.passwordAfterBitmap = passwordAfterBitmap;
	}
}
