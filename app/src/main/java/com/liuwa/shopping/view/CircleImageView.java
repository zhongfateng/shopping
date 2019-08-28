package com.liuwa.shopping.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * 圆头像ImageView
 *
 * @author zft
 * @version 1.0.0
 */
public class CircleImageView extends MaskedImage {
	
	public CircleImageView(Context paramContext) {
        super(paramContext);  
    }  
   
    public CircleImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);  
    }  
   
    public CircleImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);  
    }  
   
    public Bitmap createMask() {
    	
        int w = getWidth();  
        int h = getHeight();  
        
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);
        //三原色光显示红32蓝32绿64
        //https://zh.wikipedia.org/wiki/三原色光模式#16.E4.BD.8D.E5.85.83.E6.A8.A1.E5.BC.8F
//        paint.setColor(-16777216);  
        
        RectF rectF = new RectF(0.0F, 0.0F, w, h);
        canvas.drawOval(rectF, paint);  
        return bitmap;  
    }  
}
