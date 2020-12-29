package se.boalbert.offerthanterare.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
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
public class ReadWriteDataImpl implements ReadWriteData {

	private static final OfferDataServiceImpl offerDataServiceImpl = new OfferDataServiceImpl();

	private final static String offerDirectory = "\\\\PROTSRV6.intra.protoma.se\\Pyramiddok\\PYMLT\\EXPORT\\DATA\\OFFERT";

	public static ArrayList<Offer> importDataFromFolder() throws IOException, ParseException {

		Path filePath = Paths.get(offerDirectory);
		List<Path> listOffers = listFiles(filePath);

		ArrayList<Offer> importedOffers = new ArrayList<>();

		for (Path path : listOffers) {

			String absolutPath = String.valueOf(path.toAbsolutePath());

			Reader fileReader = new FileReader(absolutPath, StandardCharsets.UTF_8);
			Iterable<CSVRecord> records = CSVFormat.
					newFormat(',')
					.withQuote('"')
					.withIgnoreEmptyLines(true)
					.withNullString("")
					.withRecordSeparator("\r\n")
					.parse(fileReader);



			for (CSVRecord record : records) {

				Offer offer = offerDataServiceImpl.createOffer(record);

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
		int orderNr = offer.getOfferNo();
		int status = offer.getStatus();
		int chans = offer.getChance();


		try (CSVPrinter printer = new CSVPrinter(new FileWriter(filepath, StandardCharsets.UTF_8, true), CSVFormat.EXCEL)) {
			printer.printRecord(head, orderNr,status,chans);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


}
