package se.boalbert.offerthanterare.services;

import se.boalbert.offerthanterare.models.OfferStats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface OfferDataService {

	List<OfferStats> getAllOffers();

	void importOfferData() throws IOException;

	OfferStats getOfferById(int id);

	void saveArrayListToCsv(ArrayList<OfferStats> arrayList, String filepath);


	void updateAllOffersWithUpdatedFields(OfferStats offerStats);


}
