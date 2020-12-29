package se.boalbert.offerthanterare.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import se.boalbert.offerthanterare.models.OfferStats;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OfferDataServiceImpl implements OfferDataService {

	String offerDirectory = "src/main/java/se/boalbert/offerthanterare/datasource/import";



	//TODO Update this to env variables before deploying
	private final String OFFER_DATA_UPDATES_MLT =           "src/main/java/se/boalbert/offerthanterare/datasource/MLT_UPDATES.CSV";
	private final String OFFER_DATA_UPDATES_PROTOMA =       "src/main/java/se/boalbert/offerthanterare/datasource/PROTOMA_UPDATES.CSV";
	private List<OfferStats> allOffers = new ArrayList<>();

	public ArrayList<OfferStats> importDataFromFolder() throws IOException, ParseException {

		Path filePath = Paths.get(offerDirectory);
		List<Path> listOffers = listFiles(filePath);

		ArrayList<OfferStats> importedOffers = new ArrayList<>();

		for (Path path : listOffers) {

			String absolutPath = String.valueOf(path.toAbsolutePath());

			Reader fileReader = new FileReader(absolutPath, StandardCharsets.UTF_8);
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
		}

		return importedOffers;
	}


	public static List<Path> listFiles(Path path) throws IOException {

		List<Path> result;
		try (Stream<Path> walk = Files.walk(path)) {
			result = walk.filter(Files :: isRegularFile)
					.collect(Collectors.toList());
		}
		return result;
	}

	//TODO @Scheduele()
	@PostConstruct
	private void populateAllStatsWithImportedStats() {

		try {
			this.allOffers = importDataFromFolder();
		} catch (ParseException | IOException e) {
			System.out.println("Problem when updatingAllOffers: " + e.getMessage());
			e.printStackTrace();
		}
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
		if (record.get(6) != null) offerStats.setStatus(Integer.parseInt(record.get(6)));
		if (record.get(7) != null) offerStats.setChans(Integer.parseInt(record.get(7)));
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

	public String checkObjectCompanyBeforeSavingToArrayList(OfferStats offerStats) {
		if (offerStats.getKoncernBolag().equalsIgnoreCase("MLT")) {
			return OFFER_DATA_UPDATES_MLT;
		} else if (offerStats.getKoncernBolag().equalsIgnoreCase("Protoma")) {
			return OFFER_DATA_UPDATES_PROTOMA;
		}
		return null;
	}

	public List<OfferStats> offersCustomer(List<OfferStats> allOffers, OfferStats offerStats) {

		List<OfferStats> offersCustomer = new ArrayList<>();

		for (OfferStats offer : allOffers) {
			if (offer.getForetagNamn().equalsIgnoreCase(offerStats.getForetagNamn()) && offer.getOrderNr() != offerStats.getOrderNr()) {
				offersCustomer.add(offer);
			}
		}

		return offersCustomer;
	}

	public void saveObjectToCSV(OfferStats offerStats, String filepath) {

		String head = "<HEAD>";
		int orderNr = offerStats.getOrderNr();
		int status = offerStats.getStatus();
		int chans = offerStats.getChans();


		try (CSVPrinter printer = new CSVPrinter(new FileWriter(filepath, StandardCharsets.UTF_8, true), CSVFormat.EXCEL)) {
			printer.printRecord(head, orderNr,status,chans);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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

	@Override
	public void updateObjectInAllOffersList(OfferStats offerStats) {
		int ordernr = offerStats.getOrderNr();
		int status = offerStats.getStatus();
		int chans = offerStats.getChans();
		String kommentar = offerStats.getKommentar();

		for (OfferStats offer : allOffers) {

			if (offer.getOrderNr() == ordernr) {
				offer.setStatus(status);
				offer.setChans(chans);
				offer.setKommentar(kommentar);
			}
		}
	}
}