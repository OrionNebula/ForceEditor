package forceEditor.entities;

import forceEditor.maps.Vector;

public enum NodeType
{
	JOINT(new Vector(0,0,0), 1),
	GUN(new Vector(1,.25,.25), 1),
	FORCE(new Vector(.5,.5,1), 1),
	;
	
	private Vector color;
	private double radiusMult;
	NodeType(Vector color, double radius)
	{
		this.color = color;
		radiusMult = radius;
	}
	
	public Vector getColor()
	{
		return this.color;
	}
	
	public double getRadiusMultiplier()
	{
		return radiusMult;
	}
}
