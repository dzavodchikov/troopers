
import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.World;


public class TryMove implements IAction{

	private Point point; 
	
	private Trooper trooper;
	
	public TryMove(Point point) {
		this.point = point;
	}
	
	public TryMove(Trooper trooper) {
		this.trooper = trooper;
	}
	
	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		Point next = null;
		if (point != null) {
			next = Utils.getNextPointToMove(self, world, game, point);
		}
		if (trooper != null) {
			next = Utils.getNextPointToMove(self, world, game, new Point(trooper.getX(), trooper.getY()));
		}
		if (self.getActionPoints() >= Utils.getMoveCosts(self.getStance(), game)) {
			move.setAction(ActionType.MOVE);
			move.setX(next.getX());
			move.setY(next.getY());
			return true;
		}
		return false;
	}
	
}
