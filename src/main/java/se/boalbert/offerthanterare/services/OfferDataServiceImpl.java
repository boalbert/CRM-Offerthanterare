package se.boalbert.offerthanterare.services;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se.boalbert.offerthanterare.models.Offer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class OfferDataServiceImpl implements OfferDataService {


	@Value("${pathdir.import}")
	private String offerdir;
	@Value("${pathdir.export}")
	private String EXPORT_UPDATES_DIR;

	private final String FILENAME_MLT = "\\MLT_UPDATES.CSV";
	private final String FILENAME_PROTOMA = "\\PROTOMA_UPDATES.CSV";
	private final String FILENAME_RUNTIME = "\\RUNTIME_UPDATES.CSV";

	private String dateUpdated = date();

	public String getDateUpdated() {
		return dateUpdated;
	}

	private List<Offer> allOffers = new ArrayList<>();

	@PostConstruct
//	@Scheduled(cron = "*/5 * * * *")
	private void populateAllStatsWithImportedStats() {

		System.out.println("Trying to run Cron-job: ");

		try {
			System.out.println("Running cron-job.");
			this.allOffers = ReadWriteDataImpl.importDataFromFolder(offerdir);
		} catch (ParseException | IOException e) {
			System.out.println("Failed to run Cron-job");
			System.out.println("Problem when updatingAllOffers: " + e.getMessage());
			e.printStackTrace();
		}

		this.dateUpdated = date();
	}

	public Offer createOffer(CSVRecord record) throws ParseException {

		/*
		Structure of CSV-file to import:
		"KB, "Ordernr","Företagskod","Företag","Namn","Säljare", "Status", "Chans","Belopp","Ändrad datum", "Reg datum","Kommentar"
		*/

		Offer offer = new Offer();

		offer.setCompany(record.get(0));
		offer.setOfferNo(Integer.parseInt(record.get(1)));
		offer.setCustomerNo(Integer.parseInt(record.get(2)));
		offer.setCustomerName(record.get(3));
		offer.setOfferName(record.get(4));
		offer.setSalesPerson(record.get(5));
		if (record.get(6) != null) offer.setStatus(Integer.parseInt(record.get(6)));
		if (record.get(7) != null) offer.setChance(Integer.parseInt(record.get(7)));
		offer.setOfferSum(Double.parseDouble(record.get(8)));
		offer.setDateUpdated(record.get(9));
		offer.setDateCreated(record.get(10));
		offer.setComment(record.get(11));
		offer.setDateDiff(calcDateDiff(offer.getDateCreated(), offer.getDateUpdated()));

		return offer;
	}

	public long calcDateDiff(String regDate, String updateDate) throws ParseException {

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

	public String findCompany(Offer offer) {
		if (offer.getCompany().equalsIgnoreCase("MLT AB")) {
			System.out.println("Saving to: " + EXPORT_UPDATES_DIR + FILENAME_MLT);
			return EXPORT_UPDATES_DIR + FILENAME_MLT;
		} else if (offer.getCompany().equalsIgnoreCase("Protoma")) {
			System.out.println("Saving to: " + EXPORT_UPDATES_DIR + FILENAME_PROTOMA);
			return EXPORT_UPDATES_DIR + FILENAME_PROTOMA;
		}
		else if (offer.getCompany().equalsIgnoreCase("Runtime")) {
			System.out.println("Saving to: " + EXPORT_UPDATES_DIR + FILENAME_RUNTIME);
			return EXPORT_UPDATES_DIR + FILENAME_RUNTIME;
		}
		System.out.println("Failed, saving to null");
		return null;
	}

	public List<Offer> offersCustomer(List<Offer> allOffers, Offer offerStats) {

		List<Offer> offersCustomer = new ArrayList<>();

		for (Offer offer : allOffers) {
			if (offer.getCustomerName().equalsIgnoreCase(offerStats.getCustomerName()) && offer.getOfferNo() != offerStats.getOfferNo()) {
				offersCustomer.add(offer);
			}
		}

		return offersCustomer;
	}

	public List<Offer> getAllOffersSortedByChance() {
		Collections.sort(allOffers, Comparator.comparingInt(Offer :: getChance).reversed());
		return allOffers;
	}

	public List<Offer> getAllOffers() {
		return allOffers;
	}

	@Override
	public Offer getOfferById(int id) {

		for (Offer allOffer : allOffers) {
			if (allOffer.getOfferNo() == id) {
				System.out.println("Offer found: " + allOffer.toString());
				return allOffer;
			}
		}
		return null;
	}

	@Override
	public void updateObjectInAllOffersList(Offer offerStats) {
		int ordernr = offerStats.getOfferNo();
		int status = offerStats.getStatus();
		int chans = offerStats.getChance();
		String kommentar = offerStats.getComment();

		for (Offer offer : allOffers) {

			if (offer.getOfferNo() == ordernr) {
				offer.setStatus(status);
				offer.setChance(chans);
				offer.setComment(kommentar);
			}
		}
	}

	public String date() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm");
		LocalDateTime now = LocalDateTime.now();

		return now.format(dtf).toString();
	}
}