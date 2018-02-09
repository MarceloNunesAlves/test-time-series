package br.com.flexvision.process;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;

public class AddItem {

	public static void main(String[] args) throws IOException, RrdException {
		
		double value = 100;
		long timestamp = (new Date()).getTime();
		timestamp = Long.parseLong(String.valueOf(timestamp).substring(0, 10));
		System.out.println("Timestamp utilizado: "+timestamp);
		RrdDb rrdDb = new RrdDb("/tmp/rrd/teste_1.rrd");
		for(int i=0;i<=1000;i++) {
			Sample sample = rrdDb.createSample();
			String updateData = timestamp+":"+value;
			sample.setAndUpdate(updateData);
			System.out.println(updateData);
			timestamp+=60;
			value+=randomInt(9999);
		}
		
		rrdDb.close();
	}
	
	private static int randomInt(int valueMax){
		Random gerador = new Random();
        return gerador.nextInt(valueMax);
	}
}
