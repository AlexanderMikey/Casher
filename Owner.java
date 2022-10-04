package casher;

import java.util.Scanner;

public class Owner {
	private String ownerId;
	private String ownerName;
	private String ownerEmail;
	private String ownerPhoneNumber;
	private Product product;
	private Order order;
	
	public Owner(String ownerId, String ownerName, String ownerEmail, String ownerPhoneNumber) {
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.ownerEmail = ownerEmail;
		this.ownerPhoneNumber = ownerPhoneNumber;
		this.order = new Order("");
		this.product = new Product("", "", 0);
	}
	
	public String getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public String getOwnerEmail() {
		return ownerEmail;
	}
	
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	
	public String getOwnerPhoneNumber() {
		return ownerPhoneNumber;
	}
	
	public void setOwnerPhoneNumber(String ownerPhoneNumber) {
		this.ownerPhoneNumber = ownerPhoneNumber;
	}

	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
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

	public static Owner initializedAplication(Scanner input) {
		String ownerId;
		String ownerName;
		String ownerEmail;
		String ownerPhoneNumber;

		System.out.print("Input Owner ID: ");
		ownerId = input.nextLine();
		System.out.print("Input Owner Name: ");
		ownerName = input.nextLine();

		do {
			System.out.print("Input Owner Email [StartsWith alphabet or number, contains ['@' before '.'] EndsWith [.com]: ");
			ownerEmail = input.nextLine();
		} while (!isValidEmail(ownerEmail));

		System.out.print("Input Owner PhoneNumber: ");
		ownerPhoneNumber = input.nextLine();

		Owner owner = new Owner(ownerId, ownerName, ownerEmail, ownerPhoneNumber);

		OwnerRepository or = OwnerRepository.getConnection();
		or.OwnerDB(owner);
		
		owner.getOwnerDetail();
		return owner;
	}
	
	public void getOwnerDetail() {
		System.out.println("Owner");
		System.out.println("=====");
		System.out.println("OwnerID: " + this.getOwnerId());
		System.out.println("OwnerName: " + this.getOwnerName());
		System.out.println("OwnerEmail: " + this.getOwnerEmail());
		System.out.println("OwnerPhoneNumber: " + this.getOwnerPhoneNumber());
	}
}
