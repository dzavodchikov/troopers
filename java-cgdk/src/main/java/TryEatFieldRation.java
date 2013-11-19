
import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.World;


public class TryEatFieldRation implements IAction{

	public TryEatFieldRation() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		if (self.getActionPoints() >= game.getFieldRationEatCost() && self.getActionPoints() <= 7 && self.isHoldingFieldRation()) {
			Trooper target = Utils.getWeakestTrooperInRange(self, world, game, move, self.getShootingRange(), self.getStance());
			if (target != null) {
				move.setAction(ActionType.EAT_FIELD_RATION);
				return true;
			}
		}
		return false;
	}
	
}
