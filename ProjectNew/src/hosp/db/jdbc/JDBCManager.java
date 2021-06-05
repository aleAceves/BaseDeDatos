package hosp.db.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hosp.db.ifaces.DBManager;
import hosp.db.pojos.Nurse;
import hosp.db.pojos.OperatingRoom;
import hosp.db.pojos.Operation;
import hosp.db.pojos.Patient;
import hosp.db.pojos.Surgeon;
import hosp.db.pojos.WaitingRoom;





public class JDBCManager implements DBManager { //everything related with the database
	//not put reader and prints here

	private static Connection c;
	
	@Override
	public void connect() {
		try {
			// Open database connection
			Class.forName("org.sqlite.JDBC");
			this.c = DriverManager.getConnection("jdbc:sqlite:./db/ProjectNew.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			System.out.println("Database connection opened.");
			this.createTables();
		} catch (SQLException sqlE) {
			System.out.println("There was a database exception.");
			sqlE.printStackTrace();
		} catch (Exception e) {
			System.out.println("There was a general exception.");
			e.printStackTrace();
		}
	}
	
	
	// CREATION OF TABLES AFTER CONNECT
	private void createTables() {
		
		Statement stm1;
		
		try{
		
		stm1= c.createStatement();
		
		String s1= "CREATE TABLE surgeons "
				+ "(id   INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " name  TEXT NOT NULL, "
				+ " surname TEXT NOT NULL, "
				+ " speciality TEXT NOT NULL )";
		
		stm1.executeUpdate(s1);

		s1= "CREATE TABLE patients "
				+ "(id   INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " name  TEXT NOT NULL, "
				+ " surname TEXT NOT NULL)";
				//+ " room_id INTEGER)";
				//+ " FOREIGN KEY(room_id) REFERENCES waiting_room (id)) "; 
			
		
		stm1.executeUpdate(s1);
		
		s1= "CREATE TABLE nurses "
				+ "(id   INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " name  TEXT NOT NULL, "
				+ " surname TEXT NOT NULL )";
		
		stm1.executeUpdate(s1);

		s1= "CREATE TABLE waiting_room "
				+ "(id   INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " name TEXT NOT NULL )";
		
		stm1.executeUpdate(s1);

		s1= "CREATE TABLE operating_room "
				+ "(id   INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " name TEXT NOT NULL )";
		
		stm1.executeUpdate(s1);
		
		s1= "CREATE TABLE operation "
				+ "(id   INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ " type TEXT NOT NULL,"
				+ " startdate DATE NOT NULL,"
				+ " duration INTEGER,"
			    + " roomId INTEGER,"
			    + " patientId INTEGER REFERENCES patients(id) ON UPDATE CASCADE ON DELETE SET NULL)";
				//+ " FOREIGN KEY(roomId) REFERENCES operating_room (id)";
		stm1.executeUpdate(s1);
		
		// Create table operations_surgeons: many to many relationship
		s1 = "CREATE TABLE operations_surgeons "
				+ "(operation_id INTEGER REFERENCES operation(id), "
				+ "surgeon_id INTEGER REFERENCES surgeons(id), " 
				+ "PRIMARY KEY (operation_id, surgeon_id))";
		stm1.executeUpdate(s1);
		
		// Create tale operations_nurses: many to many relationship
		s1 = "CREATE TABLE operations_nurses "
				+ "(operation_id INTEGER REFERENCES operation(id), "
				+ "nurse_id INTEGER REFERENCES nurses(id), " 
				+ "PRIMARY KEY (operation_id, nurse_id))";
		stm1.executeUpdate(s1);

		stm1.close();
		
		System.out.print("Tables created");
			
		} catch (SQLException e) {
			if (!e.getMessage().contains("already exists")) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void disconnect() {
		try {
			// Close database connection
			c.close();
		} catch (SQLException e) {
			System.out.println("There was a problem while closing the database connection.");
			e.printStackTrace();
		}

	}

	public void showNurses(){
		try{
			String sql = "SELECT * FROM nurses"; 
			Statement stm1= c.createStatement();
			ResultSet rs = stm1.executeQuery(sql);
			while (rs.next()) { // true: there is another result and I have advanced to it
								// false: there are no more results
			
				int id = rs.getInt("id");
			
				String Name = rs.getString("name");
				String Surname = rs.getString("surname");
			System.out.println(new Nurse(id, Name, Surname));
		
					//show Nurse 
			}
		}catch(Exception e){
			System.out.println("something went wrong");
			e.printStackTrace();
		}
		
		
	}
	public void showPatients(){
		try{
			String sql = "SELECT * FROM patients"; 
			Statement stm1= c.createStatement();
			ResultSet rs = stm1.executeQuery(sql);
			while (rs.next()) { // true: there is another result and I have advanced to it
								// false: there are no more results
			
				int id = rs.getInt("id");
			
				String Name = rs.getString("name");
				String Surname = rs.getString("surname");
			System.out.println(new Patient(id, Name, Surname));
		
					//show Nurse 
			}
		}catch(Exception e){
			System.out.println("something went wrong");
			e.printStackTrace();
		}
		
		
	}
	public void showSurgeons(){
		try{
			String sql = "SELECT * FROM surgeons"; 
			Statement stm1= c.createStatement();
			ResultSet rs = stm1.executeQuery(sql);
			while (rs.next()) { // true: there is another result and I have advanced to it
								// false: there are no more results
			
				int id = rs.getInt("id");
			
				String Name = rs.getString("name");
				String Surname = rs.getString("surname");
				String Speciality = rs.getString("speciality");
				Surgeon surgeon = new Surgeon (id, Name, Surname, Speciality); 
				System.out.println(surgeon);; //add the surgeon to the list
			}
		}catch(Exception e){
			System.out.println("something went wrong");
			e.printStackTrace();
		}
		
		
	}
	public void showOperations(){
		try{
			String sql = "SELECT * FROM operation"; 
			Statement stm1= c.createStatement();
			ResultSet rs = stm1.executeQuery(sql);
			while (rs.next()) { // true: there is another result and I have advanced to it
								// false: there are no more results
			
				int id = rs.getInt("id");
				String type = rs.getString("type");
				Date date = rs.getDate("startdate");
				int dur = rs.getInt("duration");
				Operation op = new Operation(id, type, date,dur); 
				System.out.println(op);; //show 
			}
		}catch(Exception e){
			System.out.println("something went wrong");
			e.printStackTrace();
		}
		
		
	}
	

	// METHOD FOR RETURN A LIST OF OPERATIONS WITH THE SAME ID OF THE SURGEON
	public List<Operation> showOperationsBySurgeonId(Integer surgeonId){
		
		List<Operation> operations = new ArrayList<Operation>();//creation of the list is going to return
		
		try {
			String sql = "SELECT * FROM operations_surgeons WHERE surgeon_id = ?"; //list of rows where the id of the operation is the one that we provided
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, surgeonId);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				int operationId = rs.getInt("operation_id");
		
				operations.add(this.getOperation(operationId)); 
			
				//we use the surgeon id, to get the whole surgeon and add it to the new list
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		OrderOperations(operations);
		return operations;
		
	}
	
	
	
	public List<Operation> showOperationsByNurseId(Integer nId){
		
		List<Operation> operations = new ArrayList<Operation>();//creation of the list is going to return
		
		try {
			String sql = "SELECT * FROM operations_nurses WHERE nurse_id = ?"; //list of rows where the id of the operation is the one that we provided
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, nId);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				int operationId = rs.getInt("operation_id");
		
				operations.add(this.getOperation(operationId)); 
	
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		OrderOperations(operations);
		return operations;
		
	}
	
	public List<Operation> showOperationsByPatientId(Integer nId){
		
		List<Operation> operations = new ArrayList<Operation>();//creation of the list is going to return
		
		//TODO
		return operations;
		
	}
	
	
	
	
	
	
	public void OrderOperations(List<Operation> LISTA){
	
		Collections.sort(LISTA, new Comparator<Operation>() {
			  public int compare(Operation o1, Operation o2) {
			      return o1.getStartdate().compareTo(o2.getStartdate());
			  }
			});
		
		
		
	}
	
	public void addSurgeon(Surgeon surgeon) {
		//we want to insert the new person into the database
		//create the statement, the SQL sentence
		try {
			// Id is chosen by the database
			String sql = "INSERT INTO surgeons (name,surname,speciality) VALUES (?,?,?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, surgeon.getName());
			prep.setString(2, surgeon.getSurname());
			prep.setString(3, surgeon.getSpeciality());
			//put just the atributes TODO (revise)
			//ResultSet rs = prep.executeQuery();
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//ADD NURSE
	@Override
	public void addNurse(Nurse nurse) {
		try {
			// Id is chosen by the database
			String sql = "INSERT INTO nurses (name,surname) VALUES (?,?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, nurse.getName());
			prep.setString(2, nurse.getSurname());

			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// SEARCH SURGEON BY NAME METHOD
	@Override
	public List<Surgeon> searchSurgeonByName(String name) {
		
		List<Surgeon> surgeons = new ArrayList<Surgeon>();//creation of the list is going to return
		try {
			String sql = "SELECT * FROM surgeons WHERE name LIKE ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, "%" + name + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) { // true: there is another result and I have advanced to it
								// false: there are no more results
				int id = rs.getInt("id");
				String Name = rs.getString("name");
				String Surname = rs.getString("surname");
				String Speciality = rs.getString("speciality");
				
				Surgeon surgeon = new Surgeon (id, Name, Surname, Speciality); 
				surgeons.add(surgeon); //add the surgeon to the list
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return surgeons; // return the list
		
	}
	
	
		
	
	
	//SEARCH NURSES BY NAME
	@Override
	public List<Nurse> searchNurseByName(String name) {
		
		List<Nurse> nurses = new ArrayList<Nurse>();//creation of the list is going to return
		try {
			String sql = "SELECT * FROM nurses WHERE name LIKE ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, "%" + name + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) { // true: there is another result and I have advanced to it
								// false: there are no more results
				int id = rs.getInt("id");
				String Name = rs.getString("name");
				String Surname = rs.getString("surname");
				Nurse nurse = new Nurse (id, Name, Surname); 
				nurses.add(nurse); //add the surgeon to the list
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return nurses; // return the list
		
	}

	@Override
	public void addOperation(Operation operation) {
		try {
			// Id is chosen by the database
			String sql = "INSERT INTO operation (type, startdate , duration , patientId) VALUES (?,?,?,?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, operation.getType());
			prep.setDate(2, operation.getStartdate());
			prep.setInt(3, operation.getDuration());
			prep.setInt(4, operation.getPatient().getId());
			//prep.setInt(5, operation.getOperatingRoom().getId());  PONER ROOM ID OTRA INTERROGACIÇON Y CREAR NUEVAS TABLAS CON ROOMID
			
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//FOR THE PATIENTS
		@Override
		public void addPatient(Patient patient) {
			try {
				// Id is chosen by the database
				String sql = "INSERT INTO patients (name,surname) VALUES (?,?)";
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setString(1, patient.getName());
				prep.setString(2, patient.getSurname());

				prep.executeUpdate();
				prep.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	@Override
	public Operation getOperation(int id) {
		
		try {
			String sql = "SELECT * FROM operation WHERE id = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			if (rs.next()) {
				String type = rs.getString("type");
				Date startDate = rs.getDate("startDate");
				Integer duration = rs.getInt("duration");
				//get the patient id
				//use another method to get the patient
				Integer patientId = rs.getInt("patientId");
				Patient patient = getPatient(patientId);
				//not include operations, just the atributes
				//Integer roomId = rs.getInt("roomId");
				OperatingRoom r = getOperationRoom(1);
				return new Operation(id,type,startDate,duration, patient,r);
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Operation> searchOperationByName(String name) {
		List<Operation> operations = new ArrayList<Operation>();//creation of the list is going to return
		try {
			String sql = "SELECT * FROM operation WHERE type LIKE ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, "%" + name + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) { // true: there is another result and I have advanced to it
								// false: there are no more results
				int id = rs.getInt("id");
				String type = rs.getString("type");
				Date startDate = rs.getDate("startDate");
				Integer duration = rs.getInt("duration");
				Integer patientId = rs.getInt("patientId");
				Integer roomId = rs.getInt("roomId"); //TODO
				Patient patient = getPatient(patientId);
				OperatingRoom r = getOperationRoom(roomId);
				Operation operation = new Operation (id,type,startDate,duration,patient,r);
				operation.setSurgeons(this.getSurgeonsOfOperation(operation.getId())); 
				operation.setNurses(this.getNursesOfOperation(operation.getId())); 
				operations.add(operation); //add the operation to the list
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return operations; // return the list 
	}
	
	

	//GET SURGEON
	@Override
	public Surgeon getSurgeon(int id) {
		try {
			String sql = "SELECT * FROM surgeons WHERE id = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery(); //is a query because is a select
			if (rs.next()) {
				String surgeonName = rs.getString("name");
				String surgeonSurname = rs.getString("surname");
				String surgeonSpeciality = rs.getString("speciality");
				//not include operations, just the atributes
				return new Surgeon(id, surgeonName, surgeonSurname,surgeonSpeciality);
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}
	
	//GET NURSE
	@Override
	public Nurse getNurse(int id) {
		try {
			String sql = "SELECT * FROM nurses WHERE id = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery(); //is a query because is a select
			if (rs.next()) {
				String nurseName = rs.getString("name");
				String nurseSurname = rs.getString("surname");
				//not include operations, just the atributes
				return new Nurse(id, nurseName, nurseSurname);
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// TODO HACER PARA EL RESTO DE POJOS
	
	// IN ORDER TO LINK THE TABLE OPERATIONS AND SURGEONS
	@Override
	public void hireSurgeon(Operation o,Surgeon s) { //we need to do an insert
		try {
			String sql = "INSERT INTO operations_surgeons (operation_id, surgeon_id) VALUES (?,?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, o.getId());
			prep.setInt(2, s.getId());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	//HIRE NURSE; TO LINT THE TABLE OPERATIONS AND NURSES
	@Override
	public void hireNurse(Operation o, Nurse n) {
		try {
			String sql = "INSERT INTO operations_nurses (operation_id, nurse_id) VALUES (?,?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, o.getId());
			prep.setInt(2, n.getId());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//FIRE SURGEONS
	@Override
	public void fireSurgeon(int operationId, int surgeonId) {
		try {
			String sql = "DELETE FROM operations_surgeons WHERE operation_id = ? AND surgeon_id = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, operationId);
			prep.setInt(2, surgeonId);
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	//FIRE NURSES FROM AN OPERATION
	@Override
	public void fireNurse(int operationId, int nurseId) {
		try {
			String sql = "DELETE FROM operations_nurses WHERE operation_id = ? AND nurse_id = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, operationId);
			prep.setInt(2, nurseId);
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	//GET SURGEONS OF OPERATION BY ID
	@Override
	public List<Surgeon> getSurgeonsOfOperation(int operationId) { //we will need a select query
		//we need to access two tables; surgeons and operations_surgeons tale
		
		List<Surgeon> surgeons = new ArrayList<Surgeon>(); //list of empty people
		try {
			String sql = "SELECT * FROM operations_surgeons WHERE operation_id = ?"; //list of rows where the id of the operation is the one that we provided
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, operationId);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				int surgeonId = rs.getInt("surgeon_id");
				surgeons.add(this.getSurgeon(surgeonId)); //we use the surgeon id, to get the whole surgeon and add it to the new list
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return surgeons;
	}

	//GET NURSES OF OPERATION BY ID
	@Override
	public List<Nurse> getNursesOfOperation(int operationId) {
	//we need to access two tables; nurses and operations_surgeons table
		
		List<Nurse> nurses = new ArrayList<Nurse>(); //list of empty people
		try {
			String sql = "SELECT * FROM operations_nurses WHERE operation_id = ?"; //list of rows where the id of the operation is the one that we provided
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, operationId);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				int nurseId = rs.getInt("nurse_id");
				nurses.add(this.getNurse(nurseId)); //we use the surgeon id, to get the whole surgeon and add it to the new list
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nurses;
		
	}

	
	
    //GET PATIENT OF OPERATION BY ID

	public Patient getPatient(int id) {
		try {
			String sql = "SELECT * FROM patients WHERE id = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			if (rs.next()) {
				String name = rs.getString("name");
				String surname = rs.getString("surname");
				return new Patient(id,name,surname);
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public List<Patient> searchPatientByName(String name) {
		List<Patient> patients = new ArrayList<Patient>();//creation of the list is going to return
		try {
			String sql = "SELECT * FROM patients WHERE name LIKE ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, "%" + name + "%");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) { 
				int id = rs.getInt("id");
				String Name = rs.getString("name");
				String Surname = rs.getString("surname");
				Patient patient = new Patient (id, Name, Surname); 
				patients.add(patient); //add the surgeon to the list
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return patients; // return the list
		
	}

	
	
	
	
	 //add an operation at the same time with the id of the patient
	
	@Override
	public void hirePatient(Patient patient) {
			try {
				String sql = "INSERT INTO operation (patient_id) VALUES (?)";
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setInt(1, patient.getId());
				prep.executeUpdate();
				prep.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		
	}
	
	//DELETE SURGEON using JDBC
	@Override
	public void deleteSurgeon(int surgeonId) {
		
		try {
			String sql = "DELETE * FROM surgeons WHERE id=?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, surgeonId);
			prep.executeUpdate();
			prep.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	//DELETE NURSE using JDBC
	@Override
	public void deleteNurse(int nurseId) {
		
		try {
			String sql = "DELETE FROM nurses WHERE id=?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, nurseId);
			prep.executeUpdate();
			prep.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	//delete patient using JPA
	@Override
	public void deletePatient(int patientId) {
		try {
			String sql = "DELETE FROM patients WHERE id=?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, patientId);
			prep.executeUpdate();
			prep.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//delete operation 
	@Override
	public void deleteOperation(int operationId) {
		try {
			String sql = "DELETE FROM operation WHERE id=?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, operationId);
			prep.executeUpdate();
			prep.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


	public void updateSurgeon(int id, String sp) {  
		try {
			String sql = "UPDATE surgeons SET speciality=? WHERE id=?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, sp);
			prep.setInt(2, id);
			
			prep.executeUpdate();
			prep.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void updateOperation(int id,String type) {
		try {
			String sql = "UPDATE operations SET type=? WHERE id=?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, type);
			prep.setInt(2, id);
			
			prep.executeUpdate();
			prep.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	

	@Override
	public OperatingRoom getOperationRoom(int id) {
		
		try {
			String s = "SELECT * FROM operating_room WHERE id=?";
			PreparedStatement p = c.prepareStatement(s);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			OperatingRoom r = null;
			while (rs.next()) {
				Integer r_id = rs.getInt("id");
				String r_name = rs.getString("name");

				r = new OperatingRoom(r_id, r_name);
			}
			return r;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
}
	
	public List<OperatingRoom> selectOperatingRooms() {
		try {
			Statement stmt = c.createStatement();
			String sql = "SELECT * FROM operating_room";
			ResultSet rs = stmt.executeQuery(sql);
			List<OperatingRoom> roomList = new ArrayList<OperatingRoom>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String roomtype = rs.getString("name");
				OperatingRoom room = new OperatingRoom(id, roomtype);
				roomList.add(room);
			}
			rs.close();
			stmt.close();
			return roomList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addOperationRoom(OperatingRoom operatingRoom) {
		
		try {
			// Id is chosen by the database
			String sql = "INSERT INTO operating_room (name) VALUES (?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, operatingRoom.getName());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<WaitingRoom> selectWaitingRooms() {
		try {
			Statement stmt = c.createStatement();
			String sql = "SELECT * FROM waiting_room";
			ResultSet rs = stmt.executeQuery(sql);
			List<WaitingRoom> roomList = new ArrayList<WaitingRoom>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String roomtype = rs.getString("name");
				WaitingRoom room = new WaitingRoom(id, roomtype);
				roomList.add(room);
			}
			rs.close();
			stmt.close();
			return roomList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void addWaitingRoom(WaitingRoom waitingRoom) {
		try {
			// Id is chosen by the database
			String sql = "INSERT INTO waiting_room (name) VALUES (?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, waitingRoom.getName());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
}


