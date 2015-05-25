package forceEditor;


import java.util.*;

import org.lwjgl.opengl.GL11;

import forceEditor.entities.*;
import forceEditor.maps.*;
import forceEditor.maps.Vector;
import static org.lwjgl.opengl.GL11.*;

public class GameState {
	
	private Player player;
	private MapProvider map;
	
	private List<Nanobot> nanobotList;
	
	public GameState() {
		player = new Player();
		nanobotList = new ArrayList<Nanobot>();
		nanobotList.add(player);
		for (int i = 0; i < 10; i++)
			nanobotList.add(new Nanobot());
		nanobotList.add(Nanobot.generateRandomBot(5));
	}
	
	public void update()
	{
		if(player.isDead())
			GL11.glClearColor(1, 0, 0, 0);
		
		if(nanobotList.isEmpty())
			GL11.glClearColor(0, 1, 0, 0);
		
		boolean isDead = true;
		for (Nanobot nanobot : nanobotList) {
			isDead &= nanobot.isDead();
		}
		
		if(isDead)
			GL11.glClearColor(0, 1, 0, 0);
		
		if(map == null)
			map = new RandomMap();
		
		player.update();
		
		for (ResourceBlob rb : map.getResources()) {
			rb.update();
		}
		
		for (Nanobot nb : nanobotList) {
			nb.update();
		}
		
	}
	
	public double getGridSize()
	{
		return ForceEditor.getWindowWidth()/25f;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public MapProvider getMap()
	{
		return map;
	}
	
	public List<Nanobot> getNanobots()
	{
		return nanobotList;
	}
	
	public void renderUI()
	{
		double playerPower = getPlayer().power;
		
		glPushMatrix();
			glTranslated(-ForceEditor.getWindowWidth()/2f + 50, -ForceEditor.getWindowHeight()/2f + 50f, 0);
			for (int i = 0; i < getPlayer().availableNodes.size(); i++)
			{
				getPlayer().availableNodes.get(i).render();
				glTranslated(50, 0, 0);
			}
		glPopMatrix();
		glPushMatrix();
			if (playerPower < Nanobot.addPower)
				glColor3f(1.0f, 0.0f, 0.0f);
			else if (playerPower < Nanobot.addPower * 5)
				glColor3f(0.75f, 0.5f, 0.15f);
			else {
				glColor3f(0.0f, 1.0f, 0.0f);
			}
		
			glBegin(GL_QUADS);
			glVertex2d(-ForceEditor.getWindowWidth()/2f, -ForceEditor.getWindowHeight()/2f);
			glVertex2d(-ForceEditor.getWindowWidth()/2f, -ForceEditor.getWindowHeight()/2f + 10);
			glVertex2d(-ForceEditor.getWindowWidth()/2f + getPlayer().power, -ForceEditor.getWindowHeight()/2f + 10);
			glVertex2d(-ForceEditor.getWindowWidth()/2f + getPlayer().power, -ForceEditor.getWindowHeight()/2f);
			glEnd();
		glPopMatrix();
	}
	
	public void render() {
		double gridSquare = getGridSize(), left = -ForceEditor.getWindowWidth()/2f - ((ForceEditor.getWindowWidth()/2f + player.getPosition().getComponents()[0]) % gridSquare) - gridSquare,
				bottom = -ForceEditor.getWindowHeight()/2f - ((ForceEditor.getWindowHeight()/2f + player.getPosition().getComponents()[1]) % gridSquare) - gridSquare;
		
		glLineWidth(2);
		glColor3f(0, 0, 0);
		glBegin(GL_LINES);
		{
			double iLeft = left, iBottom = bottom;
			while(iLeft < left + ForceEditor.getWindowWidth() + 2*gridSquare)
			{
				glVertex2d(iLeft, -ForceEditor.getWindowHeight()/2f);
				glVertex2d(iLeft, ForceEditor.getWindowHeight()/2f);
				
				iLeft += gridSquare;
			}
			
			while(iBottom < bottom + ForceEditor.getWindowHeight() + 2*gridSquare)
			{
				glVertex2d(-ForceEditor.getWindowWidth()/2f, iBottom);
				glVertex2d(ForceEditor.getWindowWidth()/2f, iBottom);
				
				iBottom += gridSquare;
			}
		}
		glEnd();
		
		glColor3f(1, 0, 0f);
		glPointSize(10);
		glBegin(GL_POINTS);
			glVertex2d(0,0);
		glEnd();
		
		player.render();
		
		for (Nanobot nb : nanobotList) {
			nb.render();
		}
		
		map.renderResources();
	}
	
	public Nanobot getClosestNanobot(Nanobot nanobot) {
		Vector position = nanobot.getPosition();
		Nanobot result = null;
		
		double minDistance = Double.MAX_VALUE;
		
		for(Nanobot nb : nanobotList) {
			double distance = nb.getDistanceTo(position);
			if (distance < minDistance && nb != nanobot) {
				minDistance = distance;
				result = nb;
			}
		}
		
		return result;
	}
}
