package com.games.hoops.hoops;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by domin on 4 Jun 2016.
 */
public class MenuState extends State {
    ZPlaneAnimation floor;
    Button playButton, backgroundButton, playerButton;
    Background background;
    Player player;
    int panelWidth = width/2+100,panelHeight = height/3+80;
    int playButtonSize = 180;
    Settings settings;
    AnimateValue animateValue;
    int colorsBackgroundID;
    private Button settingsButton;
    private Button storeButton;
    private Button statsButton;
    private Button xButton, tickButton;
    Bitmap currentPlayer, nextPlayer, previousPlayer, overNext, overPrevious;
    RectF playerSelectionArea;
    private boolean playerSelectionStarted = false;
    private int touchX, touchY;
    AnimateValue playerAnimation;
    int playerAnimationDownID, playerAnimationUpID;
    private float animationRange;
    private boolean showPanel = false;

    @Override
    public void init() {
        settings = MainActivity.sGame.getSettings();

        background = new Background(settings);
        player = new Player(settings);

        MainActivity.showAd(!MainActivity.sGame.getSettings().isPremium());

        tickButton = new Button(-100,-100,100,100, Assets.tickIcon);
        xButton = new Button(-100,-100,100,100, Assets.xIcon);
        tickButton.setActive(false);
        xButton.setActive(false);

        floor = new ZPlaneAnimation();
        floor.setHorizon(-40);
        floor.setIsStarted(true);
        floor.setFlat(true);
        floor.setXbounds(-40, 40);
        floor.setAltitude(10);
        floor.setImages(background.floorTexture1.getFloorTextures());
        floor.init(100, 6, background.floorTexture1.getWavelength());

        int border = 40;
        int thumbnailWidth = panelWidth/2-2*border;
        int alignmentHeight = height/2 - 40;
        int thumbnailHeight = (int) (((float)height/(float)width)*(thumbnailWidth));
        playButton = new Button((width-playButtonSize)/2,alignmentHeight-playButtonSize/2,playButtonSize,playButtonSize,Assets.playbutton);
        playButton.setSound(Assets.playButtonSound);
        backgroundButton = new Button((4*width)/5 - thumbnailWidth/2, alignmentHeight-thumbnailHeight/2,thumbnailWidth,thumbnailHeight,background.getThumbnail());
        backgroundButton.setSound(Assets.buttonSelect);
        int playerSize = (int)(1.3f*backgroundButton.height);
        playerButton = new Button((width)/5-playerSize/2,alignmentHeight-playerSize/2,playerSize,playerSize,player.getImage());
        playerButton.setSound(Assets.buttonSelect);

        playerAnimation = new AnimateValue();

        setPlayerImages();
        playerSelectionArea = new RectF(playerButton.x,playerButton.y - 0.75f*0.7f*playerSize,playerButton.x + playerSize,playerButton.y + (0.75f*0.7f+1)*playerSize);

        animationRange = playerButton.y - playerSelectionArea.top;
        playerAnimationDownID = playerAnimation.addLinearAnimation(0, animationRange,0.4f);
        playerAnimationUpID = playerAnimation.addLinearAnimation(0, -(animationRange),0.4f);
        playerAnimation.setLooping(false, playerAnimationUpID);
        playerAnimation.setLooping(false, playerAnimationDownID);

        int buttonSize = 120;
        settingsButton = new Button(3*width/5-buttonSize/2,height - 30 - buttonSize,buttonSize,buttonSize,Assets.settingsButtonIcon);

        buttonSize = Assets.storeIcon.getHeight();
        storeButton = new Button(3*width/4 - buttonSize/2, height - 30 - buttonSize, buttonSize, buttonSize, Assets.storeIcon);

        buttonSize = 120;
        statsButton = new Button((int)(0.9f*width) - buttonSize/2, height - 30 - buttonSize, buttonSize, buttonSize, Assets.menubutton);
        animateValue = new AnimateValue();
        colorsBackgroundID = animateValue.addLinearAnimation(0,width,10);
        animateValue.setLooping(true,colorsBackgroundID);

        setCurrentMusic("music.wav");
    }

    @Override
    public void update(float delta) {
        backgroundButton.setImage(background.getThumbnail());
        playerButton.setImage(player.getImage());

        floor.setVelocity(6);
        floor.update(delta);

        Assets.astroidFire.update(delta);
        playerAnimation.update(delta);
        animateValue.update(delta);
    }

