package se.boalbert.offerthanterare.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import se.boalbert.offerthanterare.models.OfferStats;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class OfferDataServiceImpl {

    private static String OFFER_DATA_FILE ="src/main/java/se/boalbert/offerthanterare/datasource/OFFERDATA.TXT";

    public List<OfferStats> getAllOffers() {
        return allOffers;
    }

//    private static SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD", ISO);

//    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern( "YYYY-MM-DD" ) ;

    private List<OfferStats> allOffers = new ArrayList<>();

    @PostConstruct
    public void importOfferData() throws IOException, ParseException {

        // This list will temporarily hold the imported data
        List<OfferStats> newStats = new ArrayList<>();

        Reader in = new FileReader(OFFER_DATA_FILE, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withNullString("").withFirstRecordAsHeader().withQuote('"').parse(in);
        for (CSVRecord record : records) {

            // "Ordernr","Företagskod","Företag","Namn","Säljare","Status","Chans","Belopp","Ändrad datum","Reg datum","Kommentar","Typ"

            OfferStats offerStats = new OfferStats();

            offerStats.setOrderNr(Integer.parseInt(record.get("Ordernr")));
            offerStats.setForetagKod(Integer.parseInt(record.get("Företagskod")));
            offerStats.setForetagNamn(record.get("Företag"));
            offerStats.setOffertNamn(record.get("Namn"));
            offerStats.setSaljare(record.get("Säljare"));
            offerStats.setStatus(record.get("Status"));
            offerStats.setChans(record.get("Chans"));
            offerStats.setBelopp(Double.parseDouble(record.get("Belopp")));
            offerStats.setUpdateDate(record.get("Ändrad datum"));
            offerStats.setRegDate(record.get("Reg datum"));
            offerStats.setKommentar(record.get("Kommentar"));

            System.out.println(offerStats.toString());

            newStats.add(offerStats);
        }

        this.allOffers = newStats;




    }



}

