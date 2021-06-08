package hosp.db.xml;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import hosp.db.pojos.*;
import hosp.xml.utils.Xml2Html;

public class XMLManager {
	
	public XMLManager() {
		super();
	}
	
	// Use the Unmarshaller to unmarshal the XML document from a file
	
	// we are going to marshall and unmarshall(Nurse, surgeon, patient, operation)
	
	public void marshallNurse(Nurse nurse,String name) throws JAXBException {
			JAXBContext jaxbContext = JAXBContext.newInstance(Nurse.class);
			Marshaller marshaller = jaxbContext.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			File file = new File("./xmls/"+name);
			marshaller.marshal(nurse, file);
	}
	public void marshallSurgeons(List<Surgeon> surgeons,String name) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Surgeon.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		File file = new File(name);
		marshaller.marshal(surgeons, file);
		Xml2Html.simpleTransform("./xmls/Output-Surgeons.xml", "./xmls/surgeonsStyle.xslt", "./xmls/Surgeons.html");
		marshaller.marshal(surgeons, System.out);
}
	public void marshallSurgeon(Surgeon surgeon,String name) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Surgeon.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		File file = new File(name);
		marshaller.marshal(surgeon, file);
		Xml2Html.simpleTransform("./xmls/Output-Surgeon.xml", "./xmls/surgeonStyle.xslt", "./xmls/Surgeon.html");
		marshaller.marshal(surgeon, System.out);
}
	
	public void marshallPatient(Patient patient,String name) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Patient.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		File file = new File("./xmls/"+name);
		marshaller.marshal(patient, file);
}
	
	public void marshallOperation(Operation operation,String name) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Operation.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		File file = new File("./xmls/"+name);
		marshaller.marshal(operation, file);
}
	
	
	//UNMARSHALL
	
	public Nurse unmarshallNurse(String name) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Nurse.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		File file = new File("./xmls/"+name);
		Nurse nurse = (Nurse) unmarshaller.unmarshal(file);
		return nurse;
	}
	
	public Surgeon unmarshallSurgeon(String name) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Surgeon.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		File file = new File("./xmls/"+name);
		Surgeon surgeon = (Surgeon) unmarshaller.unmarshal(file);
		return surgeon;
	}
	
	public Patient unmarshallPatient(String name) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Patient.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		File file = new File("./xmls/"+name);
		Patient patient = (Patient) unmarshaller.unmarshal(file);
		return patient;
	}
	
	public Operation unmarshallOperation(String name) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Patient.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		File file = new File("./xmls/"+name);
		Operation operation = (Operation) unmarshaller.unmarshal(file);
		return operation;
	}
	
	//TRANSFORM
	public void simpleTransform(String sourcePath, String xsltPath,String resultDir) {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = tFactory.newTransformer(new StreamSource(new File(xsltPath)));
			transformer.transform(new StreamSource(new File(sourcePath)),new StreamResult(new File(resultDir)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
