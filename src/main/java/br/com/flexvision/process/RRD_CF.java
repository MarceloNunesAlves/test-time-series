package br.com.flexvision.process;

public enum RRD_CF {

	/*
	 * Consolidation function.
	 */	
	AVERAGE("AVERAGE", "AVERAGE"),
	MIN("MIN", "MIN"),
	MAX("MAX", "MAX"),
	TOTAL("TOTAL", "TOTAL"),
	LAST("LAST", "LAST"),
	PERCENT("","PERCENTNAN");
	
	private String name = null;
	
	private String graphCF = null;
	
	
	private RRD_CF(String name, String graphCF){
		this.name = name;
		this.graphCF = graphCF;
	}
	
	/**
	 * PERCENT apenas valido para 
	 * consolidacao dos graficos.
	 */
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getGraphCF() {
		return graphCF;
	}
	
	public void setGraphCF(String graphCF) {
		this.graphCF = graphCF;
	}
}