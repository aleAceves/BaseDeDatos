package hosp.db.ifaces;

import java.util.List;

import hosp.db.pojos.*;


public interface DBManager { //add all method we are going to use
	
	public void connect();
	public void disconnect();
	
	public void addSurgeon(Surgeon surgeon);
	public Surgeon getSurgeon(int id);
	public List<Surgeon> searchSurgeonByName(String name);
	
	public void addOperation(Operation operation); //add a new operation to the database
	public Operation getOperation(int id);
	public List<Operation> searchOperationByName(String name);
	public void hireSurgeon(Operation o, Surgeon s);//method that associates a surgeon with an operation

	public void fireSurgeon(int operationId, int surgeonId);
	
	public List<Surgeon> getSurgeonsOfOperation(int operationId); //surgeons that participate in a given operation
	
	//FOR THE NURSES
	public void addNurse(Nurse nurse);
	public Nurse getNurse(int id);
	public List<Nurse> searchNurseByName(String name);
	
	public void hireNurse(Operation o, Nurse n);
	public void fireNurse(int operationId, int nurseId);
	public List<Nurse> getNursesOfOperation(int operationId); //nurses that participate in a given operation


	
	//FOR THE PATIENTS
	public void addPatient(Patient patient);
	public Patient getPatient(int id);
	public List<Patient> searchPatientByName(String name);
	//public void hirePatient(Operation operation,Patient patient);


}
