package forceEditor;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

public class FrameBufferObject
{
	private int fboId, texId, depthId, fboWidth, fboHeight;
	
	public FrameBufferObject(int width, int height)
	{
		if(!GLContext.createFromCurrent().getCapabilities().GL_EXT_framebuffer_object)
			throw new UnsupportedOperationException("This system does not support GL_EX_framebuffer_object");
		
		fboWidth = width;
		fboHeight = height;
		fboId = glGenFramebuffersEXT();
		texId = glGenTextures();
		depthId = glGenRenderbuffersEXT();
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
		
		glBindTexture(GL_TEXTURE_2D, texId);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_INT, (ByteBuffer)null);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, texId, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthId);
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height);
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthId);
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, 0);
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		
		int framebuffer = glCheckFramebufferStatusEXT( GL_FRAMEBUFFER_EXT ); 
		switch ( framebuffer ) {
		    case GL_FRAMEBUFFER_COMPLETE_EXT:
		        break;
		    case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
		        throw new RuntimeException( "FrameBuffer: " + fboId
		                + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
		    case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
		        throw new RuntimeException( "FrameBuffer: " + fboId
		                + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
		    case GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
		        throw new RuntimeException( "FrameBuffer: " + fboId
		                + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception" );
		    case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
		        throw new RuntimeException( "FrameBuffer: " + fboId
		                + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
		    case GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
		        throw new RuntimeException( "FrameBuffer: " + fboId
		                + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception" );
		    case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
		        throw new RuntimeException( "FrameBuffer: " + fboId
		                + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
		    default:
		        throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer );
		}
	}
	
	public void bindFBO()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
		glPushAttrib(GL_VIEWPORT_BIT);
		glViewport(0, 0, fboWidth, fboHeight);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void unbindFBO()
	{
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		glPopAttrib();
	}
	
	public int getTextureId()
	{
		return texId;
	}
}
