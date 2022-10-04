package casher;
import java.sql.*;
import java.util.ArrayList;

public final class ProductRepository {
	
	private final String USERNAME = "root"; 
	private final String PASSWORD = ""; 
	private final String DATABASE = "casher"; 
	private final String HOST = "localhost:3306";
	private final String CONECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
	
	private Connection con;
	private Statement st;
	private static ProductRepository connect;
	private ArrayList<Product> product;
	private ArrayList<ProductEvent> productEvent;
	
    private ProductRepository() {
    	try {  
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(CONECTION, USERNAME, PASSWORD);  
            st = con.createStatement(); 
        } catch(Exception e) {
        	e.printStackTrace();
        	System.out.println("Failed to connect the database, the system is terminated!");
        	System.exit(0);
        }  
    	this.product = new ArrayList<>();
    	this.productEvent = new ArrayList<>();
    }
    
    public static synchronized ProductRepository getConnection() {
		return connect = (connect == null) ? new ProductRepository() : connect;
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
	
    public static int isValidProduct(ArrayList<Product> p, String id) {
		if(p.size() == 0) {
			return -1;
		}
		else{
			for (int i = 0; i < p.size(); i++) {
				if (p.get(i).getProductId().equals(id)) {
					return i;
				}
			} 
		}
		return -1;
	}
    
    public void ProductDB(Product product) {
    	try {
			PreparedStatement ps = connect.prepareStatement("INSERT INTO Products VALUES (?,?,?,?,?) ");
			ps.setString(1, product.getProductId());
			ps.setString(2, product.getProductBrand());
			ps.setDouble(3, product.getproductPrice());
			ps.setInt(4, product.getProductDetail().getStock());
			ps.setString(5, product.getProductDetail().getSize());

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void ProductList() {
    	product.clear();
    	String productId;
		String productBrand;
		double productPrice = 0;
		int stock = 0;
		String size;
		int quantity = 0;
		String action;
		try {
			ResultSet rs = connect.executeQuery("SELECT * FROM Products");
			while(rs.next()) {
				productId = rs.getString("ProductID");
				productBrand = rs.getString("ProductBrand");
				productPrice = rs.getDouble("ProductPrice");
				stock = rs.getInt("Stock");
				size = rs.getString("Size");
				
				Product p = new Product(productId, productBrand, productPrice);
				p.setProductDetail(p.initializedProductDetail(stock, size));
				
				this.product.add(p);
				
			}
			
			ResultSet rsp = connect.executeQuery("SELECT * FROM ProductEvents");
			while(rsp.next()) {
				productId = rsp.getString("ProductID");
				quantity = rsp.getInt("Quantity");
				action = rsp.getString("Action");
				
				int index = isValidProduct(product, productId);
				if (product.get(index).getProductId().equals(productId)) {
					ProductEvent p = product.get(index).initializedProductEvent(productId, quantity, action);
					product.get(index).setProductEvent(p);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void ProductEventDB(ProductEvent productEvent) {
    	try {
			PreparedStatement ps = connect.prepareStatement("INSERT INTO ProductEvents VALUES (?,?,?) ");
			ps.setString(1, productEvent.getProductId());
			ps.setInt(2, productEvent.getQuantity());
			ps.setString(3, productEvent.getAction());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void ProductEventList() {
    	productEvent.clear();
    	String productId;
		int quantity = 0;
		String action;
		
		try { 
			ResultSet rsp = connect.executeQuery("SELECT * FROM ProductEvents");
			while(rsp.next()) {
				productId = rsp.getString("ProductID");
				quantity = rsp.getInt("Quantity");
				action = rsp.getString("Action");
				ProductEvent p = new ProductEvent(productId, quantity, action);
				this.productEvent.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
    
    public void deleteProduct(String productId) {
    	try {
            PreparedStatement ps = connect.prepareStatement("DELETE FROM Products WHERE ProductID = ?");
            ps.setString(1, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();

        }
    	
    	try {
            PreparedStatement ps = connect.prepareStatement("DELETE FROM ProductEvents WHERE ProductID = ?");
            ps.setString(1, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    public void addStock(String productId, int stock) {
    	try {
            PreparedStatement ps = connect.prepareStatement("UPDATE Products SET Stock = Stock + ? WHERE ProductID = ?");
            ps.setInt(1, stock);
            ps.setString(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    public void reduceStock(String productId, int stock) {
    	try {
            PreparedStatement ps = connect.prepareStatement("UPDATE Products SET Stock = Stock - ? WHERE ProductID = ?");
            ps.setInt(1, stock);
            ps.setString(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

	public ArrayList<Product> getProduct() {
		return product;
	}

	public void setProduct(ArrayList<Product> Product) {
		this.product = Product;
	}

	public ArrayList<ProductEvent> getProductEvent() {
		return productEvent;
	}

	public void setProductEvent(ArrayList<ProductEvent> productEvent) {
		this.productEvent = productEvent;
	}
  
}
