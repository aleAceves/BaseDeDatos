package hosp.db.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import hosp.xml.utils.*;

public class DTDCheckerReport {

	//TO PARSE A XML DOCUMENT
    public static void main(String[] args) {
        File xmlFile = new File("./xmls/Output-Surgeon.xml"); //load the XML file
        try {
        	// Create a DocumentBuilderFactory
            DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance(); //create objects that represent XML documents
            // Set it up so it validates XML documents
            dBF.setValidating(true);
            // Create a DocumentBuilder and an ErrorHandler (to check validity)
            DocumentBuilder builder = dBF.newDocumentBuilder();
            CustomErrorHandler customErrorHandler = new CustomErrorHandler(); //detects if the document is valid and well-formed
            builder.setErrorHandler(customErrorHandler); 
            // Parse the XML file and print out the result
            Document doc = builder.parse(xmlFile);
            
            if (customErrorHandler.isValid()) { //if the document is valid
                System.out.println(xmlFile + " was valid!");
            }
            
        } catch (ParserConfigurationException ex) {
            System.out.println(xmlFile + " error while parsing!"); // if the document has an error while parsing 
        } catch (SAXException ex) {
            System.out.println(xmlFile + " was not well-formed!");  // if the document is not well-formed
        } catch (IOException ex) {
            System.out.println(xmlFile + " was not accesible!"); // if the document is not accesible 
        }

    }
}
