package se.boalbert.offerthanterare.models;

public class OfferStats {

	private int orderNr;
	private String koncernBolag;
	private int foretagKod;
	private String foretagNamn;
	private String offertNamn;
	private String saljare;
	private int status;
	private int chans;
	private double belopp;
	private String updateDate;
	private String regDate;
	private String kommentar;
	private long dateDiff;
	public long getDateDiff() {
		return dateDiff;
	}

	public void setDateDiff(long dateDiff) {
		this.dateDiff = dateDiff;
	}


	public int getOrderNr() {
		return orderNr;

	}

	public void setOrderNr(int orderNr) {
		this.orderNr = orderNr;
	}

	public String getKoncernBolag() {
		return koncernBolag;
	}

	public void setKoncernBolag(String koncernBolag) {
		this.koncernBolag = koncernBolag;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getChans() {
		return chans;
	}

	public void setChans(int chans) {
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
