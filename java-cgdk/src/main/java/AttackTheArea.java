import java.util.EnumMap;
import java.util.List;

import model.Game;
import model.Move;
import model.Trooper;
import model.TrooperType;
import model.World;

public class AttackTheArea implements ITeamStrategy {

	private static final int AREA_RADIUS = 3;
	
	private Point areaCenterPoint;
	
	private EnumMap<TrooperType, Boolean> lockedTroopers;
	
	public AttackTheArea(Point nextPoint) {
		this.areaCenterPoint = nextPoint;
		this.lockedTroopers = new EnumMap<>(TrooperType.class);
	}

	@Override
	public boolean isComplete(Trooper self, World world, Game game, Move move) {
		return allTroopersInArea(self, world, game, move) && noEnemyTroopersInArea(self, world, game, move);
	}

	@Override
	public boolean isInvalid(Trooper self, World world, Game game, Move move) {
		return false;
	}

	@Override
	public void move(Trooper self, World world, Game game, Move move) {
		
		ActionChain actionChain = new ActionChain(self, world, game, move);
		actionChain.chain(new TryThrowGrenade());
		actionChain.chain(new TryEatFieldRation());
		actionChain.chain(new TryUseMedkit());
		actionChain.chain(new TryDown());
		actionChain.chain(new TryShoot());
		
		if (self.getType() == Utils.getMainTrooper(world, self, lockedTroopers).getType()) {
			if (Utils.getDamagedTrooper(world, self) != null && Utils.getMedic(world, self) != null && Utils.getVisibleEnemies(world, self).isEmpty()) {
				if (self.getType() == TrooperType.FIELD_MEDIC) {
					actionChain.chain(new TryHeal(self));
				} else {
					actionChain.chain(new EndTurn());
				}
			}
		} else {
			actionChain.chain(new TryWaitMedic());
		}
		
		actionChain.chain(new TryUp());
		
		Point bonusPoint = Utils.getNearestCellWithBonus(self, world, game, move);
		if (bonusPoint != null) {
			actionChain.chain(new TryMove(bonusPoint));
		}
		
		if (self.getType() == Utils.getMainTrooper(world, self, lockedTroopers).getType()) {
			List<Trooper> enemies = Utils.getVisibleEnemies(world, self);
			Trooper nearestEnemy = Utils.getNearestTrooper(enemies, self);
			if (nearestEnemy != null) {
				Point enemyPoint = new Point(nearestEnemy.getX(), nearestEnemy.getY());
				Point nextPoint = getNextPointToMove(self, world, game, move, enemyPoint);
				if (Utils.ensureCommandBonus(self, world, game, nextPoint)) {
					actionChain.chain(new TryMove(nextPoint));
				} else {
					actionChain.chain(new EndTurn());
				}
			} else {
				Point nextPoint = getNextPointToMove(self, world, game, move, areaCenterPoint);
				if (Utils.ensureCommandBonus(self, world, game, nextPoint)) {
					actionChain.chain(new TryMove(nextPoint));
				} else {
					actionChain.chain(new EndTurn());
				}
			}
		} else if (self.getType() == TrooperType.FIELD_MEDIC) {
			Trooper damagedTrooper = Utils.getDamagedTrooper(world, self);
			if (damagedTrooper != null) {
				if (Utils.trooperIsNear(damagedTrooper, self)) {
					actionChain.chain(new TryHeal(damagedTrooper));
				} else {
					Point damagedPoint = new Point(damagedTrooper.getX(), damagedTrooper.getY());
					Point nextPoint = getNextPointToMove(self, world, game, move, damagedPoint);
					actionChain.chain(new TryMove(nextPoint));
				}
			} else {
				Trooper commander = Utils.getMainTrooper(world, self, lockedTroopers);
				if (commander != null) {
					Point commanderPoint = new Point(commander.getX(), commander.getY());
					Point nextPoint = getNextPointToMove(self, world, game, move, commanderPoint);
					actionChain.chain(new TryMove(nextPoint));
				} else {
					Point nextPoint = getNextPointToMove(self, world, game, move, areaCenterPoint);
					actionChain.chain(new TryMove(nextPoint));
				}
			}
		} else {
			Trooper commander = Utils.getMainTrooper(world, self, lockedTroopers);
			if (commander != null) {
				Point commanderPoint = new Point(commander.getX(), commander.getY());
				Point nextPoint = getNextPointToMove(self, world, game, move, commanderPoint);
				actionChain.chain(new TryMove(nextPoint));
			} else {
				Point nextPoint = getNextPointToMove(self, world, game, move, areaCenterPoint);
				actionChain.chain(new TryMove(nextPoint));
			}
		}
		
		actionChain.chain(new EndTurn());
		
		actionChain.execute();
		
	}

	private Point getNextPointToMove(Trooper self, World world, Game game, Move move, Point point) {
		Point next = Utils.nextPointToTarget(world, self, point);
		if (next != null && Utils.isCellFree(next, world)) {
			lockedTroopers.put(self.getType(), false);
			return next;
		} else {
			Point free = Utils.nextPointToTarget(world, self, areaCenterPoint);
			if (free == null || !Utils.isCellFree(free, world)) {
				lockedTroopers.put(self.getType(), true);
			}
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

	private boolean allTroopersInArea(Trooper self, World world, Game game, Move move) {
		boolean result = true;
		List<Trooper> troopers = Utils.getTeam(world, self);
		for (Trooper trooper : troopers) {
			if (Utils.getDistance(areaCenterPoint, trooper) > AREA_RADIUS) {
				result = false;
			}
		}
		return result;
	}
	
	private boolean noEnemyTroopersInArea(Trooper self, World world, Game game, Move move) {
		List<Trooper> enemies = Utils.getVisibleEnemies(world, self);
		return enemies.isEmpty();
	}
	
}
