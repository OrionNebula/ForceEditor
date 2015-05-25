package forceEditor.maps;

import java.util.Arrays;

import forceEditor.maps.MathHelper.ElementProvider;

public class Vector
{
	private double[] points;
	
	public Vector(double...fs)
	{
		this.points = fs;
	}
	
	public int getDimension()
	{
		return this.points.length;
	}
	
	public boolean hasEqualDimension(Vector arg)
	{
		return this.getDimension() == arg.getDimension();
	}
	
	public double[] getComponents()
	{
		return points;
	}
	
	public double dotProduct(Vector arg)
	{
		if(!this.hasEqualDimension(arg))
			throw new ArithmeticException(this + " does not have the same dimensions as " + arg);
		
		return MathHelper.accumulate(this.points, (i, elem) -> elem * arg.getComponents()[i], (o, t) -> o+t);
	}
	
	public double getMagnitude()
	{
		return (double) Math.sqrt(MathHelper.accumulate(getComponents(), (i, elem) -> elem*elem, (o,t) -> o+t));
	}
	
	public Vector subtract(Vector arg)
	{
		if(!this.hasEqualDimension(arg))
			throw new ArithmeticException(this + " does not have the same dimensions as " + arg);
		
		double[] list = new double[arg.getDimension()];
		for(int i = 0; i < list.length; i++)
			list[i] = this.getComponents()[i]-arg.getComponents()[i];
		
		return new Vector(list);
	}
	
	public Vector add(Vector arg)
	{
		if(!this.hasEqualDimension(arg))
			throw new ArithmeticException(this + " does not have the same dimensions as " + arg);
		
		double[] list = new double[arg.getDimension()];
		for(int i = 0; i < list.length; i++)
			list[i] = this.getComponents()[i]+arg.getComponents()[i];
		
		return new Vector(list);
	}
	
	public Vector scale(double scalar)
	{
		double[] list = new double[this.getDimension()];
		for(int i = 0; i < list.length; i++)
			list[i] = this.getComponents()[i]*scalar;
		
		return new Vector(list);
	}
	
	public Vector getMidpoint(Vector arg)
	{
		if(!this.hasEqualDimension(arg))
			throw new ArithmeticException(this + " does not have the same dimensions as " + arg);
		
		double[] list = new double[arg.getDimension()];
		for(int i = 0; i < list.length; i++)
			list[i] = (this.getComponents()[i]+arg.getComponents()[i])/2;
		
		return new Vector(list);
	}
	
	public Vector getUnitVector()
	{
		if(this.getMagnitude() > 0)
			return new Vector(MathHelper.transformList(this.getComponents(), (i, elem)->elem/this.getMagnitude()));
		else
			return new Vector(new double[this.getDimension()]);
	}
	
	public Vector getRightWindVector()
	{
		if(this.getDimension() != 2)
			throw new ArithmeticException("getRightWindVector can only be applied to 2D vectors");
		
		return new Vector(this.getComponents()[1], -this.getComponents()[0]);
	}
	
	public Vector transformComponents(ElementProvider prov)
	{
		double[] newPoints = new double[this.getDimension()];
		for (int i = 0; i < points.length; i++)
			newPoints[i] = prov.transformElement(i, points[i]);
		
		return new Vector(newPoints);
	}
	
	public String toString()
	{
		return String.format("Vector%d: %s", this.getDimension(), Arrays.toString(this.points));
	}
	
	public boolean equals(Object arg)
	{
		if(arg == null)
			return false;
		return arg.toString().equals(this.toString());
	}
}
