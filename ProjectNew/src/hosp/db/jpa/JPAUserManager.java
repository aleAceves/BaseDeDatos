package hosp.db.jpa;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import hosp.db.ifaces.UserManager;
import hosp.db.pojos.Operation;
import hosp.db.pojos.Patient;
import hosp.db.pojos.users.Role;
import hosp.db.pojos.users.User;





public class JPAUserManager implements UserManager {
	
	
	
	private EntityManager em;

	@Override
	public void connect() {
		em = Persistence.createEntityManagerFactory("user-hospital").createEntityManager();;
		
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();
		List<Role> existingRoles = this.getRoles();
		if (existingRoles.size()<2) {
			this.newRole(new Role("admin"));
			this.newRole(new Role("user"));
		}

	}

	@Override
	public void disconnect() {
		em.close();

	}
	

	// delete surgeon using JDBC
	@Override
	public void deleteSurgeon(Integer surgeonId) {
		
	}
	
	//delete nurse using JDBC
	@Override
	public void deleteNurse(Integer nurseId) {
		
	}
	
	//delete patient using JPA
	@Override
	public void deletePatient(Integer id) {

		int patient_id = id;
		Query q2 = em.createNativeQuery("SELECT * FROM patients WHERE id = ?", Patient.class);
		q2.setParameter(1, patient_id);
		Patient p = (Patient) q2.getSingleResult();

		em.getTransaction().begin();
		em.remove(p);
		em.getTransaction().commit();		
	}
	
	
	@Override
	public List<Patient> selectPatients() {
		Query q1 = em.createNativeQuery("SELECT * FROM patients", Patient.class);
		List <Patient> patients= (List<Patient>) q1.getResultList();
		return patients;
	}

	//delete operation using JPA
	@Override
	public void deleteOperation(Integer id) {

		int operation_id = id;
		Query q2 = em.createNativeQuery("SELECT * FROM operation WHERE id = ?", Operation.class);
		q2.setParameter(1, operation_id);
		Operation o = (Operation) q2.getSingleResult();

		em.getTransaction().begin();
		em.remove(o);
		em.getTransaction().commit();

	}
	
	
	//update patient using JPA
	@Override
	public void updatePatient(Patient p) {
		// Begin transaction
		em.getTransaction().begin();
		// Make changes
		em.flush();
		// End transaction
		em.getTransaction().commit();

	}

	@Override
	public void newUser(User u) {
		em.getTransaction().begin();
		em.persist(u);
		em.getTransaction().commit();

	}

	@Override
	public void newRole(Role r) {
		em.getTransaction().begin();
		em.persist(r);
		em.getTransaction().commit();

	}

	@Override
	public Role getRole(int id) {
		Query q = em.createNativeQuery("SELECT * FROM roles WHERE id = ?", Role.class);
		q.setParameter(1, id);
		return (Role) q.getSingleResult();
		
	}

	@Override
	public List<Role> getRoles() {
		Query q = em.createNativeQuery("SELECT * FROM roles", Role.class);
		return (List<Role>) q.getResultList(); //return a list of objects, that we are casting
		
	}

	@Override
	public User checkPassword(String email, String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5"); //what we are going to use for the hash function
			md.update(password.getBytes());
			byte[] hash = md.digest();
			Query q = em.createNativeQuery("SELECT * FROM users WHERE email = ? AND password = ?", User.class);
			q.setParameter(1, email);
			q.setParameter(2, hash);
			return (User) q.getSingleResult();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoResultException nre) {
			return null;
		}
		return null;
	
		
	}

}
