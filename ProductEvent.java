package casher;

public class ProductEvent {
	private String productId;
	private int quantity;
	private String action;

	public ProductEvent(String productId, int quantity, String action) {
		this.productId = productId;
		this.quantity = quantity;
		this.action = action;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public void printDetailProductEvent() {
		System.out.printf("| %-25s |", this.getProductId());
		System.out.printf(" %-11d |", this.getQuantity());
		System.out.printf(" %-20s|\n", this.getAction());
		
	}
}

