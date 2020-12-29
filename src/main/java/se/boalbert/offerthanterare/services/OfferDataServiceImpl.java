package se.boalbert.offerthanterare.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import se.boalbert.offerthanterare.models.Offer;

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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OfferDataServiceImpl implements OfferDataService {

	//TODO Update this to env variables before deploying
	private final String OFFER_DATA_UPDATES_MLT =           "src/main/java/se/boalbert/offerthanterare/datasource/MLT_UPDATES.CSV";
	private final String OFFER_DATA_UPDATES_PROTOMA =       "src/main/java/se/boalbert/offerthanterare/datasource/PROTOMA_UPDATES.CSV";

	private List<Offer> allOffers = new ArrayList<>();

	//TODO @Scheduele()
	@PostConstruct
	private void populateAllStatsWithImportedStats() {

		try {
			this.allOffers = ReadWriteDataImpl.importDataFromFolder();
		} catch (ParseException | IOException e) {
			System.out.println("Problem when updatingAllOffers: " + e.getMessage());
			e.printStackTrace();
		}
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
			System.out.println("Saving to: " + OFFER_DATA_UPDATES_MLT);
			return OFFER_DATA_UPDATES_MLT;
		} else if (offer.getCompany().equalsIgnoreCase("Protoma")) {
			System.out.println("Saving to: " + OFFER_DATA_UPDATES_PROTOMA);
			return OFFER_DATA_UPDATES_PROTOMA;
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
}