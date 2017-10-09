package com.games.hoops.hoops;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

/**
 * Created by domin on 4 Jun 2016.
 */
public class Painter {
    public final static int LEFT = 1;
    public final static int CENTER = 2;
    public final static int RIGHT = 3;


    private Canvas canvas;
    private Paint paint;
    private Rect srcRect;
    private Rect dstRect;
    private RectF srcRectF;
    private RectF dstRectF;
    private Matrix matrix;

    public Painter(Canvas canvas) {
        this.canvas = canvas;
        paint = new Paint();
        srcRect = new Rect();
        dstRect = new Rect();
        dstRectF = new RectF();
        srcRectF = new RectF();

        matrix = new Matrix();
        paint.setAntiAlias(true);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setFont(Typeface typeface, float textSize) {
        paint.setTypeface(typeface);
        paint.setTextSize(textSize);
    }

    public void drawString(String str, int x, int y) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(str, x, y, paint);
    }

    public void drawString(String str, int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(100);
        paint.setTextScaleX(1.0f);

        paint.getTextBounds(str, 0, str.length(), dstRect);
        int h = dstRect.height();
        float size = (((float)height)/dstRect.height())*100;
        paint.setTextSize(size);
        paint.getTextBounds(str,0,str.length(),dstRect);
        float scale = ((float)width)/dstRect.width();
        paint.setTextScaleX(scale);
        canvas.drawText(str, x, y + height, paint);

        paint.setTextScaleX(1.0f);
    }

    public void drawString(String str, int x, int y, int width) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(100);

        paint.getTextBounds(str,0,str.length(),dstRect);
        float size = ((float)width)/dstRect.width()*100;
        paint.setTextSize(size);
        canvas.drawText(str, x, y, paint);

        paint.setTextScaleX(1.0f);
    }

    public void drawStringwithBackground(String str, int x, int y, int border, int backgroundColor){
        int tmpColor = paint.getColor();
        paint.setColor(backgroundColor);
        int stringWidth = (int) getTextWidth(str);
        int stringHeight = (int) getTextHeight(str);
        paint.setStyle(Paint.Style.FILL);

        dstRect.set(x - border, y - border, x + stringWidth + border, y + stringHeight + border);
        canvas.drawRect(dstRect, paint);

        paint.setColor(tmpColor);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(str, x, y, paint);
    }

    public void drawStringwithBackground(String str, int x, int y, int border, int backgroundColor, int alignment){
        int tmpColor = paint.getColor();
        paint.setColor(backgroundColor);
        int stringWidth = (int) getTextWidth(str);
        int stringHeight = (int) getTextHeight(str);
        paint.setStyle(Paint.Style.FILL);

        if (alignment == LEFT) {
            paint.setTextAlign(Paint.Align.LEFT);
            dstRect.set(x - border, y - stringHeight - border, x + stringWidth + border, y + border);

        }
        else if (alignment == CENTER) {
            paint.setTextAlign(Paint.Align.CENTER);
            dstRect.set(x - stringWidth / 2 - border, y - stringHeight - border, x + stringWidth / 2 + border, y + border);
        }
        else if (alignment == RIGHT) {
            paint.setTextAlign(Paint.Align.RIGHT);
            dstRect.set(x - stringWidth - border, y -stringHeight - border, x + border, y + border);
        }

        canvas.drawRect(dstRect, paint);

        paint.setColor(tmpColor);

        canvas.drawText(str, x, y, paint);
    }

    public void getRect (Rect rect){
        rect.set(dstRect.left, dstRect.top, dstRect.right, dstRect.bottom);
    }

    public void drawCenteredString(String str, int x, int y) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(str, x, y, paint);
    }

    public void drawCenteredString(String str, int x, int y, int width) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(100);

        paint.getTextBounds(str,0,str.length(),dstRect);
        float size = ((float)width)/dstRect.width()*100;
        paint.setTextSize(size);
        canvas.drawText(str, x, y, paint);

        paint.setTextScaleX(1.0f);
    }

    public void drawCenteredString(String str, int centerX, int centerY, int width, int height) {
        drawString(str,centerX-width/2,centerY-height/2,width,height);
    }

    public void drawRightString(String str, int x, int y) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(str, x, y, paint);
    }

    public void fillRect(float x, float y, float width, float height) {
        dstRectF.set(x, y, x + width, y + height);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(dstRectF, paint);
    }

    public void fillRoundRect(float x, float y, float width, float height, float radius){
        dstRectF.set(x, y, x + width, y + height);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(dstRectF,radius, radius, paint);
    }

    public void drawImage(Bitmap bitmap, int x, int y) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    public void drawImage(Bitmap bitmap, float x, float y, float width, float height) {
//        srcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        dstRect.set((int) x,(int) y,(int)(x + width),(int) (y + height));
//        canvas.drawBitmap(bitmap, srcRect, dstRect, paint);

        float oldWidth = bitmap.getWidth();
        float oldHeight = bitmap.getHeight();
        float scaleWidth = width/oldWidth;
        float scaleHeight = height/oldHeight;
        matrix.reset();
        matrix.postScale(scaleWidth,scaleHeight);
        matrix.postTranslate(x,y);
        canvas.drawBitmap(bitmap,matrix,paint);
    }

    public void drawCenteredImage(Bitmap bitmap, float centerX, float centerY, float width, float height){
        drawImage(bitmap, centerX-width/2, centerY - height/2, width, height);
    }

    public void fillOval(int centerX, int centerY, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        dstRectF.set(centerX - width, centerY - height, centerX + width, centerY + height);
        canvas.drawOval(dstRectF, paint);
    }

    public void drawOval(float centerX, float centerY, float width, float height, float strokeWidth) {
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        dstRectF.set(centerX - width, centerY - height, centerX + width, centerY + height);
        canvas.drawOval(dstRectF, paint);
        paint.setStyle(Paint.Style.FILL);
    }

    public void drawButton (Button button){
        if (button.isVisible()) {
            srcRect.set(0, 0, button.image.getWidth(), button.image.getHeight());
            dstRect.set(button.x, button.y, button.x + button.width, button.y + button.height);
            canvas.drawBitmap(button.image, srcRect, dstRect, paint);
        }
    }

    public void drawToggleButton (ToggleButton button){
        srcRect.set(0, 0, button.getImage().getWidth(), button.getImage().getHeight());
        dstRect.set(button.x, button.y, button.x + button.width, button.y + button.height);
        canvas.drawBitmap(button.getImage(), srcRect, dstRect, paint);
    }

    public void setTypeface(Typeface typeface){
        paint.setTypeface(typeface);
    }

    public void setTextSize (float size){
        paint.setTextSize(size);
    }

    public float getTextSize (){
        return paint.getTextSize();
    }

    public float getTextWidth(String str){
        paint.getTextBounds(str,0,str.length(),dstRect);
        return dstRect.width();
    }

    public float getTextHeight(String str){
        paint.getTextBounds(str,0,str.length(),dstRect);
        return dstRect.height();
    }

    public void setBlur (int blur){
        paint.setMaskFilter(new BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL));
    }

    public void removeBlur (){
        paint.setMaskFilter(null);
    }

    public void drawPath(Path path){
        canvas.drawPath(path, paint);
    }

    public void rotateCanvas(float radians, int pivotX, int pivotY){
        canvas.save();
        canvas.rotate((float) (radians*180/Math.PI),pivotX,pivotY);
    }

    public void restoreCanvas(){
        canvas.restore();
    }
}
