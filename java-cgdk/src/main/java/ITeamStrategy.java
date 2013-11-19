
import model.Game;
import model.Move;
import model.Trooper;
import model.World;

public interface ITeamStrategy {
	
	public abstract boolean isComplete(Trooper self, World world, Game game, Move move);

	public abstract boolean isInvalid(Trooper self, World world, Game game, Move move);

	public abstract void move(Trooper self, World world, Game game, Move move);

}
