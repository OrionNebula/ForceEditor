package forceEditor.rendering;

import forceEditor.*;
import static org.lwjgl.opengl.GL11.*;

public class RenderManager {
	private static int windowWidth = -1;
	private static int windowHeight = -1;
	
	public static void initDimensions(ForceEditor instance) {
		windowWidth = instance.getWindowHeight();
		windowHeight = instance.getWindowHeight();
	}
	
	public static void initImmediateMode() {
		if (windowWidth < 0 || windowHeight < 0) {
			throw new UnsupportedOperationException("Window dimensions were not initialized yet in RenderManager.");
		}
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, windowWidth, 0, windowHeight, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
}
