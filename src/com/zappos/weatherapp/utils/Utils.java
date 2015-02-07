package com.zappos.weatherapp.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import com.zappos.weatherapp.R;

public class Utils {
	public static Bitmap getImage(Activity context, String type){
		int image_id;
		if(type == null)
			image_id = R.drawable.sunny;
		else if(type.equals("clear-night"))
			image_id = R.drawable.clear_night;
		else if(type.equals("rain"))
			image_id = R.drawable.rain;
		else if(type.equals("cloudy"))
			image_id = R.drawable.cloudy;
		else if(type.equals("partly-cloudy-day"))
			image_id = R.drawable.partly_cloudy;
		else if(type.equals("partly-cloudy-night"))
			image_id = R.drawable.cloudy_night;
		else if(type.equals("snow"))
			image_id = R.drawable.snow;
		else
			image_id = R.drawable.sunny;
		
		return decodeSampledBitmapFromResource(context, image_id);
		
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Activity context, int resId) {

	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    DisplayMetrics dm = new DisplayMetrics();
	    context.getWindowManager().getDefaultDisplay().getMetrics(dm);
	    int reqWidth = dm.widthPixels;
	    int reqHeight = dm.heightPixels;
	    Resources res = context.getResources();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);

	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static int calculateSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
}
