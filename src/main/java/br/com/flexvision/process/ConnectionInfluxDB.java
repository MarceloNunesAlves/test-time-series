package br.com.flexvision.process;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

public class ConnectionInfluxDB {
	
	private static ConnectionInfluxDB instance;

	private InfluxDB influxDB;
	
	public ConnectionInfluxDB() {
		influxDB = InfluxDBFactory.connect("http://localhost:8086");
		String dbName = "flex_metric";
        influxDB.setDatabase(dbName);
        // Enable batching with a very large buffer and flush interval so writes will be triggered by our call to flush().
        
	}

	public static ConnectionInfluxDB getInstance() {
		if (instance==null)
			instance = new ConnectionInfluxDB();
		return instance;
	}

	public InfluxDB getInfluxDB() {
		return influxDB;
	}

	public void setInfluxDB(InfluxDB influxDB) {
		this.influxDB = influxDB;
	}
	
}
