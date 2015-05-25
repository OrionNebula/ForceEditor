package forceEditor.entities;

import java.util.*;

import org.lwjgl.opengl.*;

import forceEditor.*;
import forceEditor.maps.*;
import forceEditor.maps.Vector;

public class Nanobot {
	public static final double addPower = 50, baseHealth = 100;
	
	public double power, health = baseHealth;
	protected Vector position = new Vector(0,0);
	protected Vector movementVector;
	protected double maxSpeed;
	protected double absorbRadius;
	protected double rotAngle;
	protected Node headNode;
	public ArrayList<FreeNode> availableNodes = new ArrayList<>();
	private ArrayList<Node> nodeList = new ArrayList<>();
	private Vector targetVector;

	/**
	 * @param nodes Number of nodes to be randomly generated into the nanobot.
	 * @return A new nanobot object containing randomly placed nodes.
	 */
	public static Nanobot generateRandomBot(int nodes) {
		Nanobot result = new Nanobot();
		for (int i = 1; i < nodes; i++) {
			Node targetNode;
			do {
				targetNode = result.getRandomNode();
			} while (!result.addNode(new Node(result, (NodeType.values()[RandomMap.rng.nextInt(NodeType.values().length)])), targetNode));
		}
		return result;
	}
	
	public Nanobot() {
		this.headNode = new Node(this, NodeType.JOINT);
		nodeList.add(headNode);
		movementVector = new Vector(0,0);
		maxSpeed = 200;
		absorbRadius = 100;
	}
	
	public ArrayList<Node> getNodeList() {
		return nodeList;
	}
	
	public boolean addNode(Node node, Node targetNode)
	{
		boolean toReturn = headNode.addNode(node, targetNode);
		if(toReturn)
			nodeList.add(node);
		return toReturn;
	}
	
	public void move() {
		
//		setTarget(ForceEditor.getInstance().getGameState().getPlayer().getPosition());
		if (power > addPower && availableNodes.isEmpty()) {
			Nanobot nb = ForceEditor.getInstance().getGameState().getClosestNanobot(this);
			setTarget(nb.getPosition());
			int gunCount = 0;
			for (Node node : this.getNodeList())
				if(node.getType() == NodeType.GUN)
					gunCount++;
			if(nb.getPosition().subtract(position).getMagnitude() <= gunCount*100)
				attack();
		}
		else {
			ResourceBlob rb;
			rb = ForceEditor.getInstance().getGameState().getMap().getClosestResourceBlob(position);
				setTarget(rb.getPosition());
		}
		
		if (targetVector != null) {
			movementVector = movementVector.add(targetVector.subtract(position).getUnitVector().scale(1));
			
			if (position.equals(targetVector))
				targetVector = null;
		}

		if (movementVector.getMagnitude() > maxSpeed)
			movementVector = movementVector.getUnitVector().scale(maxSpeed);
	}
	
