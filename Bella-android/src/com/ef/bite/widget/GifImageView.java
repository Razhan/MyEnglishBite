package com.ef.bite.widget;

import java.io.InputStream;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ef.bite.utils.FormatBitmapTools;

public class GifImageView extends ImageView {

	GifAnimationDrawable gifDrawable;

	public GifImageView(Context context) {
		super(context);
	}

	public GifImageView(Context context, int gifResId) {
		super(context);
	}

	public GifImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void startGifFromAsset(String assetPath, boolean loop) {
		try {
			if (gifDrawable == null) {
				InputStream stream = getContext().getAssets().open(assetPath);
				gifDrawable = new GifAnimationDrawable(stream);
			}
			setImageDrawable(gifDrawable);
			gifDrawable.startAnimation(loop);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void startGifFromDrawable(int GifID, boolean loop) {
		try {
			if (gifDrawable == null) {
				InputStream is = FormatBitmapTools.getInstance()
						.Bitmap2InputStream(
								BitmapFactory.decodeResource(getResources(),
										GifID));
				gifDrawable = new GifAnimationDrawable(is);
			}
			setImageDrawable(gifDrawable);
			gifDrawable.startAnimation(loop);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void stopGIF() {
		if (gifDrawable != null)
			gifDrawable.stop();
	}
}
