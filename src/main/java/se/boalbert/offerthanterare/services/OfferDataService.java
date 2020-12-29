package se.boalbert.offerthanterare.services;

import se.boalbert.offerthanterare.models.Offer;

import java.util.List;

public interface OfferDataService {

	List<Offer> getAllOffers();

//	void importOfferData() throws IOException;

	Offer getOfferById(int id);

//	void saveArrayListToCsv(ArrayList<OfferStats> arrayList, String filepath);


	void updateObjectInAllOffersList(Offer offer);


}
