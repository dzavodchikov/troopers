import java.util.EnumMap;
import java.util.Map;

import model.Trooper;
import model.TrooperType;


public class TrooperContext {
	
	private static Map<TrooperType, TrooperContext> instanceMap = new EnumMap<>(TrooperType.class);
	
	private TrooperContext() {
		
	}
	
	public static synchronized TrooperContext getContext(Trooper trooper) {
		TrooperType type = trooper.getType();
		return getContext(type);
	}

	public static synchronized TrooperContext getContext(TrooperType type) {
		if (instanceMap.containsKey(type)) {
			return instanceMap.get(type);
		} else {
			TrooperContext context = new TrooperContext();
			instanceMap.put(type, context);
			return context;
		}
	}
	
}
