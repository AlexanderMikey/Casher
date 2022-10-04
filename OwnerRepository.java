package casher;
import java.sql.*;

public final class OwnerRepository {
	
	private final String USERNAME = "root"; 
	private final String PASSWORD = ""; 
	private final String DATABASE = "casher"; 
	private final String HOST = "localhost:3306";
	private final String CONECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	private Connection con;
	private Statement st;
	private static OwnerRepository connect;
	private Owner owner;
	
    private OwnerRepository() {
    	try {  
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(CONECTION, USERNAME, PASSWORD);  
            st = con.createStatement(); 
        } catch(Exception e) {
        	e.printStackTrace();
        	System.out.println("Failed to connect the database, the system is terminated!");
        	System.exit(0);
        }  
    }
    
    public static synchronized OwnerRepository getConnection() {
		return connect = (connect == null) ? new OwnerRepository() : connect;
    }

    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
    	try {
            rs = st.executeQuery(query);
        } catch(Exception e) {
        	e.printStackTrace();
        }
        return rs;
    }

    public void executeUpdate(String query) {
    	try {
			st.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public PreparedStatement prepareStatement(String query) {
    	PreparedStatement ps = null;
    	try {
			ps = con.prepareStatement(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ps;
    }
    
    public void OwnerDB(Owner Owner) {
    	try {
			PreparedStatement ps = connect.prepareStatement("INSERT INTO Owner VALUES (?,?,?,?) ");
			ps.setString(1, Owner.getOwnerId());
			ps.setString(2, Owner.getOwnerName());
			ps.setString(3, Owner.getOwnerEmail());
			ps.setString(4, Owner.getOwnerPhoneNumber());

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void OwnerList() {
    	String ownerID;
		String ownerName;
		String ownerEmail;
		String ownerPhoneNumber;
		try {
			ResultSet rs = connect.executeQuery("SELECT * FROM Owner");
			while(rs.next()) {
				ownerID = rs.getString("OwnerID");
				ownerName = rs.getString("OwnerName");
				ownerEmail = rs.getString("OwnerEmail");
				ownerPhoneNumber = rs.getString("OwnerPhoneNumber");
				
				owner = new Owner(ownerID, ownerName, ownerEmail, ownerPhoneNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}
  
}