    @Override
    public void render(Painter g) {
        g.setColor(background.getColor());
        g.fillRect(0, 0, width, height);
        if (settings.getBackground() != Settings.BACKGROUND_COLORS) {
            floor.render(g);
        } else {
            g.drawImage(background.getImage(),0,0,1280,720);
        }

        if (settings.getBackground() == Settings.BACKGROUND_COLORS){
            g.drawImage(Assets.colorsbackground, (int) animateValue.getValue(colorsBackgroundID),0,width,height);
            g.drawImage(Assets.colorsbackground, (int) animateValue.getValue(colorsBackgroundID) - width,0,width,height);
        }

        g.drawImage(background.getDistantfloor(), 0, 0, width, 200);
//        g.drawImage(Assets.panel, (width - panelWidth) / 2, -backgroundButton.height, panelWidth, backgroundButton.height + panelHeight);
        int border = 40;
        g.drawImage(Assets.panel,backgroundButton.x-border,backgroundButton.y - border,backgroundButton.width+2*border, backgroundButton.height+2*border+60);
//        g.drawImage(Assets.panel,width/4 - backgroundButton.width/2-border,height/2 - backgroundButton.height/2 - border,backgroundButton.width+2*border, backgroundButton.height+2*border+60);
        g.drawButton(playButton);
        g.drawButton(backgroundButton);
//        g.drawButton(playerButton);
        g.drawButton(settingsButton);
        g.drawButton(storeButton);
//        g.drawButton(statsButton);

        g.setColor(Color.YELLOW);
        g.setTypeface(Assets.hobo);
        g.drawCenteredString("Hoops", width/2, 150, 460);

        float animationUp = playerAnimation.getValue(playerAnimationUpID);
        float animationDown = playerAnimation.getValue(playerAnimationDownID);
        float animation = animationUp + animationDown;

        float playerX = playerButton.x + playerButton.width/2;
        float playerY = playerButton.y + playerButton.height/2;
        float playerSize = playerButton.width;

        float previousPlayerSize = (1+0.3f*(animationUp/animationRange))*playerSize;
        float nextPlayerSize = (1-0.3f*(animationDown/animationRange))*playerSize;
        float currentPlayerSize = (1-0.3f*(Math.abs(animation)/animationRange))*playerSize;

        float previousPlayerY = playerY - playerSize/2 - 0.25f*0.7f*playerSize + animationUp + animationRange;
        float nextPlayerY = playerY + playerSize/2 + 0.25f*0.7f*playerSize + animationDown - animationRange;
        float currentPlayerY = playerY + animation;

        g.setColor(Color.argb(255 - (int)((-animationUp/animationRange)*255),255,255,255));
        g.drawCenteredImage(overPrevious, playerX, playerY - playerSize/2 - 0.25f*0.7f*playerSize, 0.7f*playerSize, 0.7f*playerSize);

        g.setColor(Color.argb(255 - (int)((animationDown/animationRange)*255),255,255,255));
        g.drawCenteredImage(overNext, playerX, playerY + playerSize/2 + 0.25f*0.7f*playerSize, 0.7f*playerSize, 0.7f*playerSize);

        g.setColor(Color.WHITE);
        g.drawCenteredImage(previousPlayer,playerX, previousPlayerY, previousPlayerSize,previousPlayerSize);
        g.drawCenteredImage(nextPlayer,    playerX, nextPlayerY,     nextPlayerSize,nextPlayerSize);
        g.drawCenteredImage(currentPlayer, playerX, currentPlayerY,  currentPlayerSize,currentPlayerSize);

        g.setTypeface(Assets.hobo);
        g.setColor(Color.WHITE);
        g.drawString("Backgrounds", backgroundButton.x, backgroundButton.y + backgroundButton.height+10, backgroundButton.width, 50);
//        g.drawCenteredString("Player", (width) / 4, backgroundButton.y + backgroundButton.height + 60);

        if (showPanel){
            drawPanelToast(g);
        }
    }

