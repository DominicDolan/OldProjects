package com.games.hoops.hoops;

import android.graphics.Color;
import android.view.MotionEvent;

/**
 * Created by domin on 8 Jun 2016.
 */
public class PlayState extends State {

    private Settings settings;
    boolean menuOpen = false;
    Button menu, exitx, inMenuRestart, inMenuMenu, inMenuSettings, settingsButton;
    boolean paused = false;
    boolean failed = false;

    Button restartButton, menuStateButton, nextLevelButton;
    int panelWidth = width/2+100,panelHeight = height/3+80;
    int buttonSize = 180;

    AnimateValue animateValue;
    int scoreAnimationID, starAnimationID, colorsBackgroundID;
    int scoreAchievedPosition;

    float fov = 1.2f;
    float horizon = height/3;
    float playerPosX = width/2;
    float playerPosY;
    float playerRadius = 100;
    float playerPhysicalSize=1;
    float playerPhysicalPosY = 1.2f*playerPhysicalSize;
    double playerDistance;
    float playerMovementArea;

    float hoopPhysicalRadius;
    boolean[] hoopCreated,hoopObtained, hoopMissed;
    float hoopWavelength = 4.0f;
    float hoopPhysicalStartingDistance = 180;
    float hoopPhysicalVelocity = 15;
    boolean hoopHit = false, zHit = false, xHit = false;
    float hoopsStartingWidth = 3.5f*playerPhysicalSize, hoopEndingWidth = 1.2f*playerPhysicalSize;

    int floorPanelQuantity = 20;
    float floorSpawnTracking = 0;
    float floorPhysicalDistanceY = 10;
    float floorPhysicalDistanceApart = 10;

    ZPlaneAnimation floor1, floor2, hoops;
    float playerVelX = 0;

    int score = 0;
    private Background background;
    private Player player;
    private float playerAcceleration;
    private boolean slowMotion;
    private float playerMaxVel = 1500;
    private int initialHighScore;

    private boolean renderStar;
    private int starIndex;
    private boolean starObtained;
    private float starTimer;
    private int starCount;
    private int starsObtained =0;
    private int initialStarsObtained;
    private int initialTotalStarsObtained;
    private boolean endScreenStars = true, endScreenHighScore = true, endScreenNewBackground = true;
    private int nextBackgroundID, nextBackgroundStars;
    private int highScoreFlashID, endStarObtainedID, endScreenOverlayID;

    private int maxHealth = 4;
    private int health = maxHealth;
    private int endScreenID;
    private int wait;

    private long timeStamp;
    private boolean timerStarted = false;
    private boolean isLockAnimationPlaying = false;
    private boolean starFlash;
    private boolean newBackground = false;
    private boolean showTip = false;

