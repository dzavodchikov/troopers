
import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.World;


public class TryUseMedkit implements IAction {

	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		if (self.getActionPoints() >= game.getMedikitUseCost() && self.getHitpoints() <= 75 && self.isHoldingMedikit()) {
			move.setAction(ActionType.USE_MEDIKIT);
			move.setX(self.getX());
			move.setY(self.getY());
			return true;
		}
		return false;
	}
	
}
