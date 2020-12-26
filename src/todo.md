# TODO
***

### Önskemål från CHER
* Sätt 1 månad = färg gul på rad
* Sätt 2 min = färg röd på rad
* Sortera offerter efter Chans

### Mina idéer
* Lägg in mer info under "Info/Uppdatera", lägg till detta i klass OffertStats
    * Er referens
    * Ert ordernr
    * Avdelning
    * Done - Liten tabell med alla andra offerter på kunden

### Etc
* Glöm ej lägga in PTC 850 som schemalagt, krävs för att import ska fungera
	//TODO Implement scheduele when deploying
	// Schedules an interval when this method should run, defined via "cron = "second, minute, hour, day, month, year"
	// Need to use annotation @EnableScheduling in main-method for scheduling to work
	//	@Scheduled(cron = "* * 1 * * *") // Runs the first hour of every day*/