    @Override
    public void init() {
        settings = MainActivity.sGame.getSettings();
        if (background == null) {
            background = new Background(settings);
        }
        player = new Player(settings);

        MainActivity.showAd(!settings.isPremium());

        menu = new Button(20,20,70,70,Assets.menu);
        exitx = new Button(20,20,70,70,Assets.exitX);
        int inMenuButtonSize = 160;
        inMenuMenu = new Button(80,height/2-280,inMenuButtonSize,inMenuButtonSize,Assets.menubutton);
        inMenuRestart = new Button(80,height/2-inMenuButtonSize/2,inMenuButtonSize,inMenuButtonSize,Assets.restartbutton);
        inMenuSettings = new Button(80,height/2+280-inMenuButtonSize,inMenuButtonSize,inMenuButtonSize,Assets.settingsButtonIcon);

        restartButton = new Button((width- buttonSize)/2+150,(height)/2 -40, buttonSize, buttonSize,Assets.restartbutton);
        restartButton.setSound(Assets.buttonSelect);

        menuStateButton = new Button(width/2-150 - buttonSize/2, height/2 - 40,buttonSize,buttonSize,Assets.menubutton);


        nextLevelButton = new Button(width/2 - 220,restartButton.y + buttonSize + (height - restartButton.y - buttonSize)/2 - 70,440,140,Assets.textBox);

        restartButton.setActive(false);
        menuStateButton.setActive(false);
        nextLevelButton.setActive(false);


//      δ = 2*atan(d/2D)
//      tan(δ/2) = d/2D
//      D = d/2*tan(δ/2)
//      δ =
        double delta = (playerRadius*2 /width)*fov;
        playerDistance = playerPhysicalSize/(2*tan(delta/2));
        playerPosY = (float) (horizon + (width/fov)*2*atan2(playerPhysicalPosY,2*playerDistance));

        playerMovementArea = (float)(playerDistance*(2*tan(fov/2)));

        animateValue = new AnimateValue();
        scoreAnimationID = animateValue.addLinearAnimation(0,-255,2);
        animateValue.setLooping(false,scoreAnimationID);
        animateValue.stop(scoreAnimationID);

        starAnimationID = animateValue.addLinearAnimation(0,-300,1);
        animateValue.setLooping(false, starAnimationID);
        animateValue.stop(starAnimationID);

        colorsBackgroundID = animateValue.addLinearAnimation(0,width,10);
        animateValue.setLooping(true,colorsBackgroundID);

        endScreenID = animateValue.addLinearAnimation(height + 300, height/2,1);
        animateValue.setLooping(false,endScreenID);
        animateValue.stop(endScreenID);

        wait = animateValue.addLinearAnimation(0,0,2);
        animateValue.setLooping(false,endScreenID);
        animateValue.stop(endScreenID);

        highScoreFlashID = animateValue.addLinearAnimation(0,255,0.5f);
        animateValue.setLooping(false,highScoreFlashID);
        animateValue.stop(highScoreFlashID);

        endStarObtainedID = animateValue.addLinearAnimation(400,150,0.3f);
        animateValue.setLooping(false,endStarObtainedID);
        animateValue.stop(endStarObtainedID);

        endScreenOverlayID = animateValue.addLinearAnimation(0,255,0.5f);
        animateValue.setLooping(false,endScreenOverlayID);
        animateValue.stop(endScreenOverlayID);

        initialStarsObtained = settings.getStarsObtained(settings.getBackground());
        initialHighScore = settings.getHighScore();
        initialTotalStarsObtained = settings.getTotalStars();

        int min = 0;
        int[] starsToUnlock = new int[Settings.STARS_TO_UNLOCK.length];
        for (int i = 0; i < Settings.STARS_TO_UNLOCK.length; i++) {
            if (initialTotalStarsObtained < Settings.STARS_TO_UNLOCK[i]){
                starsToUnlock[i] = Settings.STARS_TO_UNLOCK[i];
            }
            else {
                starsToUnlock[i] = -1;
            }
        }


        // TODO Will cause an error when all levels are unlocked.
        for (int i = 0; i < starsToUnlock.length; i++) {
            if (starsToUnlock[min] == -1){
                min++;
            }
        }

        if (min < starsToUnlock.length) {
            for (int i = 0; i < starsToUnlock.length; i++) {
                if (starsToUnlock[i] != -1) {
                    if (starsToUnlock[i] < starsToUnlock[min]) {
                        min = i;
                    }
                }
            }

            nextBackgroundID = min;
            nextBackgroundStars = Settings.STARS_TO_UNLOCK[min];
        }
        else {
            nextBackgroundStars = -1;
        }

        starCount = background.starLocations.length;

        hoopPhysicalRadius = playerPhysicalSize*0.6f;

//        hoopRadii = new float[hoopQuantity];
//        hoopStartingPosition = new float[hoopQuantity];
//        hoopScreenPositionX = new float[hoopQuantity];
//        hoopScreenPositionY = new float[hoopQuantity];
//        individualTracking = new float[hoopQuantity];
//        hoopDistance = new float[hoopQuantity];
//        hoopLifeSpan = new float[hoopQuantity];
//
//        hoopStartingPosition[hoopQuantity-1]=playerMovementArea/2;
//        currentHoopLifeSpan = hoopQuantity*hoopSpawnFrequency;
//        hoopPhysicalVelocity = hoopPhysicalStartingDistance/currentHoopLifeSpan;

        hoops = new ZPlaneAnimation();
        hoops.setFov(fov);
        hoops.setHorizon(horizon);
        hoops.setXbounds(-playerMovementArea / 2, playerMovementArea / 2);
        hoops.setAltitude(playerPhysicalPosY);
        hoops.init(hoopPhysicalStartingDistance, hoopPhysicalVelocity, hoopWavelength);
        hoops.setWavelength((hoopPhysicalStartingDistance/hoops.getQuantity())/hoopPhysicalVelocity);
        for (int i = 0; i < hoops.getQuantity(); i++) {hoops.setPhysicalWidth(hoopsStartingWidth,i);}
        hoopCreated = new boolean[hoops.getQuantity()];
        hoopObtained = new boolean[hoops.getQuantity()];
        hoopMissed = new boolean[hoops.getQuantity()];
        for (int i=0; i<hoops.getQuantity();i++){
            hoopCreated[i] = false;
            hoopObtained[i] = false;
            hoopMissed[i] = false;
        }


        floor1 = new ZPlaneAnimation();
        floor1.setFov(fov);
        floor1.setHorizon(horizon);
        floor1.setIsStarted(true);
        floor1.setXbounds(-60, 60);
        floor1.setAnchorPoints(background.floorTexture1.getAnchorPoints());
        floor1.setAltitude(floorPhysicalDistanceY);
        floor1.setFlat(background.floorTexture1.isFlat());
        floor1.setImages(background.floorTexture1.getFloorTextures());
        floor1.init(floorPanelQuantity * floorPhysicalDistanceApart, hoopPhysicalVelocity, background.floorTexture1.getWavelength());

        floor2 = new ZPlaneAnimation();
        floor2.setFov(fov);
        floor2.setHorizon(horizon);
        floor2.setIsStarted(true);
        floor2.setXbounds(-60, 60);
        floor2.setAltitude(floorPhysicalDistanceY);
        floor2.setAnchorPoints(background.floorTexture2.getAnchorPoints());
        floor2.setFlat(background.floorTexture2.isFlat());
        floor2.setImages(background.floorTexture2.getFloorTextures());
        floor2.init(floorPanelQuantity * floorPhysicalDistanceApart,hoopPhysicalVelocity,background.floorTexture2.getWavelength());

        Tips.update();

        if (Tips.tipID != Tips.NO_TIP){
            showTip = true;
        }
    }
    //δ=2arctan(d/2D)

