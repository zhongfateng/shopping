package com.liuwa.shopping.view;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * 形状图片基类
 *
 * @Company 中纺豪华有限公司
 * @author zft
 * @version 1.0.0
 * @create 2014年9月12日
 */
public abstract class MaskedImage extends ImageView {

	private Bitmap mask;
	private Paint paint;


	public MaskedImage(Context paramContext) {
		super(paramContext);
	}

	public MaskedImage(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public MaskedImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable != null) {
			try {
				if (this.paint == null) {
					this.paint = new Paint();
					//如果你画的图形很多而又不想去控制一个又一个不同的paint,你可以对画布进行设置过滤器
					//canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
					this.paint.setFilterBitmap(true);
					this.paint.setAntiAlias(true);
					//设置两张图片相交时的模式 
					PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
					this.paint.setXfermode(porterDuffXfermode);
				}

				int w = getWidth();
				int h = getHeight();
				float wf = w;
				float hf = h;

				int savePoint = canvas.saveLayer(0.0F, 0.0F, wf, hf, null, 31);
				drawable.setBounds(0, 0, w, h);
				drawable.draw(canvas);
				if ((this.mask == null) || (this.mask.isRecycled())) {
					this.mask = createMask();
				}
				canvas.drawBitmap(this.mask, 0.0F, 0.0F, this.paint);
				canvas.restoreToCount(savePoint);
				return;
			} catch (Exception e) {
				Log.e("MaskedImage", e == null ? "" : e.getMessage());
				Log.e("MaskedImage","localStringBuilder==Attempting to draw with recycled bitmap. View ID = ");
			}
		}
	}

	public abstract Bitmap createMask();
}
