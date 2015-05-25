package forceEditor.maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Polygon
{
	protected ModulusList<Vector> points = new ModulusList<>();
	protected ArrayList<Vector> normals = new ArrayList<>();
	private ArrayList<Triangle> decompose = new ArrayList<>();
	
	public Polygon(Vector...points)
	{
		this.points.addAll(Arrays.asList(points));
		
		for(int i = 0; i < this.points.size(); i++)
			normals.add(this.points.get(i+1).subtract(this.points.get(i)).getRightWindVector().getUnitVector());
	}
	
	public static Polygon generateCircle(int res, double radius)
	{
		Vector[] newPoints = new Vector[res];
		double subDivide = 2*Math.PI/newPoints.length;
		for (int i = 0; i < newPoints.length; i++)
			newPoints[i] = new Vector(Math.cos(Math.PI/2 - subDivide*i) * radius, (float)Math.sin(Math.PI/2 - subDivide*i) * radius);
		
		return new Polygon(newPoints);
	}
	
	public static Polygon generateApproxCircle(int points, double radius, double spikyness)
	{
		Polygon toReturn = null;
		
		do
		{
			Vector[] newPoints = new Vector[points];
			double subDivide = 2*Math.PI/newPoints.length;
			for (int i = 0; i < newPoints.length; i++)
			{
				double random = ((1-spikyness) + spikyness*Math.random())*radius;
				newPoints[i] = new Vector(Math.cos(Math.PI/2 - subDivide*i) * random, Math.sin(Math.PI/2 - subDivide*i) * random);
			}
			
			toReturn = new Polygon(newPoints);
			try
			{
				toReturn.decompose();
			}catch(RuntimeException e)
			{
				continue;
			}
			
			return toReturn;
		}
		while(true);
	}
	
	public void decompose()
	{
		if(decompose.size() == 0)
		{
			ModulusList<Vector> newPoints = new ModulusList<>();
			newPoints.addAll(points);
			this.decompose(newPoints, decompose);
		}
	}
	
	protected void decompose(List<Vector> points, List<Triangle> toDecompose)
	{
		while(points.size() > 3)
		{
			int oldSize = points.size();
			for (int i = 0; i < points.size(); i++)
			{
				Triangle unit = new Triangle(points.get(i - 1), points.get(i), points.get(i + 1));
				Vector centroid = unit.getCentroid(), realOne = points.get(i - 1).subtract(points.get(i)).getRightWindVector(), realTwo = points.get(i).subtract(points.get(i + 1)).getRightWindVector();
				
				if(points.get(i - 1).subtract(centroid).dotProduct(realOne) >= 0 && points.get(i).subtract(centroid).dotProduct(realTwo) >= 0)
				{
					points.remove(i);
					toDecompose.add(unit);
					break;
				}
			}
			if(oldSize == points.size())
				throw new RuntimeException("Polygon decomposition failed");
		}
		
		toDecompose.add(new Triangle(points.get(0), points.get(1), points.get(2)));
		points.clear();
	}
	
	public boolean testForPoint(Vector point)
	{
		this.decompose();
		for (int i = 0; i < decompose.size(); i++)
			if(decompose.get(i).testForPoint(point))
				return true;
		return false;
	}
	
	public void render()
	{
		glBegin(GL_POLYGON);
		for (int i = 0; i < points.size(); i++)
			glVertex2d(points.get(i).getComponents()[0], points.get(i).getComponents()[1]);
		glEnd();
	}
	
	public double getArea()
	{
		this.decompose();
		
		float toReturn = 0;
		for (Triangle triangle : decompose)
			toReturn += triangle.getArea();
		
		return toReturn;
	}
	
	public Vector getCentroid()
	{
		this.decompose();
		float Cx = 0, Cy = 0;
		for (int i = 0; i < decompose.size(); i++)
		{
			Cx += decompose.get(i).getCentroid().getComponents()[0] * decompose.get(i).getArea();
			Cy += decompose.get(i).getCentroid().getComponents()[1] * decompose.get(i).getArea();
		}
		
		return new Vector(Cx / this.getArea(), Cy / this.getArea());
	}
}
