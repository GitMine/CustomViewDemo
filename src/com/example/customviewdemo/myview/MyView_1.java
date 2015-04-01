package com.example.customviewdemo.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.customviewdemo.R;

public class MyView_1 extends View {
	/**
	 * 默认环宽
	 */
	public static final float DEFAULT_RING_WIDTH = 20.0f;
	/**
	 * 默认环上背景颜色
	 */
	public static final int DEFAULT_RINGBGCOLOR = 0xFFFF99;
	/**
	 * 默认环上前景颜色
	 */
	public static final int DEFAULT_RINGFGCOLOR = 0xFFCC33;
	/**
	 * 默认环内填充颜色
	 */
	public static final int DEFAULT_FILLCOLOR = 0xffffffff;
	/**
	 * 默认起始点
	 */
	public static final int DEFAULT_STARTANGLE = 0;
	/**
	 * 默认画的角度
	 */
	public static final int DEFAULT_SWEEPANGLE = 360;
	/**
	 * 默认宽度
	 */
	public static final int DEFAULT_WIDTH = 450;
	/**
	 * 默认高度
	 */
	public static final int DEFAULT_HEIGHT = 450;

	private int ringBgColor;
	private int ringFgColor;
	private int fillColor;
	private float ringWidth;
	private float startAngle;
	private float sweepAngle;
	private boolean isShowText;

	private int text1Color;
	private int text2Color;
	private int text3Color;
	private int text4Color;
	private int text5Color;

	private float text1Size;
	private float text2Size;
	private float text3Size;
	private float text4Size;
	private float text5Size;

	private String text1 = "";
	private String text2 = "0";
	private String text3 = "kb/s";
	private String text4 = "";
	private String text5 = "";

	private Paint ringPaint;
	private Paint textPaint_1;
	private Paint textPaint_2;
	private Paint textPaint_3;
	private Paint textPaint_4;
	private Paint textPaint_5;

	public float mSweepAngle;

	private int width;
	private int height;

	private RectF outRect;
	private RectF inRect;

	private ViewData data;

	public MyView_1(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.MyView_1, 0, 0);

