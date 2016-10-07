package forceEditor.input;

import org.lwjgl.glfw.*;

public class CursorPos implements GLFWCursorPosCallbackI {
	public static int x, y;
	
	public void invoke(long window, double xpos, double ypos) {
		x = (int) xpos;
		y = (int) ypos;
	}
}