	public void update() {
		if(isDead())
			return;
		
		maxSpeed = 100 + 400d/nodeList.size();
		move();
		for (Node node : getNodeList())
		{
			ArrayList<ResourceBlob> toEat = new ArrayList<>();
			for (ResourceBlob res : ForceEditor.getInstance().getGameState().getMap().getResources())
			{
				Vector relativePos = res.getPosition().subtract(this.getPosition().add(node.getRelativePosition()));
				if(relativePos.getMagnitude() < this.getAbsorptionRadius())
					toEat.add(res);
			}
			
			for (ResourceBlob resourceBlob : toEat)
			{
				Vector relativePos = resourceBlob.getPosition().subtract(this.getPosition().add(node.getRelativePosition()));
				if(relativePos.getMagnitude() < 50)
				{
					if(resourceBlob instanceof FreeNode)
						availableNodes.add((FreeNode)resourceBlob);
					ForceEditor.getInstance().getGameState().getMap().collectResource(resourceBlob);
					if(resourceBlob instanceof Bullet)
						takeDamage(1);
					if(!(resourceBlob instanceof FreeNode) && !(resourceBlob instanceof Bullet))
						this.power += resourceBlob.getResourceValue() / 10f;
				}
			}
		}
		
		if(!(this instanceof Player))
			while(!availableNodes.isEmpty() && power >= addPower)
			{
				consumeAvailable();
				power -= addPower;
			}
		
		position = position.add(movementVector.scale(TimeManager.getDelta()));
		
		double mapWidth = ForceEditor.getInstance().getGameState().getMap().getMapWidth();
		double mapHeight = ForceEditor.getInstance().getGameState().getMap().getMapHeight();
		
		if (position.getComponents()[0] > mapWidth/2.0)
			movementVector = movementVector.add(new Vector(-10,0));
		if(position.getComponents()[0] < -mapWidth/2.0)
			movementVector = movementVector.add(new Vector(10,0));
		
		if (position.getComponents()[1] > mapHeight/2.0)
			movementVector = movementVector.add(new Vector(0,-10));
		if(position.getComponents()[1] < -mapHeight/2.0)
			movementVector = movementVector.add(new Vector(0,10));
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public void setPosition(Vector vector) {
		position = vector;
	}
	
	public Node getHeadNode() {
		return headNode;
	}
	
	public double getAbsorptionRadius()
	{
		return absorbRadius;
	}
	
	public Node getRandomNode() {
		Node result;
		result = getNodeList().get((int) (RandomMap.rng.nextInt(getNodeList().size())));
		return result;
	}
	
	public void setTarget(Vector target) {
		targetVector = target;
	}
	
	public void consumeAvailable()
	{
		if (!availableNodes.isEmpty()) {
			Node targetNode;
			do {
				targetNode = getRandomNode();
			} while(!addNode(new Node(this, availableNodes.get(0).type), targetNode));
			this.availableNodes.remove(0);
		}
	}
	
	public void takeDamage(double powerUsed)
	{
		power -= powerUsed;
		if(power < 0)
		{
			health += power;
			power = 0;
		}
	}
	
	public boolean isDead()
	{
		if(health <= 0)
			headNode = new Node(this, NodeType.JOINT);
		return health <= 0;
	}
	
	public void attack()
	{
		if(power >= 1)
		{
			ForceEditor.getInstance().getGameState().getMap().insertResource(new Bullet(position.add(movementVector), movementVector.getUnitVector()));
			
			int gunCount = 0;
			for (Node node : this.getNodeList())
				if(node.getType() == NodeType.GUN)
					gunCount++;
			
			for (Nanobot bot : ForceEditor.getInstance().getGameState().getNanobots())
			{
				Vector vec = bot.getPosition().subtract(this.getPosition());
				if(vec.getMagnitude() <= 100*gunCount && bot != this)
					bot.takeDamage(1);
			}
			
			power--;
		}
	}
	
	public void render()
	{
		if(isDead())
			return;
		
		GL11.glPushMatrix();
		if(!(this instanceof Player))
			GL11.glTranslated(this.getPosition().getComponents()[0] - ForceEditor.getInstance().getGameState().getPlayer().getPosition().getComponents()[0],
					this.getPosition().getComponents()[1] - ForceEditor.getInstance().getGameState().getPlayer().getPosition().getComponents()[1], 0);
//		GL11.glRotated(rotAngle, 0, 0, 1);
		GL11.glPushMatrix();
		headNode.render();
		GL11.glPopMatrix();
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glColor3f(0, 0, 0);
			GL11.glVertex2d(10, 0);
			GL11.glVertex2d(10, 10);
			GL11.glVertex2d(110, 10);
			GL11.glVertex2d(110, 0);
			GL11.glColor3f(0, 1, 0);
			
			GL11.glVertex2d(10, 0);
			GL11.glVertex2d(10, 10);
			GL11.glVertex2d(10 + health, 10);
			GL11.glVertex2d(10 + health, 0);
		}
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	public double getDistanceTo(Vector abs)
	{
		return this.getPosition().subtract(abs).getMagnitude();
	}
}