    @Override
    public void update(float delta) {

        if (isLockAnimationPlaying) {
            Assets.unlockAnimation.update(delta);
        }
        if (settings.getPlayer()==Settings.PLAYER_ASTEROID){
            Assets.astroidFire.update(delta);
        }

        menu.setActive(!menuOpen);
        exitx.setActive(menuOpen);
        inMenuMenu.setActive(menuOpen);
        inMenuRestart.setActive(menuOpen);
        inMenuSettings.setActive(menuOpen);

        animateValue.update(delta);

        if (((playerPosX <= 1280 + playerRadius) || (playerVelX <= 0)) && ((playerPosX >= -playerRadius) || (playerVelX >= 0))) {
            if (!paused) {
                if (((playerVelX <= playerMaxVel) || (playerAcceleration<=0)) && ((playerVelX>=-playerMaxVel) || (playerAcceleration>=0))) {
                    playerVelX += playerAcceleration * delta;
                }

                playerPosX += delta * playerVelX;
            }
        } else {
            paused = true;
            failed = true;
            Tips.tipID = Tips.EDGE_TIP;
//            menuStateButton.setActive(true);
//            restartButton.setActive(true);
//            nextLevelButton.setActive(newBackground);

            if (score > settings.getHighScore()){
                settings.setHighScore(score);
            }
            if (score == settings.getHighScore()){
                Tips.tipID = Tips.NEXT_STAR_TIP;
            }
        }

        if (health == 0){
            paused = true;
            failed = true;
            Tips.tipID = Tips.HEALTH_TIP;
            if (score > settings.getHighScore()){
                settings.setHighScore(score);
            }
            if (score == settings.getHighScore()){
                Tips.tipID = Tips.NEXT_STAR_TIP;
            }
        }

        if (!paused) {
            floorSpawnTracking+=delta;

            if (settings.getBackground()== Settings.BACKGROUND_NY) {
                hoopPhysicalVelocity = 15 + 0.3f*score;
            } else if (settings.getBackground() == Settings.BACKGROUND_SATURN){
                hoopPhysicalVelocity = 15 + 0.7f*score;
            } else {
                hoopPhysicalVelocity = 15 + 0.5f*score;
            }
            hoops.setVelocity(hoopPhysicalVelocity);
            hoops.setWavelength((hoopPhysicalStartingDistance/hoops.getQuantity())/hoopPhysicalVelocity);   /*Old Version: 4.0f * (15.0f / (15.0f + score))*/
            for (int i = 0; i < hoops.getQuantity(); i++) {
                hoops.setPhysicalWidth(hoopEndingWidth + (hoopsStartingWidth-hoopEndingWidth)*15.0f/(15.0f+score),i);
            }
            hoops.update(delta);

            if (settings.getBackground() != Settings.BACKGROUND_COLORS) {
                floor1.setVelocity(hoopPhysicalVelocity);
                floor1.update(delta);

                floor2.setVelocity(hoopPhysicalVelocity);
                floor2.update(delta);
            }

            int hoopHitCount = 0;
            int zHitCount = 0;
            int xHitCount = 0;
            int hoopHitIndex=0;
            for (int i = 0; i < hoops.getQuantity(); i++) {
                if ((hoops.getPhysicalZ(i)-playerDistance)/hoopPhysicalVelocity>0.15 && hoopObtained[i]){
                    hoopObtained[i] = false;
                }

                if ((hoops.getPhysicalZ(i)-playerDistance)/hoopPhysicalVelocity<=0.1 && hoops.getPhysicalZ(i)-playerDistance >= 0 && Math.abs(hoops.getScreenPositionX(i)-playerPosX)<=hoops.getPixelWidth(i)/2 - playerRadius && !hoopObtained[i]){
                    hoopObtained[i] = true;
                    hoopMissed[i] = false;

                    if (!renderStar || i!=starIndex) {
                        animateValue.play(scoreAnimationID);
                        Assets.playSound(Assets.gothoop);
                        scoreAchievedPosition = (int) playerPosX;
                        score += 1;
                    } else {
                        animateValue.play(starAnimationID);
                        scoreAchievedPosition = width/2;
                        starObtained = true;
                        score ++;
                        starsObtained++;
                        if (starsObtained >= settings.getStarsObtained(settings.getBackground())){
                            settings.setStarsObtained(starsObtained,settings.getBackground());
                        }
                    }
                }

                if ((hoops.getPhysicalZ(i)-playerDistance)/hoopPhysicalVelocity>0.3 && !hoopMissed[i]){
                    hoopMissed[i] = true;
                }

                if ((hoops.getPhysicalZ(i)-playerDistance)/hoopPhysicalVelocity<0 && (hoops.getPhysicalZ(i)-playerDistance)/hoopPhysicalVelocity>=-0.1 && hoopMissed[i]){
                    hoopMissed[i] = false;
                    vibrate(50);
                    health--;
                }


                if (Math.abs(hoops.getPhysicalZ(i)-playerDistance)/hoopPhysicalVelocity<=0.15 && Math.abs(hoops.getScreenPositionX(i)-playerPosX)<=hoops.getPixelWidth(i)/2){
                    hoopHitCount++;
                }

                if (Math.abs(hoops.getPhysicalZ(i)-playerDistance)/hoopPhysicalVelocity<=0.15){
                    zHitCount++;
                    hoopHitIndex = i;
                }

                if (Math.abs(hoops.getScreenPositionX(i)-playerPosX)<=hoops.getPixelWidth(i)/2){
                    xHitCount++;
                }
            }
            if (hoopHitCount>0){
                hoopHit=true;
            } else hoopHit=false;

            if (xHitCount>0){
                xHit=true;
            } else xHit=false;

            if (zHitCount>0){
                zHit=true;
//                Log.d("Game","xHit: "+xHit);
                if(!xHit) {
//                    Log.d("Game", "Hoops Position Y: "+hoops.getScreenPositionY(hoopHitIndex));
//                    Log.d("Game", "Player Position Y: "+playerPosY);
                }
            } else zHit=false;
        }

        if (starObtained && !animateValue.isPlaying(starAnimationID)){

        }
        if (starTimer <= 3){
            starTimer += delta;
        } else {
            starObtained = false;
        }

    }

