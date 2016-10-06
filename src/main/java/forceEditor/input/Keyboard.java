package forceEditor.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard extends GLFWKeyCallback {

	public static boolean[] keys = new boolean[65535];
	
	public static int lastKey;
	public static int lastAction;
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW_RELEASE;
		lastKey = key;
		lastAction = action;
	}
	
	public static boolean keyPressed(int key) {
		if (lastKey == key && lastAction == GLFW_PRESS) {
			resetKeys();
			return true;
		} else {
			resetKeys();
			return false;
		}
	}
	
	public static void resetKeys() {
		lastKey = GLFW_KEY_UNKNOWN;
		lastAction = GLFW_KEY_UNKNOWN;
	}
}
