/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ef.bite.widget;
import com.ef.bite.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * Draws circles (one for each view). The current view position is filled and
 * others are only stroked.
 */
public class CirclePageIndicator extends View {

	private float mRadius;
	private final Paint mPaintStroke = new Paint(ANTI_ALIAS_FLAG);
	private final Paint mPaintFill = new Paint(ANTI_ALIAS_FLAG);
	private int mCurrentPage = 0;
	private int mTotalPage = 3;
	private float mPageOffset;
	private int mOrientation = HORIZONTAL;
	private boolean mCentered = true;

	public CirclePageIndicator(Context context) {
		this(context, null);
		initialize();
	}

	public CirclePageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public CirclePageIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	private void initialize() {
		final Resources res = getResources();
		final float defaultStrokeWidth = res
				.getDimension(R.dimen.default_circle_indicator_stroke_width);
		final float defaultRadius = res
				.getDimension(R.dimen.default_circle_indicator_radius);

		// Retrieve styles attributes
		mCentered = true;
		mPaintStroke.setStyle(Style.STROKE);
		mPaintStroke.setColor(getContext().getResources().getColor(
				R.color.white));
		mPaintStroke.setStrokeWidth(defaultStrokeWidth);
		mPaintFill.setStyle(Style.FILL);
		mPaintFill
				.setColor(getContext().getResources().getColor(R.color.white));
		mRadius = defaultRadius;
	}

	public void setIndicator(int index, int total) {
		mCurrentPage = index;
		mTotalPage = total;
		invalidate();
	}

	public void setFillColor(int fillColor) {
		mPaintFill.setColor(fillColor);
		mPaintStroke.setColor(fillColor);
		invalidate();
	}

	public int getFillColor() {
		return mPaintFill.getColor();
	}

	public void setStrokeColor(int strokeColor) {
		mPaintStroke.setColor(strokeColor);
		invalidate();
	}

	public void setStrokeWidth(float strokeWidth) {
		mPaintStroke.setStrokeWidth(strokeWidth);
		invalidate();
	}

	public float getRadius() {
		return mRadius;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		final int count = mTotalPage;
		if (count == 0) {
			return;
		}

		if (mCurrentPage >= count) {
			setIndicator(count - 1, count);
			return;
		}

		int longSize;
		int longPaddingBefore;
		int longPaddingAfter;
		int shortPaddingBefore;
		if (mOrientation == HORIZONTAL) {
			longSize = getWidth();
			longPaddingBefore = getPaddingLeft();
			longPaddingAfter = getPaddingRight();
			shortPaddingBefore = getPaddingTop();
		} else {
			longSize = getHeight();
			longPaddingBefore = getPaddingTop();
			longPaddingAfter = getPaddingBottom();
			shortPaddingBefore = getPaddingLeft();
		}

		final float threeRadius = mRadius * 3;
		final float shortOffset = shortPaddingBefore + mRadius;
		float longOffset = longPaddingBefore + mRadius;
		if (mCentered) {
			longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f)
					- ((count * threeRadius) / 2.0f);
		}

		float dX;
		float dY;

		float pageFillRadius = mRadius;
		if (mPaintStroke.getStrokeWidth() > 0) {
			pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
		}

		// Draw stroked circles
		for (int iLoop = 0; iLoop < count; iLoop++) {
			float drawLong = longOffset + (iLoop * threeRadius);
			if (mOrientation == HORIZONTAL) {
				dX = drawLong;
				// dY = shortOffset;
				dY = canvas.getHeight() / 2;
			} else {
				dX = shortOffset;
				dY = drawLong;
			}

			// Only paint stroke if a stroke width was non-zero
			if (pageFillRadius != mRadius) {
				canvas.drawCircle(dX, dY, mRadius, mPaintStroke);
			}
		}

		// Draw the filled circle according to the current scroll
		float cx = mCurrentPage * threeRadius;
		cx += mPageOffset * threeRadius;
		if (mOrientation == HORIZONTAL) {
			dX = longOffset + cx;
			// dY = shortOffset;
			dY = canvas.getHeight() / 2;
		} else {
			dX = shortOffset;
			dY = longOffset + cx;
		}
		canvas.drawCircle(dX, dY, mRadius, mPaintFill);
	}
}
