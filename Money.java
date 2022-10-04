package casher;

public class Money {
	private double amount;
	private String currency;

	public Money(double amount, String currency) {
		this.amount = amount;
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double convertCurrency(double price) {
		if(this.getCurrency().equals("USD")) {
			double totalPrice = price / 14500;
			return totalPrice;
		}
		else if(this.getCurrency().equals("RM")) {
			double totalPrice = price / 3500;
			return totalPrice;
		}
		return this.getAmount();
	}
}
