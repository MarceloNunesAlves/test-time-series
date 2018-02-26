package br.com.flexvision.process;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.jrobin.core.FetchData;
import org.jrobin.core.FetchRequest;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;

public class AddItem {

	public static void main(String[] args) throws IOException, RrdException {
		
		double value = 100;
		long timestamp = (new Date()).getTime();
		timestamp = Long.parseLong(String.valueOf(timestamp).substring(0, 10));
		System.out.println("Timestamp utilizado: "+timestamp);
		String file = "/tmp/rrd/teste_1.rrd";
		RrdDb rrdDb = new RrdDb(file);
		for(int i=0;i<=1000;i++) {
			Sample sample = rrdDb.createSample();
			String updateData = timestamp+":"+value;
			sample.setAndUpdate(updateData);
			//System.out.println(updateData);
			timestamp+=60;
			value+=randomInt(9999);
			
			FetchRequest fetchRequest = rrdDb.createFetchRequest("AVERAGE",timestamp-60,timestamp);
			System.out.println("Dados: "+(getLastValue(rrdDb, file, 60, timestamp).getData())[0].getValue());

		}
		
		rrdDb.close();
	}
	
	public static RRDLine getLastValue(RrdDb rrdDb, String file, Integer lastSeconds, long timestamp_end){
		RRDLine	line = new RRDLine();
		
		RRDFetch fetch = new RRDFetch();
		fetch.setFile(file);
		fetch.setConsolidation(RRD_CF.AVERAGE);
		
		if(lastSeconds!=null){
			Long end = timestamp_end;
			fetch.setStart((end - lastSeconds));
			fetch.setEnd(end);				
		}	
		
		FetchData fetchRRD;	

		try{	

			FetchRequest fetchRequest = rrdDb.createFetchRequest(
					fetch.getConsolidation().toString(), 
					fetch.getStart(), 
					fetch.getEnd());
			
			fetchRRD = fetchRequest.fetchData();

			
			if(fetchRRD!=null){
				
				int fetchLin = fetchRRD.getRowCount() - 1;
				int fetchCol = fetchRRD.getColumnCount();
				
				for (int row = fetchLin - 1; row >= 0; row--) {
					
					/*
					 * Pega o ultimo valor sem ser NaN ou o primeiro caso nao
					 * encontre nenhum.
					 */
					if (!Double.isNaN(fetchRRD.getValues()[0][row]) || row == 0) {
						
						line.setTimestamp(fetchRRD.getTimestamps()[row+1]);
						line.setData( new RRDData[ (fetchCol+1) ]);
						
						for (int col = 0; col < fetchCol; col++) {						
							line.getData()[col] = new RRDData();
							line.getData()[col].setValue( fetchRRD.getValues()[col][row]);
							line.getData()[col].setColun( fetchRRD.getDsNames()[col]);	
							//System.out.println("rrd="+fetchRRD.getValues()[col][row]);
						}
						break;
					}			
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		return line;
	}
	
	private static int randomInt(int valueMax){
		Random gerador = new Random();
        return gerador.nextInt(valueMax);
	}
}
