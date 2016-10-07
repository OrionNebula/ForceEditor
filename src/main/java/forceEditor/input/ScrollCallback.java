package forceEditor.input;

import org.lwjgl.glfw.*;

import forceEditor.*;

public class ScrollCallback implements GLFWScrollCallbackI {
	public void invoke(long window, double xoffset, double yoffset) {
		ForceEditor.getInstance().scale -= yoffset / Math.abs(yoffset) * .2;
		if (ForceEditor.getInstance().scale < 1)
			ForceEditor.getInstance().scale = 1;
	}
}
