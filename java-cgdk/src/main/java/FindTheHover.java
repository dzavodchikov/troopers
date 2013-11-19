

import java.util.ArrayList;
import java.util.List;

import model.CellType;
import model.Game;
import model.Move;
import model.Trooper;
import model.World;

public class FindTheHover implements ITeamStrategy {

	public FindTheHover() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isComplete(Trooper self, World world, Game game, Move move) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInvalid(Trooper self, World world, Game game, Move move) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void move(Trooper self, World world, Game game, Move move) {
		// TODO Auto-generated method stub
		
	}
	
public Point getNearestHover(Trooper trooper, World world, Game game) {
    	
		Trooper[] troopers = world.getTroopers();
		
		List<Point> hovers = new ArrayList<>();
		
        CellType[][] cells = world.getCells();
        
        for (int x = 0; x < cells.length; x++) {
			for (int y = 0; y < cells[x].length; y++) {
				if (isGoodHover(cells[x][y])) {
					hovers.add(new Point(x, y));
				}
			}
		}
        
        Point nearestHover = null;
        
        for (Point hover : hovers) {
			
		}
        
    	return null;
    	
    }
	
	private boolean isGoodHover(CellType cellType) {
		// TODO Auto-generated method stub
		return false;
	}


}
