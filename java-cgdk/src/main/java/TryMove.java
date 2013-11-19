
import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.World;


public class TryMove implements IAction{

	private Point next; 
	
	public TryMove(Point next) {
		this.next = next;
	}
	
	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		if (self.getActionPoints() >= Utils.getMoveCosts(self.getStance(), game)) {
			move.setAction(ActionType.MOVE);
			move.setX(next.getX());
			move.setY(next.getY());
			return true;
		}
		return false;
	}
	
}
