package br.com.flexvision.process;

public class RRDLine {
	
	private Long timestamp;

	private RRDData[] data;	
	
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public RRDData[] getData() {
		return data;
	}

	public void setData(RRDData[] data) {
		this.data = data;
	}
}