    //δ=2arctan(d/2D)
//    (hoopQuantity+(i-1))%hoopQuantity
//

    @Override
    public void render(Painter g) {

        g.setColor(0xFFDDDDDD);
        g.fillRect(0, 0, width, height);

        int horizonAdjustment = 40;

        if (background.getImage().getHeight() > horizon+horizonAdjustment) {
            g.drawImage(background.getImage(), 0, 0);
        } else {
            g.drawImage(background.getImage(), 0, 0, width, (int) (horizon + horizonAdjustment));
        }

        if (settings.getBackground() == Settings.BACKGROUND_COLORS){
            g.drawImage(Assets.colorsbackground, (int) animateValue.getValue(colorsBackgroundID),-5,width,height+10);
            g.drawImage(Assets.colorsbackground, (int) animateValue.getValue(colorsBackgroundID) - width,-5,width,height + 10);
        }


        if (settings.getBackground() != Settings.BACKGROUND_COLORS) {
            g.setColor(background.getColor());
            g.fillRect(0, (int) horizon + horizonAdjustment, width, (int) (height - (horizon + horizonAdjustment)));

            floor1.render(g);
            floor2.render(g);
        }

        g.drawImage(background.getDistantfloor(), 0, (int) horizon + horizonAdjustment, width, 100);

        if (settings.getBackground() == Settings.BACKGROUND_MOON){
            g.setColor(Color.BLACK);
            g.fillRect(0,(int) horizon + horizonAdjustment - 40, width, 40);
        }
        if (renderStar && !hoops.isCreated(starIndex)){
            renderStar = false;
        }

        g.setColor(0xFF00FF00);
        for (int i = 0; i<hoops.getQuantity();i++){
            if(!hoops.isCreated(i)){
                if(!renderStar && intArrayContains(background.starLocations,score)){
                    renderStar = true;
                    starIndex = i;
                }

            }

            if (hoops.getPhysicalZ(i) >= playerDistance && hoops.isCreated(i)) {
                renderHoops(g, i);
            }

        }

        g.setColor(0xFFFF0000);
        if (settings.getPlayer()==Settings.PLAYER_ASTEROID) {
            float angle;
            float opp = playerPosY - horizon;
            float adj = -(width/2 - playerPosX);
            angle = (float) (atan(opp/adj));

            if (playerPosX == width/2)
                angle = PI;
            else if (playerPosX < width/2)
                angle -= PI/2.0f;
            else
                angle += PI/2.0f;

            g.rotateCanvas(angle, (int) playerPosX,(int) playerPosY);
            int fireSize = (int) ((30f / 11f) * 2*playerRadius);
            int fireTop = (int) ((playerPosY) - ((playerRadius / 55f) * 230));
            Assets.astroidFire.render(g, (int) (playerPosX - fireSize / 2), fireTop, fireSize, fireSize);
            g.restoreCanvas();

        }
        g.drawImage(player.getImage(), (int) (playerPosX - playerRadius), (int) (playerPosY - playerRadius), (int) (2 * playerRadius), (int) (2 * playerRadius));

        g.setColor(0xFF00FF00);
        for (int i = 0; i<hoops.getQuantity();i++){
            if (hoops.getPhysicalZ(i) < playerDistance && hoops.isCreated(i)) {
                if (!(renderStar && i==starIndex && hoopObtained[starIndex])){
                    renderHoops(g,i);
                }

            }

        }

        g.setColor(0xFFFFFF00);
        g.setTextSize(80);
        g.setTypeface(Assets.hobo);
        g.drawString(""+score, (width) - starCount* 60 ,height - 180);

        int starWidth;

        if (animateValue.isPlaying(scoreAnimationID)) {
            g.setTextSize(60);
            g.setColor(Color.argb(256 + (int) animateValue.getValue(scoreAnimationID), 255, 255, 0));
            g.drawString("+1", scoreAchievedPosition, (int) (playerPosY + 0.85f * animateValue.getValue(scoreAnimationID)));
        }
        if (animateValue.isPlaying(starAnimationID)) {
            starFlash = true;
            g.setColor(Color.argb(200, 255, 255, 0));
            starWidth = (int) (100 - 2.0f * animateValue.getValue(starAnimationID) / 3.0f);
            g.drawImage(Assets.star, (width - starWidth) / 2, (int) (playerPosY + animateValue.getValue(starAnimationID) - starWidth/2), starWidth, starWidth);
        }
        if (starFlash && !animateValue.isPlaying(starAnimationID)){
            starFlash = false;
            animateValue.play(highScoreFlashID);
        }
        if (!failed && animateValue.isPlaying(highScoreFlashID)){
            starWidth = 200 + 3*(int) animateValue.getValue(highScoreFlashID);
            g.setColor(Color.argb(128 - (int) (animateValue.getValue(highScoreFlashID)/2),255,255,255));
            g.drawImage(Assets.star, (width - starWidth) / 2, (int) (playerPosY -300 - starWidth/2), starWidth, starWidth);
            g.setColor(Color.WHITE);
            starWidth = 300;
            g.drawImage(Assets.star,(width - starWidth) / 2, (int) (playerPosY -300 - starWidth/2), starWidth, starWidth);
        }

        g.setColor(0xFFFFFFFF);
        for (int i = 0; i < starCount; i++) {
            if (starCount-i>starsObtained) {
                g.drawImage(Assets.emptystar,  (width) - (i+1) * 60, (height - 160), 55, 55);
            } else {
                g.drawImage(Assets.star, (width) - (i+1) * 60,  (height - 160), 55, 55);
            }
        }

        for (int i = 0; i < maxHealth; i++) {
            if (!(maxHealth-i>health)) {
                g.drawImage(Assets.heart,  (width) - (i+1) * 60, (height - 80), 55, 55);
            }
        }

        g.setColor(0xFFFFFF00);
        if (menuOpen){
            g.drawImage(Assets.panel,-30,-30,330,height+60);
            g.drawButton(exitx);
            g.drawButton(inMenuMenu);
            g.drawButton(inMenuRestart);
            g.drawButton(inMenuSettings);
        } else {
            g.drawButton(menu);
        }

        if (showTip){
            paused = true;
            Tips.render(g);
        }

        if (failed){
            g.drawButton(restartButton);
            g.drawButton(menuStateButton);
            if (newBackground) {
                g.drawButton(nextLevelButton);
                g.setColor(0xFF7FC578);
                g.drawString("Next Level",nextLevelButton.x + 30, nextLevelButton.y + nextLevelButton.height - 40, nextLevelButton.width - 60);
            }

            g.setTypeface(Assets.hobo);
            g.setColor(Color.YELLOW);

            g.setTextSize(50);
            g.drawCenteredString("High Score: "+settings.getHighScore(),width/2,height/4-30);

            g.setTextSize(80);
            g.drawCenteredString("Score: "+score,width/2,height/4+70);

            for (int i = 0; i < starCount; i++) {
                if (starCount-i>starsObtained) {
                    g.drawImage(Assets.emptystar,  (width+starCount*60)/2 - (i+1) * 60, 20, 55, 55);
                } else {
                    g.drawImage(Assets.star, (width+starCount*60)/2 - (i+1) * 60, 20, 55, 55);
                }
            }
            long currentTime = System.currentTimeMillis();
            if (!timerStarted){
                timerStarted = true;
                timeStamp = System.currentTimeMillis();
            }

            int scoreTime =0;
            int starTime = 0;
            int backgroundTime = 0;
            int newStars = settings.getStarsObtained(settings.getBackground()) - initialStarsObtained;

            if (score > initialHighScore){
                scoreTime = 3000;
            }
            if (settings.getStarsObtained(settings.getBackground()) > initialStarsObtained){
                starTime = 2000 + newStars*500;
            }
            int animationTime = 1600;
            if (nextBackgroundStars > initialTotalStarsObtained && nextBackgroundStars <= settings.getTotalStars()){
                newBackground = true;
                backgroundTime = 1000 + animationTime + 1000;
            }

            animateValue.playOnce(endScreenOverlayID,1);
            if (currentTime <= timeStamp + scoreTime + starTime + backgroundTime) {
                if (animateValue.isPlaying(endScreenOverlayID)) {
                    g.setColor(Color.argb((int) (animateValue.getValue(endScreenOverlayID)), 255, 255, 255));
                } else {
                    g.setColor(Color.WHITE);
                }
            } else {
                animateValue.playOnce(endScreenOverlayID,2);
                g.setColor(Color.argb(255 - (int) (animateValue.getValue(endScreenOverlayID)), 255, 255, 255));

                menuStateButton.setActive(true);
                restartButton.setActive(true);
                nextLevelButton.setActive(newBackground);
            }

            g.drawImage(Assets.endscreen, 0, 0, width, height);


            g.setTextSize(100);
            g.setColor(Color.YELLOW);
            if (currentTime < timeStamp + scoreTime){
                if (currentTime <= timeStamp + 2000) {
                    animateValue.playOnce(endScreenID, 1);
                    g.drawCenteredString("New High Score: " + score, width / 2, (int) animateValue.getValue(endScreenID));
                } else {
                    animateValue.playOnce(endScreenID, 2);
                    g.drawCenteredString("New High Score: " + score, width/2, (int) animateValue.getValue(endScreenID)- 1020+height/2);
                }

                if (currentTime >= timeStamp + 980){
                    animateValue.playOnce(highScoreFlashID,1);
                    g.setTextSize(100 + animateValue.getValue(highScoreFlashID)/3);
                    g.setColor(Color.argb(255-(int) animateValue.getValue(highScoreFlashID),255,255,0));
                    g.drawCenteredString("New High Score: " + score, width/2, height/2+(int)(animateValue.getValue(highScoreFlashID)/6));
                }
            }

            if (currentTime >= timeStamp + scoreTime && currentTime < timeStamp + scoreTime + starTime){
                int starSize = 150;

                if (currentTime <= timeStamp + scoreTime + 1000+newStars*500){
                    animateValue.playOnce(endScreenID,3);

                    for (int i = 0; i < starCount; i++) {
                        if (starCount - i > initialStarsObtained) {
                            g.drawImage(Assets.emptystar, (width + starCount * starSize) / 2 - (i + 1) * starSize, (int) animateValue.getValue(endScreenID) - starSize/2, starSize - 15, starSize - 15);
                        } else {
                            g.drawImage(Assets.star, (width + starCount * starSize) / 2 - (i + 1) * starSize, (int) animateValue.getValue(endScreenID) - starSize/2, starSize - 15, starSize - 15);
                        }
                    }
                } else {
                    animateValue.playOnce(endScreenID,4);

                    for (int i = 0; i < starCount; i++) {
                        if (starCount - i > starsObtained) {
                            g.drawImage(Assets.emptystar, (width + starCount * starSize) / 2 - (i + 1) * starSize, (int) animateValue.getValue(endScreenID) - starSize/2- 1020+height/2, starSize - 15, starSize - 15);
                        } else {
                            g.drawImage(Assets.star, (width + starCount * starSize) / 2 - (i + 1) * starSize, (int) animateValue.getValue(endScreenID) - starSize/2- 1020+height/2, starSize - 15, starSize - 15);
                        }
                    }
                }

                int oldStarSize = starSize;
                for (int i = 0; i < newStars; i++) {
                    if (currentTime > timeStamp + scoreTime + 1000+i*500 && currentTime <= timeStamp + scoreTime + 1000+(i+1)*500){
                        animateValue.playOnce(endStarObtainedID,i);
                        starSize = (int) animateValue.getValue(endStarObtainedID);
                        g.drawImage(Assets.star, (width - (starCount - 1) * oldStarSize - starSize) / 2 + (initialStarsObtained + i) * oldStarSize, height/2 - starSize/2, starSize - 15, starSize - 15);
                    }

                    if (currentTime > timeStamp + scoreTime + 1000+(i+1)*500 && currentTime <= timeStamp + scoreTime + 1000+(newStars)*500){
                        g.drawImage(Assets.star, (width - starCount * oldStarSize) / 2 + (initialStarsObtained + i) * oldStarSize, height/2 - oldStarSize/2, oldStarSize - 15, oldStarSize - 15);
                    }
                }
            }

            int thumbnailWidth = 400;
            int thumbnailHeight = (int) ((9.0f/16.0f)*thumbnailWidth);

            int lockWidth = 120, lockHeight = 155;

            int nextLevel;
            switch (settings.getBackground()){
                case Settings.BACKGROUND_NY:
                    nextLevel = Settings.BACKGROUND_COLORS;
                    break;
                case Settings.BACKGROUND_COLORS:
                    nextLevel = Settings.BACKGROUND_MOON;
                    break;
                case Settings.BACKGROUND_MOON:
                    nextLevel = Settings.BACKGROUND_SATURN;
                    break;
                default:
                    nextLevel = Settings.BACKGROUND_COLORS;
                    break;
            }

            if (currentTime >= timeStamp + scoreTime + starTime && currentTime < timeStamp + scoreTime + starTime + backgroundTime){
                if (currentTime >= timeStamp + scoreTime + starTime + 1000 + animationTime - 300){
                    animateValue.playOnce(highScoreFlashID,2);
                    int backgroundFlashWidth = thumbnailWidth + (int) (5.0f*animateValue.getValue(highScoreFlashID));
                    int backgroundFlashHeight = (int) (((float)thumbnailHeight/(float)thumbnailWidth)*(float)backgroundFlashWidth);
                    g.setColor(Color.argb(128 - (int) (animateValue.getValue(highScoreFlashID)/2),255,255,255));
                    g.drawImage(Background.getThumbnail(nextLevel),(width - backgroundFlashWidth)/2,(height - backgroundFlashHeight)/2,backgroundFlashWidth,backgroundFlashHeight);

                }

                if (currentTime <= timeStamp + scoreTime + starTime + 1000 + animationTime) {
                    animateValue.playOnce(endScreenID, 5);
                    g.setColor(Color.WHITE);
                    g.drawImage(Background.getThumbnail(nextLevel), width/2 - thumbnailWidth/2, (int)animateValue.getValue(endScreenID) - thumbnailHeight/2, thumbnailWidth,thumbnailHeight);
                    g.drawCenteredString("New Level Unlocked", width/2, (int)animateValue.getValue(endScreenID) + thumbnailHeight/2 + 120,thumbnailWidth);
                    g.setColor(0xAA000000);
                    g.fillRect(width/2 - thumbnailWidth/2, (int)animateValue.getValue(endScreenID) - thumbnailHeight/2, thumbnailWidth,thumbnailHeight);
                    if (currentTime < timeStamp + scoreTime + starTime + 1000){
                        g.setColor(0xFFFFFFFF);
                        g.drawImage(Assets.lockImage, width/2 - lockWidth/2, (int)animateValue.getValue(endScreenID) - lockHeight/2, lockWidth,lockHeight);
                    } else {
                        g.setColor(0xFFFFFFFF);
                        if (!isLockAnimationPlaying){
                            isLockAnimationPlaying = true;
                            Assets.unlockAnimation.start();
                        }
                        Assets.unlockAnimation.render(g,width/2 - lockWidth/2,height/2 - lockHeight/2,lockWidth,lockHeight);
                    }
                } else {
                    animateValue.playOnce(endScreenID, 6);

                    g.setColor(Color.WHITE);
                    g.drawImage(Background.getThumbnail(nextLevel), width/2 - thumbnailWidth/2, (int)animateValue.getValue(endScreenID) - thumbnailHeight/2- 1020+height/2, thumbnailWidth,thumbnailHeight);
                    g.drawCenteredString("New Level Unlocked", width/2, (int)animateValue.getValue(endScreenID) + thumbnailHeight/2 + 120 - 1020+height/2,thumbnailWidth);
                }
            }
        }



//        g.drawString("Hoop Hit: "+Boolean.toString(hoopHit),0,40);
//        g.drawString("Hoop Same Z: "+Boolean.toString(zHit),0,60);
//        g.drawString("Hoop Same X: "+Boolean.toString(xHit),0,80);
    }