    @Override
    public boolean onTouch(MotionEvent e, int scaledX, int scaledY) {
        if (playButton.isPressed(e,scaledX,scaledY)){
            if (!settings.isLocked(settings.getPlayer())){
                showPanel = true;
                MainActivity.showAd(false);
            }else {
                PlayState playState = new PlayState();
                setCurrentState(playState);
            }
        }
        if (backgroundButton.isPressed(e,scaledX,scaledY)){
            BackgroundSelectionState backgroundSelectionState = new BackgroundSelectionState();
            setCurrentState(backgroundSelectionState);
        }
        if (playerButton.isPressed(e,scaledX,scaledY)){
            PlayerSelectionState playerSelectionState = new PlayerSelectionState();
            setCurrentState(playerSelectionState);
        }
        if (settingsButton.isPressed(e,scaledX,scaledY)){
            setCurrentState(new SettingsState());
        }

        if (storeButton.isPressed(e,scaledX,scaledY)){
            setCurrentState(new StoreState());
        }

        if (e.getAction() == MotionEvent.ACTION_DOWN && playerSelectionArea.contains(scaledX,scaledY)){
            playerSelectionStarted = true;
            touchX = scaledX;
            touchY = scaledY;
        }
        if (e.getAction() == MotionEvent.ACTION_UP && playerSelectionStarted){
            playerSelectionStarted = false;
            if (scaledY<touchY-30){
                int playerCount = player.getPlayerCount();
                settings.setPlayer((playerCount + (settings.getPlayer()+1))%(playerCount));
                playerAnimation.play(playerAnimationUpID);
                setPlayerImages();
            }
            if (scaledY>touchY+30){
                int playerCount = player.getPlayerCount();
                settings.setPlayer((playerCount + (settings.getPlayer()-1))%(playerCount));
                playerAnimation.play(playerAnimationDownID);
                setPlayerImages();
            }

        }

        if (tickButton.isPressed(e,scaledX,scaledY)){
            setCurrentState(new PlayerSelectionState());
        }
        if (xButton.isPressed(e,scaledX,scaledY)){
            showPanel = false;
            MainActivity.showAd(true);
        }
        return true;
    }

    @Override
    public void onSensorChanged(float x, float y, float z) {

    }

    public void setSettings(Settings settings){
        this.settings = settings;
    }

    public void setPlayerImages(){
        int playerCount = player.getPlayerCount();
        int currentPlayerID = settings.getPlayer();

        int nextPlayerID = (playerCount + (settings.getPlayer()+1))%(playerCount);
        int oNextPlayerID = (playerCount + (settings.getPlayer()+2))%(playerCount);

        int previousPlayerID = (playerCount + (settings.getPlayer()-1))%(playerCount);
        int oPreviousPlayerID = (playerCount + (settings.getPlayer()-2))%(playerCount);

        if (settings.isLocked(currentPlayerID)) {
            currentPlayer = player.getImage(currentPlayerID);
        } else {
            currentPlayer = Assets.lockedball;
        }

        if (settings.isLocked(nextPlayerID)) {
            nextPlayer = player.getImage(nextPlayerID);
        }else {
            nextPlayer = Assets.lockedball;
        }
        if (settings.isLocked(oNextPlayerID)) {
            overNext = player.getImage(oNextPlayerID);
        }else {
            overNext = Assets.lockedball;
        }

        if (settings.isLocked(previousPlayerID)) {
            previousPlayer = player.getImage(previousPlayerID);
        }else {
            previousPlayer = Assets.lockedball;
        }
        if (settings.isLocked(oPreviousPlayerID)) {
            overPrevious = player.getImage(oPreviousPlayerID);
        } else {
            overPrevious = Assets.lockedball;
        }
    }

    private void drawPanelToast(Painter g){
        int panelWidth = width/2;
        int panelHeight = height/2 + 70;
        int top = height/2 - height/4;
        int bottom = top + panelHeight;
        int left = width/2 - panelWidth/2;
        int right = left + panelWidth;
        int buttonSize = 220;

        xButton.set(left + 40,bottom - buttonSize/2,buttonSize,buttonSize);
        tickButton.set(right - buttonSize - 40,bottom-buttonSize/2,buttonSize,buttonSize);

        xButton.setActive(true);
        tickButton.setActive(true);

        g.drawImage(Assets.panel,left,top,panelWidth,panelHeight);

        g.setColor(Color.WHITE);
        g.setTypeface(Assets.hobo);
        if (settings.getPlayer()==Settings.PLAYER_ASTEROID) {
            int fireSize = (int) ((30f / 11f) * 200);
            int fireTop = top - (int) ((20f / 11f) * 230);
            Assets.astroidFire.render(g, width / 2 - fireSize / 2, fireTop, fireSize, fireSize);
        }
        g.drawCenteredImage(player.getImage(settings.getPlayer()), width/2, top, 200,200);
        g.drawCenteredString("This Skin is locked", width / 2, top + 170, panelWidth - 60);
        g.drawCenteredString("Do you want to unlock more Skins", width / 2, top + 220, panelWidth - 60);

        g.drawButton(xButton);
        g.drawButton(tickButton);
    }
}
