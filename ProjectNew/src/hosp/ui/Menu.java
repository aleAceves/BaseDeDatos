package hosp.ui;

import java.io.InputStreamReader;

import java.security.MessageDigest;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import hosp.db.ifaces.DBManager;
import hosp.db.ifaces.UserManager;
import hosp.db.jdbc.JDBCManager;
import hosp.db.jpa.JPAUserManager;
import hosp.db.pojos.Surgeon;
import hosp.db.pojos.WaitingRoom;
import hosp.db.pojos.users.Role;
import hosp.db.pojos.users.User;
import hosp.db.pojos.Operation;
import hosp.db.pojos.Patient;
import hosp.db.pojos.Nurse;
import hosp.db.pojos.OperatingRoom;

import javax.xml.bind.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

 //Si se comenta esta linea el resto de imports me salen que se usan, si no, me sale como que no se usan
import hosp.xml.utils.Xml2Html; 
//


import javax.persistence.Persistence;
import javax.persistence.Query;


public class Menu {
	
	// static, because is the only DBManager that is going to work
	private static DBManager dbman = new JDBCManager();
	//private static UserManager userman = new JPAUserManager();
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	
	public static JPAUserManager userman = new JPAUserManager();
	
	//public static XMLManager xm = new XMLManager();



	// Used for parsing dates
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static void main(String[] args) throws Exception {
		
		dbman.connect();
		userman.connect();
		
	
		do {
			System.out.println("Choose an option:");
			System.out.println("1: Register");
			System.out.println("2: Log in");
			System.out.println("0: Exit");
			try {
			int choice = Integer.parseInt(reader.readLine());
			
			switch (choice) {
			
			case 1:
				register();
				break;
			case 2:
				logIn();
				break;
			case 0:
				dbman.disconnect(); //close connection with the database
				userman.disconnect();
				System.exit(0);
				break;
			default:
				break;	
			
		}}catch(NumberFormatException nfe){
			System.out.println("Please type one of the valids Integers");
		}
			
		
		}while(true); // to show again the menu

		}
		
	
	
	private static void logIn() throws Exception {
		// Ask the user for an email
		System.out.println("Please, write your email address:");
		String email = reader.readLine();
		// Ask the user for a password
		System.out.println("Please write your password:");
		String password = reader.readLine();
		// Check the type of the user and redirect to the proper menu
		User user = userman.checkPassword(email, password);
		if (user == null) {
			System.out.println("Wrong email or password");
			return;
		} else if (user.getRole().getName().equalsIgnoreCase("admin")) {
			adminMenu();
		} else if (user.getRole().getName().equalsIgnoreCase("surgeon")) {
			surgeonMenu(user); 
		} else if (user.getRole().getName().equalsIgnoreCase("nurse")) {
			
			nurseMenu(user);
		} else if (user.getRole().getName().equalsIgnoreCase("patient")) {
			
			patientMenu(user);
		}
		
		
	}


	private static void register() throws Exception {
		
		// Ask the user for an email
		System.out.println("Please, write your email address:");
		String email = reader.readLine();
		// Ask the user for a password
		System.out.println("Please write your password:");
		String password = reader.readLine();
		// List the roles
		System.out.println(userman.getRoles());
		// Ask the user for a role
		System.out.println("Type the chosen role ID:");
		int id = 0; 
		int ide = 0; 
		//do {
		id = Integer.parseInt(reader.readLine());
		
		
		switch (id){
		
		case 1:
			System.out.println("You Selected Admin");
			break;
		case 2:
			dbman.showSurgeons();
			System.out.println("you selected Surgeon please input the id that reference your name");
			ide = Integer.parseInt(reader.readLine());
			break;
		case 3:
			System.out.println("you selected Nurse, please input the id that reference your name");
			dbman.showNurses();;
			ide = Integer.parseInt(reader.readLine());
			break;
		case 4:
			dbman.showPatients();
			System.out.println("you selected Patient, please input the id that reference your name");
			ide = Integer.parseInt(reader.readLine());
			
		}
		//}while(id!=1||!=2||id!=3||id!=4);
		Role role = userman.getRole(id);
		// Ask the user for a role
		System.out.println("Type the chosen role ID:");
		// Generate the hash to store it in the array of bytes
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] hash = md.digest();
		User user = new User(email, hash, role, ide);
		userman.newUser(user); //insert the user into the database
		
		
	}


