package states;

import renderengine.Painter;

/**
 * Created by domin on 4 Aug 2017.
 */
public abstract class LoadState extends State{
    private float waitTime = 0.2f;
    private float currentWait = 0;
    private boolean startLoading = false;
    private boolean finishedLoading = false;
    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        currentWait += delta;
        startLoading = currentWait > waitTime;
        if (finishedLoading) onFinish();

    }

    @Override
    public void render(Painter g) {
        renderLoadScreen(g);
        if (startLoading) {
            load();
            finishedLoading = true;
        }

    }

    public abstract void renderLoadScreen(Painter g);

    public abstract void load();

    public abstract void onFinish();
}
