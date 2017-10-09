package com.dubul.dire.orbitalwatch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

/**
 * Created by domin on 8 Aug 2016.
 */
public class Painter {

    public final static int LEFT = 1;
    public final static int CENTER = 2;
    public final static int RIGHT = 3;

    Canvas canvas;
    Paint paint;
    Rect srcRect, dstRect;
    RectF dstRectF;
    Matrix matrix;

    public Painter(Canvas canvas){
        this.canvas = canvas;
        paint = new Paint();
        srcRect = new Rect();
        dstRect = new Rect();
        dstRectF = new RectF();

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

    public void drawString(String str, float x, float y) {
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
        canvas.drawText(str, x, y, paint);

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

    public void getRect (Rect rect){
        rect.set(dstRect.left, dstRect.top, dstRect.right, dstRect.bottom);
    }

    public void drawCenteredString(String str, float x, float y) {
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

    public void drawRightString(String str, float x, float y) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(str, x, y, paint);
    }

    public void fillRect(int x, int y, int width, int height) {
        dstRect.set(x, y, x + width, y + height);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(dstRect, paint);
    }

    public void fillRoundRect (float x, float y, float width, float height, float radius){
        dstRectF.set(x,y,x+width,y+height);
        canvas.drawRoundRect(dstRectF, radius, radius,paint);
    }

    public void drawImage(Bitmap bitmap, int x, int y) {
        canvas.drawBitmap(bitmap, x, y, paint);
    }

    public void drawImage(Bitmap bitmap, float x, float y, float width, float height) {
        float oldWidth = bitmap.getWidth();
        float oldHeight = bitmap.getHeight();
        float scaleWidth = width/oldWidth;
        float scaleHeight = height/oldHeight;
        matrix.reset();
        matrix.postScale(scaleWidth,scaleHeight);
        matrix.postTranslate(x,y);
        canvas.drawBitmap(bitmap,matrix,paint);
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

    public void drawButton(Button button){
        if (button.isActive()) {
            if (button.hasBackground()) {
                paint.setColor(button.getBackgroundColor());
                float top = button.y + button.insetTop;
                float left = button.x + button.insetLeft;
                canvas.drawOval(button.getRect(), paint);
                canvas.drawBitmap(button.image, left, top, paint);
            } else {
                drawImage(button.image, button.x, button.y, button.width, button.height);
            }
        }
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

    public void drawPath(Path path){
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
    }

    public void setUnderlineText(boolean underlineText){
        paint.setUnderlineText(underlineText);
    }

    public void drawLabel (Label label){
        float centerX = Orbital.settings.getScreenWidth()/2.0f;
        float centerY = Orbital.settings.getScreenHeight()/2.0f;
        float textSize = label.textPaint.getTextSize();
        if (label.inPath){
            canvas.save();
            canvas.rotate(-label.rotation + 90, centerX, centerY);
            label.textPaint.setTextSize(textSize);
            label.textPaint.setColor(0xFF000000);
            label.textPaint.setFakeBoldText(true);
            canvas.drawText(label.name, centerX, centerY - label.radius + label.height / 2, label.textPaint);
            label.textPaint.setTextSize(textSize);
            label.textPaint.setColor(0xFFFFFFFF);
            label.textPaint.setFakeBoldText(false);
            canvas.drawText(label.name, centerX, centerY - label.radius + label.height / 2, label.textPaint);
            canvas.restore();
        }
    }

    public void setStrokeWidth (float strokeWidth){
        paint.setStrokeWidth(strokeWidth);
    }

    public void rotateCanvas(float radians, int pivotX, int pivotY){
        canvas.save();
        canvas.rotate((float) (radians*180/Math.PI),pivotX,pivotY);
    }

    public void restoreCanvas(){
        canvas.restore();
    }

    public void setAntiAlias (boolean antiAlias){
        paint.setAntiAlias(antiAlias);
    }

}
