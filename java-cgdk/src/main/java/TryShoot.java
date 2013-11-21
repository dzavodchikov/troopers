
import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.World;


public class TryShoot implements IAction {

	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		if (self.getActionPoints() >= self.getShootCost()) {
			Trooper target = Utils.getWeakestVisibleTrooperInRange(self, world, game, move, self.getShootingRange(), self.getStance());
			if (target != null) {
				move.setAction(ActionType.SHOOT);
				move.setX(target.getX());
				move.setY(target.getY());
				return true;
			}
		}
		return false;
	}
	
}
