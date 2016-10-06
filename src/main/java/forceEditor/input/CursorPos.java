package forceEditor.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorPos extends GLFWCursorPosCallback {
	public static int x, y;
	
	@Override
	public void invoke(long window, double xpos, double ypos) {
		x = (int) xpos;
		y = (int) ypos;
	}
}
