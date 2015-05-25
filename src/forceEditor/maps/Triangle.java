package forceEditor.maps;

import static org.lwjgl.opengl.GL11.*;

public class Triangle extends Polygon
{
	public Triangle(Vector v1, Vector v2, Vector v3)
	{
		super(v1, v2, v3);
	}
	
	public void decompose()
	{
		throw new UnsupportedOperationException("Triangles are the minimum decomposition unit");
	}

	public boolean testForPoint(Vector point)
	{
		boolean isWithin = true;
		
		for (int i = 0; i < normals.size(); i++)
			isWithin &= point.subtract(points.get(i)).dotProduct(normals.get(i)) >= 0;
			
		return isWithin;
	}
	
	public Vector getCentroid()
	{
		return points.get(0).add(points.get(1).add(points.get(2))).scale(1/3f);
	}

	public double getArea()
	{
		Vector sideOne = points.get(0).subtract(points.get(1)), sideTwo = points.get(1).subtract(points.get(2)), sideThree = points.get(2).subtract(points.get(3));
		double s = (sideOne.getMagnitude() + sideTwo.getMagnitude() + sideThree.getMagnitude()) / 2f;
		return Math.sqrt(s*(s-sideOne.getMagnitude())*(s-sideTwo.getMagnitude())*(s-sideThree.getMagnitude()));
	}

	public void render()
	{
		glBegin(GL_LINES);
		for (int i = 0; i < points.size(); i++)
		{
			glVertex2d(points.get(i).getComponents()[0], points.get(i).getComponents()[1]);
			glVertex2d(points.get(i + 1).getComponents()[0], points.get(i + 1).getComponents()[1]);
		}
		glEnd();
	}
	
	
}
