package se.boalbert.offerthanterare.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import se.boalbert.offerthanterare.models.Offer;
import se.boalbert.offerthanterare.services.OfferDataServiceImpl;
import se.boalbert.offerthanterare.services.ReadWriteDataImpl;

import java.util.List;

@Controller
public class HomeController {

	@Autowired
	OfferDataServiceImpl offerDataServiceImpl;

	@Autowired
	ReadWriteDataImpl readWriteDataImpl;

	@GetMapping("/")
	public String home(Model model) {

		List<Offer> allOffers = offerDataServiceImpl.getAllOffersSortedByChance();

		model.addAttribute("offers", allOffers);

		long counterOffers = allOffers.stream().map(Offer :: getOfferName).count();
		model.addAttribute("countOffers", counterOffers);

		int  sumOffers = (int) allOffers.stream().mapToDouble(Offer :: getOfferSum).sum();
		model.addAttribute("sumOffers", sumOffers);

		model.addAttribute("date", offerDataServiceImpl.getDateUpdated());

		return "index";
	}

	@GetMapping("/update/{id}")
	public String showUpdatePage(@PathVariable(value = "id") int id, Model model) {

		Offer offer = offerDataServiceImpl.getOfferById(id);

		// This offer object
		model.addAttribute("offer", offer);

		// Other offers for this customer
		model.addAttribute("offersCustomer", offerDataServiceImpl.offersCustomer(offerDataServiceImpl.getAllOffersSortedByChance(), offer));

		return "update";
	}


	@PostMapping("/updateOffer")
	public String saveOffer(@ModelAttribute("offer") Offer offer) {

		offerDataServiceImpl.updateObjectInAllOffersList(offer);
		readWriteDataImpl.saveObjectToCSV(offer, offerDataServiceImpl.findCompany(offer));

		return "redirect:/";
	}
}
