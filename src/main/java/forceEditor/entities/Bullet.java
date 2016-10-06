package forceEditor.entities;

import forceEditor.*;
import forceEditor.maps.*;

public class Bullet extends ResourceBlob {

	public Bullet(Vector pos, Vector trajectory) {
		super(pos, new Vector(.5f, .5f, .5f));
		super.setMovementVector(trajectory);
	}
	
	public void update() {
		position = position.add(movementVector.getUnitVector().scale(150 * TimeManager.getDelta()));
	}

}
