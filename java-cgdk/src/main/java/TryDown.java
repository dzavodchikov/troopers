
import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.TrooperStance;
import model.World;


public class TryDown implements IAction {

	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		if (self.getActionPoints() >= game.getStanceChangeCost() && self.getStance() != TrooperStance.PRONE) {
			Trooper target = Utils.getWeakestVisibleTrooperInRange(self, world, game, move, self.getShootingRange(), Utils.getLowerStance(self.getStance()));
			if (target != null) {
				move.setAction(ActionType.LOWER_STANCE);
				return true;
			}
		}
		return false;
	}
	
}
