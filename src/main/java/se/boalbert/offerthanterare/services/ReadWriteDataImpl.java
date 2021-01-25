package se.boalbert.offerthanterare.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.boalbert.offerthanterare.models.Offer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReadWriteDataImpl {

	static Logger logger = LoggerFactory.getLogger(ReadWriteDataImpl.class);

	private static final OfferDataServiceImpl offerDataServiceImpl = new OfferDataServiceImpl();

	public static ArrayList<Offer> importDataFromFolder(String offerDirectory) throws IOException, ParseException {

		Path filePath = Paths.get(offerDirectory);

		logger.trace("Looking for offers in: ");
		logger.trace(filePath.toString());
		List<Path> listFiles = listFiles(filePath);

		ArrayList<Offer> importedOffers = new ArrayList<>();

		for (Path file : listFiles) {

			String absolutPath = String.valueOf(file.toAbsolutePath());
			logger.trace("Absolute filepath to offer files: ");
			logger.trace(absolutPath);

			Reader fileReader = new FileReader(absolutPath, StandardCharsets.UTF_8);
			Iterable<CSVRecord> recordsOffers = CSVFormat.
					newFormat(',')
					.withQuote('"')
					.withIgnoreEmptyLines(true)
					.withNullString("")
					.withRecordSeparator("\r\n")
					.parse(fileReader);

			for (CSVRecord recordOffer : recordsOffers) {

				Offer offer = offerDataServiceImpl.createOffer(recordOffer);

				importedOffers.add(offer);

			}
		}
		return importedOffers;
	}

	public static List<Path> listFiles(Path path) throws IOException {

		List<Path> result;
		try (Stream<Path> walk = Files.walk(path)) {
			result = walk.filter(Files :: isRegularFile)
					.collect(Collectors.toList());
		}
		return result;
	}

	public void saveObjectToCSV(Offer offer, String filepath) {

		String head = "<HEAD>";
		int offerNo = offer.getOfferNo();
		int status = offer.getStatus();
		int chance = offer.getChance();
		String customerContact = offer.getCustomerContact();
		String purchaseOrder = offer.getPurchaseOrder();

		try (CSVPrinter printer = new CSVPrinter(new FileWriter(filepath, StandardCharsets.UTF_8, true), CSVFormat.EXCEL)) {
			printer.printRecord(head, offerNo, status, chance, customerContact, purchaseOrder);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
