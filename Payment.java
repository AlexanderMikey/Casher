package casher;

public class Payment {
	private String paymentId;
	private String paymentType;
	private Money money;
	
	public Payment(String paymentId, String paymentType) {
		this.paymentId = paymentId;
		this.paymentType = paymentType;
		money = new Money(0, "");
	}
	
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Money getMoney() {
		return money;
	}

	public void setMoney(Money money) {
		this.money = money;
	}
	
	public Money initializedMoney(double amount, String currency) {
		return new Money(amount, currency);
	}

}
