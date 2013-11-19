
import model.Game;
import model.Move;
import model.Trooper;
import model.World;


public interface IAction {

	public abstract boolean run(Trooper self, World world, Game game, Move move);
	
}
