package se.boalbert.offerthanterare.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.boalbert.offerthanterare.models.OfferStats;
import se.boalbert.offerthanterare.services.OfferDataServiceImpl;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    OfferDataServiceImpl offerDataServiceImpl;

    @GetMapping("/")
    public String home(Model model) {

        List<OfferStats> allOfferStats = offerDataServiceImpl.getAllOffers();
        Collections.sort(allOfferStats, Collections.reverseOrder());


        long totalBelopp = allOfferStats.stream().map(stat -> stat.getOffertNamn()).count();

        model.addAttribute("offerStats", allOfferStats);
        model.addAttribute("totalBelopp", totalBelopp);

        return "index";
    }

    @GetMapping("/update/{id}")
    public String showUpdatePage(@PathVariable(value = "id") int id Model model) {




    }
}
