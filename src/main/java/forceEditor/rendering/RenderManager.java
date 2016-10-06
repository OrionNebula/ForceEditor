package forceEditor.rendering;

import static org.lwjgl.glfw.GLFWvidmode.*;
import forceEditor.*;
import static org.lwjgl.opengl.GL11.*;

public class RenderManager {
	
	public static void initImmediateMode() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width(ForceEditor.getVidMode()), 0, height(ForceEditor.getVidMode()), 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
}
