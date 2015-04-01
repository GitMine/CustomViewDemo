package com.example.customviewdemo.myview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.customviewdemo.R;

public class MyView extends View {
	private List<Item> mData = new ArrayList<Item>();

	private boolean mShowText;
	private int mTextPos = TEXTPOS_LEFT;
	private int mTextColor;

	private float mTextY = 0.0f;
	private float mTextX = 0.0f;
	private float mTextWidth = 0.0f;
	private float mTextHeight = 0.0f;

	private float mHighlightStrength = 1.15f;

	private boolean mAutoCenterInSlice;

	private int mPieRotation;

	private float mPointerRadius = 2.0f;
	private float mPointerX;
	private float mPointerY;

	private Paint mTextPaint;
	private Paint mPiePaint;
	private Paint mShadowPaint;

	private int mCurrentItem = 0;
	private RectF mShadowBounds = new RectF();

	public static final int TEXTPOS_LEFT = 0;

	public static final int TEXTPOS_RIGHT = 1;

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.MyView, 0, 0);
		try {
			mShowText = a.getBoolean(R.styleable.MyView_mShowText, false);
			mTextPos = a.getInteger(R.styleable.MyView_labelPosition, 0);
			mTextColor = a.getColor(R.styleable.MyView_labelColor, 0xff000000);
			mTextY = a.getDimension(R.styleable.MyView_labelY, 0.0f);
			mTextWidth = a.getDimension(R.styleable.MyView_labelWidth, 0.0f);
			mTextHeight = a.getDimension(R.styleable.MyView_labelHeight, 0.0f);
			mHighlightStrength = a.getFloat(
					R.styleable.MyView_highlightStrength, 1.0f);
			mAutoCenterInSlice = a.getBoolean(
					R.styleable.MyView_autoCenterPointerInSlice, false);
			mPieRotation = a.getInteger(R.styleable.MyView_pieRotation, 0);
			mPointerRadius = a.getDimension(R.styleable.MyView_pointerRadius,
					2.0f);
		} finally {
			a.recycle();
		}

		init();
	}

	private void init() {
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setColor(mTextColor);
		if (mTextHeight == 0) {
			mTextHeight = mTextPaint.getTextSize();
		} else {
			mTextPaint.setTextSize(mTextHeight);
		}

		mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPiePaint.setStyle(Paint.Style.FILL);
		mPiePaint.setTextSize(mTextHeight);

		mShadowPaint = new Paint(0);
		mShadowPaint.setColor(0xff101010);
		mShadowPaint.setMaskFilter(new BlurMaskFilter(8,
				BlurMaskFilter.Blur.NORMAL));

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// 计算padding
		float xpad = (float) (getPaddingLeft() + getPaddingRight());
		float ypad = (float) (getPaddingTop() + getPaddingBottom());

		if (mShowText)
			xpad += mTextWidth;

		float ww = (float) w - xpad;
		float hh = (float) h - ypad;

		float diamater = Math.min(ww, hh);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int minw = getPaddingLeft() + getPaddingRight()
				+ getSuggestedMinimumWidth();
		int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

		int minH = getPaddingTop() + getPaddingBottom()
				+ getSuggestedMinimumHeight();
		int h = resolveSizeAndState(minH, heightMeasureSpec, 0);

		setMeasuredDimension(w, h);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawOval(mShadowBounds, mShadowPaint);

		if (isShowText()) {
			canvas.drawText(mData.get(mCurrentItem).mLabel, mTextX, mTextY,
					mTextPaint);
		}
		
		if(Build.VERSION.SDK_INT < 11) {
			
		}

	}

	public boolean isShowText() {
		return mShowText;
	}

	public void setShowText(boolean showText) {
		this.mShowText = showText;
		invalidate();
		requestLayout();
	}

	public int getTextPos() {
		return mTextPos;
	}

	public void setTextPos(int textPos) {
		if (textPos != TEXTPOS_LEFT && textPos != TEXTPOS_RIGHT) {
			throw new IllegalArgumentException(
					"TextPos must be one of TEXTPOS_LEFT or TEXTPOS_RIGHT");
		}
		mTextPos = textPos;
		invalidate();
	}
	
	public int addItem(String label, float value, int color) {
		Item it = new Item();
		it.mLabel = label;
		it.mValue = value;
		it.mColor = color;
		
		mData.add(it);
		return mData.size() - 1;
	}

	private class Item {
		public String mLabel;
		public float mValue;
		public int mColor;

		// computed values
		public int mStartAngle;
		public int mEndAngle;

		public int mHighlight;
		public Shader mShader;
	}

}
