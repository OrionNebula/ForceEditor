package forceEditor;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWvidmode.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import forceEditor.input.*;

public class ForceEditor {
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private GLFWCursorPosCallback cursorPosCallback;
	private GLFWScrollCallback scrollCallback;

	public double scale = 1;
	
	private long window;
	private static ByteBuffer vidmode;

	private static ForceEditor instance;
	private GameState gameState;
	
	public void start()
	{
		errorCallback = errorCallbackPrint(System.err);
		keyCallback = new Keyboard();
		mouseButtonCallback = new MouseButton();
		cursorPosCallback = new CursorPos();
		scrollCallback = new ScrollCallback();

		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

		if (glfwInit() != GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW.");

		vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		int width = width(vidmode);
		int height = height(vidmode);

		initialize(width, height);

		glfwSetKeyCallback(window, keyCallback);
		glfwSetMouseButtonCallback(window, mouseButtonCallback);
		glfwSetCursorPosCallback(window, cursorPosCallback);
		glfwSetScrollCallback(window, scrollCallback);

		glfwSetWindowPos(window, 0, 0);
		glfwSetCursor(window, NULL);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
		
		runLoop();

		glfwDestroyWindow(window);
		keyCallback.release();
		mouseButtonCallback.release();
		cursorPosCallback.release();
		glfwTerminate();
		errorCallback.release();
	}

	public static ByteBuffer getVidMode() {
		return vidmode;
	}

	private void initialize(int width, int height) {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		glfwWindowHint(GLFW_DECORATED, GL_FALSE);

		window = glfwCreateWindow(width, height, "Force Editor", NULL, NULL);

		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
	}

	private void runLoop()
	{
		GLContext.createFromCurrent();

		glClearColor(1.0f, 1.0f, 1.0f, 0f);
		glOrtho(-width(getVidMode())/2f, width(getVidMode())/2f, -height(getVidMode())/2f, height(getVidMode())/2f, -1, 1);

		while (glfwWindowShouldClose(window) == GL_FALSE)
		{
			TimeManager.update();
			if (TimeManager.getDelta() < 1) {
				update();
				render();
			}
		}
	}

	private void update() {
		glfwPollEvents();

		if (Keyboard.keys[GLFW_KEY_ESCAPE])
			glfwSetWindowShouldClose(window, GL_TRUE);
	
		gameState.update();
	}

	private void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glEnable(GL_SCISSOR_TEST);
		
		glScissor(0, getWindowHeight()/10, getWindowWidth(), getWindowHeight()*9/10);
		glPushMatrix();
		glScaled(scale, scale, 1);
		gameState.render();
		glPopMatrix();
		glScissor(0, 0, getWindowWidth(), getWindowHeight()/10);
		gameState.renderUI();
		
		glDisable(GL_SCISSOR_TEST);
		
		glfwSwapBuffers(window);
	}

	public static int getWindowWidth()
	{
		return width(getVidMode());
	}
	
	public static int getWindowHeight()
	{
		return height(getVidMode());
	}
	
	public GameState getGameState()
	{
		return gameState;
	}
	
	
	public static ForceEditor getInstance()
	{
		return instance;
	}
	
	public static void main(String[] args)
	{
		instance = new ForceEditor();
		instance.gameState = new GameState();
		instance.start();
	}

}