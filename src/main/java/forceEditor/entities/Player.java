package forceEditor.entities;

import static org.lwjgl.glfw.GLFW.*;
import forceEditor.ForceEditor;
import forceEditor.input.*;
import forceEditor.maps.Vector;

public class Player extends Nanobot {
	
	public Player() {
		super();
		headNode.setHighlighted(true);
	}
	
	@Override
	public void move() {
		Vector startVector = movementVector;
		if (Keyboard.keys[GLFW_KEY_W])
			super.movementVector = movementVector.add(new Vector(0, 1f));
		
		if (Keyboard.keys[GLFW_KEY_S])
			super.movementVector = movementVector.subtract(new Vector(0, 1f));
		
		if (Keyboard.keys[GLFW_KEY_A])
			super.movementVector = movementVector.subtract(new Vector(1, 0));
		
		if (Keyboard.keys[GLFW_KEY_D])
			super.movementVector = movementVector.add(new Vector(1, 0));
		
		if(Keyboard.keyPressed(GLFW_KEY_R))
		{
			if(this.availableNodes.size() > 0 && power >= addPower)
			{
				power -= addPower;
				this.consumeAvailable();
			}
		}
		
		if(Keyboard.keys[GLFW_KEY_Q])
		{
			if(power >= 1)
			{
				power--;
				int forceCount = 0;
				for (Node node : this.getNodeList())
				{
					if(node.getType() == NodeType.FORCE)
						forceCount++;
				}
				
				for (Nanobot bot : ForceEditor.getInstance().getGameState().getNanobots())
				{
					Vector vec = bot.getPosition().subtract(this.getPosition());
					if(vec.getMagnitude() <= 500 && bot != this)
						bot.movementVector = bot.movementVector.add(vec.getUnitVector().scale(forceCount));
				}
			}
		}
		
		if(Keyboard.keys[GLFW_KEY_E])
		{
			attack();
		}
		
		if (movementVector.equals(startVector))
			movementVector = movementVector.add(movementVector.scale(-0.01));
		
		if (movementVector.getMagnitude() > super.maxSpeed)
			movementVector = movementVector.getUnitVector().scale(super.maxSpeed);
	}
}
