package forceEditor.maps;

public class MathHelper
{
	public static interface ElementProvider
	{
		public double transformElement(int i, double elem);
	}
	
	public static interface MathOperation
	{
		public double doMath(double one, double two);
	}
	
	public static double[] transformList(double[] list, ElementProvider transform)
	{
		double[] toReturn = new double[list.length];
		
		for (int i = 0; i < toReturn.length; i++)
			toReturn[i] = transform.transformElement(i, list[i]);
		
		return toReturn;
	}
	
	public static double accumulate(double[] list, ElementProvider transform, MathOperation acc)
	{
		if(list.length == 0)
			throw new ArithmeticException("Cannot accumulate a zero-length list");
		
		double val = transform.transformElement(0, list[0]);
		for(int i = 1; i < list.length; i++)
			val = acc.doMath(val, transform.transformElement(i, list[i]));
		
		return val;
	}
}
