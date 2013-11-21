

public class TeamContext {

	private static TeamContext instance;

	private ITeamStrategy target;
	
	private TeamContext() {
		
	}
	
	public ITeamStrategy getTarget() {
		return target;
	}

	public void setTarget(ITeamStrategy target) {
		this.target = target;
	}

	public static synchronized TeamContext getInstance() {
		if (instance == null) {
			instance = new TeamContext();
		}
		return instance;
	}
	
}