    public void renderHoops(Painter g, int i){
        float screenX = (hoops.getScreenPositionX(i));
        float screenY = (hoops.getScreenPositionY(i));
        if (!renderStar || i!=starIndex) {
            g.setColor(Color.GREEN);
            g.drawOval(screenX, screenY, hoops.getPixelWidth(i) / 2, hoops.getPixelWidth(i) / 2, (hoops.getPixelWidth(i) / (5 * hoops.getPhysicalWidth(i))));
        } else {
            g.setColor(Color.YELLOW);
            g.drawOval(screenX, screenY, hoops.getPixelWidth(i) / 2, hoops.getPixelWidth(i) / 2, (hoops.getPixelWidth(i) / (7 * hoops.getPhysicalWidth(i))));
            g.drawImage(Assets.star,screenX - (int)hoops.getPixelWidth(i)/4,screenY - (int)hoops.getPixelWidth(i)/4,(int)hoops.getPixelWidth(i)/2,(int)hoops.getPixelWidth(i)/2);
        }
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (menu.isPressed(e,scaledX,scaledY) && !menuOpen){
            paused = true;
            menuOpen = true;
        }

        if (exitx.isPressed(e,scaledX,scaledY) && menuOpen){
            menuOpen = false;
            paused = false;
        }

        if (inMenuMenu.isPressed(e,scaledX,scaledY) && menuOpen){
            setCurrentState(new MenuState());
        }

        if (inMenuRestart.isPressed(e,scaledX,scaledY) && menuOpen){
            setCurrentState(new PlayState());
        }

        if (inMenuSettings.isPressed(e,scaledX,scaledY) && menuOpen){
            Tips.tipID = Tips.NO_TIP;
            setCurrentState(new SettingsState().setPreviousState(this));
        }

        if (menuStateButton.isPressed(e,scaledX,scaledY)){
            Tips.tipID = Tips.NO_TIP;
            setCurrentState(new MenuState());
        }

        if (restartButton.isPressed(e,scaledX,scaledY)){
            PlayState playState = new PlayState();
            setCurrentState(playState);
        }

        if (nextLevelButton.isPressed(e,scaledX,scaledY)){
            setCurrentState(new BackgroundSelectionState());
        }


        if (e.getAction()==MotionEvent.ACTION_DOWN){
            if (menuOpen && scaledX >= 330 && !failed){
                menuOpen = false;
                paused = false;
            }
            if (scaledX>1080 && scaledY>520){
//                slowMotion = true;
            }
            return true;
        }

        if (e.getAction()==MotionEvent.ACTION_UP){
            if (scaledX>1080 && scaledY>520){
                slowMotion = false;
            }
            if (showTip){
                showTip = false;
                paused = false;
            }
            return true;
        }

        if (e.getAction()==MotionEvent.ACTION_MOVE){

            return true;
        }


        return false;
    }

