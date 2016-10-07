package forceEditor;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import forceEditor.input.*;

public class ForceEditor {
	private long window;

	public double scale = 1;

	private static ForceEditor instance;

	private GameState gameState;

	public ForceEditor() {
		this.gameState = new GameState(this);
	}

	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}

		int width = 800;
		int height = 600;

		String title = "Force Editor";

		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden
													// after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be
													// resizable
		glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);

		window = glfwCreateWindow(width, height, title, NULL, NULL);

		if (window == NULL) {
			throw new RuntimeException("Failed to create GLFW window.");
		}

		glfwSetKeyCallback(window, new Keyboard());
		glfwSetCursorPosCallback(window, new CursorPos());
		glfwSetMouseButtonCallback(window, new MouseButton());
		glfwSetScrollCallback(window, new ScrollCallback());

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
	}

	public void start() {
		try {
			init();
			loop();
			glfwFreeCallbacks(window);
			glfwDestroyWindow(window);
		} finally {
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		}

	}

	private void loop() {
		GL.createCapabilities();
		glfwMakeContextCurrent(window);

		glClearColor(1.0f, 1.0f, 1.0f, 0f);
		glOrtho(-getWindowWidth() / 2f, getWindowWidth() / 2f, -getWindowHeight() / 2f, getWindowHeight() / 2f, -1, 1);

		while (!glfwWindowShouldClose(window)) {
			TimeManager.update();
			if (TimeManager.getDelta() < 1) {
				update();
				render();
			}
		}
	}

	private void update() {
		glfwPollEvents();

		if (Keyboard.keys[GLFW_KEY_ESCAPE]) {
			glfwDestroyWindow(window);
		}

		gameState.update();
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glEnable(GL_SCISSOR_TEST);

		glScissor(0, getWindowHeight() / 10, getWindowWidth(), getWindowHeight() * 9 / 10);
		glPushMatrix();
		glScaled(scale, scale, 1);
		gameState.render();
		glPopMatrix();
		glScissor(0, 0, getWindowWidth(), getWindowHeight() / 10);
		gameState.renderUI();

		glDisable(GL_SCISSOR_TEST);

		glfwSwapBuffers(window);
	}

	public int getWindowWidth() {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window, width, height);
		return width.get();
	}

	public int getWindowHeight() {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window, width, height);
		return height.get();
	}

	public GameState getGameState() {
		return gameState;
	}

	public static ForceEditor getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		instance = new ForceEditor();
		instance.start();
	}

}