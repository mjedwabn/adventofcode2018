package io.github.mjedwabn.adventofcode2018.day15;

public class ElvesAlwaysWinGame {
	private final String map;
	private Day15 parallelWorld;
	private int elfAttackPower = 2;

	public ElvesAlwaysWinGame(String map) {
		this.map = map;
	}

	public void play() {
		do {
			parallelWorld = new Day15();
			parallelWorld.setElfAttackPower(++elfAttackPower);
			parallelWorld.parseInput(map);
			parallelWorld.play();
		} while (parallelWorld.getElfCasualties() > 0);
	}

	public int getOutcome() {
		return parallelWorld.getOutcome();
	}

	public int getElfAttackPower() {
		return elfAttackPower;
	}
}
