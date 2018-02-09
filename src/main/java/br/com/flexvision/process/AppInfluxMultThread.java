package br.com.flexvision.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.influxdb.*;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

public class AppInfluxMultThread {
	
	public static int COUNT_INSERTS=0;

	public static void main(String[] args) {
		AppInfluxMultThread app = new AppInfluxMultThread();
		System.out.println("Inicio do processo!!! " + new Date());
		app.exec();
		System.out.println("Fim do processo!!! -> " + new Date() + " - Total: " + COUNT_INSERTS);
	}
	
	public void exec() {
		ConnectionInfluxDB conn = ConnectionInfluxDB.getInstance();
		InfluxDB influxDB = conn.getInfluxDB();
		
		ThreadFactory influxNamedThreadFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "influx-test-paralelo");
			}
		};
		ExecutorService executor = Executors.newCachedThreadPool(influxNamedThreadFactory);
		
		influxDB.enableBatch(Integer.MAX_VALUE, Integer.MAX_VALUE, TimeUnit.SECONDS, influxNamedThreadFactory);
		
        List<Future<Boolean>> list = new ArrayList<Future<Boolean>>();
        for(int i=1;i<=2;i++){//10000
            Future<Boolean> future = executor.submit(new ThreadInflux(i));
            list.add(future);
        }
        for(Future<Boolean> fut : list){
        	try {
        		fut.get();
				//System.out.println(new Date()+ "::"+fut.get());
			} catch (InterruptedException | ExecutionException e) {
				System.out.println("Erro na thread: "+e.getMessage());
			}
        }
        executor.shutdown();
		influxDB.disableBatch();
	}
	
	public class ThreadInflux implements Callable<Boolean>{
		
		private int nod_id;
		
		public ThreadInflux(int nod_id) {
			super();
			this.nod_id = nod_id;
		}

		public Boolean call() throws Exception {
			String dbName = "flex_metric";
			
			ConnectionInfluxDB conn = ConnectionInfluxDB.getInstance();
			InfluxDB influxDB = conn.getInfluxDB();
			/*
			InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");
			influxDB.setDatabase(dbName);
	        */
			double value = 100;
			long timestamp = (new Date()).getTime();
			timestamp = Long.parseLong(String.valueOf(timestamp).substring(0, 10));
			
			BatchPoints batchPoints = BatchPoints.database(dbName).build();

    		for(int nod_dif=1;nod_dif<=3;nod_dif++) {//30
    	        // Enable batching with a very large buffer and flush interval so writes will be triggered by our call to flush().
    	        //influxDB.enableBatch(Integer.MAX_VALUE, Integer.MAX_VALUE, TimeUnit.SECONDS);

    			try {
					for(int i=1;i<=10;i++) {
		    			Point point = Point.measurement("data_metric")
		    					.time(timestamp, TimeUnit.MILLISECONDS)
		    					.addField("node_id", nod_id)
		    					.addField("met_id", 210)
		    					.addField("value", value)
		    					.build();
		    			//influxDB.write(dbName, "autogen", point);
		    			batchPoints.point(point);
		    			
		    			timestamp+=60;
		    			value+=randomInt(9999);
		    			
		    			//countInsert();
		    		}
		        } finally {
		            //influxDB.disableBatch();
		        }
    		}
			influxDB.write(batchPoints);
    		influxDB.flush();

			return true;
		}
		
	}
	
	public static synchronized void countInsert() {
		COUNT_INSERTS++;
	}
	
	private static int randomInt(int valueMax){
		Random gerador = new Random();
        return gerador.nextInt(valueMax);
	}

}
