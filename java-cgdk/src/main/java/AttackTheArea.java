import java.util.List;

import model.Game;
import model.Move;
import model.Trooper;
import model.TrooperStance;
import model.TrooperType;
import model.World;

public class AttackTheArea implements ITeamStrategy {

	private static final int AREA_RADIUS = 3;
	
	private Point areaCenterPoint;
	
	public AttackTheArea(Point nextPoint) {
		this.areaCenterPoint = nextPoint;
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
		actionChain.chain(new TryHide());
		actionChain.chain(new TryDown());
		actionChain.chain(new TryShoot());
		
		if (self.getType() == Utils.getMainTrooper(world, self).getType()) {
			if (Utils.getOwnDamagedTrooper(world, self) != null && Utils.getMedic(world, self) != null && Utils.getVisibleEnemies(world, self).isEmpty()) {
				if (self.getType() == TrooperType.FIELD_MEDIC) {
					actionChain.chain(new TryHeal(self));
				} else {
					actionChain.chain(new EndTurn());
				}
			}
		} 
		
		actionChain.chain(new TryUp());
		
		Point bonusPoint = Utils.getNearestCellWithBonus(self, world, game, move);
		if (bonusPoint != null) {
			actionChain.chain(new TryMove(bonusPoint));
		}
		
		if (self.getType() == TrooperType.COMMANDER || self.getType() == Utils.getMainTrooper(world, self).getType()) {
			List<Trooper> enemies = Utils.getVisibleEnemies(world, self);
			Trooper nearestEnemy = Utils.getNearestTrooper(enemies, self);
			if (nearestEnemy != null) {
				Point shootingPoint = Utils.getNearestShootingPoint(self, world, game, nearestEnemy);
				if (Utils.getShootingEnemyesCount(self, world, shootingPoint, TrooperStance.STANDING) <= 1) {
					actionChain.chain(new TryMove(shootingPoint));
				} else {
					actionChain.chain(new EndTurn());
				}
			} else {
				actionChain.chain(new TryMove(areaCenterPoint));
			}
		} 
		
		if (self.getType() == TrooperType.FIELD_MEDIC) {
			Trooper damagedTrooper = Utils.getOwnDamagedTrooper(world, self);
			if (damagedTrooper != null) {
				if (Utils.trooperIsNear(damagedTrooper, self)) {
					actionChain.chain(new TryHeal(damagedTrooper));
				} else {
					actionChain.chain(new TryMove(damagedTrooper));
				}
			}
		} 
		
		if (self.getType() == TrooperType.SOLDIER) {
			List<Trooper> enemies = Utils.getVisibleEnemies(world, self);
			Trooper nearestEnemy = Utils.getNearestTrooper(enemies, self);
			if (nearestEnemy != null) {
				Point shootingPoint = Utils.getNearestShootingPoint(self, world, game, nearestEnemy);
				if (Utils.getShootingEnemyesCount(self, world, shootingPoint, TrooperStance.STANDING) <= 1) {
					actionChain.chain(new TryMove(shootingPoint));
				}
			}
		}
		
		Trooper commander = Utils.getMainTrooper(world, self);
		if (commander != null && Utils.trooperCanMoveToTarget(self, world, game, commander, areaCenterPoint)) {
			//Point freeCell = Utils.getNearestFreeCell(self, world, game, new Point(commander));
			//actionChain.chain(new TryMove(freeCell));
			actionChain.chain(new TryMove(commander));
		} else {
			Trooper head = Utils.getHeadTrooper(world, self);
			if (head != null && Utils.trooperCanMoveToTarget(self, world, game, head, areaCenterPoint)) {
				actionChain.chain(new TryMove(head));
			} else {
				actionChain.chain(new TryMove(areaCenterPoint));
			}
		}
		
		actionChain.chain(new EndTurn());
		actionChain.execute();
		
	}

	private boolean allTroopersInArea(Trooper self, World world, Game game, Move move) {
		boolean result = true;
		List<Trooper> troopers = Utils.getTeamTroopers(world, self);
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
