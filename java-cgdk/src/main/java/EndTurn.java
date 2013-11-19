import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.World;


public class EndTurn implements IAction {

	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		move.setAction(ActionType.END_TURN);
		return true;
	}
	
}
