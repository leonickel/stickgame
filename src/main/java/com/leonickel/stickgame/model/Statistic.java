package com.leonickel.stickgame.model;

public class Statistic {

	private int totalWon;
	private int totalLost;
	
	public Statistic() {
	}
	
	public Statistic(int totalWon, int totalLost) {
		this.totalWon = totalWon;
		this.totalLost = totalLost;
	}
	
	public int getTotalWon() {
		return totalWon;
	}
	public void setTotalWon(int totalWon) {
		this.totalWon = totalWon;
	}
	public int getTotalLost() {
		return totalLost;
	}
	public void setTotalLost(int totalLost) {
		this.totalLost = totalLost;
	}
}