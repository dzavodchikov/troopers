


import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import model.Bonus;
import model.CellType;
import model.Game;
import model.Move;
import model.Trooper;
import model.TrooperStance;
import model.TrooperType;
import model.World;

public class Utils {
	
	public static int getMoveCosts(TrooperStance stance, Game game) {
		switch (stance) {
		case STANDING:
			return game.getStandingMoveCost();
		case KNEELING:
			return game.getKneelingMoveCost();
		case PRONE:
			return game.getProneMoveCost();
		default:
			return 6;
		}
	}
	
	public static TrooperStance getLowerStance(TrooperStance stance) {
		switch (stance) {
		case STANDING:
			return TrooperStance.KNEELING;
		case KNEELING:
			return TrooperStance.PRONE;
		default:
			return TrooperStance.PRONE;
		}
	}

	public static Trooper getMainTrooper(World world, Trooper self) {
		
		TrooperType[] typesOrder = new TrooperType[] {TrooperType.SCOUT, TrooperType.COMMANDER, 
			TrooperType.SNIPER, TrooperType.SOLDIER, TrooperType.FIELD_MEDIC};
		
		for (TrooperType trooperType : typesOrder) {
			Trooper trooper = Utils.getOwnTrooperByType(world, self, trooperType);
			if (trooper != null) {
				return trooper;
			}
		}
		
		return null;
		
	}
	
	public static Trooper getMedic(World world, Trooper self) {
		return Utils.getOwnTrooperByType(world, self, TrooperType.FIELD_MEDIC);
	}
	
	public static Trooper getSniper(World world, Trooper self) {
		return Utils.getOwnTrooperByType(world, self, TrooperType.SNIPER);
	}
	
	public static Trooper getSoldier(World world, Trooper self) {
		return Utils.getOwnTrooperByType(world, self, TrooperType.SOLDIER);
	}
	
	public static Trooper getCommander(World world, Trooper self) {
		return Utils.getOwnTrooperByType(world, self, TrooperType.COMMANDER);
	}
	
	public static Trooper getScout(World world, Trooper self) {
		return Utils.getOwnTrooperByType(world, self, TrooperType.SCOUT);
	}
	
	public static Trooper getOwnTrooperByType(World world, Trooper self, TrooperType type) {
		Trooper[] troopers = world.getTroopers();
		for (Trooper trooper : troopers) {
			if (self.getPlayerId() == trooper.getPlayerId()) {
				if (trooper.getType() == type) {
					return trooper;
				}
			}
		}
		return null;
	}
	
	public static Trooper getOwnDamagedTrooper(World world, Trooper self) {
		
		TrooperType[] typesOrder = new TrooperType[] {TrooperType.SCOUT, TrooperType.COMMANDER, 
				TrooperType.SNIPER, TrooperType.SOLDIER, TrooperType.FIELD_MEDIC};
			
		for (TrooperType trooperType : typesOrder) {
			Trooper trooper = Utils.getOwnTrooperByType(world, self, trooperType);
			if (trooper != null && trooper.getHitpoints() < 100) {
				return trooper;
			}
		}
		return null;
	}
	
	
	public static List<Trooper> getVisibleEnemies(World world, Trooper self) {
		Trooper[] troopers = world.getTroopers();
		List<Trooper> enemies = new ArrayList<>();
		for (Trooper trooper : troopers) {
			if (self.getPlayerId() != trooper.getPlayerId()) {
				enemies.add(trooper);
			}
		}
		return enemies;
	}
	
	public static List<Trooper> getTeamTroopers(World world, Trooper self) {
		Trooper[] troopers = world.getTroopers();
		List<Trooper> team = new ArrayList<>();
		for (Trooper trooper : troopers) {
			if (self.getPlayerId() == trooper.getPlayerId()) {
				team.add(trooper);
			}
		}
		return team;
	}
	
	public static Point nextPointToTarget(World world, Trooper self, Point target) {
		
		int[][] sea = new int[world.getWidth()][world.getHeight()];
		
		// mark walls
		for(int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				if (world.getCells()[x][y] != CellType.FREE) {
					sea[x][y] = -1;
				}
			}
		}
		
		// mark units
		for (Trooper trooper : world.getTroopers()) {
			sea[trooper.getX()][trooper.getY()] = -1;
		}
		
		// mark target
		sea[target.getX()][target.getY()] = 0;
		sea[self.getX()][self.getY()] = 1;
		
		Queue<Point> points = new LinkedList<>();
		points.offer(new Point(self.getX(), self.getY()));
		
