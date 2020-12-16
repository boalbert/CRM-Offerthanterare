package se.boalbert.offerthanterare.services;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import se.boalbert.offerthanterare.models.OfferStats;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Service
public class OfferDataServiceImpl implements OfferDataService {

	private static final String OFFER_DATA_UPDATES = "src/main/java/se/boalbert/offerthanterare/datasource/offertUpdates.csv";
	private static final String OFFER_DATA_FILE = "src/main/java/se/boalbert/offerthanterare/datasource/OFFERDATA.TXT";

	private ArrayList<OfferStats> updatedOffers = new ArrayList<>();
	private List<OfferStats> allOffers = new ArrayList<>();

	public String getOfferDataUpdatesFilepath() {
		return OFFER_DATA_UPDATES;
	}

	public ArrayList<OfferStats> getUpdatedOffers() {
		return updatedOffers;
	}

	public List<OfferStats> getAllOffers() {
		return allOffers;
	}

	// Implement scheduele when deploying
	// Schedules an interval when this method should run, defined via "cron = "second, minute, hour, day, month, year"
	// Need to use annotation @EnableScheduling in main-method for scheduling to work
	//	@Scheduled(cron = "* * 1 * * *") // Runs the first hour of every day

	@PostConstruct // Run this method when @Service-class is constructed / app starts
	public void importOfferData() throws IOException {

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


			try {
				offerStats.setDateDiff(calculateDateDiff(offerStats.getRegDate(),offerStats.getUpdateDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			newStats.add(offerStats);
		}

		this.allOffers = newStats;


	}

	@Override
	public OfferStats getOfferById(int id) {

		for (OfferStats allOffer : allOffers) {


			if (allOffer.getOrderNr() == id) {
				System.out.println("Offer found: " + allOffer.toString());
				return allOffer;
			}
		}
		return null;
	}

	@Override
	public void saveArrayListToCsv(ArrayList<OfferStats> arrayList, String filepath) {

		try {
			Writer writer = new FileWriter(filepath, true);
			StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).withSeparator(',').withApplyQuotesToAll(false).build();
			beanToCsv.write(arrayList);
			writer.close();
			System.out.println("Sucessfully written file to: " + filepath);
		} catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
			System.out.println("Writing to " + filepath + " failed.");
			e.printStackTrace();
		}
	}

	@Override
	public void updateAllOffersWithUpdatedFields(OfferStats offerStats) {
		int ordernr = offerStats.getOrderNr();
		String status = offerStats.getStatus();
		String chans = offerStats.getChans();
		String kommentar = offerStats.getKommentar();

		for (int i = 0; i < allOffers.size(); i++) {

			if (allOffers.get(i).getOrderNr() == ordernr) {
				allOffers.get(i).setStatus(status);
				allOffers.get(i).setChans(chans);
				allOffers.get(i).setKommentar(kommentar);
			}
		}
	}

	public long calculateDateDiff(String regDate, String updateDate) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


		if (updateDate != null) {
			Date firstDate = sdf.parse(regDate);
			Date secondDate = sdf.parse(updateDate);

			long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
			long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			return diff;
		} else {
			System.out.println("Updated value was null / not correct");
		}
		return -1;

	}


}