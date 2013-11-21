import java.util.List;

import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.TrooperStance;
import model.World;


public class TryHide implements IAction {

	private static final int HIDE_POINTS = 4;

	public TryHide() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		List<Trooper> enemies = Utils.getVisibleEnemies(world, self);
		int count = 0;
		for (Trooper enemy : enemies) {
			if (world.isVisible(enemy.getShootingRange(), enemy.getX(), enemy.getY(), enemy.getStance(), self.getX(), self.getY(), enemy.getStance())) {
				count++;
			}
		}
		if (count > 1) {
			
		}
		if (self.getActionPoints() >= game.getStanceChangeCost() && self.getActionPoints() <= HIDE_POINTS && self.getStance() != TrooperStance.PRONE) {
			Trooper target = Utils.getWeakestVisibleTrooperInRange(self, world, game, move, self.getShootingRange(), Utils.getLowerStance(self.getStance()));
			if (target == null) {
				move.setAction(ActionType.LOWER_STANCE);
				return true;
			}
		}
		return false;
	}

}
