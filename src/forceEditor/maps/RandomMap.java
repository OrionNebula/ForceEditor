package forceEditor.maps;

import java.util.*;

import org.lwjgl.opengl.*;

import forceEditor.*;
import forceEditor.entities.*;

public class RandomMap implements MapProvider
{
	private ArrayList<ResourceBlob> resList = new ArrayList<>();
	private double width, height;
	private int resourceCount;
	public static final Random rng = new Random();
	
	public RandomMap(double width, double height, int resourceCount)
	{
		this.width = width;
		this.height = height;
		this.resourceCount = resourceCount;
		for (int i = 0; i < resourceCount; i++)
			if(rng.nextDouble() > 1/20d)
				resList.add(new ResourceBlob(new Vector((rng.nextBoolean() ? -1 : 1) * rng.nextDouble() * width/2, (rng.nextBoolean() ? -1 : 1) * rng.nextDouble() * height/2), new Vector(rng.nextDouble(), rng.nextDouble(), rng.nextDouble())));
			else
				resList.add(new FreeNode(new Vector((rng.nextBoolean() ? -1 : 1) * rng.nextDouble() * width/2, (rng.nextBoolean() ? -1 : 1) * rng.nextDouble() * height/2)));
	}
	
	public RandomMap()
	{
		this((rng.nextInt(100) + 50) * ForceEditor.getInstance().getGameState().getGridSize(), (rng.nextInt(100) + 50) * ForceEditor.getInstance().getGameState().getGridSize(), rng.nextInt(1024) + 2048);
	}
	
	public void renderResources()
	{
		for (ResourceBlob rb : resList) {
			if (rb instanceof Bullet)
				rb.update();
		}
		
		for (ResourceBlob resourceBlob : resList)
		{
			Vector relativePos = resourceBlob.getPosition().subtract(ForceEditor.getInstance().getGameState().getPlayer().getPosition());
			GL11.glPushMatrix();
			GL11.glTranslated(relativePos.getComponents()[0], relativePos.getComponents()[1], 0);
			resourceBlob.render();
			GL11.glPopMatrix();
		}
	}

	public double getMapWidth()
	{
		return width;
	}

	public double getMapHeight()
	{
		return height;
	}
	
	public void collectResource(ResourceBlob blob)
	{
		resList.remove(blob);
		
		if(rng.nextDouble() > 1/20d)
			resList.add(new ResourceBlob(new Vector((rng.nextBoolean() ? -1 : 1) * rng.nextDouble() * width/2, (rng.nextBoolean() ? -1 : 1) * rng.nextDouble() * height/2), new Vector(rng.nextDouble(), rng.nextDouble(), rng.nextDouble())));
		else
			resList.add(new FreeNode(new Vector((rng.nextBoolean() ? -1 : 1) * rng.nextDouble() * width/2, (rng.nextBoolean() ? -1 : 1) * rng.nextDouble() * height/2)));
	}
	
	public void insertResource(ResourceBlob blob)
	{
		resList.add(blob);
	}
	
	public ResourceBlob getClosestResourceBlob(Vector position) {
		ResourceBlob result = null;
		
		double minDistance = Double.MAX_VALUE;
		
		for(ResourceBlob rb : resList) {
			double distance = rb.getDistanceTo(position);
			if (distance < minDistance) {
				minDistance = distance;
				result = rb;
			}
		}
		
		return result;
	}
	
	public ArrayList<ResourceBlob> getResources()
	{
		return resList;
	}

}
