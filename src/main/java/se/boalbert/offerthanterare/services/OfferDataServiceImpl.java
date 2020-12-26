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
import java.util.concurrent.TimeUnit;

@Service
public class OfferDataServiceImpl implements OfferDataService {

	//TODO Update this to env variables before deploying
	private static final String OFFER_DATA_UPDATES = "src/main/java/se/boalbert/offerthanterare/datasource/offertUpdates.csv";
	private static final String OFFER_DATA_FILE_MLT = "src/main/java/se/boalbert/offerthanterare/datasource/MLT_OFFERDATA.TXT";
	private static final String OFFER_DATA_FILE_PROTOMA = "src/main/java/se/boalbert/offerthanterare/datasource/PROTOMA_OFFERDATA.TXT";

	private ArrayList<OfferStats> updatedOffers = new ArrayList<>();
	private List<OfferStats> allOffers = new ArrayList<>();

	//TODO @Scheduele()
	@PostConstruct
	private void populateAllStatsWithImportedStats() {

		try {
			this.allOffers = createCombinedListFromTwoCsvFiles(OFFER_DATA_FILE_PROTOMA, OFFER_DATA_FILE_MLT);
		} catch (ParseException | IOException e) {
			System.out.println("Problem when updatingAllOffers: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private ArrayList<OfferStats> createCombinedListFromTwoCsvFiles(String filepathOne, String filepathTwo) throws IOException, ParseException {
		ArrayList<OfferStats> allOffers = new ArrayList<>();

		ArrayList<OfferStats> fileOne = importDataFromCSV(filepathOne);
		ArrayList<OfferStats> fileTwo = importDataFromCSV(filepathTwo);

		allOffers.addAll(fileOne);
		allOffers.addAll(fileTwo);

		return allOffers;
	}

	private ArrayList<OfferStats> importDataFromCSV(String filepath) throws IOException, ParseException {

		ArrayList<OfferStats> importedOffers = new ArrayList<>();

		Reader fileReader = new FileReader(filepath, StandardCharsets.UTF_8);
		Iterable<CSVRecord> records = CSVFormat.
				newFormat(',')
				.withQuote('"')
				.withIgnoreEmptyLines(true)
				.withNullString("")
				.withRecordSeparator("\r\n")
				.parse(fileReader);

		for (CSVRecord record : records) {

			OfferStats offerStats = createOffer(record);

			importedOffers.add(offerStats);

		}

		return importedOffers;
	}

	private OfferStats createOffer(CSVRecord record) throws ParseException {

		/*
		Structure of CSV-file to import:
		"KB, "Ordernr","Företagskod","Företag","Namn","Säljare", "Status", "Chans","Belopp","Ändrad datum", "Reg datum","Kommentar"
		*/

		OfferStats offerStats = new OfferStats();

		offerStats.setKoncernBolag(record.get(0));
		offerStats.setOrderNr(Integer.parseInt(record.get(1)));
		offerStats.setForetagKod(Integer.parseInt(record.get(2)));
		offerStats.setForetagNamn(record.get(3));
		offerStats.setOffertNamn(record.get(4));

		offerStats.setSaljare(record.get(5));
		offerStats.setStatus(record.get(6));
		offerStats.setChans(record.get(7));
		offerStats.setBelopp(Double.parseDouble(record.get(8)));
		offerStats.setUpdateDate(record.get(9));
		offerStats.setRegDate(record.get(10));
		offerStats.setKommentar(record.get(11));
		offerStats.setDateDiff(calculateDateDiff(offerStats.getRegDate(), offerStats.getUpdateDate()));

		return offerStats;
	}

	public long calculateDateDiff(String regDate, String updateDate) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


		if (updateDate != null) {
			Date firstDate = sdf.parse(regDate);
			Date secondDate = sdf.parse(updateDate);

			long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
			long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			return diff;
		}
		return -1;

	}

	public List<OfferStats> offersCustomer(List<OfferStats> allOffers, OfferStats offerStats) {

		List<OfferStats> offersCustomer = new ArrayList<>();

		for (OfferStats offer : allOffers) {
			if (offer.getForetagNamn().equalsIgnoreCase(offerStats.getForetagNamn())) {

				offersCustomer.add(offer);
			}

		}

		offersCustomer.remove(offerStats);
		return offersCustomer;
	}

	//TODO Seperate lists per company
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

		for (OfferStats offer : allOffers) {

			if (offer.getOrderNr() == ordernr) {
				offer.setStatus(status);
				offer.setChans(chans);
				offer.setKommentar(kommentar);
			}
		}
	}

	public String getOfferDataUpdatesFilepath() {
		return OFFER_DATA_UPDATES;
	}

	public ArrayList<OfferStats> getUpdatedOffers() {
		return updatedOffers;
	}

	public List<OfferStats> getAllOffers() {
		return allOffers;
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


}