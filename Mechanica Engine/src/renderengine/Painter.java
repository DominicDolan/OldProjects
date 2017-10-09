package renderengine;

import font.FontType;
import font.GUIText;
import matrices.TransformationMatrix;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import renderers.RenderFunction;
import renderers.Renderers;
import renderers.color.ColorShader;
import renderers.texture.TextureShader;
import statics.G;
import statics.Loader;
import statics.Units;
import statics.W;
import world.entities.RawEntity;

/**
 * Created by domin on 30 Mar 2017.
 */

public class Painter {
    private RenderFunction textureRenderer, colorRenderer, fontRenderer;
    private ColorShader colorShader;
    private TextureShader textureShader;
    private TexturedModel model;
//    private FontRenderer fontRenderer;
    private GUIText guiText;
    private TransformationMatrix matrix;
    private float zDistance = 0;

    private float[] colorArray;
    private int color;

    public Painter(){
        Renderers.makeShaders();
        Renderers.makeRenderers();

        textureRenderer = Renderers.textureRenderer;
        colorRenderer = Renderers.colorRenderer;
        fontRenderer = Renderers.fontRenderer;

        colorShader = Renderers.colorShader;
        textureShader = Renderers.textureShader;

        colorArray = Renderers.colorArray;

        model = Loader.loadTexturedQuad(0,1,1,0);
        colorShader.loadViewMatrix(W.viewMatrix.create());
        textureShader.loadViewMatrix(W.viewMatrix.create());


        guiText = new GUIText(" Test Test Test Test Test Test", 10, G.arial, 0,0,1,false);
//        guiText.set(model);
        matrix = Renderers.transformationMatrix;
        matrix.setTranslate(0, 0, zDistance);

    }

    public void setColor(int color){
//        this.boxColor = boxColor;
        int a = (color & 0xFF);
        color = (color - a) >> 8;
        int b = (color & 0xFF);
        color = (color - b) >> 8;
        int g = (color & 0xFF);
        color = (color - g) >> 8;
        int r = (color & 0xFF);
        colorArray[0] = r/255f;
        colorArray[1] = g/255f;
        colorArray[2] = b/255f;
        colorArray[3] = a/255f;
    }

    public void setColor(float r, float g, float b, float a){
        colorArray[0] = r;
        colorArray[1] = g;
        colorArray[2] = b;
        colorArray[3] = a;
    }



    public void cleanUp(){
        Renderers.cleanUp();
        Loader.cleanUp();
    }

    public void prepare(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.95f, 0.95f, 0.95f, 1);
    }

    public void drawRect(double x, double y, double width, double height){
        matrix.setTranslate(x, y, zDistance);
        matrix.setScale(width, height, 1);
        colorRenderer.render(model);
    }

    public void drawRotatedRect(double centerX, double centerY, double width, double height, double degrees){
        matrix.setPivot(width/2, height/2);
        matrix.setTranslate(centerX - width/2, centerY - height/2, zDistance);
        matrix.setRotate(0,0,degrees);
        matrix.setScale(width, height, 1);
        colorRenderer.render(model);
        matrix.setRotate(0,0,0);
    }

    public void drawEntity(RawEntity entity){
        float x = entity.getPosition().x, y = entity.getPosition().y;
        float centerX = entity.centroid.x, centerY = entity.centroid.y;

        if (entity.type == RawEntity.COLOR){
            setColor(entity.color);

            matrix.rewind();
            matrix.setPivot(centerX, centerY);
            matrix.setTranslate(x - centerX, y - centerY, zDistance);
            matrix.setRotate(0,0,entity.getRadians()*degrees);

            colorRenderer.render(entity.model);

        } else if (entity.type == RawEntity.IMAGE){
            centerX = entity.imageX;
            centerY = entity.imageY;
            matrix.setPivot(centerX, centerY);
            matrix.setTranslate(x - centerX, y - centerY, zDistance);
            matrix.setRotate(0,0,entity.getRadians()*degrees);
            matrix.setScale(entity.imageWidth, entity.imageHeight, 1);
            model.setTexture(entity.image);

            textureRenderer.render(model);
            matrix.setRotate(0,0,0);
        }
//        drawRotatedRect(entity.getPosition().x, entity.getPosition().y, 1, 1, entity.getRadians()*degrees);
    }

    public void drawFillModel(TexturedModel model){
        matrix.rewind();
        colorRenderer.render(model);
    }

    public void drawRotatedFillModel(TexturedModel model, double centerX, double centerY, double width, double height, double degrees){

        matrix.rewind();
        matrix.setPivot(centerX, centerY);
        matrix.setTranslate(-width/2f, -height/2f, zDistance);
        matrix.setRotate(0,0,degrees);

        colorRenderer.render(model);
    }

    public void drawFillModel(TexturedModel model, double x, double y){
        matrix.rewind();
        matrix.setTranslate(x,y,zDistance);
        colorRenderer.render(model);
    }

    public void drawFillModel(TexturedModel model, double x, double y, double scaleWidth, double scaleHeight){
        matrix.rewind();
        matrix.setTranslate(x,y,zDistance);
        matrix.setScale(scaleWidth, scaleHeight, 0);
        colorRenderer.render(model);
    }

    public void fillScreenModel(TexturedModel model, double x, double y, double scaleWidth, double scaleHeight){
        matrix.rewind();
        matrix.setTranslate(x,y,zDistance);
        matrix.setScale(scaleWidth, scaleHeight, 0);
        Renderers.viewMatrix = W.UIView;
        colorRenderer.render(model);
        Renderers.viewMatrix = W.viewMatrix;
    }

    public void fillScreenModel(TexturedModel model, double x, double y, double scaleWidth, double scaleHeight, float degrees){
        matrix.rewind();
        matrix.setTranslate(x,y,zDistance);
        matrix.setScale(scaleWidth, scaleHeight, 0);
        matrix.setRotate(0,0,degrees);
        Renderers.viewMatrix = W.UIView;
        colorRenderer.render(model);
        Renderers.viewMatrix = W.viewMatrix;
    }

    public void drawImage(int image){
        model.setTexture(image);
        textureRenderer.render(model);
    }

    public void drawScreenImage(int image, double xPixels, double yPixels, double widthPixels, double heightPixels){

        matrix.setTranslate(xPixels, yPixels, zDistance);
        matrix.setScale(widthPixels, heightPixels, 1);
        model.setTexture(image);
        Renderers.viewMatrix = W.UIView;
        textureRenderer.render(model);
        Renderers.viewMatrix = W.viewMatrix;

    }

    public void drawImage(int image, Matrix4f matrix){
        model.setTexture(image);
        Renderers.matrix4f = matrix;
        Renderers.customMatrixRenderer.render(model);
    }

    public void drawImage(int image, double x, double y, double width, double height){

        matrix.setTranslate(x, y, zDistance);
        matrix.setScale(width, height, 1);
        model.setTexture(image);
        textureRenderer.render(model);
    }

    public void drawCenteredImage(int image, double x, double y, double width, double height){
        drawImage(image, x-width/2, y-height/2, width, height);
    }

    public void drawPositionalImage(int image, double left, double top, double right, double bottom){
        drawImage(image, left, bottom, right - left, top - bottom);
    }

    public void drawRotatedImage(int image, double centerX, double centerY, double width, double height, double degrees){
        matrix.setPivot(width/2, height/2);
        matrix.setTranslate(centerX - width/2, centerY - height/2, zDistance);
        matrix.setRotate(0,0,degrees);
        matrix.setScale(width, height, 1);
        model.setTexture(image);
        textureRenderer.render(model);
        matrix.setRotate(0,0,0);
    }

