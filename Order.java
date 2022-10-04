package casher;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Order {
	private String orderId;
	private String orderDate;
	private ArrayList<Integer> quantity;
	private ArrayList<Product> product;
	private static Product p;
	private Payment payment;
	private Customer customer;
	private static OrderRepository or = OrderRepository.getConnection();

	public Order(String orderId) {
		this.orderId = orderId;
		this.quantity = new ArrayList<>();
		this.product = new ArrayList<>();
		Order.p = new Product("", "", 0);
		this.payment = new Payment("", "");
		this.customer = new Customer("", "", "", "");
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public ArrayList<Integer> getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity.add(quantity);
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public ArrayList<Product> getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product.add(product);
	}
	
	public void setProductList(ArrayList<Product> product) {
		this.product = product;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(String paymentId, String paymentType) {
		this.payment = new Payment(paymentId, paymentType);
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void generateId() {
		String id = "OR000";
		try {
			ResultSet rs = or.executeQuery("SELECT * FROM Orders");
			while (rs.next()) {
				id =rs.getString("OrderID");
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
		id = String.format("OR%03d", newId);

		this.setOrderId(id);
	}

	public double calculatePrice(Product product, int quantity) {
		double totalPrice = product.getproductPrice() * quantity;
		return totalPrice;
	}

	public void printOrderCustomer() {
		System.out.println("OrderDetails");
		System.out.println("============");
		System.out.println("OrderID : " + this.getOrderId());
		System.out.println("Customer Name : " + this.getCustomer().getCustomerName());
		System.out.println("=======================================================================");
		System.out.println("| ProductBrand             | Quantity   | Price        | Total Price  |");
		System.out.println("=======================================================================");
		for (int i = 0; i < this.getProduct().size(); i++) {
			System.out.printf("| %-25s|", this.product.get(i).getProductBrand());
			System.out.printf(" %-11d|", this.getQuantity().get(i));
			System.out.printf(" %-13.2f|", this.product.get(i).getproductPrice());
			System.out.printf(" %-13.2f|\n", this.calculatePrice(this.product.get(i), this.getQuantity().get(i)));
		}
		System.out.println("=======================================================================");
	}
	
	public static ArrayList<Order> getOrder() {
		or.OrderList();
		ArrayList<Order> order = or.getOrder();
		return order;
	}
	
	public void ViewOrderHistory() {
		or.OrderList();
		ArrayList<Order> order = or.getOrder();
		System.out.println("=========================================");
		System.out.println("| OrderID                  | OrderDate  |");
		System.out.println("=========================================");
		for (Order order2 : order) {
			order2.printDetailOrder();
		}
		System.out.println("=========================================");
	}
	
	public void printDetailOrder() {
		System.out.printf("| %-25s|", this.orderId);
		System.out.printf(" %-11s|\n", this.orderDate);
	}

	public static OrderRepository getOr() {
		return or;
	}

	public static Product getP() {
		return p;
	}

}
