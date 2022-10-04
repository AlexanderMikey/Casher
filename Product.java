package casher;

import java.util.ArrayList;
import java.util.Scanner;

public class Product {
	private String productId;
	private String productBrand;
	private double productPrice; 
	private ProductDetail productDetail;
	ArrayList<ProductEvent> productEvent;
	private static ProductRepository pr = ProductRepository.getConnection();

	public Product(String productId, String productBrand, double productPrice) {
		this.productId = productId;
		this.productBrand = productBrand;
		this.productPrice = productPrice;
		this.productEvent = new ArrayList<>();
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
	}

	public double getproductPrice() {
		return productPrice;
	}

	public void setproductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public ProductDetail getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(ProductDetail productDetail) {
		this.productDetail = productDetail;
	}

	public ArrayList<ProductEvent> getProductEvent() {
		return productEvent;
	}

	public void setProductEvent(ProductEvent productEvent) {
		this.productEvent.add(productEvent);
	}

	public ProductEvent initializedProductEvent(String productId, int quantity, String action) {
		return new ProductEvent(productId, quantity, action);
	}

	public ProductDetail initializedProductDetail(int stock, String size) {
		return new ProductDetail(stock, size);
	}


	public boolean isProductAvailable(ArrayList<Product> p) {
		boolean isBreak = false;
		if(p.size() == 0) {
			return false;
		}
		else{
			for (Product product : p) {
				if(product.getProductDetail().getStock() >= 1) {
					isBreak = true;
				}
			}
		}
		return isBreak;
	}

	public static void addProduct(Scanner input) {
		String productId;
		String productBrand;
		double productPrice = 0;
		int stock = 0;

		String size;
		System.out.print("Input ProductID: ");
		productId = input.nextLine();
		System.out.print("Input ProductBrand: ");
		productBrand = input.nextLine();

		boolean isBreak = true;
		do {
			System.out.print("Input ProductStock: ");
			try {
				stock = input.nextInt();
				isBreak = false;
			} catch (Exception e) {
				isBreak = true;
			}
			input.nextLine();
		}while(stock < 1 || isBreak == true);

		System.out.print("Input ProductSize: ");
		size = input.nextLine();

		do {
			System.out.print("Input ProductPrice: ");
			try {
				productPrice = input.nextInt();
				isBreak = false;
			} catch (Exception e) {
				isBreak = true;
			}
			input.nextLine();
		}while(productPrice < 1 || isBreak == true);

		Product p = new Product(productId, productBrand, productPrice);
		p.setProductDetail(p.initializedProductDetail(stock, size));
		p.setProductEvent(p.initializedProductEvent(productId, stock, "Add"));
		pr.ProductDB(p);

		pr.ProductEventDB(p.initializedProductEvent(productId, stock, "Add"));

	}

	public static void deleteProduct(Scanner input) {
		String id;
		pr.ProductList();
		ArrayList<Product> p = pr.getProduct();
		if(p.size() == 0) {
			System.out.println("No Product available to delete..");
		}
		else {
			System.out.print("Input ProductID to be deleted : ");
			id = input.nextLine();
			pr.deleteProduct(id);
		}
	}

	public static void ViewProduct() {
		pr.ProductList();
		ArrayList<Product> product = pr.getProduct();
		System.out.println("==================================================================================================");
		System.out.println("| ProductID      | ProductBrand             | Price      | Stock      | Size                     |");
		System.out.println("==================================================================================================");
		for (Product product2 : product) {
			product2.getDetailProduct();
		}
		System.out.println("==================================================================================================");
	}

	public static void updateStock(Scanner input) {
		pr.ProductList();
		ArrayList<Product> product = pr.getProduct();
		Product.ViewProduct();
		String productId;
		int quantity = 0;
		boolean isBreak = true;
		if(product.size() == 0) {
			System.out.println("No Product available to Update..");
		}
		else {
			do {
				System.out.print("Input product to Update [Valid ProductID]: ");
				productId = input.nextLine();
			} while (ProductRepository.isValidProduct(product, productId) == -1);

			do {
				System.out.print("Input Quantity: ");
				try {
					quantity = input.nextInt();
					isBreak = false;
				} catch (Exception e) {
					isBreak = true;
				}
				input.nextLine();
			}while(quantity < 1 || isBreak == true);

			int menuP = 0;

			System.out.println("");
			System.out.println("Stock");
			System.out.println("=======");
			System.out.println("1. Add More Stock");
			System.out.println("2. Reduce Stock");
			System.out.println("3. Exit");
			System.out.print(">>> ");

			try {
				menuP = input.nextInt();
			} catch (Exception e) {
				menuP = 0;
			}
			input.nextLine();

			if(menuP == 1) {
				pr.addStock(productId, quantity);
				pr.ProductEventDB(new ProductEvent(productId, quantity, "Add"));
			}

			else if (menuP == 2) {
				if(product.get(ProductRepository.isValidProduct(product, productId)).getProductDetail().getStock() >= quantity) {
					pr.reduceStock(productId, quantity);
					pr.ProductEventDB(new ProductEvent(productId, quantity, "Reduce"));
				}
				else {
					System.out.println("Stock can't be reduced");
				}
			}

		}
	}

	public static void printProductEvent() {
		pr.ProductEventList();
		ArrayList<ProductEvent> productEvent = pr.getProductEvent();
		System.out.println("=================================================================");
		System.out.println("| ProductId                 | Quantity    | Action              |");
		System.out.println("=================================================================");
		for (ProductEvent productEvent2 : productEvent) {
			productEvent2.printDetailProductEvent();
		}
		System.out.println("=================================================================");
	}

	public void getDetailProduct() {
		System.out.printf("| %-15s|", this.getProductId());
		System.out.printf(" %-25s|", this.getProductBrand());
		System.out.printf(" %-11s|", this.getproductPrice());
		System.out.printf(" %-11d|", this.getProductDetail().getStock());
		System.out.printf(" %-25s|\n", this.getProductDetail().getSize());
	}

	public ProductRepository getPr() {
		return pr;
	}

}
