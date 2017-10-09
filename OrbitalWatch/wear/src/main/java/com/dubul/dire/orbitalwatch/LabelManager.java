package com.dubul.dire.orbitalwatch;

import android.graphics.Path;

/**
 * Created by domin on 10 Aug 2016.
 */
public class LabelManager {
    OrbitalCalendar t;

    Label label60;
    Label label30;
    Label label15;
    Label label45;

    int type;
    float width, height, centerX, centerY;
    float pathAngleLeft, pathAngleRight;

    Path path;

    public LabelManager(int type){
        this.type = type;
        t = Orbital.settings.getCalendar();

        width = Orbital.settings.getScreenWidth();
        height = Orbital.settings.getScreenHeight();

        centerX = width/2;
        centerY = height/2;

        path = new Path();
        pathAngleLeft = Orbital.settings.getPathAngleLeft();
        pathAngleRight = Orbital.settings.getPathAngleRight();

        label60 = new Label (Label.CURRENT,type);
        label15 = new Label (Label.NEXT,type);
        label30 = new Label (Label.THIRD,type);
        label45 = new Label (Label.FOURTH,type);

    }

    public void update(){
        label60.update();
        switch (label60.getQuadrant()){
            case 0:
                label45.setPosition(Label.CURRENT);
                label60.setPosition(Label.NEXT);
                label15.setPosition(Label.THIRD);
                label30.setPosition(Label.FOURTH);
                break;
            case 1:
                label60.setPosition(Label.CURRENT);
                label15.setPosition(Label.NEXT);
                label30.setPosition(Label.THIRD);
                label45.setPosition(Label.FOURTH);
                break;
            case 2:
                label15.setPosition(Label.CURRENT);
                label30.setPosition(Label.NEXT);
                label45.setPosition(Label.THIRD);
                label60.setPosition(Label.FOURTH);
                break;
            case 3:
                label30.setPosition(Label.CURRENT);
                label45.setPosition(Label.NEXT);
                label60.setPosition(Label.THIRD);
                label15.setPosition(Label.FOURTH);
                break;
        }

        label60.update();
        label15.update();
        label30.update();
        label45.update();

        updatePath();
    }

    public void render(Painter c){
        c.drawLabel(label60);
        c.drawLabel(label30);
        c.drawLabel(label15);
        c.drawLabel(label45);

        c.setColor(0xFF444444);
        c.setStrokeWidth(3.5f);
        c.drawPath(path);

        c.setColor(0xFFFFFFFF);
        c.setStrokeWidth(2.5f);
        c.drawPath(path);

    }

    public void logRender(Painter c, int row){
        c.drawString("Label60(" + label60.name +"): quad:" + label60.getQuadrant() + ", pos: " + label60.position + ", ang: " + label60.rotation, 10, (int)(height + 40), (int) (width - 20));
        c.setUnderlineText(true);
        float textHeight = c.getTextHeight("0");
        textHeight += 5;
        float logHeight = 4*textHeight*row;
        c.drawString("Label60(" + label60.name +"): quad:" + label60.getQuadrant() + ", pos: " + label60.position + ", ang: " + label60.rotation, 10, (int)(height - 10 - logHeight), (int) (width - 20));
        c.setUnderlineText(false);
        c.drawString("Label15(" + label15.name +"): quad:" + label15.getQuadrant() + ", pos: " + label15.position + ", ang: " + label15.rotation, 10, (int)(height - 10 - textHeight - logHeight), (int) (width - 20));
        c.drawString("Label30(" + label30.name +"): quad:" + label30.getQuadrant() + ", pos: " + label30.position + ", ang: " + label30.rotation, 10, (int)(height - 10 - 2*textHeight - logHeight), (int) (width - 20));
        c.drawString("Label45(" + label45.name +"): quad:" + label45.getQuadrant() + ", pos: " + label45.position + ", ang: " + label45.rotation, 10, (int)(height - 10 - 3*textHeight - logHeight), (int) (width - 20));
    }

    public void updatePath(){
        float currentAngle = pathAngleLeft;
        path.rewind();

        if (label60.inPath && !label45.inPath) {
            addArc(label60.radius, -currentAngle, -(label60.rotation + label60.angularSize / 2 + label60.padding));
            currentAngle = label60.rotation - label60.angularSize / 2 - label60.padding;
        }
        if (label15.inPath) {
            addArc(label60.radius, -currentAngle, -(label15.rotation + label15.angularSize / 2 + label60.padding));
            currentAngle = label15.rotation - label15.angularSize / 2 - label60.padding;
        }
        if (label30.inPath) {
            addArc(label60.radius, -currentAngle, -(label30.rotation + label30.angularSize / 2 + label60.padding));
            currentAngle = label30.rotation - label30.angularSize / 2 - label60.padding;
        }
        if (label45.inPath) {
            addArc(label60.radius, -currentAngle, -(label45.rotation + label45.angularSize / 2 + label60.padding));
            currentAngle = label45.rotation - label45.angularSize / 2 - label60.padding;
        }
        if (label60.inPath && label45.inPath) {
            addArc(label60.radius, -currentAngle, -(label60.rotation + label60.angularSize / 2 + label60.padding));
            currentAngle = label60.rotation - label60.angularSize / 2 - label60.padding;
        }
        addArc(label60.radius, -currentAngle, -pathAngleRight);
    }

    public void addArc(float radius, float startAngle, float endAngle){
        path.addArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, startAngle, endAngle - startAngle);
    }
}
