package casher;

public class ProductDetail {
	private int stock;
	private String size;
	
	public ProductDetail(int stock, String size) {
		this.stock = stock;
		this.size = size;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
}
