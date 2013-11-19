
import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.World;

public class TryThrowGrenade implements IAction {
	
	public TryThrowGrenade() {

	}
	
	@Override
	public boolean run(Trooper self, World world, Game game, Move move) { 
		if (self.getActionPoints() >= game.getGrenadeThrowCost() && self.isHoldingGrenade()) {
			Trooper target = Utils.getWeakestTrooperInRange(self, world, game, move, game.getGrenadeThrowRange(), self.getStance());
			if (target != null) {
				move.setAction(ActionType.THROW_GRENADE);
				move.setX(target.getX());
				move.setY(target.getY());
				return true;
			}
		}
		return false;
	}
	
}
