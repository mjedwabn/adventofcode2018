package io.github.mjedwabn.adventofcode2018.day15;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

enum CreatureType {
	ELF("E"),
	GOBLIN("G");

	private final String presentableType;

	CreatureType(String presentableType) {
		this.presentableType = presentableType;
	}

	@Override
	public String toString() {
		return presentableType;
	}
}

public class Day15 {
	private static Comparator<Creature> readingOrder = Comparator.comparing(c -> c.location);
	private GameMap map;
	private int round = 0;
	private boolean combatFinished = false;
	private int elfAttackPower = 3;
	private int elfCasualties;

	public static void main(String[] args) {
		new Day15().run();
	}

	void run() {
		readInput();
		System.out.println("Initially:");
		map.print();
		play();
	}

	int getOutcome() {
		return getRound() * getHitPointsSum();
	}

	private Integer getHitPointsSum() {
		return map.creatures.stream()
				.map(c -> c.hitPoints)
				.reduce(Integer::sum)
				.orElse(0);
	}

	private int getRound() {
		return round - 1;
	}

	private void readInput() {
		try {
			final URL input = getClass().getClassLoader().getResource("input");
			final List<String> rows = Files.readAllLines(Paths.get(input.getPath()));
			map = parseMap(rows);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	GameMap parseMap(List<String> rows) {
		GameMap gameMap = new GameMap();
		gameMap.width = rows.stream().map(String::length).mapToInt(l -> l).max().orElse(0);
		gameMap.height = rows.size();
		gameMap.map = new char[gameMap.height][gameMap.width];

		for (int y = 0; y < rows.size(); y++) {
			String row = rows.get(y);
			for (int x = 0; x < row.length(); x++) {
				gameMap.map[y][x] = row.charAt(x);
				if (gameMap.map[y][x] == 'G')
					gameMap.creatures.add(new Creature(new Location(x, y), CreatureType.GOBLIN));
				else if (gameMap.map[y][x] == 'E')
					gameMap.creatures.add(Creature.makeElf(new Location(x, y), elfAttackPower));
			}
		}
		return gameMap;
	}

	void play() {
		round = 0;
		combatFinished = false;
		while (!combatFinished) {
			++round;
			System.out.printf("Round %d, fight!\n", round);
			playRound();
			System.out.printf("After round %d:\n", round);
			map.print();
			map.printCreatures();
			System.out.println("-----");
		}
	}

	private void playRound() {
		getSortedCreatures().forEach(this::makeTurn);
	}

	void makeTurn(Creature c) {
		if (c.isAlive()) {
			if (getTargets(c).isEmpty()) {
				combatFinished = true;
			}
			getTarget(c).ifPresentOrElse(t -> attack(c, t), () -> step(c));
		}
	}

	private void step(Creature c) {
		List<Creature> targets = getTargets(c);
		List<Location> inRange = map.getInRangeSquares(targets);
		List<Location> reachable = getReachable(c.location, inRange);
		Optional<Location> nearest = getNearest(c.location, reachable);

		nearest.ifPresent(loc -> {
			step(c, loc);
			getTarget(c).ifPresent(t -> attack(c, t));
		});
	}

	private void step(Creature creature, Location location) {
		System.out.printf("Step %s to %s\n", creature, location);
		map.openSquare(creature.location);
		creature.location = location;
		map.putCreature(creature);
	}

	private Optional<Creature> getTarget(Creature c) {
		return getTargets(c).stream().filter(t -> t.isAdjacent(c))
				.min(Comparator.<Creature, Integer>comparing(t1 -> t1.hitPoints)
						.thenComparing(readingOrder));
	}

	private void attack(Creature attacker, Creature target) {
		System.out.printf("%s attacks %s\n", attacker, target);
		target.hitPoints -= attacker.getAttackPower();
		if (target.hitPoints <= 0) {
			map.removeCreature(target);
			if (target.type == CreatureType.ELF)
				++elfCasualties;
			System.out.printf("Removed target %s [%d, %d]\n", target.type, target.location.x, target.location.y);
		}
	}

	private Optional<Location> getNearest(Location from, List<Location> locations) {
		final ShortestConnectionFinder finder = new ShortestConnectionFinder(map);
		finder.runDijkstra(from);

		final Map<Integer, List<List<Location>>> collect = locations.stream()
				.map(finder::findShortestPath)
				.collect(groupingBy(List::size));
		final Optional<Map.Entry<Integer, List<List<Location>>>> shortestPaths = collect.entrySet().stream()
				.min(Comparator.comparing(Map.Entry::getKey));
		return shortestPaths
				.flatMap(e -> e.getValue().stream()
						.min(Comparator.<List<Location>, Location>comparing(p -> p.get(p.size() - 1))
								.thenComparing(p -> p.get(0))))
				.map(p -> p.get(0));
	}

	private List<Location> getReachable(Location from, List<Location> locations) {
		return locations.stream().filter(loc -> isReachable(from, loc)).collect(toList());
	}

	private boolean isReachable(Location from, Location to) {
		return new ConnectionFinder(map).exists(from, to);
	}

	private List<Creature> getTargets(Creature c) {
		return getSortedCreatures().stream()
				.filter(t -> t.type != c.type)
				.collect(toList());
	}

	List<Creature> getSortedCreatures() {
		return map.creatures.stream()
				.filter(Creature::isAlive)
				.sorted(readingOrder)
				.collect(toList());
	}

	public void parseInput(String input) {
		map = parseMap(Arrays.stream(input.split("\n"))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(toList()));
	}

	public int getElfCasualties() {
		return elfCasualties;
	}

	public void setElfAttackPower(int elfAttackPower) {
		this.elfAttackPower = elfAttackPower;
	}

	static class ShortestConnectionFinder {
		private final GameMap map;
		private List<Location> queue = new ArrayList<>();
		private Map<Location, Integer> dist = new HashMap<>();
		private Map<Location, Location> prev = new HashMap<>();

		public ShortestConnectionFinder(GameMap map) {
			this.map = map;
		}

		private List<Location> getNeighbours(Location u) {
			return map.getAdjacent(u);
		}

		public List<Location> findShortestPath(Location to) {
			List<Location> path = new ArrayList<>();
			Location tmp = to;
			while (prev.containsKey(tmp)) {
				path.add(tmp);
				tmp = prev.get(tmp);
			}
			Collections.reverse(path);
			return path;
		}

		void runDijkstra(Location from) {
			for (Location location : map.getLocations(from)) {
				dist.put(location, Integer.MAX_VALUE);
				queue.add(location);
			}
			dist.put(from, 0);

			while (!queue.isEmpty()) {
				dist.entrySet().stream()
						.filter(v -> queue.contains(v.getKey()))
						.min(Comparator.comparing(Map.Entry::getValue))
						.map(Map.Entry::getKey)
						.ifPresent(u -> {
							queue.remove(u);
							getNeighbours(u).stream()
									.filter(a -> queue.contains(a))
									.forEach(v -> {
										int alt = dist.get(u) + getLength(u, v);
										if (alt < dist.get(v) || (alt == dist.get(v) && u.compareTo(prev.get(v)) < 0)) {
											dist.put(v, alt);
											prev.put(v, u);
										}
									});
						});
			}
		}

		private int getLength(Location from, Location to) {
			return 1;
		}
	}

	static class ConnectionFinder {
		private final GameMap map;
		private Set<Location> visited = new HashSet<>();

		ConnectionFinder(GameMap map) {
			this.map = map;
		}

		boolean exists(Location from, Location to) {
			visited = new HashSet<>();
			return connectionExists(from, to);
		}

		private boolean connectionExists(Location from, Location to) {
			return from.equals(to) || map.getAdjacent(from).stream()
					.filter(a1 -> !visited.contains(a1))
					.peek(visited::add)
					.anyMatch(a -> connectionExists(a, to));
		}
	}

	class GameMap {
		private char[][] map;
		private int height;
		private int width;
		private List<Creature> creatures = new ArrayList<>();

		List<Location> getAdjacent(Creature creature) {
			return getAdjacent(creature.location);
		}

		List<Location> getAdjacent(Location location) {
			final int x = location.x;
			final int y = location.y;
			return Stream
					.of(new Location(x - 1, y), new Location(x + 1, y),
							new Location(x, y - 1), new Location(x, y + 1))
					.filter(this::isOpenSquare)
					.collect(toList());
		}

		private boolean isOpenSquare(Location loc) {
			return isOpenSquare(loc.x, loc.y);
		}

		void print() {
			for (char[] chars : map) {
				System.out.println(chars);
			}
		}

		private List<Location> getInRangeSquares(List<Creature> targets) {
			return targets.stream()
					.flatMap(t -> getAdjacent(t).stream())
					.collect(toList());
		}

		List<Location> getLocations(Location source) {
			return IntStream.range(0, height).boxed()
					.flatMap(y -> IntStream.range(0, width)
							.filter(x -> isOpenSquare(x, y) || (x == source.x && y == source.y))
							.mapToObj(x -> new Location(x, y)))
					.collect(toList());
		}

		private boolean isOpenSquare(int x, int y) {
			return map[y][x] == '.';
		}

		void openSquare(Location location) {
			map[location.y][location.x] = '.';
		}

		void putCreature(Creature creature) {
			map[creature.location.y][creature.location.x] = creature.type == CreatureType.ELF ? 'E' : 'G';
		}

		void removeCreature(Creature creature) {
			map[creature.location.y][creature.location.x] = '.';
			creatures.remove(creature);
		}

		void printCreatures() {
			creatures.stream().sorted(readingOrder).forEach(System.out::println);
		}
	}
}

class Location implements Comparable<Location> {
	final int x;
	final int y;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Location location = (Location) o;
		return x == location.x &&
				y == location.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + ']';
	}

	boolean isAdjacent(Location location) {
		return (Math.abs(x - location.x) == 1 && y == location.y)
				|| (x == location.x && Math.abs(y - location.y) == 1);
	}

	@Override
	public int compareTo(Location o) {
		final int yDiff = y - o.y;
		return yDiff == 0 ? x - o.x : yDiff;
	}
}

class Creature {
	int hitPoints = 200;
	Location location;
	CreatureType type;
	private int attackPower = 3;

	Creature(Location location, CreatureType type) {
		this.location = location;
		this.type = type;
	}

	static Creature makeElf(Location location, int elfAttackPower) {
		final Creature elf = new Creature(location, CreatureType.ELF);
		elf.attackPower = elfAttackPower;
		return elf;
	}

	boolean isAdjacent(Creature creature) {
		return location.isAdjacent(creature.location);
	}

	boolean isAlive() {
		return hitPoints > 0;
	}

	@Override
	public String toString() {
		return type + "(" + hitPoints + ") " + location;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Creature creature = (Creature) o;
		return Objects.equals(location, creature.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(location);
	}

	public int getAttackPower() {
		return attackPower;
	}
}