//----------------------------------------------
	
	
	//ADMIN MENU
	
	
//---------------------------------------------------
	
	private static void adminMenu() throws Exception{
		
		do {
			System.out.println("\t~ ~ ~ ~ ADMINISTRATOR MAIN MENU ~ ~ ~ ~");
			System.out.println(
					"Select what do you want to manage: \n1.Surgeons. \n2.Nurses. \n3.Patients. \n4.Operations.  \n0.Exit");
			int option = Integer.parseInt(reader.readLine());
			switch (option) {
			case 1:
				surgeons();
				break;
			case 2:
				nurses();
				break;
			case 3:
				patients();
				break;
			case 4:
				operations();
			case 0:
				return;
			default:
				break;

			}
		} while(true);
		
		
		
}
	

	//----------------------------------------------
		
		
		//SUBMENU FOR MANAGE SURGEONS
		
		
	//---------------------------------------------------
	
	public static void surgeons() throws Exception {
		do {
			
			System.out.println("\t~ ~ ~ ~ MENU FOR MANAGE SURGEONS ~ ~ ~ ~");
			System.out.println("Choose an option:");
			System.out.println("1: Add surgeon");
		    System.out.println("2: Search a surgeon");
			System.out.println("3: Add a surgeon to an operation");
			System.out.println("4: Eliminate a surgeon from an operation");		
			System.out.println("5: Delete a surgeon");		
			System.out.println("6: Update surgeon info");
			System.out.println("7: Show operations of a surgeon");
			System.out.println("8. Generate XML         ");
			System.out.println("0: Back");
			int choice = Integer.parseInt(reader.readLine());
			
			switch (choice) {
			case 1:
				addSurgeon();
				break;
			case 2:
				searchSurgeonByName();
				break;
			
			case 3: 
				hireSurgeon();
				break;
			case 4:
				fireSurgeon();
				break;
			
			case 5:
				deleteSurgeon();
				break;
			
			case 6:
				updateSurgeon();
				break;
			
			case 7:
				listOperationsOfSurgeon();
				break;
			case 8:
				generateSurgeonXML();
				System.out.println( "XML successfully created, "
						+ "to see the html please go to the xmls folder and open the Surgeon.html");
				break;
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

	}
	

	//----------------------------------------------
		
		
		//SUBMENU FOR MANAGE NURSES
		
		
	//---------------------------------------------------
	
	
	public static void nurses() throws Exception {
		do {
			System.out.println("\t~ ~ ~ ~ MENU FOR MANAGE NURSES ~ ~ ~ ~");
			System.out.println("Choose an option:");
			
            System.out.println("1: Add a nurse");
			System.out.println("2: Search a nurse");
			System.out.println("3: Add a nurse to an operation");
			System.out.println("4: Eliminate a nurse from an operation");		
			System.out.println("5: Delete a nurse");
			System.out.println("6: Show operation of a nurse");
			System.out.println("7: Generate XML");
			System.out.println("0: Back");
			int choice = Integer.parseInt(reader.readLine());
			
			switch (choice) {
			
			case 1:
				addNurse();
				break;
			case 2:
				searchNurseByName();
				break;
			case 3:
				hireNurse();
				break;
			case 4:
				fireNurse();
				break;
			
			case 5:
				deleteNurse();
				break;

			case 6:
				listOperationsOfNurse();
			
			case 7:
				generateNurseXML();
				System.out.println( "XML successfully created, "
						+ "to see the html please go to the xmls folder and open the Nurse.html");
				break;
			case 8: 
				showPersonalInfoNurse();
			
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

	}
	

	//----------------------------------------------
		
		
		//SUBMENU FOR MANAGE PATIENTS
		
		
	//---------------------------------------------------
	
	
	
	public static void patients() throws Exception {
		do {
			System.out.println("\t ~ ~ ~ ~ MENU FOR MANAGE PATIENTS ~ ~ ~ ~");
			System.out.println("Choose an option:");
			System.out.println("1: Add a patient");
			System.out.println("2: Search a patient");
			System.out.println("3: Delete a patient");
			System.out.println("4: Generate XML");
			
			System.out.println("0: Back");
			int choice = Integer.parseInt(reader.readLine());
			
			switch (choice) {
			
			case 1:
				addPatient();
				break;
			case 2:
				searchPatientByName();
				break;
			
			case 3:
				deletePatient();
				break;
			
			case 4:
				generatePatientXML();
				System.out.println( "XML successfully created, "
						+ "to see the html please go to the xmls folder and open the Patient.html");
				break;
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

	}
	

	//----------------------------------------------
		
		
		//SUBMENU FOR MANAGE OPERATIONS
		
		
	//---------------------------------------------------
	
	
	public static void operations() throws Exception {
		do {
			System.out.println("~ ~ ~ ~ MENU FOR MANAGE OPERATIONS ~ ~ ~ ~");
			System.out.println("Choose an option:");
			System.out.println("1:  Add an operation");
			
			System.out.println("2: Delete an operation");
			System.out.println("3: Update operation info");
			System.out.println("4: Add an operating room");
			System.out.println("5: Add a waiting room");
			System.out.println("6: Update operating room");
			System.out.println("7: Search an operation");
			System.out.println("8: Show operating rooms");
			System.out.println("9: Show waiting rooms");
			System.out.println("10: Update a waiting room");
			System.out.println("11: Update an operation");
			
			System.out.println("12: Generate XML");
			
			
			System.out.println("0: Back");
			int choice = Integer.parseInt(reader.readLine());
			
			switch (choice) {
			
			case 1:
				addOperation();
				break;
			
			case 2:
				deleteOperation();
				break;
			
			case 3:
				updateOperation();
				break;
				
			case 4:
				addOperationRoom();
				break;
				
			case 5:
				addWaitingRoom();
				break;
				
			case 6:
				updateOperatingRoom();
				break;
				
			case 7:
				searchOperationByName();
				break;
				
			case 8:
				searchOperationRoom();
				break;
				
			case 9:
				searchWaitingRoom();
				break;
				
			case 10:
				updateWaitingRoom();
				break;
			
			case 11:
				updateOperation(); 
				
			case 12:
				generateOperationXML();
				System.out.println( "XML successfully created, "
						+ "to see the html please go to the xmls folder and open the Operation.html");
				break;
			case 0:
				adminMenu();
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

	}

	
	
	
	private static void updateOperatingRoom() throws Exception {
		
		System.out.println("Which operating room are you going to modify?");
		searchOperationRoom();
		
		System.out.println("Choose a room, type its ID: ");
		Integer id = Integer.parseInt(reader.readLine());
		String newname=null;
		OperatingRoom or = dbman.getOperationRoom(id);
		System.out.println("Do you want to change the room name?");
		System.out.println("Y/N");
		String answer = reader.readLine();
		if (answer.equalsIgnoreCase("Y")) {
			System.out.print("Type the new name type. ");
			String room_type = reader.readLine();
			newname=room_type;
			}
		
		userman.updateOperatingRoom(id,newname);
		System.out.println("Room updated:\n" + or);
		}
		
		



	private static void surgeonMenu(User user) throws Exception{
		
		do {
			System.out.println(" \t ~ ~ ~ ~ SURGEON MENU ~ ~ ~ ~");
			System.out.println("Choose an option:");
			System.out.println("1: Check schedule");
			System.out.println("2: Check your personal information");
			System.out.println("0: Back");
			int choice = Integer.parseInt(reader.readLine());
			switch (choice) {
			case 1:
				listOperationsOfSurgeonById(user.getRef_id());
				break;
			case 2:
				showPersonalInfoSurgeonById(user.getRef_id());
				break;
			case 0:
				adminMenu();
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

		
	}
	
	




	private static void nurseMenu(User user) throws Exception{
		
		do {
			System.out.println("\t ~ ~ ~ ~ MENU FOR NURSES ~ ~ ~ ~");
			System.out.println("Choose an option:");
			System.out.println("1: Check schedule");
			System.out.println("2: Check your personal information");
			System.out.println("0: Back");
			int choice = Integer.parseInt(reader.readLine());
			
			switch (choice) {
			case 1:
				listOperationsOfNurseById(user.getRef_id()); 
				break;
			case 2:
				showPersonalInfoNurseById(user.getRef_id());
				break;
			case 0:
				adminMenu();
				return;
			default:
				
				break;	
			}
			
		}while(true); // to show again the menu

		
	}

    private static void patientMenu(User user) throws Exception{
	
	do {
		System.out.println("\t ~ ~ ~ ~ MENU FOR PATIENTS ~ ~ ~ ~");
		System.out.println("Choose an option:");
		System.out.println("1: Check schedule");
		System.out.println("2: Check your personal information");
		System.out.println("0: Back");
		int choice = Integer.parseInt(reader.readLine());
		
		switch (choice) {
		case 1:
			listOperationsOfPatientById(user.getRef_id()); 
			break;
		case 2:
			showPersonalInfoPatientById(user.getRef_id());
			break;
		case 0:
			adminMenu();
			return;
		default:
			break;	
		}
		
	}while(true); // to show again the menu

	
}
	
	



//SHOW THE OPERATIONS FOR THE NURSE
    private static void listOperationsOfPatientById(int Pid) throws Exception {
    	System.out.println(dbman.getPatient(Pid));
    	System.out.println(dbman.showOperationsByPatientId(Pid));
    }
    private static void listOperationsOfNurseById(int NId) throws Exception {
    	System.out.println(dbman.getNurse(NId));
    	System.out.println(dbman.showOperationsByNurseId(NId));
    	
    	
    }
private static void listOperationsOfNurse() throws Exception {
	System.out.println("From which Nurse do you want to see the operations?");
	searchNurseByName();
	System.out.println("Please intruduce the id of the nurse:");
	int NId = Integer.parseInt(reader.readLine());
	System.out.println(dbman.getNurse(NId));
	System.out.println(dbman.showOperationsByNurseId(NId));
	
	
}





private static void searchWaitingRoom() throws IOException {
		
	System.out.println("Input:");
	System.out.println("Name contains:");
	String name = reader.readLine();
	List<WaitingRoom> rooms = dbman.selectWaitingRooms();
	if (rooms.isEmpty()) {
		System.out.println("No results");
	}else {
		System.out.println(rooms);
		}
	
		
	}



private static void addWaitingRoom() throws IOException {
		
	System.out.println("1: Input the room data:");
	System.out.println("Name:");
	String name=reader.readLine();
	dbman.addWaitingRoom(new WaitingRoom(name));
		
	}



private static void searchOperationRoom() throws IOException {
	
	System.out.println("Input:");
	System.out.println("Name contains:");
	String name = reader.readLine();
	List<OperatingRoom> rooms = dbman.selectOperatingRooms();
	if (rooms.isEmpty()) {
		System.out.println("No results");
	}else {
		System.out.println(rooms);
		}
	
}



//------------------------------------------------------------------------------------
	
	//PATIENT PART
	
//------------------------------------------------------------------------

	

	private static void updateOperation() {
		
		dbman.showOperations();
		
		System.out.println("Please intro the id of the operation to update");
		try {
			int operationId = Integer.parseInt(reader.readLine());
			System.out.println(dbman.getSurgeon(operationId));
			System.out.println("writte new info");
			String sp = reader.readLine();
			dbman.updateOperation(operationId, sp);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}




	private static void searchPatientByName() throws Exception {
		System.out.println("Input:");
		System.out.println("Name contains:");
		String name = reader.readLine();
		List<Patient> patients = dbman.searchPatientByName(name);
		if (patients.isEmpty()) {
			System.out.println("No results");
		}else {
			System.out.println(patients);
			}	
		
	}
	

	private static void addPatient() throws Exception{
		System.out.println("1: Input the patient data:");
		System.out.println("Name:");
		String name=reader.readLine();
		System.out.println("Surname:");
		String surname=reader.readLine();
				
		dbman.addPatient(new Patient(name,surname));
		
	}
	
	// DELETE PATIENT 
	public static void deletePatient() throws Exception {
		System.out.println("Choose a patient, type its ID: ");
		searchPatientByName();
		
		System.out.println("Choose a patient to delete, type its ID: ");
		Integer id = Integer.parseInt(reader.readLine());
		//userman.deletePatient(id);// jpa
		dbman.deletePatient(id);
		System.out.println("Deletion completed.");

		

	}
	
	    // THIS METHODS ASK FOR THE ID OF THE PATIENT, FOR THE ADMINISTRATOR
	private static void listOperationsOfPatient() throws Exception {
	System.out.println("From which patient do you want to see the operations?");
	searchPatientByName();
	System.out.println("Please intruduce the id of the surgeon:");
	int patientId = Integer.parseInt(reader.readLine());
	System.out.println(dbman.getPatient(patientId));
	System.out.println(dbman.showOperationsByPatientId(patientId));
	
	
}
	
	//SHOW THE PERSONAL INFORMATION OF EACH PATIENT: SUBMENU FOR PATIENT
    private static void showPersonalInfoPatientById(int pId) throws Exception{
    	Patient patient=dbman.getPatient(pId);
    	System.out.println("Your name:" +patient.getName());
    	System.out.println("Your surname:" +patient.getSurname());
    	

	}
    
    private static void generatePatientXML() throws Exception{
		System.out.print("Please introduce the id of the Patient");
		System.out.print("1. patient id ");
		Integer patientId = Integer.parseInt(reader.readLine());
		Patient patient = dbman.getPatient(patientId);
		// Create a JAXBContext
		JAXBContext context = JAXBContext.newInstance(Patient.class);
		// Get the marshaller
		Marshaller marshal = context.createMarshaller();
		// Pretty formatting
		marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// Marshall the dog to a file
		File file = new File("./xmls/Output-Patient.xml");
		marshal.marshal(patient, file);
		// Marshall the dog to the screen
		marshal.marshal(patient, System.out);
		
		// Generate the HTML
		Xml2Html.simpleTransform("./xmls/Output-Patient.xml", "./xmls/patientStyle.xslt", "./xmls/Patient.html");
    }

	
//-----------------------------------------------------------------------
	
	//NURSE PART
	
//---------------------------------------------------------------------------
	
	private static void hireNurse() throws Exception{
		// we need to find a nurse and to find an operation; we have methods for that
		System.out.println("Choose the operation where is going to assist:");
		searchOperationByName();
		System.out.println("Type the operation's id:");
		int operationId = Integer.parseInt(reader.readLine());
		System.out.println("Choose the nurse");
		searchNurseByName();
		System.out.println("Type the nurse's id:");
		int nurseId = Integer.parseInt(reader.readLine());
		dbman.hireNurse(dbman.getOperation(operationId), dbman.getNurse(nurseId)); //we call the methods that search by the id
		
		
	}

	private static void fireNurse() throws Exception {
	
		System.out.println("Choose the operation where is NOT going to assist:");
		searchOperationByName();
		System.out.println("Type the operation's id:");
		int operationId = Integer.parseInt(reader.readLine());
		System.out.println("Choose the nurse");
		searchNurseByName();
		System.out.println("Type the nurse's id:");
		int nurseId = Integer.parseInt(reader.readLine());
		dbman.fireNurse(operationId,nurseId); 
	}


	private static void searchNurseByName()throws Exception {
		dbman.showNurses();
		System.out.println("Input:");
		System.out.println("Name contains:");
		String name = reader.readLine();
		List<Nurse> nurses = dbman.searchNurseByName(name);
		if (nurses.isEmpty()) {
			System.out.println("No results");
		}else {
			System.out.println(nurses);
			}	
	}

	private static void addNurse() throws Exception {
		System.out.println("1: Input the nurse data:");
		System.out.println("Name:");
		String name=reader.readLine();
		System.out.println("Surname:");
		String surname=reader.readLine();
		dbman.addNurse(new Nurse(name,surname));
		
	}
	
	private static void deleteNurse() throws Exception {
		System.out.println("Enter the nurse id of the nurse you want to eliminate:");
		searchNurseByName();
		System.out.println("Type the nurse's id:");
		int nurseId = Integer.parseInt(reader.readLine());
		dbman.deleteNurse(nurseId);
		System.out.println("Deletion completed.");
		
		
		
	}
	
	//SHOW THE PERSONAL INFORMATION OF EACH NURSE: SUBMENU FOR NURSE
    private static void showPersonalInfoNurseById(int nurseId) throws Exception{

    	Nurse s=dbman.getNurse(nurseId);
    	
    	System.out.println("Your name:" +s.getName());
    	System.out.println("Your surname:" +s.getSurname());
    	

	}
    private static void showPersonalInfoNurse() throws Exception{
    	System.out.println("Type your ID:");
    	int nurseId=Integer.parseInt(reader.readLine());
    	Nurse s=dbman.getNurse(nurseId);
    	
    	System.out.println("Your name:" +s.getName());
    	System.out.println("Your surname:" +s.getSurname());
    	

	}
    
    private static void generateNurseXML() throws Exception{
		System.out.print("Please introduce the id of the Nurse");
		System.out.print("1. nurse id ");
		Integer nurseId = Integer.parseInt(reader.readLine());
		Nurse nurse = dbman.getNurse(nurseId);
		// Create a JAXBContext
		JAXBContext context = JAXBContext.newInstance(Nurse.class);
		// Get the marshaller
		Marshaller marshal = context.createMarshaller();
		// Pretty formatting
		marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// Marshall the dog to a file
		File file = new File("./xmls/Output-Nurse.xml");
		marshal.marshal(nurse, file);
		// Marshall the dog to the screen
		marshal.marshal(nurse, System.out);
		
		// Generate the HTML
		Xml2Html.simpleTransform("./xmls/Output-Nurse.xml", "./xmls/nurseStyle.xslt", "./xmls/Nurse.html");
    }
	
		

	
//----------------------------------------------------------------------------
	
	//SURGEON PART
	
//----------------------------------------------------------------------------
	
	private static void addSurgeon() throws Exception {
			System.out.println("1: Input the surgeon data:");
			System.out.println("Name:");
			String name=reader.readLine();
			System.out.println("Surname:");
			String surname=reader.readLine();
			System.out.println("Speciality:");
			String speciality=reader.readLine();
			
			dbman.addSurgeon(new Surgeon(name,surname,speciality));
			
		}
	 
	private static void deleteSurgeon() throws Exception { //TODO
		
		searchSurgeonByName();
		System.out.println("Type the surgeon's id to eliminate:");
		int surgeonId = Integer.parseInt(reader.readLine());
		dbman.deleteSurgeon(surgeonId);
		System.out.println("Deletion completed.");
		
	}
	private static void updateSurgeon() {
		try {
			searchSurgeonByName();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("Please intruduce the id of the surgeon to update");
		try {
			int surgeonId = Integer.parseInt(reader.readLine());
			System.out.println(dbman.getSurgeon(surgeonId));
			System.out.println("writte the new speciality");
			
			String sp = reader.readLine();
			dbman.updateSurgeon(surgeonId, sp);
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
			}
	
		
	private static void searchSurgeonByName() throws Exception {
			dbman.showSurgeons();
			System.out.println("Input:");
			System.out.println("Name contains:");
			String name = reader.readLine();
			List<Surgeon> surgeons = dbman.searchSurgeonByName(name);
			if (surgeons.isEmpty()) {
				System.out.println("No results");
			}else {
				System.out.println(surgeons);
			}
		}
	
	private static void hireSurgeon()throws Exception{
		// we need to find a surgeon and to find an operation; we have methods for that
		dbman.showOperations();
		System.out.println("Choose the operation where is going to assist:");
		System.out.println("Type the operation's id:");
		int operationId = Integer.parseInt(reader.readLine());
		System.out.println("Choose the surgeon");
		searchSurgeonByName();
		System.out.println("Type the surgeon's id:");
		int surgeonId = Integer.parseInt(reader.readLine());
		dbman.hireSurgeon(dbman.getOperation(operationId), dbman.getSurgeon(surgeonId)); //we call the methods that search by the id
	}
	
	private static void fireSurgeon()throws Exception{
		System.out.println("Choose the operation where is going to assist:");
		dbman.showOperations();
		System.out.println("Type the operation's id:");
		int operationId = Integer.parseInt(reader.readLine());
		System.out.println("Choose first the surgeon");
		searchSurgeonByName();
		System.out.println("Type the surgeon's id:");
		int surgeonId = Integer.parseInt(reader.readLine());
		dbman.fireSurgeon(operationId,surgeonId); 
		
		
	}
    // THIS METHODS ASK FOR THE ID OF THE SURGEON, FOR THE ADMINISTRATOR
	private static void listOperationsOfSurgeon() throws Exception {
	System.out.println("From which surgeon do you want to see the operations?");
	searchSurgeonByName();
	System.out.println("Please intruduce the id of the surgeon:");
	int surgeonId = Integer.parseInt(reader.readLine());
	System.out.println(dbman.getSurgeon(surgeonId));
	System.out.println(dbman.showOperationsBySurgeonId(surgeonId));
	
	
}
	private static void listOperationsOfSurgeonById(int id) throws Exception {
	System.out.println(dbman.getSurgeon(id));
	System.out.println(dbman.showOperationsBySurgeonId(id));
	
}
	
	//SHOW THE PERSONAL INFORMATION OF EACH SURGEON: SUBMENU FOR SURGEON
    private static void showPersonalInfoSurgeon() throws Exception{
    	System.out.println("Type the id of the Surgeon ID:");
    	int surgeonId=Integer.parseInt(reader.readLine());
    	Surgeon s=dbman.getSurgeon(surgeonId);
    	
    	System.out.println("Your name:" +s.getName());
    	System.out.println("Your surname:" +s.getSurname());
    	System.out.println("Your speciality:" +s.getSpeciality());

	}
    private static void showPersonalInfoSurgeonById(int surgeonId) throws Exception{

    	Surgeon s=dbman.getSurgeon(surgeonId);
    	
    	System.out.println("Your name:" +s.getName());
    	System.out.println("Your surname:" +s.getSurname());
    	System.out.println("Your speciality:" +s.getSpeciality());

	}
	
    private static void generateSurgeonXML() throws Exception{
		System.out.print("Please introduce the id of the Surgeon");
		System.out.print("1. surgeon id ");
		Integer surgeonId = Integer.parseInt(reader.readLine());
		Surgeon surgeon = dbman.getSurgeon(surgeonId);
		// Create a JAXBContext
		JAXBContext context = JAXBContext.newInstance(Surgeon.class);
		// Get the marshaller
		Marshaller marshal = context.createMarshaller();
		// Pretty formatting
		marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// Marshall the dog to a file
		File file = new File("./xmls/Output-Surgeon.xml");
		marshal.marshal(surgeon, file);
		// Marshall the dog to the screen
		marshal.marshal(surgeon, System.out);
		
		// Generate the HTML
		Xml2Html.simpleTransform("./xmls/Output-Surgeon.xml", "./xmls/surgeonStyle.xslt", "./xmls/Surgeon.html");
    }
	
//-----------------------------------------------------------------------------
	
	//OPERATION PART
	
//------------------------------------------------------------------------------
	
	
	private static void addOperation() throws Exception {
			System.out.println("1: Input the operation info:");
			System.out.println("Type:");
			String type=reader.readLine();
			System.out.println("Start Date (yyyy-MM-dd):");
			LocalDate startDate = LocalDate.parse(reader.readLine(), formatter);
			System.out.println("Total duration:");
			int duration=Integer.parseInt(reader.readLine());
			System.out.println("Which patient do you want to add?:");
			searchPatientByName();
			System.out.println("Type the patient's id to assign to the operation:");
			int patientId = Integer.parseInt(reader.readLine());
			Patient p = null;
			p = dbman.getPatient(patientId);
			System.out.println("Which room is going to be selected?");
			searchOperationRoom();
			System.out.println("Type the room´s id where the operations is going to take place:");
			int roomId = Integer.parseInt(reader.readLine());
		//	OperatingRoom r=null;
		//	r = dbman.getOperationRoom(roomId);
			dbman.addOperation(new Operation(type,Date.valueOf(startDate),duration,p)); //transform date into a sql date
			
		}
		
	private static void searchOperationByName() throws Exception {
			System.out.println("Name contains:");
			String name = reader.readLine();
			List<Operation> operations = dbman.searchOperationByName(name);
			if (operations.isEmpty()) {
				System.out.println("No results");
			}else {
				
				System.out.println(operations);
			}
		}
	
	// DELETE OPERATION 
	private static void deleteOperation() throws Exception{
		dbman.showOperations();

		System.out.println("Choose the operation you wantto eliminate:");
		
		System.out.println("Type the operation's id:");
		int operationId = Integer.parseInt(reader.readLine());
		userman.deleteOperation(operationId); //userman because we are using JPA
		System.out.println("Deletion completed.");
		
	}
	
    private static void generateOperationXML() throws Exception{
		System.out.print("Please introduce the id of the operation");
		System.out.print("1. operation id ");
		Integer operationId = Integer.parseInt(reader.readLine());
		Operation operation = dbman.getOperation(operationId);
		// Create a JAXBContext
		JAXBContext context = JAXBContext.newInstance(Operation.class);
		// Get the marshaller
		Marshaller marshal = context.createMarshaller();
		// Pretty formatting
		marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// Marshall the dog to a file
		File file = new File("./xmls/Output-Operation.xml");
		marshal.marshal(operation, file);
		// Marshall the dog to the screen
		marshal.marshal(operation, System.out);
		
		// Generate the HTML
		Xml2Html.simpleTransform("./xmls/Output-Operation.xml", "./xmls/operationStyle.xslt", "./xmls/Operation.html");
    }
	
	//---------------------------------------------------------

		//OPERATION ROOM

	//----------------------------------------------------

	
	private static void addOperationRoom() throws Exception {
		System.out.println("1: Input the room data:");
		System.out.println("Name:");
		String name=reader.readLine();
		dbman.addOperationRoom(new OperatingRoom(name));
		
	}
	
	//---------------------------------------------------------

			//WAITING ROOM

	//----------------------------------------------------

	// method for update the waiting room name

	private static void updateWaitingRoom() throws Exception{
		
		
		searchWaitingRoom();
		System.out.println("Choose a waiting room, type its ID: ");
		int room_id = Integer.parseInt(reader.readLine());
		
		System.out.print("Type the new name for the room: ");
		String newName = reader.readLine();
		
		dbman.updateWaitingRoom(dbman.getWaitingRoom(room_id), newName);
		
		
		
		System.out.println("Update finished.");
	}
	
	

	
}
		
	
		
		




