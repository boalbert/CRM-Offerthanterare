package se.boalbert.offerthanterare.models;


// "Ordernr","Företagskod","Företag","Namn","Säljare","Status","Chans","Belopp","Ändrad datum","Reg datum","Kommentar","Typ"

import java.util.Date;

public class OfferStats {

    private int ordernr;
    private String företagsKod;
    private String företagsNamn;
    private String offertNamn;
    private String saljare;
    private int status;
    private int chans;
    private double belopp;
    private Date updateDate;
    private Date regDate;
    private String kommentar;

    public int getOrdernr() {
        return ordernr;
    }

    public void setOrdernr(int ordernr) {
        this.ordernr = ordernr;
    }

    public String getFöretagsKod() {
        return företagsKod;
    }

    public void setFöretagsKod(String företagsKod) {
        this.företagsKod = företagsKod;
    }

    public String getFöretagsNamn() {
        return företagsNamn;
    }

    public void setFöretagsNamn(String företagsNamn) {
        this.företagsNamn = företagsNamn;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }
}
