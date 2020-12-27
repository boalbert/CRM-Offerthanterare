package se.boalbert.offerthanterare.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import se.boalbert.offerthanterare.models.OfferStats;
import se.boalbert.offerthanterare.services.OfferDataServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

	@Autowired
	OfferDataServiceImpl offerDataServiceImpl;

	@GetMapping("/")
	public String home(Model model) {

		List<OfferStats> allOfferStats = offerDataServiceImpl.getAllOffers();
		Collections.sort(allOfferStats, Comparator.comparingInt(OfferStats ::getChans).reversed());
		model.addAttribute("offerStats", allOfferStats);

		long amountOfOffers = allOfferStats.stream().map(OfferStats :: getOffertNamn).count();
		model.addAttribute("countOffers", amountOfOffers);

		int valueOfOffers = (int) allOfferStats.stream().mapToDouble(OfferStats::getBelopp).sum();
		model.addAttribute("valueOfOffers",valueOfOffers);

		return "index";
	}

	@GetMapping("/update/{id}")
	public String showUpdatePage(@PathVariable(value = "id") int id, Model model) {

		OfferStats offerStats = offerDataServiceImpl.getOfferById(id);

		// This offer object
		model.addAttribute("offert", offerStats);

		// Other offers for this customer
		model.addAttribute("offersCustomer", offerDataServiceImpl.offersCustomer(offerDataServiceImpl.getAllOffers(), offerStats));

		return "update";
	}


	@PostMapping("/updateOffer")
	public String saveOffer(@ModelAttribute("offer") OfferStats offerStats) {

		// Updated the read-Arraylist allOffers
		offerDataServiceImpl.updateAllOffersWithUpdatedFields(offerStats);

		// List<OfferStats> allOfferStats = offerDataServiceImpl.getAllOffers();
		ArrayList<OfferStats> updatedOffers = offerDataServiceImpl.getUpdatedOffers();

		//TODO Move this to controllers / inside function
		updatedOffers.add(offerStats);

		// Saves it to .csv file, appends the new object
		offerDataServiceImpl.saveArrayListToCsv(updatedOffers, offerDataServiceImpl.getOfferDataUpdatesFilepath());

		// Clears the arraylist
		updatedOffers.clear();

		return "redirect:/";
	}
}
