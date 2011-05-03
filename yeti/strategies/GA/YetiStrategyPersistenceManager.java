package yeti.strategies.GA;

import org.jgap.Configuration;
import org.jgap.IChromosome;
import org.jgap.xml.XMLManager;
import org.w3c.dom.Document;
import java.io.File;


/**
 * Created by IntelliJ IDEA.
 * User: slucas
 * Date: 4/30/11
 * Time: 10:01 AM
 */
public class YetiStrategyPersistenceManager {

    public static void saveChromosome( IChromosome aChromosome, String filePath ) throws Exception {
        // Convert the Genotype to a DOM object.
        // -------------------------------------
        Document xmlRepresentation = XMLManager.representChromosomeAsDocument(aChromosome);
        XMLManager.writeFile(xmlRepresentation, new File(filePath));
    }


    public static IChromosome loadChromosome(Configuration aConfiguration, String filePath ) throws Exception {
        Document chromosomeDoc = XMLManager.readFile(new File(filePath));
        return XMLManager.getChromosomeFromDocument(aConfiguration,chromosomeDoc);
    }
}
