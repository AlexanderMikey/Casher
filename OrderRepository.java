package casher;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public final class OrderRepository {

	private final String USERNAME = "root"; 
	private final String PASSWORD = ""; 
	private final String DATABASE = "casher"; 
	private final String HOST = "localhost:3306";
	private final String CONECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

	private Connection con;
	private Statement st;
	private static OrderRepository connect;
	private ArrayList<Payment> payment;
	private ArrayList<Customer> customer;
	private ArrayList<Order> order;

	private OrderRepository() {
		try {  
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(CONECTION, USERNAME, PASSWORD);  
			st = con.createStatement(); 
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect the database, the system is terminated!");
			System.exit(0);
		}  
		this.customer = new ArrayList<>();
		this.order = new ArrayList<>();
		this.payment = new ArrayList<>();
	}

	public static synchronized OrderRepository getConnection() {
		return connect = (connect == null) ? new OrderRepository() : connect;
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

	public void CustomerDB(Customer customer) {
		try {
			PreparedStatement ps = connect.prepareStatement("INSERT INTO Customers VALUES (?,?,?,?,?,?,?) ");
			ps.setString(1, customer.getCustomerId());
			ps.setString(2, customer.getCustomerName());
			ps.setString(3, customer.getCustomerEmail());
			ps.setString(4, customer.getCustomerPhoneNumber());
			ps.setString(5, customer.getAddress().getStreet());
			ps.setString(6, customer.getAddress().getCity());
			ps.setString(7, customer.getAddress().getZipCode());

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void CustomerList() {
		customer.clear();
		String customerId;
		String customerName;
		String customerEmail;
		String customerPhoneNumber;
		String street;
		String city;
		String zipCode;
		try {
			ResultSet rs = connect.executeQuery("SELECT * FROM Customers");
			while(rs.next()) {
				customerId = rs.getString("CustomerID");
				customerName = rs.getString("CustomerName");
				customerEmail = rs.getString("CustomerEmail");
				customerPhoneNumber = rs.getString("CustomerPhoneNumber");
				street = rs.getString("Street");
				city = rs.getString("City");
				zipCode = rs.getString("ZipCode");

				Customer cs = new Customer(customerId, customerName, customerEmail, customerPhoneNumber);
				cs.initializedAddress(street, city, zipCode);
				this.customer.add(cs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int isValidCustomer(ArrayList<Customer> customer, String id) {
		if(customer.size() == 0) {
			return -1;
		}
		else{
			for (int i = 0; i < customer.size(); i++) {
				if (customer.get(i).getCustomerId().equals(id)) {
					return i;
				}
			} 
		}
		return -1;
	}

	public int isValidOrder(ArrayList<Order> order, String id) {
		if(order.size() == 0) {
			return -1;
		}
		else{
			for (int i = 0; i < order.size(); i++) {
				if (order.get(i).getOrderId().equals(id)) {
					return i;
				}
			} 
		}
		return -1;
	}

	public void OrderList(String customerId) {
		CustomerList();
		String orderId;
		String productId;
		String orderDate;
		int quantity = 0;
		String productBrand;
		double productPrice = 0;
		int stock = 0;
		String size;
		try {
			ResultSet rs = connect.executeQuery("SELECT * FROM OrderDetails JOIN Orders ON Orders.OrderID = OrderDetails.OrderID "
					+ "JOIN Products ON Products.productId = OrderDetails.productID JOIN Customers ON Customers.customerID = orderDetails.customerID ORDER BY Orders.orderID ASC");
			while(rs.next()) {
				orderId = rs.getString("OrderID");
				productId = rs.getString("ProductID");
				orderDate = rs.getString("OrderDate");
				productBrand = rs.getString("ProductBrand");
				productPrice = rs.getDouble("ProductPrice");
				quantity = rs.getInt("Quantity");
				stock = rs.getInt("Stock");
				size = rs.getString("Size");
	
				int index = isValidCustomer(customer, customerId);
				if (customer.get(index).getCustomerId().equals(customerId)) {
					Order o = new Order(orderId);
					o.setOrderDate(orderDate);
					o.setCustomer(customer.get(index));

					int idx = isValidOrder(customer.get(index).getOrder(), orderId);
					if(idx == -1) {
						customer.get(index).setOrder(o);
					}

					for (int j = 0; j < customer.get(index).getOrder().size(); j++) {
						if(customer.get(index).getOrder().get(j).getOrderId().equals(orderId)) {
							Product p = new Product(productId, productBrand,productPrice);
							p.setProductDetail(p.initializedProductDetail(stock, size));
							customer.get(index).getOrder().get(j).setProduct(p);
							customer.get(index).getOrder().get(j).setQuantity(quantity);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showCustomerHistoryOrder(Scanner input) {
		input.nextLine();
		connect.CustomerList();
		ArrayList<Customer> customer = connect.getCustomer();
		String customerId;
		System.out.print("Input CustomerID : ");
		customerId = input.nextLine();
		int idx = connect.isValidCustomer(customer, customerId);
		
		if (idx == -1) {
			System.out.println("No history transaction..");
		}

		else {
			connect.OrderList(customerId);
			System.out.println("History transaction CustomerID " + customerId);

			for (int i = 0; i < customer.get(idx).getOrder().size(); i++) {
				customer.get(idx).getOrder().get(i).printOrderCustomer();
			}
		} 
	}
	
	public void OrderDB(Order order) {
    	try {
			PreparedStatement ps = connect.prepareStatement("INSERT INTO Orders(OrderID) VALUES (?) ");
			ps.setString(1, order.getOrderId());

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void orderDetailsDB(Order order) {
    	try {
			PreparedStatement ps = connect.prepareStatement("INSERT INTO OrderDetails VALUES (?,?,?,?) ");
			for (int i = 0; i < order.getProduct().size(); i++) {
				ps.setString(1, order.getOrderId());
				ps.setString(2, order.getProduct().get(i).getProductId());
				ps.setString(3, order.getCustomer().getCustomerId());
				ps.setInt(4, order.getQuantity().get(i));
				
				ps.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void OrderList() {
    	String orderId;
		String orderDate;
		try {
			ResultSet rs = connect.executeQuery("SELECT * FROM Orders");
			while(rs.next()) {
				orderId = rs.getString("OrderID");
				orderDate = rs.getString("OrderDate");
				
				Order o = new Order(orderId);
				o.setOrderDate(orderDate);
				this.order.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void PaymentDB(Payment payment, String id) {
    	try {
			PreparedStatement ps = connect.prepareStatement("INSERT INTO Payments VALUES (?,?,?,?,?) ");
			ps.setString(1, payment.getPaymentId());
			ps.setString(2, id);
			ps.setString(3, payment.getPaymentType());
			ps.setDouble(4, payment.getMoney().getAmount());
			ps.setString(5, payment.getMoney().getCurrency());

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	public String generatePaymentId() {
		String id = "PA000";
		try {
			ResultSet rs = executeQuery("SELECT * FROM Payments");
			while (rs.next()) {
				id =rs.getString("PaymentID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		id = id.substring(2,5);
		int newId = 0;
		try {
			newId = Integer.parseInt(id);
		} catch (Exception e) {
		}
		newId += 1;
		id = String.format("PA%03d", newId);

		return id;
	}
    
	public ArrayList<Payment> getPayment() {
		return payment;
	}

	public void setPaymentt(ArrayList<Payment> payment) {
		this.payment = payment;
	}

	public ArrayList<Order> getOrder() {
		return order;
	}

	public void setOrder(ArrayList<Order> order) {
		this.order = order;
	}
	
	public ArrayList<Customer> getCustomer() {
		return customer;
	}

	public void setCustomer(ArrayList<Customer> customer) {
		this.customer = customer;
	}

}
