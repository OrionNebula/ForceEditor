package forceEditor.entities;

import forceEditor.maps.Polygon;
import forceEditor.maps.RandomMap;
import forceEditor.maps.Vector;

public class FreeNode extends ResourceBlob
{
	public NodeType type;
	public FreeNode(Vector pos)
	{
		super(pos, null);
		type = (NodeType.values()[RandomMap.rng.nextInt(NodeType.values().length)]);
		this.setColor(type.getColor());
		this.setShape(Polygon.generateCircle(100, 10));
	}
}
