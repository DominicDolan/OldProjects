package com.games.hoops.hoops;

import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by domin on 15 Sep 2016.
 */
public class StoreState extends State{

    private Button exitx, star1, star5, star10, playerSelectionButton, goPremiumButton;
    int buttonsHeight, buttonsWidth;
    private AnimateValue animateValue;
    int animationID;
    @Override
    public void init() {
        MainActivity.showAd(false);
        animateValue = new AnimateValue();
        exitx = new Button(20,20,100,100,Assets.exitX);

        int border = 50;
        int start = (exitx.x + exitx.width);
        int startY = 150;
        buttonsWidth = (width - start - 4*border)/3;
        buttonsHeight = 2*height/3- 110;
        star1 = new Button(start + border,startY,buttonsWidth,buttonsHeight,null);
        star5 = new Button(start + border + buttonsWidth + border,startY,buttonsWidth,buttonsHeight,null);
        star10 = new Button(start + 2*(border + buttonsWidth) + border,startY,buttonsWidth,buttonsHeight,null);
        int borderY = 30;
        goPremiumButton = new Button(start + border, startY + buttonsHeight + borderY,(width - start - 3*border)/2,height - (startY + buttonsHeight + 2*borderY),Assets.textBox);
        playerSelectionButton = new Button(start + 2*border + goPremiumButton.width, startY + buttonsHeight + borderY,(width - start - 3*border)/2,height - (startY + buttonsHeight + 2*borderY),Assets.textBox);

        animationID = animateValue.addSineWaveAnimation(1,5,0);

    }

    @Override
    public void update(float delta) {
        UIBackground.update(delta);
        animateValue.update(delta);
    }

    @Override
    public void render(Painter g) {
        UIBackground.render(g);
        g.setTypeface(Assets.hobo);

        g.setColor(Color.WHITE);
        g.drawCenteredString("Total Stars: "+MainActivity.sGame.getSettings().getTotalStars(),width/2,100, 500);

        float value = animateValue.getValue(animationID);
        g.setColor(0xFFE0E0E0);
        g.fillRoundRect(star1.x - value/2,star1.y - value/2,buttonsWidth + value,buttonsHeight + value,20);
        g.drawCenteredImage(Assets.star, star1.x + buttonsWidth/2, star1.y + buttonsHeight/3, buttonsWidth-100,buttonsWidth-100);
        g.setColor(Color.WHITE);
        g.drawCenteredString("Buy 1 Star",star1.x + buttonsWidth/2, star1.y + buttonsHeight - 50, buttonsWidth-50);

        float starSize = buttonsWidth - 130;
        g.setColor(0xFFE0E0E0);
        g.fillRoundRect(star5.x - value/2,star5.y - value/2,buttonsWidth + value,buttonsHeight + value,20);
        g.drawCenteredImage(Assets.fivestar,star5.x+buttonsWidth/2,star5.y + buttonsHeight/3,  buttonsWidth-50,  buttonsWidth-50);
        g.setColor(Color.WHITE);
        g.drawCenteredString("Buy 5 Stars",star5.x + buttonsWidth/2, star5.y + buttonsHeight - 50, buttonsWidth-50);

        g.setColor(0xFFE0E0E0);
        g.fillRoundRect(star10.x - value/2,star10.y - value/2,buttonsWidth + value,buttonsHeight + value,20);
        g.drawCenteredImage(Assets.starchest,star10.x + buttonsWidth/2, star10.y + buttonsHeight/3, buttonsWidth-50, buttonsWidth-50);
        g.setColor(Color.WHITE);
        g.drawCenteredString("Buy 10 Stars",star10.x + buttonsWidth/2, star10.y + buttonsHeight - 50, buttonsWidth-50);

        g.drawButton(exitx);

        g.drawButton(goPremiumButton);
        g.drawButton(playerSelectionButton);

        g.drawCenteredString("Go Premium!", goPremiumButton.x + goPremiumButton.width/2,goPremiumButton.y + goPremiumButton.height/2,goPremiumButton.width-100,goPremiumButton.height-50);
        g.drawCenteredString("More Skins", playerSelectionButton.x + playerSelectionButton.width/2,goPremiumButton.y + goPremiumButton.height/2, goPremiumButton.width - 100, goPremiumButton.height-50);
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (star1.isPressed(e,scaledX,scaledY)){
            MainActivity.purchases.onStar1Pressed();
        }

        if (star5.isPressed(e,scaledX,scaledY)){
            MainActivity.purchases.onStar5Pressed();
        }

        if (star10.isPressed(e,scaledX,scaledY)){
            MainActivity.purchases.onStar10Pressed();
        }

        if (goPremiumButton.isPressed(e,scaledX,scaledY)){
            MainActivity.purchases.onGoPremiumPressed();
        }

        if (exitx.isPressed(e,scaledX,scaledY)){
            setCurrentState(new MenuState());
        }

        if (playerSelectionButton.isPressed(e,scaledX,scaledY)){
            setCurrentState(new PlayerSelectionState());
        }
        return true;
    }

    @Override
    public void onSensorChanged(float x, float y, float z) {

    }
}
