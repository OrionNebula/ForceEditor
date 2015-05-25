package forceEditor.input;

import org.lwjgl.glfw.GLFWScrollCallback;

import forceEditor.ForceEditor;

public class ScrollCallback extends GLFWScrollCallback
{
	public void invoke(long window, double xoffset, double yoffset)
	{
		ForceEditor.getInstance().scale -= yoffset / Math.abs(yoffset) * .2;
		if(ForceEditor.getInstance().scale < 1)
			ForceEditor.getInstance().scale = 1;
	}
}
