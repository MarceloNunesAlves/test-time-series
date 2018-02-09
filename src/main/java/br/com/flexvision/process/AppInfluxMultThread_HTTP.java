package br.com.flexvision.process;

import java.io.IOException;
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
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppInfluxMultThread_HTTP {
	
	public static int COUNT_INSERTS=0;
	
	public static void main(String[] args) {
		AppInfluxMultThread_HTTP app = new AppInfluxMultThread_HTTP();
		System.out.println("Inicio do processo!!! " + new Date());
		app.exec();
		System.out.println("Fim do processo!!! -> " + new Date() + " - Total: " + COUNT_INSERTS);
	}
	
	public void exec() {
		
		
		ThreadFactory influxNamedThreadFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "influx-test-paralelo");
			}
		};
		ExecutorService executor = Executors.newCachedThreadPool(influxNamedThreadFactory);
		
		
        List<Future<Boolean>> list = new ArrayList<Future<Boolean>>();
        for(int i=1;i<=10000;i++){//10000
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
	}
	
	public class ThreadInflux implements Callable<Boolean>{
		
		private int nod_id;
		private OkHttpClient client = new OkHttpClient();
		private String url = "http://localhost:8086/write?db=flex_metric";
		
		public ThreadInflux(int nod_id) {
			super();
			this.nod_id = nod_id;
		}

		public Boolean call() throws Exception {
			double value = 100;
			long timestamp = (new Date()).getTime();
			timestamp = Long.parseLong(String.valueOf(timestamp).substring(0, 10));
			
    		for(int nod_dif=1;nod_dif<=30;nod_dif++) {//30
    			try {
					for(int met_id=1;met_id<=10;met_id++) {
		    			String insert = "data_metric,node_id="+nod_id+",met_id="+(met_id*nod_dif)+" value="+value+" "+timestamp;
		    			System.out.println(insert);
		    			
		    			System.out.println(post(url, insert));
		    			timestamp+=60;
		    			value+=randomInt(9999);
		    			countInsert();
		    		}
		        } finally {
		        }
    		}
    		return true;
		}
		
		public String post(String url, String dado) throws IOException {
			RequestBody body = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), dado);
			Request request = new Request.Builder().url(url).post(body).build();
			try (Response response = client.newCall(request).execute()) {
				return response.body().string();
			}
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
