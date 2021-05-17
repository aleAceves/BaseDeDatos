package hosp.ui;

import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.BufferedReader;

import hosp.db.ifaces.DBManager;
import hosp.db.ifaces.UserManager;
import hosp.db.jdbc.JDBCManager;
import hosp.db.jpa.JPAUserManager;
import hosp.db.pojos.Surgeon;
import hosp.db.pojos.users.Role;
import hosp.db.pojos.users.User;
import hosp.db.pojos.Operation;
import hosp.db.pojos.Patient;
import hosp.db.pojos.Nurse;


public class Menu {
	
	// static, because is the only DBManager that is going to work
	private static DBManager dbman = new JDBCManager();
	private static UserManager userman = new JPAUserManager();
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	// Used for parsing dates
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static void main(String[] args) throws Exception {
		
		dbman.connect();
		userman.connect();
		
		
		do {
			System.out.println("Choose an option:");
			System.out.println("1: Register");
			System.out.println("3: Log in");
	
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
		} else if (user.getRole().getName().equalsIgnoreCase("user")) {
			userMenu();
		}
		
		
	}



	private static void userMenu()throws Exception {
		// TODO Auto-generated method stub
		do {
			System.out.println("Choose an option:");
			//TODO
			System.out.println("0: Exit");
			int choice = Integer.parseInt(reader.readLine());
			
			switch (choice) {
			case 1:
				
				break;
			
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

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



	private static void adminMenu() throws Exception{
		do {
			System.out.println("Choose an option:");
			System.out.println("1: Add surgeon");
			System.out.println("2: Search a surgeon");
			System.out.println("3: Add an operation");
			System.out.println("4: Search an operation");
			System.out.println("5: Add a surgeon to an operation");
			System.out.println("6: Eliminate a surgeon from an operation");
			System.out.println("7: Add a nurse");
			System.out.println("8: Search a nurse");
			System.out.println("9: Add a nurse to an operation");
			System.out.println("10: Eliminate a nurse from an operation");
			System.out.println("11: Add a patient");
			System.out.println("12: Search a patient");
			System.out.println("13: Add a patient to a surgery"); //TODO
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
				addOperation();
				break;
			case 4: 
				searchOperationByName();
				break;
			case 5: 
				hireSurgeon();
				break;
			case 6:
				fireSurgeon();
				break;
			case 7:
				addNurse();
				break;
			case 8:
				searchNurseByName();
				break;
			case 9:
				hireNurse();
				break;
			case 10:
				fireNurse();
				break;
			case 11:
				addPatient();
				break;
			case 12:
				searchPatientByName();
				break;
			case 13:
				//hirePatient();
				break;
			case 0:
				return;
			default:
				break;	
			}
			
		}while(true); // to show again the menu

}

	/*
	private static void hirePatient() throws Exception{
		// TODO Auto-generated method stub
		
		System.out.println("Choose the operation which is going to be perform:");
		searchOperationByName();
		System.out.println("Type the operation´s id:");
		int operationId = Integer.parseInt(reader.readLine());
		System.out.println("Agregate the patient:");
		searchPatientByName();
		System.out.println("Type the patient´s id:");
		int patientId = Integer.parseInt(reader.readLine());
		dbman.hirePatient(dbman.getOperation(operationId), dbman.getPatient(patientId)); 
		
	}
*/

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

	private static void hireNurse() throws Exception{
		// we need to find a nurse and to find an operation; we have methods for that
		System.out.println("Choose the operation where is going to assist:");
		searchOperationByName();
		System.out.println("Type the operation´s id:");
		int operationId = Integer.parseInt(reader.readLine());
		System.out.println("Choose the nurse");
		searchNurseByName();
		System.out.println("Type the nurse´s id:");
		int nurseId = Integer.parseInt(reader.readLine());
		dbman.hireNurse(dbman.getOperation(operationId), dbman.getNurse(nurseId)); //we call the methods that search by the id
		
		
	}

	private static void fireNurse() throws Exception {
		System.out.println("Choose the operation where is NOT going to assist:");
		searchOperationByName();
		System.out.println("Type the operation´s id:");
		int operationId = Integer.parseInt(reader.readLine());
		System.out.println("Choose the nurse");
		searchNurseByName();
		System.out.println("Type the nurse´s id:");
		int nurseId = Integer.parseInt(reader.readLine());
		dbman.fireNurse(operationId,nurseId); 
	}


	private static void searchNurseByName()throws Exception {
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
		
	private static void searchSurgeonByName() throws Exception {
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
	private static void addOperation() throws Exception {
			System.out.println("1: Input the operation info:");
			System.out.println("Type:");
			String type=reader.readLine();
			System.out.println("Start Date (yyyy-MM-dd):");
			LocalDate startDate = LocalDate.parse(reader.readLine(), formatter);
			System.out.println("Total duration:");
			int duration=Integer.parseInt(reader.readLine());
			dbman.addOperation(new Operation(type,Date.valueOf(startDate),duration)); //transform date into a sql date
		}
		
	private static void searchOperationByName() throws Exception {
			System.out.println("Input:");
			System.out.println("Name contains:");
			String name = reader.readLine();
			List<Operation> operations = dbman.searchOperationByName(name);
			if (operations.isEmpty()) {
				System.out.println("No results");
			}else {
				System.out.println(operations);
			}
		}
	
	private static void hireSurgeon()throws Exception{
		// we need to find a surgeon and to find an operation; we have methods for that
		System.out.println("Choose the operation where is going to assist:");
		searchOperationByName();
		System.out.println("Type the operation´s id:");
		int operationId = Integer.parseInt(reader.readLine());
		System.out.println("Choose the surgeon");
		searchSurgeonByName();
		System.out.println("Type the surgeon´s id:");
		int surgeonId = Integer.parseInt(reader.readLine());
		dbman.hireSurgeon(dbman.getOperation(operationId), dbman.getSurgeon(surgeonId)); //we call the methods that search by the id
	}
	
	private static void fireSurgeon()throws Exception{
		System.out.println("Choose the operation where is going to assist:");
		searchOperationByName();
		System.out.println("Type the operation´s id:");
		int operationId = Integer.parseInt(reader.readLine());
		System.out.println("Choose first the surgeon");
		searchSurgeonByName();
		System.out.println("Type the surgeon´s id:");
		int surgeonId = Integer.parseInt(reader.readLine());
		dbman.fireSurgeon(operationId,surgeonId); 
		
	}
			


}
		
	
		
		




