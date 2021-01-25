package se.boalbert.offerthanterare.services;

import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se.boalbert.offerthanterare.models.Offer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class OfferDataServiceImpl implements OfferDataService {

	Logger logger = LoggerFactory.getLogger(OfferDataServiceImpl.class);

	@Value("${pathdir.import}")
	private String OFFER_DIR;
	@Value("${pathdir.export}")
	private String EXPORT_UPDATES_DIR;

	@Value("${file.export.mlt}")
	private String FILENAME_MLT;
	@Value("${file.export.protoma}")
	private String FILENAME_PROTOMA;
	@Value("${file.export.runtime}")
	private String FILENAME_RUNTIME;

	private String dateUpdated = null;

	@Override
	public String getDateUpdated() {
		return dateUpdated;
	}

	private List<Offer> allOffers = new ArrayList<>();


	@Override
	@PostConstruct
	@Scheduled(cron = "0 10 6,10,14,18 * * *")
	public void populateAllStatsWithImportedStats() {
		logger.info("Trying to run running schedueled import");
		logger.info("0 10 6,10,14,18 * * *");
		try {
			logger.info("Looking for .csv import file in " + OFFER_DIR);
			this.allOffers = ReadWriteDataImpl.importDataFromFolder(OFFER_DIR);
			logger.info("Imported: " + this.allOffers.size() + " offers.");
		} catch (ParseException | IOException e) {
			logger.error(e.getMessage());
			logger.error(Arrays.toString(e.getStackTrace()));
		}

		this.dateUpdated = LocalDate.now().toString();
	}

	@Override
	public Offer createOffer(CSVRecord record) throws ParseException {

		/*
		Structure of CSV-file to import:
		"KB, "Ordernr","Företagskod","Företag","Namn","Säljare", "Er referens", "Ert ordernr", "Status", "Chans","Belopp","Ändrad datum", "Reg datum","Kommentar"
		*/

		Offer offer = new Offer();

		offer.setCompany(record.get(0));
		offer.setOfferNo(Integer.parseInt(record.get(1)));
		offer.setCustomerNo(Integer.parseInt(record.get(2)));
		offer.setCustomerName(record.get(3));
		offer.setOfferName(record.get(4));
		offer.setSalesPerson(record.get(5));
		offer.setCustomerContact(record.get(6));
		offer.setPurchaseOrder(record.get(7));
		if (record.get(8) != null) offer.setStatus(Integer.parseInt(record.get(8)));
		if (record.get(9) != null) offer.setChance(Integer.parseInt(record.get(9)));
		offer.setOfferSum(Double.parseDouble(record.get(10)));
		offer.setDateUpdated(record.get(11));
		offer.setDateCreated(record.get(12));
		offer.setComment(record.get(13));
		offer.setDateDiff(calcDateDiff(offer.getDateCreated(), offer.getDateUpdated()));

		return offer;
	}

	@Override
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

	@Override
	public String findCompany(Offer offer) {
		if (offer.getCompany().equalsIgnoreCase("MLT AB")) {
			logger.info("Saving: " + offer.toString());
			logger.info("Location: " + EXPORT_UPDATES_DIR + FILENAME_MLT);
			return EXPORT_UPDATES_DIR + FILENAME_MLT;
		} else if (offer.getCompany().equalsIgnoreCase("Protoma")) {
			logger.info("Saving: " + offer.toString());
			logger.info("Location: " + EXPORT_UPDATES_DIR + FILENAME_PROTOMA);
			return EXPORT_UPDATES_DIR + FILENAME_PROTOMA;
		}
		else if (offer.getCompany().equalsIgnoreCase("Runtime")) {
			logger.info("Saving: " + offer.toString());
			logger.info("Location: " + EXPORT_UPDATES_DIR + FILENAME_RUNTIME);
			return EXPORT_UPDATES_DIR + FILENAME_RUNTIME;
		} else {
			logger.info("Failed, saving to null");
		}

		return null;
	}

	@Override
	public List<Offer> offersCustomer(List<Offer> allOffers, Offer offerStats) {

		List<Offer> offersCustomer = new ArrayList<>();

		for (Offer offer : allOffers) {
			if (offer.getCustomerName().equalsIgnoreCase(offerStats.getCustomerName()) && offer.getOfferNo() != offerStats.getOfferNo()) {
				offersCustomer.add(offer);
			}
		}

		return offersCustomer;
	}

	@Override
	public List<Offer> getAllOffersSortedByChance() {
		Collections.sort(allOffers, Comparator.comparingInt(Offer :: getChance).reversed());
		return allOffers;
	}

	@Override
	public List<Offer> getAllOffers() {
		return allOffers;
	}


	@Override
	public Offer getOfferById(int id) {

		for (Offer allOffer : allOffers) {
			if (allOffer.getOfferNo() == id) {

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

	@Override
	public String date() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm");
		LocalDateTime now = LocalDateTime.now();

		return now.format(dtf);
	}
}