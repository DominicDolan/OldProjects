package renderengine;

import models.TexturedModel;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import renderers.framebuffer.ContrastShader;
import renderers.framebuffer.Fbo;
import renderers.framebuffer.FrameRenderer;
import states.State;
import statics.G;
import statics.Loader;

public class DisplayManager {
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;

	static long updateDurationMillis = 0;
	static long sleepDurationMillis = 0;

	static ContrastShader shader;
	static FrameRenderer renderer;

	static Fbo fbo;

	private static TexturedModel quad;

	public static void createDisplay(){
		
		ContextAttribs attribs = new ContextAttribs(3,2)
				.withForwardCompatible(true)
				.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat().withDepthBits(24), attribs);
			Display.setTitle("Game Test");
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}

	public static void initDisplay(){
		fbo = new Fbo(WIDTH, HEIGHT);
//		shader = new ContrastShader();
		renderer = new FrameRenderer(fbo);
		quad = Loader.loadTexturedQuad(-1, 1, 1, -1);
		quad.setTexture(fbo.getColourTexture());
	}
	
	public static void updateDisplay(){

		long beforeUpdateRender = System.nanoTime();
		long deltaMillis = sleepDurationMillis + updateDurationMillis;

		fbo.bindFrameBuffer();
		prepare();

		updateAndRender(deltaMillis, G.g);

//		fbo.unbindFrameBuffer();
		fbo.resolveToScreen();

		quad.setTexture(fbo.getColourTexture());
		renderer.render(quad);

		updateDurationMillis = (System.nanoTime() - beforeUpdateRender) / 1000000L;
		sleepDurationMillis = Math.max(2, 17 - updateDurationMillis);

		try {
			Thread.sleep(sleepDurationMillis);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Display.sync(FPS_CAP);
		Display.update();
	}
	
	public static void closeDisplay(){
		
		Display.destroy();
		
	}


	public static void prepare(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.95f, 0.95f, 0.95f, 1);
	}


	private static void updateAndRender(long delta, Painter g) {
		float del = (float)delta/1000f;
		G.update(del);
		State.currentState.update(del);
		State.currentState.render(g);
	}

	private static void renderFrame(int texture){
		start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.render(quad);
		end();
	}


	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		shader.start();
	}

	private static void end(){
		shader.stop();
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
