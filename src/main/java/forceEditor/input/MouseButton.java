package forceEditor.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButton extends GLFWMouseButtonCallback {

	public static int lastButton;
	public static int lastAction;
	
	@Override
	public void invoke(long window, int button, int action, int mods) {
		lastButton = button;
		lastAction = action;
	}

}
