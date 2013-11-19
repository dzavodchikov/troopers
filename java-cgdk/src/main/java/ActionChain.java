import java.util.ArrayList;
import java.util.List;

import model.Game;
import model.Move;
import model.Trooper;
import model.World;

public class ActionChain {

	private Trooper self;
	
	private World world;
	
	private Game game;
	
	private Move move;
	
	private List<IAction> actions;
	
	public ActionChain(Trooper self, World world, Game game, Move move) {
		this.self = self;
		this.world = world;
		this.game = game;
		this.move = move;
		this.actions = new ArrayList<>();
	}
	
	public ActionChain chain(IAction action) {
		actions.add(action);
		return this;
	}
	
	public void execute() {
		for (IAction action : actions) {
			if (action.run(self, world, game, move) == true) {
				return;
			}
		}
	}
	
}