    @Override
    public void onSensorChanged(float x, float y, float z) {
        playerAcceleration = Math.signum(x)*y*500;
//        playerVelX = y*300;
    }

    private boolean intArrayContains (int[] array, int value){
        boolean contains = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value){
                contains = true;
            }
        }
        return contains;
    }


}

//    public void resolveHoops(float delta){
//        hoopSpawnFrequency = (float) (2.6 - (2-Math.pow(speedIncreaseConstant, -(score / 10 - speedIncreaseLogConstant))));
//        //(3-(1.05^-(-log(1.05,3))))
//
//        currentHoopLifeSpan = hoopQuantity*hoopSpawnFrequency;
//        hoopPhysicalVelocity = hoopPhysicalStartingDistance/currentHoopLifeSpan;
//
//
//        hoopSpawnTracking += delta;
//        hoopSpawnTracking = hoopSpawnTracking%(currentHoopLifeSpan);
//        for (int i=0; i<hoopQuantity;i++){
//            if (hoopSpawnTracking >= i*hoopSpawnFrequency && !hoopCreated[i]){
//                hoopCreated[i] = true;
//                hoopObtained[i] = false;
//                hoopLifeSpan[i] = currentHoopLifeSpan;
//                hoopRadii[i] = 0;
//                float constant = 4;
////                hoopStartingPosition[i] = hoopStartingPosition[(hoopQuantity+(i-1))%hoopQuantity]-(constant/2)*hoopSpawnFrequency+RandomNumberGenerator.getRandFloat(constant*hoopSpawnFrequency);
//                hoopStartingPosition[i] = RandomNumberGenerator.getRandFloat(playerMovementArea);
//                if (hoopStartingPosition[i] > playerMovementArea){
//                    hoopStartingPosition[i] = playerMovementArea-RandomNumberGenerator.getRandFloat((constant/2)*hoopSpawnFrequency);
//                }
//                if (hoopStartingPosition[i] < 0){
//                    hoopStartingPosition[i] = RandomNumberGenerator.getRandFloat((constant/2)*hoopSpawnFrequency);
//                }
//                individualTracking[i] = 0;
//            }
//            if (hoopCreated[i]){
//                hoopDistance[i]= (float) (playerDistance/2 + (hoopPhysicalStartingDistance+score/4)*(hoopLifeSpan[i]-individualTracking[i])/(hoopLifeSpan[i]));
//                hoopRadii[i] = (width/(fov))*((float)(2*atan2(hoopPhysicalRadius,2*hoopDistance[i])));
//                hoopScreenPositionX[i] = width/2 + (width/(fov))*((float)(2*atan2(hoopStartingPosition[i]-playerMovementArea/2,2*hoopDistance[i])));
//                hoopScreenPositionY[i] = horizon + (width/(fov))*((float)(2*atan2(playerPhysicalPosY,2*hoopDistance[i])));
//                individualTracking[i] += delta;
//            }
//            if (individualTracking[i]>=hoopLifeSpan[i]){
//                hoopCreated[i] = false;
//            }
//
//            if (Math.abs(hoopDistance[i]-playerDistance)<=0.1*playerDistance && Math.abs(hoopScreenPositionX[i]-playerPosX)<=hoopRadii[i] && !hoopObtained[i]){
//                hoopObtained[i] = true;
//                score += 1;
//            }
//        }
//    }