		try {
			this.ringBgColor = a.getColor(R.styleable.MyView_1_ringBgColor,
					DEFAULT_STARTANGLE);
			this.ringFgColor = a.getColor(R.styleable.MyView_1_ringFgColor,
					DEFAULT_RINGFGCOLOR);
			this.fillColor = a.getColor(R.styleable.MyView_1_fillColor,
					DEFAULT_FILLCOLOR);
			this.ringWidth = a.getDimension(R.styleable.MyView_1_ringWidth,
					DEFAULT_RING_WIDTH);
			this.startAngle = a.getInt(R.styleable.MyView_1_startAngle,
					DEFAULT_STARTANGLE);
			this.sweepAngle = a.getInt(R.styleable.MyView_1_sweepAngle,
					DEFAULT_SWEEPANGLE);
			this.isShowText = a.getBoolean(R.styleable.MyView_1_isShowText,
					false);
			this.text1Color = a.getColor(R.styleable.MyView_1_text1Color,
					Color.GRAY);
			this.text2Color = a.getColor(R.styleable.MyView_1_text2Color,
					Color.BLUE);
			this.text3Color = a.getColor(R.styleable.MyView_1_text3Color,
					Color.BLUE);
			this.text4Color = a.getColor(R.styleable.MyView_1_text4Color,
					Color.BLUE);
			this.text5Color = a.getColor(R.styleable.MyView_1_text5Color,
					Color.BLUE);
			this.text1Size = a.getDimension(R.styleable.MyView_1_text1Size, 36);
			this.text2Size = a.getDimension(R.styleable.MyView_1_text2Size, 80);
			this.text3Size = a.getDimension(R.styleable.MyView_1_text3Size, 42);
			this.text4Size = a.getDimension(R.styleable.MyView_1_text4Size, 42);
			this.text5Size = a.getDimension(R.styleable.MyView_1_text5Size, 42);
		} finally {
			a.recycle();
		}
		init();
	}

	private void init() {
		System.out.println("init");

		this.ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.ringPaint.setStyle(Paint.Style.FILL);
		this.ringPaint.setColor(ringBgColor);

		this.outRect = new RectF();
		this.inRect = new RectF();

		this.textPaint_1 = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.textPaint_1.setTextAlign(Paint.Align.CENTER);
		this.textPaint_1.setColor(text1Color);
		this.textPaint_1.setTextSize(text1Size);

		this.textPaint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.textPaint_2.setTextAlign(Paint.Align.CENTER);
		this.textPaint_2.setColor(text2Color);
		this.textPaint_2.setTextSize(text2Size);

		this.textPaint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.textPaint_3.setTextAlign(Paint.Align.LEFT);
		this.textPaint_3.setColor(text3Color);
		this.textPaint_3.setTextSize(text3Size);

		this.textPaint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.textPaint_4.setTextAlign(Paint.Align.CENTER);
		this.textPaint_4.setColor(text4Color);
		this.textPaint_4.setTextSize(text4Size);

		this.textPaint_5 = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.textPaint_5.setTextAlign(Paint.Align.CENTER);
		this.textPaint_5.setColor(text5Color);
		this.textPaint_5.setTextSize(text5Size);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		System.out.println("onMeasure()");
		System.out.println("widthMeasureSpec:" + widthMeasureSpec
				+ ",heightMeasureSpec:" + heightMeasureSpec);

		int w = measureWidth(widthMeasureSpec);
		int h = measureHeight(heightMeasureSpec);
		System.out.println("w:" + w + ",h:" + h);

		if (w > h) {
			w = h;
		} else {
			h = w;
		}
		this.width = w;
		this.height = h;

		setMeasuredDimension(w, h);
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = DEFAULT_WIDTH;
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = DEFAULT_HEIGHT;
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float x1 = 0 + getPaddingLeft();
		float y1 = 0 + getPaddingTop();
		float x2 = 0 + width - getPaddingRight();
		float y2 = 0 + height - getPaddingBottom();

//		System.out.println("onDraw()");
		outRect.set(x1, y1, x2, y2);
		inRect.set(x1 + ringWidth, y1 + ringWidth, x2 - ringWidth, y2
				- ringWidth);

		ringPaint.setColor(ringBgColor);
		canvas.drawArc(outRect, startAngle, sweepAngle, true, ringPaint);

		ringPaint.setColor(ringFgColor);
		canvas.drawArc(outRect, startAngle, mSweepAngle, true, ringPaint);

		ringPaint.setColor(fillColor);
		canvas.drawArc(inRect, startAngle - 5, 360, true, ringPaint);

		if (isShowText) {
			FontMetrics fm;
			// 画文字1
			float text1X = width / 2.0f;
			float text1Y = height / 3.0f;
			canvas.drawText(text1, text1X, text1Y, textPaint_1);

			// 画文字2
			fm = textPaint_2.getFontMetrics();
			float text2Width = textPaint_2.measureText(text2);
			int text2Height = (int) Math.ceil(fm.descent - fm.ascent);
			float text2X = width / 2.0f;
			float text2Y = height / 2.0f + text2Height / 3.0f;
			canvas.drawText(text2 + "", text2X, text2Y, textPaint_2);

			// 画文字3
			float text3X = width / 2.0f + text2Width / 2.0f + 5;
			float text3Y = height / 2.0f + text2Height / 3.0f;
			canvas.drawText(text3, text3X, text3Y, textPaint_3);

			// 画文字4
			float text4X = width / 2.0f;
			float text4Y = height - height / 4.5f;
			canvas.drawText(text4, text4X, text4Y, textPaint_4);

			// 画文字4
			float text5X = width / 2.0f;
			float text5Y = height - height / 9.0f;
			canvas.drawText(text5, text5X, text5Y, textPaint_5);

		}

		if (!isShowText) {
			// 画线
			float r = (width - getPaddingLeft() - getPaddingRight()) / 2.0f
					- ringWidth;
			Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			linePaint.setColor(ringFgColor);
			float startX = width / 2.0f;
			float startY = height / 2.0f;
			float stopX = startX + (float) Math.sqrt((r * r) / 2.0f);
			float stopY = startY + (float) Math.sqrt((r * r) / 2.0f);
			float x = stopX;
			float y = stopY;
			double angle = Math.abs(mSweepAngle - (Math.abs(y - r))
					/ (r + Math.abs(y - x)) * 180.0f);
			float a = Math.abs(r - (float) Math.cos(angle) * r);
			float b = Math.abs(r - (float) Math.sin(angle) * r);
			canvas.drawLine(startX, startY, stopX, stopY, linePaint);

			if (mSweepAngle > 0 && mSweepAngle < 45) {
				angle = 45.0 - mSweepAngle;

				a = startX - (float) Math.cos(angle) * r;
				System.out.println("sin(" + angle + "):" + Math.sin(angle));
				b = startY + Math.abs((float) Math.sin(angle) * r);
				System.out.println("angle:" + angle + ",a:" + a + ",b:" + b);
			} else if (mSweepAngle == 45) {
				a = startX - r;
				b = startY;
			} else if (mSweepAngle > 45 && mSweepAngle < 90) {

			} else if (mSweepAngle == 90) {

			}
			System.out.println("startX:" + startX + ",startY:" + startY);
			canvas.drawLine(startX, startY, a, b, linePaint);
		}

	}

	public ViewData getData() {
		return data;
	}

	public void setData(ViewData data) {
		this.data = data;
		this.mSweepAngle = data.getSweepAngle();
		this.text1 = data.getTestSpeedStatus();
		this.text2 = data.getWifiSpeed();
		this.text3 = data.getSpeedUnit();
		this.text4 = data.getWifiLinkStatus();
		this.text5 = data.getWifiName();
		invalidate();
	}

	public class ViewData {
		private String testSpeedStatus = "";
		private String wifiSpeed = "";
		private String speedUnit = "";
		private String wifiLinkStatus = "";
		private String wifiName = "";
		private float sweepAngle = 0;

		public float getSweepAngle() {
			return sweepAngle;
		}

		public void setSweepAngle(float sweepAngle) {
			this.sweepAngle = sweepAngle;
		}

		public String getTestSpeedStatus() {
			return testSpeedStatus;
		}

		public void setTestSpeedStatus(String testSpeedStatus) {
			this.testSpeedStatus = testSpeedStatus;
		}

		public String getWifiSpeed() {
			return wifiSpeed;
		}

		public void setWifiSpeed(String wifiSpeed) {
			this.wifiSpeed = wifiSpeed;
		}

		public String getSpeedUnit() {
			return speedUnit;
		}

		public void setSpeedUnit(String speedUnit) {
			this.speedUnit = speedUnit;
		}

		public String getWifiLinkStatus() {
			return wifiLinkStatus;
		}

		public void setWifiLinkStatus(String wifiLinkStatus) {
			this.wifiLinkStatus = wifiLinkStatus;
		}

		public String getWifiName() {
			return wifiName;
		}

		public void setWifiName(String wifiName) {
			this.wifiName = wifiName;
		}

	}

}
