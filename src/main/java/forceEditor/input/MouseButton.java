package forceEditor.input;

import org.lwjgl.glfw.*;

public class MouseButton implements GLFWMouseButtonCallbackI {

	public static int lastButton;
	public static int lastAction;
	
	public void invoke(long window, int button, int action, int mods) {
		lastButton = button;
		lastAction = action;
	}

}