		while(!points.isEmpty()) {

			Point point = points.poll();

			int x = point.getX();
			int y = point.getY();
			int value = sea[x][y];
			
			if (x < world.getWidth() - 1 && sea[x + 1][y] == 0) {
				sea[x + 1][y] = value + 1;
				points.offer(new Point(x + 1, y));
			}
			if (x > 0 && sea[x - 1][y] == 0) {
				sea[x - 1][y] = value + 1;
				points.offer(new Point(x - 1, y));
			}
			if (y < world.getHeight()-1 && sea[x][y + 1] == 0) {
				sea[x][y + 1] = value + 1;
				points.offer(new Point(x, y + 1));
			}
			if (y > 0 && sea[x][y - 1] == 0) {
				sea[x][y - 1] = value + 1;
				points.offer(new Point(x, y - 1));
			}

		}
		
		int finishValue = sea[target.getX()][target.getY()];
		
		// check end cell
		if (finishValue == -1 || finishValue == 0 || finishValue == 1) {
			return null;
		} else {
			Point curPoint = new Point(target.getX(), target.getY());
			while(finishValue != 2) {
				if (curPoint.getX() > 0 && sea[curPoint.getX()-1][curPoint.getY()] == finishValue - 1) {
					finishValue--;
					curPoint = new Point(curPoint.getX()-1, curPoint.getY());
					continue;
				}
				if (curPoint.getX() < world.getWidth()-1 && sea[curPoint.getX()+1][curPoint.getY()] == finishValue - 1) {
					finishValue--;
					curPoint = new Point(curPoint.getX()+1, curPoint.getY());
					continue;
				}
				if (curPoint.getY() > 0 && sea[curPoint.getX()][curPoint.getY()-1] == finishValue - 1) {
					finishValue--;
					curPoint = new Point(curPoint.getX(), curPoint.getY()-1);
					continue;
				}
				if (curPoint.getY() < world.getHeight()-1 && sea[curPoint.getX()][curPoint.getY()+1] == finishValue - 1) {
					finishValue--;
					curPoint = new Point(curPoint.getX(), curPoint.getY()+1);
					continue;
				}
			}
			return curPoint;
		}

	}
	
	public static boolean isPointValid(int x, int y, World world) {
		if (x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight() && world.getCells()[x][y] == CellType.FREE)
			return true;
		else 
			return false;
	}
	
	public static double getDistance(int width, int height) {
		return StrictMath.hypot(width, height);
	}
	
	public static double getDistance(Point p1, Point p2) {
		return StrictMath.hypot(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
	}
	
	public static double getDistance(Point p, Trooper t) {
		return StrictMath.hypot(Math.abs(p.getX() - t.getX()), Math.abs(p.getY() - t.getY()));
	}
	
	public static double getDistance(Trooper t1, Trooper t2) {
		return StrictMath.hypot(Math.abs(t1.getX() - t2.getX()), Math.abs(t1.getY() - t2.getY()));
	}
	
	public static Trooper getNearestTrooper(List<Trooper> troopers, Trooper self) {
		Trooper nearest = null;
		for (Trooper trooper : troopers) {
			if (nearest == null || getDistance(trooper, self) < getDistance(nearest, self)) {
				nearest = trooper;
			}
		}
		return nearest;
	}
	
	public static Point getNearestPoint(List<Point> points, Trooper self) {
		Point nearest = null;
		for (Point point : points) {
			if (nearest == null || getDistance(point, self) < getDistance(nearest, self)) {
				nearest = point;
			}
		}
		return nearest;
	}
	
	public static boolean ensureCommandBonus(Trooper self, World world, Game game, Point point) {
		Trooper commander = Utils.getCommander(world, self);
		if (commander == null) {
			return true;
		}
		List<Trooper> team = getTeamTroopers(world, self);
		boolean result = true;
		for (Trooper trooper : team) {
			if (self.getType() == commander.getType()) {
				if (getDistance(point, trooper) > game.getCommanderAuraRange()) {
					return false;
				}
			}
		}
		return result;
	}
	
	public static boolean trooperIsNear(Trooper first, Trooper second) {
		if (getDistance(first, second) <= 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean pointIsNear(Point point, Trooper trooper) {
		if (getDistance(point, trooper) <= 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isCellFree(Point p, World world) {
		return isCellFree(p.getX(), p.getY(), world);
	}
	
	public static boolean isCellFree(int x, int y, World world) {
		if (x < 0 || x >= world.getWidth() || y < 0 || y >= world.getHeight()) {
			return false;
		}
		if (world.getCells()[x][y] != CellType.FREE) {
			return false;
		}
		Trooper[] troopers = world.getTroopers();
		for (Trooper trooper : troopers) {
			if (trooper.getX() == x && trooper.getY() == y) {
				return false;
			}
		}
		return true;
	}

	public static Point getNearestCellWithBonus(Trooper trooper, World world, Game game, Move move) {
		Bonus[] bonuses = world.getBonuses();
		for (Bonus bonus : bonuses) {
			Point bonusPoint = new Point(bonus.getX(), bonus.getY()); 
			if (pointIsNear(bonusPoint, trooper) && trooperDoesNotHaveBonus(trooper, bonus) && isCellFree(bonus.getX(), bonus.getY(), world)) {
				return bonusPoint;
			}
		}
		return null;
	}

	public static boolean trooperDoesNotHaveBonus(Trooper trooper, Bonus bonus) {
		switch (bonus.getType()) {
		case MEDIKIT:
			return trooper.isHoldingMedikit() ? false : true;
		case GRENADE:
			return trooper.isHoldingGrenade() ? false : true;
		case FIELD_RATION:
			return trooper.isHoldingFieldRation() ? false : true;
		default:
			return false;
		}
	}
	
	public static Trooper getWeakestVisibleTrooperInRange(Trooper self, World world, Game game, Move move, double range, TrooperStance stance) {
		Trooper target = null;
		List<Trooper> enemies = Utils.getVisibleEnemies(world, self);
		for (Trooper trooper : enemies) {
			if (world.isVisible(range, self.getX(), self.getY(), stance, trooper.getX(), trooper.getY(), trooper.getStance())) {
				if (target == null || target.getHitpoints() > trooper.getHitpoints()){
					target = trooper;
				}
			}
		}
		return target;
	}
	
	public static Trooper getWeakestTrooperInRange(Trooper self, World world, Game game, Move move, double range, TrooperStance stance) {
		Trooper target = null;
		List<Trooper> enemies = Utils.getVisibleEnemies(world, self);
		for (Trooper trooper : enemies) {
			if (getDistance(self, trooper) <= range) {
				if (target == null || target.getHitpoints() > trooper.getHitpoints()){
					target = trooper;
				}
			}
		}
		return target;
	}

	public static boolean trooperCanMoveToTarget(Trooper self, World world, Game game, Trooper trooper, Point target) {
		Point next = Utils.nextPointToTarget(world, trooper, target);
		if (next != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Point getNextPointToMove(Trooper trooper, World world, Game game, Point point) {
		Point next = Utils.nextPointToTarget(world, trooper, point);
		if (next != null && Utils.isCellFree(next, world) && Utils.ensureCommandBonus(trooper, world, game, next)) {
			return next;
		} else {
			return null;
		}
	}

	public static Point getNearestFreeCell(Trooper self, World world, Game game, Point target) {
		Point free = null;
		int min = Integer.MAX_VALUE;
		List<Point> points = getPointsInRadius(self, world, game, target, 2);
		for (Point point : points) {
			if (stepsFromTo(self, world, game, point) < min) {
				free = point;
			}
		}
		return free;
	}

	private static int stepsFromTo(Trooper self, World world, Game game, Point target) {
		
		int[][] sea = new int[world.getWidth()][world.getHeight()];
		
		// mark walls
		for(int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				if (world.getCells()[x][y] != CellType.FREE) {
					sea[x][y] = -1;
				}
			}
		}
		
		// mark units
		for (Trooper trooper : world.getTroopers()) {
			sea[trooper.getX()][trooper.getY()] = -1;
		}
		
		// mark target
		sea[target.getX()][target.getY()] = 0;
		sea[self.getX()][self.getY()] = 1;
		
		Queue<Point> points = new LinkedList<>();
		points.offer(new Point(self.getX(), self.getY()));
		
		while(!points.isEmpty()) {

			Point point = points.poll();

			int x = point.getX();
			int y = point.getY();
			int value = sea[x][y];
			
			if (x < world.getWidth() - 1 && sea[x + 1][y] == 0) {
				sea[x + 1][y] = value + 1;
				Point p = new Point(x + 1, y);
				points.offer(p);
				if (p.equals(target)) {
					return value;
				}
			}
			if (x > 0 && sea[x - 1][y] == 0) {
				sea[x - 1][y] = value + 1;
				Point p = new Point(x - 1, y);
				points.offer(p);
				if (p.equals(target)) {
					return value;
				}
			}
			if (y < world.getHeight()-1 && sea[x][y + 1] == 0) {
				sea[x][y + 1] = value + 1;
				Point p = new Point(x, y + 1);
				points.offer(p);
				if (p.equals(target)) {
					return value;
				}
			}
			if (y > 0 && sea[x][y - 1] == 0) {
				sea[x][y - 1] = value + 1;
				Point p = new Point(x, y - 1);
				points.offer(p);
				if (p.equals(target)) {
					return value;
				}
			}

		}
		
		return Integer.MAX_VALUE;
	}

	private static List<Point> getPointsInRadius(Trooper self, World world, Game game, Point target, int r) {

		int[][] sea = new int[world.getWidth()][world.getHeight()];
		
		// mark walls
		for(int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				if (world.getCells()[x][y] != CellType.FREE) {
					sea[x][y] = -1;
				}
			}
		}
		
		// mark units
		for (Trooper trooper : world.getTroopers()) {
			sea[trooper.getX()][trooper.getY()] = -1;
		}
		
		// mark target
		sea[target.getX()][target.getY()] = 1;
		
		List<Point> result = new ArrayList<>();
		
		Queue<Point> points = new LinkedList<>();
		points.offer(new Point(self.getX(), self.getY()));
		
		while(!points.isEmpty()) {

			Point point = points.poll();
			result.add(point);
			
			int x = point.getX();
			int y = point.getY();
			int value = sea[x][y];
			
			if (x < world.getWidth() - 1 && sea[x + 1][y] == 0) {
				sea[x + 1][y] = value + 1;
				Point p = new Point(x + 1, y);
				if (value + 1 <= r) {
					points.offer(p);
				}
			}
			if (x > 0 && sea[x - 1][y] == 0) {
				sea[x - 1][y] = value + 1;
				Point p = new Point(x - 1, y);
				if (value + 1 <= r) {
					points.offer(p);
				}
			}
			if (y < world.getHeight()-1 && sea[x][y + 1] == 0) {
				sea[x][y + 1] = value + 1;
				Point p = new Point(x, y + 1);
				if (value + 1 <= r) {
					points.offer(p);
				}
			}
			if (y > 0 && sea[x][y - 1] == 0) {
				sea[x][y - 1] = value + 1;
				Point p = new Point(x, y - 1);
				if (value + 1 <= r) {
					points.offer(p);
				}
			}

		}
		
		return result;
		
	}

	public static Trooper getHeadTrooper(World world, Trooper self) {
		TrooperType[] typesOrder = new TrooperType[] {TrooperType.SCOUT, TrooperType.COMMANDER, 
				TrooperType.SNIPER, TrooperType.SOLDIER, TrooperType.FIELD_MEDIC};
			
		Trooper head = null;
		
		for (TrooperType trooperType : typesOrder) {
			if (trooperType == self.getType()) {
				return head;
			}
			Trooper trooper = Utils.getOwnTrooperByType(world, self, trooperType);
			if (trooper != null) {
				head = trooper;
			}
		}
		
		return head;
	}

	public static Point getNearestShootingPoint(Trooper self, World world, Game game, Trooper target) {

		int[][] sea = new int[world.getWidth()][world.getHeight()];
		
		// mark walls
		for(int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				if (world.getCells()[x][y] != CellType.FREE) {
					sea[x][y] = -1;
				}
			}
		}
		
		// mark units
		for (Trooper trooper : world.getTroopers()) {
			sea[trooper.getX()][trooper.getY()] = -1;
		}
		
		// mark self
		sea[self.getX()][self.getY()] = 1;
		
		Queue<Point> points = new LinkedList<>();
		points.offer(new Point(self.getX(), self.getY()));
		
		while(!points.isEmpty()) {

			Point point = points.poll();

			int x = point.getX();
			int y = point.getY();
			int value = sea[x][y];
			
			if (x < world.getWidth() - 1 && sea[x + 1][y] == 0) {
				sea[x + 1][y] = value + 1;
				Point p = new Point(x + 1, y);
				points.offer(p);
				if (world.isVisible(self.getShootingRange(), p.getX(), p.getY(), TrooperStance.STANDING, target.getX(), target.getY(), target.getStance())) {
					return p;
				}
			}
			if (x > 0 && sea[x - 1][y] == 0) {
				sea[x - 1][y] = value + 1;
				Point p = new Point(x - 1, y);
				points.offer(p);
				if (world.isVisible(self.getShootingRange(), p.getX(), p.getY(), TrooperStance.STANDING, target.getX(), target.getY(), target.getStance())) {
					return p;
				}
			}
			if (y < world.getHeight()-1 && sea[x][y + 1] == 0) {
				sea[x][y + 1] = value + 1;
				Point p = new Point(x, y + 1);
				points.offer(p);
				if (world.isVisible(self.getShootingRange(), p.getX(), p.getY(), TrooperStance.STANDING, target.getX(), target.getY(), target.getStance())) {
					return p;
				}
			}
			if (y > 0 && sea[x][y - 1] == 0) {
				sea[x][y - 1] = value + 1;
				Point p = new Point(x, y - 1);
				points.offer(p);
				if (world.isVisible(self.getShootingRange(), p.getX(), p.getY(), TrooperStance.STANDING, target.getX(), target.getY(), target.getStance())) {
					return p;
				}
			}

		}
		
		return null;
	}
	
}