//    public void drawScreenImage(int image, double x, double y, double width, double height){
//        transformationMatrix.setTranslate(x, y, zDistance);
//        transformationMatrix.setScale(width, height, 1);
//        model.setTexture(image);
//        textureRenderer.startForUI();
//        textureRenderer.prepareModel(transformationMatrix.create());
//        textureRenderer.render(model);
//        textureRenderer.start();
//    }

//    public void drawButton(Button button){
//        transformationMatrix.setTranslate(button.x, button.y, zDistance);
//        transformationMatrix.setScale(button.width, button.height, 1);
//        model.setTexture(button.image);
//        textureRenderer.startForUI();
//        textureRenderer.prepareModel(transformationMatrix.create());
//        textureRenderer.render(model);
//        textureRenderer.start();
//    }

//    public void drawText(String text, FontType font, float fontSize, float x, float y, boolean centered){
//        textureRenderer.stop();
//        guiText.set(text, fontSize, font, x, y, 5, centered);
//        fontRenderer.render(guiText);
//        textureRenderer.start();
//    }
//
    public void drawText(GUIText text){
//        fontRenderer.setText(text);
        fontRenderer.render(text);
    }

    public void setText(String text, float fontSize, FontType font, float x, float y, float maxLineLength,
                        boolean centered){
        guiText.set(text, fontSize, font, x, y, maxLineLength, centered);
    }

    public void setText(String text, float x, float y){
        guiText.set(text, x, y);
    }

    public void drawText(){
        fontRenderer.render(guiText);
    }
//
//
//    public void drawAnimation(FramesAnimation animation, float x, float y, float width, float height){
//        drawImage(animation.getCurrentFrameID(),x,y,width,height);
//    }

    public void drawLine(double strokeWidth, double x1, double y1, double x2, double y2){
        double triangleWidth = (x2 - x1);
        double triangleHeight = (y2 - y1);
        matrix.setScale(Math.hypot(triangleWidth, triangleHeight), strokeWidth, 1);

        matrix.setTranslate(x1, y1, 0);
        matrix.setPivot(0 ,strokeWidth/2);
        matrix.setRotate(0,0,Math.atan2(triangleHeight, triangleWidth)*degrees);

        colorRenderer.render(model);
    }

    public void drawHorizontalLine(double strokeWidth, float y){
        matrix.setScale(W.getWidth(), strokeWidth, 0);
        matrix.setTranslate(W.getViewX() - W.getWidth()/2.0, y, 0);

        colorRenderer.render(model);
    }


    public void drawVerticalLine(double strokeWidth, float x){
        matrix.setScale(strokeWidth, W.getHeight() , 0);
        matrix.setTranslate(x, W.getViewY() - W.getHeight()/2.0, 0);

        colorRenderer.render(model);
    }

    private static double pixel = Units.pixel();
    private static double cm = Units.cm();
    private static double x48th = Units.x48th(), y48th = Units.y48th();
    private static double x12th = Units.x12th(), y12th = Units.y12th();
    private static double radians = Units.radians, degrees = Units.degrees;

    public static void updateUnits(){
        pixel = Units.pixel();
        cm = Units.cm();
        x48th = Units.x48th();
        y48th = Units.y48th();
        x12th = Units.x12th();
        y12th = Units.y12th();
    }
}
