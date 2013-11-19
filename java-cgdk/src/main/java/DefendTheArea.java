import model.Game;
import model.Move;
import model.Trooper;
import model.World;

public class DefendTheArea implements ITeamStrategy {

	private static final int RADIUS = 5;
	
	private static final int HOVER_TIME = 10;
	
	private Point areaPoint;
	
	public DefendTheArea(Point googHover) {
		this.areaPoint = googHover;
	}

	@Override
	public boolean isComplete(Trooper self, World world, Game game, Move move) {
		return noEnemiesDuringNLastRounds();
	}

	@Override
	public boolean isInvalid(Trooper self, World world, Game game, Move move) {
		return false;
	}

	@Override
	public void move(Trooper self, World world, Game game, Move move) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean noEnemiesDuringNLastRounds() {
		return false;
	}

}
