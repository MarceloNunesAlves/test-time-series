package br.com.flexvision.process;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.influxdb.*;
import org.influxdb.dto.Point;

public class AppInflux {

	public static void main(String[] args) {
		InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086");//, "root", "root"
		String dbName = "flex_metric";
		double value = 100;
		long timestamp = (new Date()).getTime();
		timestamp = Long.parseLong(String.valueOf(timestamp).substring(0, 10));

		try {
            influxDB.setDatabase(dbName);

            // Enable batching with a very large buffer and flush interval so writes will be triggered by our call to flush().
            influxDB.enableBatch(Integer.MAX_VALUE, Integer.MAX_VALUE, TimeUnit.HOURS);

    		for(int i=0;i<=1000;i++) {
    			Point point = Point.measurement("data_metric")
    					.time(timestamp, TimeUnit.MILLISECONDS)
    					.addField("node_id", 11200)
    					.addField("met_id", 210)
    					.addField("value", value)
    					.build();
    			influxDB.write(dbName, "autogen", point);
    			timestamp+=60;
    			value+=randomInt(9999);
    			influxDB.flush();
    		}
        } finally {
            influxDB.disableBatch();
        }
		
		System.out.println("Fim do processo!!!");
	}
	
	private static int randomInt(int valueMax){
		Random gerador = new Random();
        return gerador.nextInt(valueMax);
	}

}
