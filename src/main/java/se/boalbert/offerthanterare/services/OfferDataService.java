package se.boalbert.offerthanterare.services;

import org.apache.commons.csv.CSVRecord;
import se.boalbert.offerthanterare.models.Offer;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.List;

public interface OfferDataService {
	String getDateUpdated();

//	@PostConstruct
//	@Scheduled(cron = "*/5 * * * *")

	void populateAllStatsWithImportedStats();

	Offer createOffer(CSVRecord record) throws ParseException;

	long calcDateDiff(String regDate, String updateDate) throws ParseException;

	String findCompany(Offer offer);

	List<Offer> offersCustomer(List<Offer> allOffers, Offer offerStats);

	List<Offer> getAllOffersSortedByChance();

	List<Offer> getAllOffers();

	Offer getOfferById(int id);

	void updateObjectInAllOffersList(Offer offerStats);

	String date();
}
