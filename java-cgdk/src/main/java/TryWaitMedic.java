
import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.TrooperType;
import model.World;


public class TryWaitMedic implements IAction {

	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		if (self.getHitpoints() < 100 && Utils.getMedic(world, self) != null && self.getType() != TrooperType.FIELD_MEDIC) {
			move.setAction(ActionType.END_TURN);
			return true;
		}
		return false;
	}
	
}
