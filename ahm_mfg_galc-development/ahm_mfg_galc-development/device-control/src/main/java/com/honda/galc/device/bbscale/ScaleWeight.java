package com.honda.galc.device.bbscale;


public class ScaleWeight {
	private double weight = 0.0D;
	private boolean stable = true;
	private Corner corner;
	
	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public boolean isStable() {
		return stable;
	}


	public void setStable(boolean stable) {
		this.stable = stable;
	}


	public Corner getCorner() {
		return corner;
	}


	public void setCorner(Corner corner) {
		this.corner = corner;
	}
}
