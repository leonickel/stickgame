package com.leonickel.stickgame.model;

public class GlobalStatistic extends Statistic {

	private double wonRatio;

	public GlobalStatistic(int totalWon, int totalLost, double wonRatio) {
		super(totalWon, totalLost);
		this.wonRatio = wonRatio;
	}
	
	public double getWonRatio() {
		return wonRatio;
	}
}