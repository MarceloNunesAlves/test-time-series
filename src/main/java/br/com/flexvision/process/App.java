package br.com.flexvision.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws RrdException, IOException
    {
        System.out.println( "Criando RRD" );
        
        RrdDef rrdDef = new RrdDef("/tmp/rrd/teste_1.rrd");
		rrdDef.setStep(60);//1 Minuto de coleta
		
		// define o timestamp de inicio do rrd
		//rrdDef.setStartTime(timeStampStart); Default

		//NUMERO DE COLETAS PASSADAS SEM SUCESSO 
		//PARA A COLETA SER CONSIDERADA UNKNOWN
		Integer heartBeat = 120;//1800 = 2 Coletas de 5 min
								//120 = 2 Coletas de 1 min
		
		rrdDef.addDatasource("column","DERIVE",heartBeat.longValue(),0.0,Double.NaN);
		
		List<Archive> listArchive = new ArrayList<Archive>();
		
		Archive a1 = new Archive("AVERAGE",26784,1,0.5);
		Archive a2 = new Archive("AVERAGE",35088,6,0.5);
		Archive a3 = new Archive("MIN",26784,1,0.5);
		Archive a4 = new Archive("MIN",35088,6,0.5);
		Archive a5 = new Archive("MAX",26784,1,0.5);
		Archive a6 = new Archive("MAX",35088,6,0.5);
		listArchive.add(a1);listArchive.add(a2);listArchive.add(a3);
		listArchive.add(a4);listArchive.add(a5);listArchive.add(a6);
		
		for (Archive archive : listArchive){
			rrdDef.addArchive(
					archive.getConsolidation(), 
					archive.getXff(),
					archive.getSteps(), 
					archive.getRows());
		}

		RrdDb rrdDb = new RrdDb(rrdDef);
		rrdDb.close();
    }
}
