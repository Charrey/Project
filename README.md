# Project
Programmeren 4opeenrij

Onze vier op een rij bied 2 manieren van spelen: via een GUI of via een TUI. 

Voor het spelen via een TUI:
	- in de package runnables staat een klasse RunTextual. Deze klasse heeft een main method waarmee je het spel via TUI kan starten.
	- Bij het starten wordt allereerst gevraagd of je lokaal of online wil spelen. Typ "a" voor 	lokaal of "b" voor online.
		Voor lokaal spelen:
			- Er wordt vervolgens gevraagd voor een naam voor speler 1. Als deze naam leeg 			gelaten wordt, dan wordt deze speler een computerspeler. 
			- Vervolgens wordt om de naam gevraagd voor speler 2. Wederom geldt, als deze naam 			leeg gelaten wordt, dan wordt deze speler een computerspeler.
			- Om en om wordt er om een beurt gevraagd. Er kan hint ingetypt worden voor een hint 			en anders een getal tussen 0 en het bordbreedte-1. 
			- Na verloop van tijd zal of het bord vol zijn of er een winnaar zijn. In dit geval 			wordt er gevraagd op nog een keer te spelen. Als hier "y" wordt ingetypt, dan zal het 			spel met de ingvoerde spelers opnieuw gestart worden. Als hier "n" ingevoerd wordt, 			dan zal het spel afsluiten.
		Voor online spelen:
			- Er wordt gevraagd om als client als server te starten. Typ "a" voor client en "b" 			voor server. Als u als server wil starten, lees onder in het blok verder.
			- "a" ingetypt, zal er gevraagd worden naar een naam. Vul een naam in.
			- Vervolgens wordt er gevraagd naar het adres van de server. Als de server waarmee u 			wilt verbinnen in hetzelfde netwerk zit als u, dan kunt u ook verbinden door "a" in 			te typen.
			- Vervolgens wordt er gevraagt naar het poort nummer van de server. Als het 			poortnummer 49999 is, dan kunt u ook "a" intypen.
			- Als alles goed ingevult is en de server aanstaat, bent u nu verbonden met de 			server. Op het moment dat u verbindt krijgt u de huidige spelers in de lobby binnen.
			- Als u een spel wilt speler dan kunt u invite <naam_speler> intypen.
			
		
