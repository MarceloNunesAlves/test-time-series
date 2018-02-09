package br.com.flexvision.process;

public class Archive {

	private String consolidation;
	private int rows;
	private int steps;
	private double xff;
	
	
	public Archive() {
	}
	
	public Archive(String consolidation, int rows, int steps, double xff) {
		this.consolidation = consolidation;
		this.rows = rows;
		this.steps = steps;
		this.xff = xff;
	}

	public String getConsolidation() {
		return consolidation;
	}
	public void setConsolidation(String consolidation) {
		this.consolidation = consolidation;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getSteps() {
		return steps;
	}
	public void setSteps(int steps) {
		this.steps = steps;
	}
	public double getXff() {
		return xff;
	}
	public void setXff(double xff) {
		this.xff = xff;
	}

}
