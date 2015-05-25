package forceEditor.maps;

import java.util.ArrayList;

import forceEditor.entities.ResourceBlob;

public interface MapProvider
{
	public ArrayList<ResourceBlob> getResources();
	public void collectResource(ResourceBlob blob);
	public ResourceBlob getClosestResourceBlob(Vector position);
	public void renderResources();
	public double getMapWidth();
	public double getMapHeight();
	public void insertResource(ResourceBlob blob);
}
