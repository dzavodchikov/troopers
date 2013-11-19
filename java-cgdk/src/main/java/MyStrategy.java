
import java.util.Random;

import model.Game;
import model.Move;
import model.Trooper;
import model.World;

public final class MyStrategy implements Strategy {

	private final Random random = new Random();
	
    @Override
    public void move(Trooper self, World world, Game game, Move move) {
    	
    	GameContext context = GameContext.getInstance();
    	
    	ITeamStrategy target = context.getTarget();
    	
    	if (target != null) {
	    	if (target.isInvalid(self, world, game, move) || target.isComplete(self, world, game, move)) {
	    		target = null;
	    	}
    	}
    	
    	if (target == null) {
    		target = selectNewTeamStrategy(self, world, game);
    	}
    	
    	if (target != context.getTarget()) {
    		context.setTarget(target);
    	}
    	
    	target.move(self, world, game, move);
    	
    }
    
    private ITeamStrategy selectNewTeamStrategy(Trooper trooper, World world, Game game) {
    	
		Point nextPoint = getNextRoute(trooper, world, game);
		return new AttackTheArea(nextPoint);	

	}
    
	public Point getNextRoute(Trooper self, World world, Game game) {
		
		int x = world.getWidth() / 2;
		int y = world.getHeight() / 2;
		
		boolean pointNotSelected = true;
		
		while (pointNotSelected) {

			int radiusY = random.nextInt(5) + 5;
			int radiusX = random.nextInt(8) + 7;
			int nextDir = random.nextInt(4);
			
			switch (nextDir) {
			case 0:
				if (Utils.isPointValid(x + radiusX, y + radiusY, world)) {
					pointNotSelected = false;
					x = x + radiusX;
					y = y + radiusY;
				}
				break;
			case 1:
				if (Utils.isPointValid(x - radiusX, y + radiusY, world)) {
					pointNotSelected = false;
					x = x - radiusX;
					y = y + radiusY;
				}
				break;
			case 2:
				if (Utils.isPointValid(x - radiusX, y - radiusY, world)) {
					pointNotSelected = false;
					x = x - radiusX;
					y = y - radiusY;
				}
				break;
			case 3:
				if (Utils.isPointValid(x + radiusX, y - radiusY, world)) {
					pointNotSelected = false;
					x = x + radiusX;
					y = y - radiusY;
				}
				break;
			default:
				break;
			}
		}
		
		return new Point(x, y);
		
	}
    
}
