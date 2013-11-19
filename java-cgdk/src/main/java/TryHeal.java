
import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.World;


public class TryHeal implements IAction {

	private Trooper damagedTrooper; 
	
	public TryHeal(Trooper damagedTrooper) {
		this.damagedTrooper = damagedTrooper;
	}
	
	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		if (self.getActionPoints() >= game.getFieldMedicHealCost()) {
			move.setAction(ActionType.HEAL);
			move.setX(damagedTrooper.getX());
			move.setY(damagedTrooper.getY());
			return true;
		}
		return false;
	}
	
}
