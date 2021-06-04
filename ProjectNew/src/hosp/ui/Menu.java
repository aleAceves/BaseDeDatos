package hosp.ui;

import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.BufferedReader;
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
			
			surgeonMenu(); //TODO
		} else if (user.getRole().getName().equalsIgnoreCase("nurse")) {
			
			nurseMenu();//TODO
		} else if (user.getRole().getName().equalsIgnoreCase("patient")) {
			
			patientMenu();//TODO
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
		int id = Integer.parseInt(reader.readLine());
		Role role = userman.getRole(id);
		// Generate the hash to store it in the array of bytes
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] hash = md.digest();
		User user = new User(email, hash, role);
		userman.newUser(user); //insert the user into the database
		
		
	}


//----------------------------------------------
	
	
	//ADMIN MENU
	
	
//---------------------------------------------------
	
	private static void adminMenu() throws Exception{
		
		do {
			System.out.println("~ ~ ~ ~ ADMINISTRATOR MAIN MENU ~ ~ ~ ~");
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
	
	public static void surgeons() throws Exception {
		do {
			
			System.out.println("~ ~ ~ ~ MENU FOR MANAGE SURGEONS ~ ~ ~ ~");
			System.out.println("Choose an option:");
			System.out.println("1:  Add surgeon");
		    System.out.println("2:  Add a surgeon to an operation");
			System.out.println("3: Search a surgeon");
			System.out.println("4: Eliminate a surgeon from an operation");		
			System.out.println("5: Delete a surgeon");		
			System.out.println("6: Update surgeon info");
			System.out.println("7: Show operations of a surgeon");
			System.out.println("0: Exit");
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
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

	}
	
	public static void nurses() throws Exception {
		do {
			System.out.println("~ ~ ~ ~ MENU FOR MANAGE NURSES ~ ~ ~ ~");
			System.out.println("Choose an option:");
			
            System.out.println("1:  Add a nurse");
			System.out.println("2: Search a nurse");
			System.out.println("3: Add a nurse to an operation");
			System.out.println("4: Eliminate a nurse from an operation");		
			System.out.println("5: Delete a nurse");
			System.out.println("6: Update nurse info");
			System.out.println("7: Show operation of a nurse");
			System.out.println("0: Exit");
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
				updateNurse();
				break;
			case 7:
				listOperationsOfNurse(); 
			
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

	}
	
	
	public static void patients() throws Exception {
		do {
			System.out.println("~ ~ ~ ~ MENU FOR MANAGE PATIENTS ~ ~ ~ ~");
			System.out.println("Choose an option:");
			System.out.println("1: Add a patient");
			System.out.println("2: Search a patient");
			System.out.println("3: Delete a patient");
			System.out.println("4: Update patient information");
			System.out.println("0: Exit");
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
				updatePatient();
				break;
			
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

	}
	
	public static void operations() throws Exception {
		do {
			System.out.println("~ ~ ~ ~ MENU FOR MANAGE OPERATIONS ~ ~ ~ ~");
			System.out.println("Choose an option:");
			System.out.println("1:  Add an operation");
			System.out.println("2: Delete an operation");
			System.out.println("3: Update operation info");
			System.out.println("0: Exit");
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
			
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

	}

	
	
	private static void surgeonMenu() throws Exception{
		
		do {
			System.out.println(" \t ~ ~ ~ ~ SURGEON MENU ~ ~ ~ ~");
			System.out.println("Choose an option:");
			System.out.println("1: Check schedule");
			System.out.println("2: Check your personal information");
			System.out.println("0: Exit");
			int choice = Integer.parseInt(reader.readLine());
			
			switch (choice) {
			case 1:
				listOperationsOfSurgeon();
				break;
			case 2:
				showPersonalInfoSurgeon();
				break;
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

		
	}
	
	




	private static void nurseMenu() throws Exception{
		
		do {
			System.out.println("\t ~ ~ ~ ~ MENU FOR NURSES ~ ~ ~ ~");
			System.out.println("Choose an option:");
			System.out.println("1: Check schedule");
			System.out.println("2: Check your personal information");
			System.out.println("0: Exit");
			int choice = Integer.parseInt(reader.readLine());
			
			switch (choice) {
			case 1:
				listOperationsOfNurse(); 
				break;
			case 2:
				showPersonalInfoNurse();
				break;
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

		
	}

    private static void patientMenu() throws Exception{
	
	do {
		System.out.println("~ ~ ~ ~ MENU FOR PATIENTS ~ ~ ~ ~");
		System.out.println("Choose an option:");
		System.out.println("1: Check schedule");
		System.out.println("2: Check your personal information");
		//System.out.println("3: Check operations");
		System.out.println("0: Exit");
		int choice = Integer.parseInt(reader.readLine());
		
		switch (choice) {
		case 1:
			listOperationsOfPatient(); 
			break;
		case 2:
			showPersonalInfoPatient();
			break;

		case 0:
			return;
		default:
			break;	
		}
		
	}while(true); // to show again the menu

	
}
	
	
	



//SHOW THE OPERATIONS FOR THE NURSE
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

	

	private static void updateNurse() {
		// TODO Auto-generated method stub
		
	}



	private static void updateOperation() {
		// TODO Auto-generated method stub
		dbman.showOperations();
		
		System.out.println("Please intro the id of the soperation to update");
		try {
			int operationId = Integer.parseInt(reader.readLine());
			System.out.println(dbman.getSurgeon(operationId));
			System.out.println("writte new info");
			String sp = reader.readLine();
			dbman.updateOperation(operationId, sp);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
	// DELETE PATIENT USING JPA
	public static void deletePatient() throws Exception {
		/*
		List<Patient> list = userman.selectPatients();// with jpa
		for (Patient p : list) {
			System.out.println(p.toString());
		}
		*/
		System.out.println("Choose a patient, type its ID: ");
		searchPatientByName();
		
		System.out.println("Choose a patient to delete, type its ID: ");
		Integer id = Integer.parseInt(reader.readLine());
		//userman.deletePatient(id);// jpa
		dbman.deletePatient(id);
		System.out.println("Deletion completed.");

		

	}
	
	// UPDATE PATIENT USING JPA
	private static void updatePatient() throws Exception { //NO FUNCIONA!!!
		// TODO Auto-generated method stub
		
		System.out.println("Choose a patient, type its ID: ");
		searchPatientByName();
		System.out.println("Type the patient's id:");
		int patientId = Integer.parseInt(reader.readLine());
		Patient p = dbman.getPatient(patientId);
		
		System.out.println("Do you want to change the name?");
		System.out.println("Y/N");
		String answer;
		answer = reader.readLine();
		if (answer.equalsIgnoreCase("Y")) {
			System.out.print("Type the new patient's name: ");
			p.setName(reader.readLine());
		}
		
		System.out.println("Do you want to change the surname?");
		System.out.println("Y/N");
		
		answer = reader.readLine();
		if (answer.equalsIgnoreCase("Y")) {
			System.out.print("Type the new patient's surname: ");
			p.setName(reader.readLine());
		}
		
		//userman.updatePatient(p); //using jpa
		dbman.updatePatient(p);
		System.out.println("Patient updated:\n" + p);
		        
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
    private static void showPersonalInfoPatient() throws Exception{
    	System.out.println("Type your ID:");
    	int pId=Integer.parseInt(reader.readLine());
    	Patient patient=dbman.getPatient(pId);
    	
    	System.out.println("Your name:" +patient.getName());
    	System.out.println("Your surname:" +patient.getSurname());
    	

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
    private static void showPersonalInfoNurse() throws Exception{
    	System.out.println("Type your ID:");
    	int nurseId=Integer.parseInt(reader.readLine());
    	Nurse s=dbman.getNurse(nurseId);
    	
    	System.out.println("Your name:" +s.getName());
    	System.out.println("Your surname:" +s.getSurname());
    	

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
	 
	private static void deleteSurgeon() throws 
	Exception { //TODO
		
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
	
	//SHOW THE PERSONAL INFORMATION OF EACH SURGEON: SUBMENU FOR SURGEON
    private static void showPersonalInfoSurgeon() throws Exception{
    	System.out.println("Type your ID:");
    	int surgeonId=Integer.parseInt(reader.readLine());
    	Surgeon s=dbman.getSurgeon(surgeonId);
    	
    	System.out.println("Your name:" +s.getName());
    	System.out.println("Your surname:" +s.getSurname());
    	System.out.println("Your speciality:" +s.getSpeciality());

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
	
	// DELETE OPERATION USING JPA
	private static void deleteOperation() throws Exception{
		dbman.showOperations();

		System.out.println("Choose the operation you wantto eliminate:");
		
		System.out.println("Type the operation's id:");
		int operationId = Integer.parseInt(reader.readLine());
		userman.deleteOperation(operationId); //userman because we are using JPA
		System.out.println("Deletion completed.");
		
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


	
}
		
	
		
		




