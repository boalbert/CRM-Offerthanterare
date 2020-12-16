package se.boalbert.offerthanterare.models;


// "Ordernr","Företagskod","Företag","Namn","Säljare","Status","Chans","Belopp","Ändrad datum","Reg datum","Kommentar","Typ"

import com.opencsv.bean.CsvBindByName;

public class OfferStats implements Comparable<OfferStats> {

	@CsvBindByName(column = "#<HEAD>")
	public String annotation = "<HEAD>";

	@CsvBindByName(column = "1-Id")
	private int orderNr;
	private int foretagKod;
	private String foretagNamn;
	private String offertNamn;
	private String saljare;
	@CsvBindByName(column = "2-Status")
	private String status;
	@CsvBindByName(column = "3-Chans")
	private String chans;
	private double belopp;
	private String updateDate;
	private String regDate;
	private String kommentar;

	public int getOrderNr() {
		return orderNr;
	}

	public void setOrderNr(int orderNr) {
		this.orderNr = orderNr;
	}

	@Override
	public int compareTo(OfferStats o) {
		return this.getRegDate().compareTo(o.getRegDate());
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "OfferStats{" +
				"orderNr=" + orderNr +
				", foretagKod='" + foretagKod + '\'' +
				", foretagNamn='" + foretagNamn + '\'' +
				", offertNamn='" + offertNamn + '\'' +
				", saljare='" + saljare + '\'' +
				", status=" + status +
				", chans=" + chans +
				", belopp=" + belopp +
				", updateDate=" + updateDate +
				", regDate=" + regDate +
				", kommentar='" + kommentar + '\'' +
				'}';
	}

	public int getForetagKod() {
		return foretagKod;
	}

	public void setForetagKod(int foretagKod) {
		this.foretagKod = foretagKod;
	}

	public String getForetagNamn() {
		return foretagNamn;
	}

	public void setForetagNamn(String foretagNamn) {
		this.foretagNamn = foretagNamn;
	}

	public String getOffertNamn() {
		return offertNamn;
	}

	public void setOffertNamn(String offertNamn) {
		this.offertNamn = offertNamn;
	}

	public String getSaljare() {
		return saljare;
	}

	public void setSaljare(String saljare) {
		this.saljare = saljare;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getChans() {
		return chans;
	}

	public void setChans(String chans) {
		this.chans = chans;
	}

	public double getBelopp() {
		return belopp;
	}

	public void setBelopp(double belopp) {
		this.belopp = belopp;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}
}
