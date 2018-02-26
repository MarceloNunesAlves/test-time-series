package br.com.flexvision.process;


public class RRDFetch {
	
	private String file;
	
	private RRD_CF consolidation;
	
	private long start;
	
	private long end;


	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public RRD_CF getConsolidation() {
		return consolidation;
	}

	public void setConsolidation(RRD_CF consolidation) {
		this.consolidation = consolidation;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
}
