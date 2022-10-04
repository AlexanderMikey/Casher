package casher;

import java.util.ArrayList;
import java.util.Scanner;

public class Customer {
	private String customerId;
	private String customerName;
	private String customerEmail;
	private String customerPhoneNumber;
	private Address address;
	private ArrayList<Order> order;
	
	public Customer(String customerId, String customerName, String customerEmail, String customerPhoneNumber) {
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.customerPhoneNumber = customerPhoneNumber;
		this.order = new ArrayList<>();
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public ArrayList<Order> getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order.add(order);
	}
	
	public Address initializedAddress(String street, String city, String zipCode) {
		return new Address(street, city, zipCode);
	}
	
	public static boolean isValidEmail(String email) {
		email = email.toLowerCase();
		if(email.length() == 0) {
			return false;
		}

		int at = -1;
		int dot = -1;
		for(int i = 0; i < email.length(); i++) {
			if(email.charAt(i) == '@') {
				at = i;
			}
			else if(email.charAt(i) == '.') {
				dot = i;
			}
		}

		if(!(email.charAt(0) >= 'a' && email.charAt(0) <='z' || email.charAt(0) >= '0' && email.charAt(0) <='9')) {
			return false;
		}

		else if(dot == -1 || at == -1) {
			return false;
		}

		else if(dot < at) {
			return false;
		}

		else if(!email.endsWith("com")) {
			return false;
		}

		return true;
	}
	
	public static void makeOrder(Scanner input){
		OrderRepository or = Order.getOr();
		or.CustomerList();
		ArrayList<Customer> customer = or.getCustomer();
		String id;
		double total = 0;

		System.out.println("");
		System.out.print("Input Register CustomerID: ");
		id = input.nextLine();

		if (or.isValidCustomer(customer, id) == -1) {
			String customerId;
			String customerName;
			String customerEmail;
			String customerPhoneNumber;
			String street;
			String city;
			String zipCode;
			System.out.print("Input CustomerID: ");
			customerId = input.nextLine();
			System.out.print("Input CustomerName: ");
			customerName = input.nextLine();

			do {
				System.out.print("Input CustomerEmail [StartsWith alphabet or number, contains ['@' before '.'] EndsWith [.com]: ");
				customerEmail = input.nextLine();
			} while (!isValidEmail(customerEmail));

			System.out.print("Input CustomerPhoneNumber: ");
			customerPhoneNumber = input.nextLine();
			System.out.print("Input CustomerStreet: ");
			street = input.nextLine();
			System.out.print("Input CustomerCity: ");
			city = input.nextLine();
			System.out.print("Input CustomerzipCode: ");
			zipCode = input.nextLine();

			id = customerId;
			Customer s = new Customer(customerId, customerName, customerEmail, customerPhoneNumber);
			s.setAddress(s.initializedAddress(street, city, zipCode));
			or.CustomerDB(s);
		}
		or.CustomerList();
		customer = or.getCustomer();
		ArrayList<Product> product;
		Order orders = new Order("");
		orders.generateId();
		int idx = or.isValidCustomer(customer, id);
		orders.setCustomer(customer.get(idx));
		customer.get(idx).setOrder(orders);
		int chooseT = 0;
		String productId;

		do {
			ProductRepository p = Order.getP().getPr();
			p.ProductList();
			product = p.getProduct();
			
			System.out.println("");
			System.out.println("Transaction");
			System.out.println("=======");
			System.out.println("1. Buy Product");
			System.out.println("2. Exit");
			System.out.print(">>> ");
			
			try {
				chooseT = input.nextInt();
			} catch (Exception e) {
				chooseT = 0;
			}
			input.nextLine();

			if(chooseT == 1) {
				int quantity = 0;
				boolean isBreak = true;
				Product.ViewProduct();

				if (Order.getP().isProductAvailable(product)) {
					do {
						System.out.print("Input product to buy [Valid ProductID]: ");
						productId = input.nextLine();
					} while (ProductRepository.isValidProduct(product, productId) == -1);
					if (product.get(ProductRepository.isValidProduct(product, productId)).getProductDetail()
							.getStock() > 0) {
						do {
							int qty = product.get(ProductRepository.isValidProduct(product, productId)).getProductDetail()
									.getStock();
							System.out.print("Input Quantity [<=" + qty + "]: ");
							try {
								quantity = input.nextInt();
								isBreak = false;
							} catch (Exception e) {
								isBreak = true;
							}
							input.nextLine();
						} while (product.get(ProductRepository.isValidProduct(product, productId)).getProductDetail()
								.getStock() < quantity || isBreak == true);

						orders.setProduct(product.get(ProductRepository.isValidProduct(product, productId)));
						total = total + (product.get(ProductRepository.isValidProduct(product, productId)).getproductPrice())*quantity;
						orders.setQuantity(quantity);
						p.reduceStock(productId, quantity);
						p.ProductEventDB(new ProductEvent(productId, quantity, "Reduce"));
					} else {
						System.out.println("No stock left..");
					}
				}
				else {
					System.out.println("Out of Stock..");
				}
			}

			else if(chooseT == 2) {
				String type;
				String currency;
				if (orders.getProduct().size() != 0) {
					do {
						System.out.print("Payment Type[Cash | Card]: ");
						type = input.nextLine();
					} while (!type.equals("Cash") && !type.equals("Card"));
					
					do {
						System.out.print("Payment Type[USD | RM]: ");
						currency = input.nextLine();
					} while (!currency.equalsIgnoreCase("USD") && !currency.equals("RM"));
					
					orders.printOrderCustomer();
					Order.getOrder();
					orders.setPayment(or.generatePaymentId(), type);
					orders.getPayment().setMoney(orders.getPayment().initializedMoney(total, currency));
					System.out.println("Total Price : " + orders.getPayment().getMoney().convertCurrency(total) + " " + orders.getPayment().getMoney().getCurrency());
					or.OrderDB(orders);
					or.orderDetailsDB(orders);
					or.PaymentDB(orders.getPayment(), orders.getOrderId());
				}
			}

		} while (chooseT != 2);	
	}
	
}
