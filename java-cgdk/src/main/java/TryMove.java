
import model.ActionType;
import model.Game;
import model.Move;
import model.Trooper;
import model.World;


public class TryMove implements IAction{

	private Point point; 
	
	private Trooper trooper;
	
	public TryMove(Point point) {
		this.point = point;
	}
	
	public TryMove(Trooper trooper) {
		this.trooper = trooper;
	}
	
	@Override
	public boolean run(Trooper self, World world, Game game, Move move) {
		Point next = null;
		if (point != null) {
			next = getNextPointToMove(self, world, game, move, point);
		}
		if (trooper != null) {
			next = getNextPointToMove(self, world, game, move, new Point(trooper.getX(), trooper.getY()));
		}
		if (self.getActionPoints() >= Utils.getMoveCosts(self.getStance(), game)) {
			move.setAction(ActionType.MOVE);
			move.setX(next.getX());
			move.setY(next.getY());
			return true;
		}
		return false;
	}
	
	private Point getNextPointToMove(Trooper self, World world, Game game, Move move, Point point) {
		Point next = Utils.nextPointToTarget(world, self, point);
		if (next != null && Utils.isCellFree(next, world) && Utils.ensureCommandBonus(self, world, game, next)) {
			return next;
		} else {
			if (Math.abs(self.getX() - point.getX()) > Math.abs(self.getY() - point.getY())) {
				return getNextPointX(self, move, world, point, 0);
			} else {
				return getNextPointY(self, move, world, point, 0);
			}
		}
	}

	private Point getNextPointX(Trooper self, Move move, World world, Point point, int count) {
		if (self.getX() - point.getX() > 0) {
			if (Utils.isCellFree(self.getX() - 1, self.getY(), world) || count > 1) {
				return new Point(self.getX() - 1, self.getY());
			} else {
				return getNextPointY(self, move, world, point, count + 1);
			}
		} else {
			if (Utils.isCellFree(self.getX() + 1, self.getY(), world) || count > 1) {
				return new Point(self.getX() + 1, self.getY());
			} else {
				return getNextPointY(self, move, world, point, count + 1);
			}
		}
	}

	private Point getNextPointY(Trooper self, Move move, World world, Point point, int count) {
		if (self.getY() - point.getY() > 0) {
			if (Utils.isCellFree(self.getX(), self.getY() - 1, world) || count > 1) {
				return new Point(self.getX(), self.getY() - 1);
			} else {
				return getNextPointX(self, move, world, point, count + 1);
			}
		} else {
			if (Utils.isCellFree(self.getX(), self.getY() + 1, world) || count > 1) {
				return new Point(self.getX(), self.getY() + 1);
			} else {
				return getNextPointX(self, move, world, point, count + 1);
			}
		}
	}
	
}
