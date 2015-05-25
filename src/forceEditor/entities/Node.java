package forceEditor.entities;

import org.lwjgl.opengl.*;

import forceEditor.*;
import forceEditor.maps.*;

public class Node
{
	public static final Polygon circle = Polygon.generateCircle(100, 10);
	
	private Vector relativePos = new Vector(0,0);
	private NodeType type = NodeType.JOINT;
	Node[] nodeArray;
	protected Nanobot nanobot;
	private int lastSlot;
	String nodeID;
	boolean isHighlighted;
	
	private static int lastID = 0;
	
	public Node(Nanobot nanobot, NodeType type) {
		this.nanobot = nanobot;
		isHighlighted = false;
		nodeArray = new Node[8];
		nodeID = "" + lastID++;
		this.type = type;
	}
	
	public NodeType getType()
	{
		return type;
	}
	
	/**
	 * Renders the node.
	 */
	public void render() {
		GL11.glColor3d(getType().getColor().getComponents()[0], getType().getColor().getComponents()[1], getType().getColor().getComponents()[2]);
		for (int i = 0; i < nodeArray.length; i++)
		{
			Node node = nodeArray[i];
			if(node != null)
			{
				double angle = Math.PI*2/nodeArray.length*i, dist = ForceEditor.getInstance().getGameState().getGridSize();
				GL11.glColor3f(.5f, .5f, .5f);
				
				GL11.glLineWidth(8);
				GL11.glBegin(GL11.GL_LINES);
				{
					GL11.glVertex2d(0, 0);
					GL11.glVertex2d(Math.cos(angle)*dist, Math.sin(angle)*dist);
				}
				GL11.glEnd();
				
				GL11.glPushMatrix();
				GL11.glTranslated(Math.cos(angle)*dist, Math.sin(angle)*dist, 0);
				if(node.relativePos.equals(new Vector(0,0)))
					node.relativePos = node.relativePos.add(relativePos).add(new Vector(Math.cos(angle)*dist, Math.sin(angle)*dist));
				node.render();
				GL11.glPopMatrix();
			}
		}

		GL11.glColor3d(type.getColor().getComponents()[0], type.getColor().getComponents()[1], type.getColor().getComponents()[2]);
		if (isHighlighted) {
			GL11.glColor3f(.75f, .5f, .15f);
		}
		circle.render();
	}
	
	public boolean addNode(Node toAdd, Node targetNode)
	{
		return addNode((int)(8*Math.random()), toAdd, targetNode);
	}
	
	public boolean addNode(int direction, Node toAdd, Node targetNode)
	{
		if(targetNode != this) {
			for (int i = 0; i < nodeArray.length; i++) {
				Node node = nodeArray[i];
				if(node != null)
					return node.addNode(direction, toAdd, targetNode);
			}
			return false;
		} else {
			if(direction < nodeArray.length && nodeArray[direction] == null)
			{
				int rev = (direction + 4) % nodeArray.length;
				nodeArray[direction] = toAdd;
				toAdd.nodeArray[rev] = new Node(null, NodeType.JOINT);
				return true;
			}
			else 
				return false;
		}
	}
	
	public Node getChild(int index) {
		if (index < 0 || index > 8)
			return null;
		return nodeArray[index];
	}
	
	public Node[] getChildren() {
		return nodeArray;
	}
	
	public boolean isLeafNode() {
		return lastSlot <= 0;
	}
	
	public boolean setHighlighted(boolean setting) {
		return (isHighlighted = setting);
	}
	
	public Vector getRelativePosition()
	{
		return relativePos;
	}
	
	public String toString() {
		return nodeID;
	}
}
