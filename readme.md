# Spring CRM System / Offerthanterare

Program för att underlätta offerthantering för ett specifikt Pyramid affärssystem. Systemet stödjer import och export av .CSV-filer.
Användargränssnittet är byggt i Thymeleaf med lite hjälp av lite Javascript och CSS.

Översikt:
* Importerar .CSV-data från affärssystemet. 
* Publicerar data via controller och sparar i en List<Offer>.
* Förändringar användare gör sparas ner kontinuerling i en .CSV-fil.
* Ovan fil importeras vid satt intervall till affärssystemet och tas sedan bort (sköts externt)
* Affärsystemet exporterar ny data (.CSV) som sedan importeras av programmet.

Körs just nu som .war-fil i en Tomcat applikationsserver.

## Verktyg och teknologier

* Spring
* Thymeleaf
* JavaScript/CSS/HTML
* Commons CSV

## TODO / WIP
* Ordentlig front-end som inte bygger på Thymeleaf.
* Hantera konfiguration via Consul
* Driftsätt mha Docker istället för Tomcat
* Ersätt .CSV hanteringen med databas-lösning för mellanlagring av data

## Obs!
Väldigt work-in-progress. Planen är att detta ska göras om till en enklare API. Vissa workarounds har gjorts för att anpassa programmet att lira med affärssystemet och att det inte fanns möjlighet att köra en egen databas i driftsmiljön.
Ovan TODO hanteras i mån av tid.