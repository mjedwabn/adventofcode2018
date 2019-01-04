package io.github.mjedwabn.adventofcode2018.day13;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day13 {
	private char[][] map;
	List<Cart> carts;
	private Location firstCollision = null;

	public Day13() {
		readInput();
		carts = findCarts();
//		print();
//		print(carts);
//		final List<Cart> sortedCarts = getSortedCarts();
	}

	private boolean collisionOccurred() {
		return carts.stream().distinct().count() != carts.size();
	}

	private void moveCarts() {
		getSortedCarts().forEach(cart -> {
			final Location previous = cart.location;
			if (!collisionOccurred())
				moveCart(cart);

//			System.out.println("move");
//			printMove(previous, cart);
		});
	}

	private Location moveCartsTillLastStanding() {
		getSortedCarts().forEach(cart -> {
			if (carts.size() > 1) {
				moveCart(cart);
				if (collisionOccurred())
					removeCollidedCarts();
			}
		});

		return carts.get(0).location;
	}

	private void removeCollidedCarts() {
		final Map<Location, List<Cart>> collisions = carts.stream().collect(Collectors.groupingBy(c -> c.location));
		final Comparator<Map.Entry<Location, List<Cart>>> comparator = Comparator.comparing(e -> e.getValue().size());
		final Comparator<Map.Entry<Location, List<Cart>>> reversed = comparator.reversed();
		carts = collisions.entrySet().stream().sorted(reversed).skip(1).flatMap(e -> e.getValue().stream()).collect(Collectors.toList());
	}

	private void printMove(Location previous, Cart cart) {
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				if (previous.x == x && previous.y == y)
					System.out.print(".");
				else if (cart.location.x == x && cart.location.y == y)
					System.out.print(cart.direction);
				else
					System.out.print(map[y][x]);
			}
			System.out.println();
		}
	}

	private void moveCart(Cart cart) {
		Location nextLocation = getNextLocation(cart);
		char nextDirection = getNextDirection(cart, nextLocation);
		if (isIntersection(nextLocation))
			cart.lastDecision = makeNextDecision(cart);
		cart.direction = nextDirection;
		cart.location = nextLocation;
	}

	private Location getNextLocation(Cart cart) {
		if (cart.direction == '<')
			return new Location(cart.location.x - 1, cart.location.y);
		else if (cart.direction == '>')
			return new Location(cart.location.x + 1, cart.location.y);
		else if (cart.direction == '^')
			return new Location(cart.location.x, cart.location.y - 1);
		else
			return new Location(cart.location.x, cart.location.y + 1);
	}

	private char getNextDirection(Cart cart, Location nextLocation) {
		if (isCurve(nextLocation)) {
			final char curve = map[nextLocation.y][nextLocation.x];
			if (curve == '/') {
				if (cart.direction == '<') {
					return 'v';
				}
				else if (cart.direction == '^') {
					return '>';
				}
				else if (cart.direction == '>') {
					return '^';
				}
				else {
					return '<';
				}
			}
			// curve == \
			else {
				if (cart.direction == '<') {
					return '^';
				}
				else if (cart.direction == '^') {
					return '<';
				}
				else if (cart.direction == '>') {
					return 'v';
				}
				else {
					return '>';
				}
			}
		}
		else if (isIntersection(nextLocation)) {
			if (cart.lastDecision == '0' || cart.lastDecision == 'R')
				return turnLeft(cart.direction);
			else if (cart.lastDecision == 'L')
				return goStraigth(cart.direction);
			else
				return turnRight(cart.direction);
		}
		else
			return cart.direction;
	}

	private char makeNextDecision(Cart cart) {
		if (cart.lastDecision == '0' || cart.lastDecision == 'R')
			return 'L';
		else if (cart.lastDecision == 'L')
			return 'S';
		else
			return 'R';
	}

	private char turnRight(char direction) {
		if (direction == '<')
			return '^';
		else if (direction == '>')
			return 'v';
		else if (direction == '^')
			return '>';
		else
			return '<';
	}

	private char goStraigth(char direction) {
		return direction;
	}

	private char turnLeft(char direction) {
		if (direction == '<')
			return 'v';
		else if (direction == '>')
			return '^';
		else if (direction == '^')
			return '<';
		else
			return '>';
	}

	private boolean isIntersection(Location location) {
		return map[location.y][location.x] == '+';
	}

	private boolean isCurve(Location location) {
		return map[location.y][location.x] == '/' || map[location.y][location.x] == '\\';
	}

	private void print(List<Cart> carts) {
		System.out.println(carts);
	}

	private List<Cart> getSortedCarts() {
		return carts.stream()
				.sorted(Comparator.<Cart, Integer>comparing(c -> c.location.y)
						.thenComparing(c -> c.location.x))
				.collect(Collectors.toList());
	}

	private List<Cart> findCarts() {
		List<Cart> carts = new ArrayList<>();
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				final char location = map[y][x];
				if (location == '<' || location == '>' || location == '^' || location == 'v')
					carts.add(new Cart(new Location(x, y), location));
			}
		}
		return carts;
	}

	public void print() {
		for (int y = 0; y < map.length; y++) {
			System.out.println(map[y]);
		}
	}

	private void readInput() {
		try {
			final URL input = getClass().getClassLoader().getResource("input2");
			final List<String> rows = Files.readAllLines(Paths.get(input.getPath()));
			final int maxWidth = rows.stream().map(String::length).mapToInt(l -> l).max().orElse(0);
			int height = rows.size();
			map = new char[height][maxWidth];
			for (int y = 0; y < rows.size(); y++) {
				String row = rows.get(y);
				for (int i = 0; i < maxWidth; i++) {
					map[y][i] = ' ';
				}
				for (int x = 0; x < row.length(); x++) {
					map[y][x] = row.charAt(x);
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Day13();
	}

	public Location getLocationOfFirstCollision() {
		while (!collisionOccurred()) {
			moveCarts();
		}
		final Map<Location, List<Cart>> collisions = carts.stream().collect(Collectors.groupingBy(c -> c.location));
		final Comparator<Map.Entry<Location, List<Cart>>> comparator = Comparator.comparing(e -> e.getValue().size());
		final Comparator<Map.Entry<Location, List<Cart>>> reversed = comparator.reversed();
		firstCollision = collisions.entrySet().stream().sorted(reversed).findFirst().map(e -> e.getKey()).orElse(null);
		return firstCollision;
	}

	public Location getLocationOfLastCart() {
		while (carts.size() > 1) {
			moveCartsTillLastStanding();
		}
		moveCart(carts.get(0));
		return carts.get(0).location;
	}
}

class Location {
	public final int x;
	public final int y;

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
		return "Location{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}

class Cart {
	Location location;
	char direction;
	char lastDecision = '0';

	public Cart(Location location, char direction) {
		this.location = location;
		this.direction = direction;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cart cart = (Cart) o;
		return Objects.equals(location, cart.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(location);
	}

	@Override
	public String toString() {
		return "Cart{" +
				"location=" + location +
				", direction=" + direction +
				'}';
	}
}