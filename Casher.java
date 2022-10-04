package casher;

import java.util.Scanner;


public class Casher {

	public static void menu() {
		System.out.println("");
		System.out.println("Welcome to Casher ^_^");
		System.out.println("=====================");
		System.out.println("1.Product");
		System.out.println("2.Transaction");
		System.out.println("3.View History");
		System.out.print(">>> ");
	}

	public static void menuProduct() {
		System.out.println("");
		System.out.println("Product");
		System.out.println("=======");
		System.out.println("1. Add Product");
		System.out.println("2. View Product");
		System.out.println("3. Remove Product");
		System.out.println("4. Update Stock");
		System.out.println("5. Exit");
		System.out.print(">>> ");

	}

	public static void menuHistory() {
		System.out.println("");
		System.out.println("History");
		System.out.println("=======");
		System.out.println("1. Product");
		System.out.println("2. Transaction");
		System.out.println("3. Customer Transaction");
		System.out.println("4. Exit");
		System.out.print(">>> ");
	}

	public static void main(String args[]) {
		Scanner input = new Scanner(System.in);
		Owner owner = Owner.initializedAplication(input);
		OrderRepository or = Order.getOr();

		int choose = 0;
		do {
			Casher.menu();
			try {
				choose = input.nextInt();
			} catch (Exception e) {
				choose = 0;
			}
			input.nextLine();

			if(choose == 1) {
				int chooseP = 0;
				do {
					Casher.menuProduct();
					try {
						chooseP = input.nextInt();
					} catch (Exception e) {
						choose = 0;
					}
					input.nextLine();
					if (chooseP == 1) {
						Product.addProduct(input);
					}

					else if(chooseP == 2) {
						Product.ViewProduct();					
					}

					else if(chooseP == 3) {
						Product.ViewProduct();
						Product.deleteProduct(input);
					}

					else if(chooseP == 4) {
						Product.updateStock(input);
					}

				} while (chooseP != 5);
			}


			else if(choose == 2) {
				Customer.makeOrder(input);
			}

			else if(choose == 3) {
				int chooseH = 0;
				do {
					Casher.menuHistory();
					try {
						chooseH = input.nextInt();
					} catch (Exception e) {
						chooseH = 0;
					}


					if(chooseH == 1) {
						Product.printProductEvent();
					}
					else if(chooseH == 2) {
						owner.getOrder().ViewOrderHistory();
					}
					else if(chooseH == 3) {
						or.showCustomerHistoryOrder(input);
					}

				} while (chooseH != 4);
			}
			
			OwnerRepository ow = OwnerRepository.getConnection();
			ow.OwnerList();

		} while (true);

	}
}
