package se.boalbert.offerthanterare.models;

public class Offer {

	private String company;
	private int offerNo;
	private int customerNo;
	private String customerName;
	private String offerName;
	private String salesPerson;
	private int status;
	private int chance;
	private double offerSum;
	private String dateUpdated;
	private String dateCreated;
	private String comment;
	private long dateDiff;

	@Override
	public String toString() {
		return "Offer{" +
				"company='" + company + '\'' +
				", offerNo=" + offerNo +
				", customerNo=" + customerNo +
				", customerName='" + customerName + '\'' +
				", offerName='" + offerName + '\'' +
				", salesPerson='" + salesPerson + '\'' +
				", status=" + status +
				", chance=" + chance +
				", offerSum=" + offerSum +
				", dateUpdated='" + dateUpdated + '\'' +
				", dateCreated='" + dateCreated + '\'' +
				", comment='" + comment + '\'' +
				", dateDiff=" + dateDiff +
				'}';
	}

	public long getDateDiff() {
		return dateDiff;
	}

	public void setDateDiff(long dateDiff) {
		this.dateDiff = dateDiff;
	}

	public int getOfferNo() {
		return offerNo;

	}

	public void setOfferNo(int offerNo) {
		this.offerNo = offerNo;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public int getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(int customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

	public double getOfferSum() {
		return offerSum;
	}

	public void setOfferSum(double offerSum) {
		this.offerSum = offerSum;
	}

	public String getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
