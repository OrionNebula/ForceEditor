package forceEditor.entities;

import org.lwjgl.opengl.GL11;

import forceEditor.maps.Polygon;
import forceEditor.maps.Vector;

public class ResourceBlob
{
	private Polygon shape;
	protected Vector color, position, movementVector = new Vector(0,0);
	
	public ResourceBlob(Vector pos, Vector color)
	{
		shape = Polygon.generateApproxCircle(15, 5, .25);
		this.position = pos;
		this.color = color;
	}
	
	public void setShape(Polygon p)
	{
		shape = p;
	}
	
	public void setMovementVector(Vector vector) {
		movementVector = vector;
	}
	
	public double getResourceValue()
	{
		return shape.getArea();
	}
	
	public Vector getPosition()
	{
		return position;
	}
	
	public Polygon getShape()
	{
		return shape;
	}
	
	public void setColor(Vector color)
	{
		this.color = color;
	}
	
	public void update()
	{
		position = position.add(movementVector);
		
		movementVector = movementVector.add(movementVector.scale(-1/10f));
	}
	
	public double getDistanceTo(Vector abs)
	{
		return this.getPosition().subtract(abs).getMagnitude();
	}
	
	public void render()
	{
		GL11.glColor3d(color.getComponents()[0], color.getComponents()[1], color.getComponents()[2]);
		shape.render();
	}
}
