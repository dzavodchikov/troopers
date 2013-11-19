

public class GameContext {

	private static GameContext instance;

	private ITeamStrategy target;
	
	private GameContext() {
		
	}
	
	public ITeamStrategy getTarget() {
		return target;
	}

	public void setTarget(ITeamStrategy target) {
		this.target = target;
	}

	public static synchronized GameContext getInstance() {
		if (instance == null) {
			instance = new GameContext();
		}
		return instance;
	}
	
}
