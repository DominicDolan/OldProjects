package com.games.hoops.hoops;

import android.graphics.Color;

/**
 * Created by domin on 9 Oct 2016.
 */
public class UIBackground {
    private static float PI = (float) Math.PI;
    private static int width = MainActivity.GAME_WIDTH;
    private static int height = MainActivity.GAME_HEIGHT;
    private static AnimateValue animateValue;
    private static int sin1, sin2, sin3, sin4;
    private static int line1, line2, line3, line4, honeycombLine;

    public static void init(){
        animateValue = new AnimateValue();

        sin1 = animateValue.addSineWaveAnimation(0.14f,200,0);
        sin2 = animateValue.addSineWaveAnimation(0.2f,200,PI/3);
        sin3 = animateValue.addSineWaveAnimation(0.14f,100,0.7f*PI);
        sin4 = animateValue.addSineWaveAnimation(0.24f,100,0.4f*PI);

        line1 = animateValue.addLinearAnimation(-100,1380,6);
        line2 = animateValue.addLinearAnimation(-100,1380,10);
        line3 = animateValue.addLinearAnimation(-100,1380,20);
        line4 = animateValue.addLinearAnimation(-300,1480,15);

        honeycombLine = animateValue.addLinearAnimation(0,604,16);
    }
    public static void update(float delta){
        animateValue.update(delta);
    }

    public static void render(Painter g){
        g.setColor(Color.YELLOW);
        g.fillRect(0,0,width,height);

        g.rotateCanvas(40*PI/180,width/2,height/2);
        g.setColor(0xFFFDF59E);

        fillLeftRectangles(g);
        fillRightRectangles(g);

        g.drawImage(Assets.honeycomb, -130, (int) (202 + animateValue.getValue(honeycombLine)));
        g.drawImage(Assets.honeycomb, -130, (int) (202 - 604 + animateValue.getValue(honeycombLine)));
        g.drawImage(Assets.honeycombflip,1069, (int) (-137 - animateValue.getValue(honeycombLine)));
        g.drawImage(Assets.honeycombflip,1069, (int) (-137 + 604 - animateValue.getValue(honeycombLine)));

        g.restoreCanvas();
    }

    private static void fillLeftRectangles(Painter g){
        g.fillRect(100 + animateValue.getValue(sin1),185,340,54);
        g.fillRect(0,143,275,173);
        g.fillRect(290 + animateValue.getValue(sin2)/2,177,261,11);
        g.fillRect(200 + animateValue.getValue(line1),221,261,11);
        g.fillRect(animateValue.getValue(sin1)*0.3f,290,610,33);
        g.fillRect(animateValue.getValue(line2),319,280,12);
        g.fillRect(animateValue.getValue(sin3),312,400,50);
        g.fillRect(animateValue.getValue(line1), 357, 300, 13);
        g.fillRect(217,424,260,10);
        g.fillRect(animateValue.getValue(sin2)/4,415,340,55);
        g.fillRect(-500 + animateValue.getValue(line3),454,500,33);
        g.fillRect(animateValue.getValue(sin1)/10,500,550,35);
        g.fillRect(-150 + animateValue.getValue(line2),515,260,11);
        g.fillRect(0,608,260,60);
        g.fillRect(animateValue.getValue(sin4),647,260,11);
        g.fillRect(animateValue.getValue(sin3),674,390,34);
        g.fillRect(animateValue.getValue(line4),694,600,34);
        g.fillRect(animateValue.getValue(sin2)/5,721,330,55);
        g.fillRect(animateValue.getValue(sin4),772,400,35);
        g.fillRect(animateValue.getValue(line2)/2,795,300,12);
        g.fillRect(100,824,340,55);
        g.fillRect(animateValue.getValue(line1)/2,867,375,33);
    }

    //    sin1 = (0.07f,200,0);         sin2 = (0.15f,200,PI/3);
//    sin3 = (0.07f,100,0.7f*PI);   sin4 = (0.25f,100,0.4f*PI);
//
//    line1 = (-100,1380,12);     line2 = (-100,1380,20);
//    line3 = (-100,1380,40);     line4 = (-300,1480,30);
    private static void fillRightRectangles(Painter g){
        g.fillRect(860 - animateValue.getValue(line1)/4,-270,260,12);
        g.fillRect(857 - animateValue.getValue(sin4)/3,-220,360,33);
        g.fillRect(893,-193,225,53);
        g.fillRect(1180 - animateValue.getValue(line3)/2,-104,260,12);
        g.fillRect(1180 - animateValue.getValue(line1)*0.7f,-102,200,54);
        g.fillRect(520 + animateValue.getValue(sin1),-70,600,34);
        g.fillRect(747 - animateValue.getValue(sin1)/3,-51,445,55);
        g.fillRect(1180 - animateValue.getValue(line3)*0.8f,-20,260,12);
        g.fillRect(850 + animateValue.getValue(sin2),34,261,12);
        g.fillRect(1280 - animateValue.getValue(line2),67,260,12);
        g.fillRect(1017,64,300,60);
        g.fillRect(900 - animateValue.getValue(sin3),73,375,33);
        g.fillRect(750 + animateValue.getValue(sin1),128,261,12);
        g.fillRect(952 - animateValue.getValue(sin1)/3,160,340,55);
        g.fillRect(820 + 0.7f*animateValue.getValue(sin2),210,600,34);
        g.fillRect(1280 - animateValue.getValue(line1),296,260,12);
        g.fillRect(950 - animateValue.getValue(sin2)/5,320,340,170);
        g.fillRect(660 + animateValue.getValue(sin4),410,600,34);
    }
}
