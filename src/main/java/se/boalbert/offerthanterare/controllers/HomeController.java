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
import java.util.List;

@Controller
public class HomeController {

	@Autowired
	OfferDataServiceImpl offerDataServiceImpl;

	@GetMapping("/")
	public String home(Model model) {

		List<OfferStats> allOfferStats = offerDataServiceImpl.getAllOffers();
		allOfferStats.sort(Collections.reverseOrder());


		long totalBelopp = allOfferStats.stream().map(OfferStats :: getOffertNamn).count();

		model.addAttribute("offerStats", allOfferStats);
		model.addAttribute("totalBelopp", totalBelopp);

		return "index";
	}

	@GetMapping("/modal")
	public String modal () {
		return "modal";
	}

	@GetMapping("/update/{id}")
	public String showUpdatePage(@PathVariable(value = "id") int id, Model model) {

		OfferStats offerStats = offerDataServiceImpl.getOfferById(id);

		model.addAttribute("offert", offerStats);
		model.addAttribute("listOffers", offerDataServiceImpl.getAllOffers());

		return "update";
	}


	@PostMapping("/updateOffer")
	public String saveOffer(@ModelAttribute("offer") OfferStats offerStats) {

		// Updated the read-Arraylist allOffers
		offerDataServiceImpl.updateAllOffersWithUpdatedFields(offerStats);


		// List<OfferStats> allOfferStats = offerDataServiceImpl.getAllOffers();
		ArrayList<OfferStats> updatedOffers = offerDataServiceImpl.getUpdatedOffers();

		updatedOffers.add(offerStats);

		offerDataServiceImpl.saveArrayListToCsv(updatedOffers, offerDataServiceImpl.getOfferDataUpdatesFilepath());
		// Saves it to .csv file, appends the new object

		// Clears the arraylist
		updatedOffers.clear();

		return "redirect:/";
	}


}
