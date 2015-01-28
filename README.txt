Voor het spelen via een TUI:
	- in de package runnables staat een klasse RunTextual. Deze klasse heeft een main method waarmee je het spel via TUI kan starten.
	- Bij het starten wordt allereerst gevraagd of je lokaal of online wil spelen. Typ "a" voor lokaal of "b" voor online.
		Voor lokaal spelen:
			- Er wordt vervolgens gevraagd voor een naam voor speler 1. Als deze naam leeg gelaten wordt, dan wordt deze speler een computerspeler. 
			- Vervolgens wordt om de naam gevraagd voor speler 2. Wederom geldt, als deze naam leeg gelaten wordt, dan wordt deze speler een computerspeler.
			- Om en om wordt er om een beurt gevraagd. Er kan hint ingetypt worden voor een hint en anders een getal tussen 0 en het bordbreedte-1. 
			- Na verloop van tijd zal of het bord vol zijn of er een winnaar zijn. In dit geval wordt er gevraagd op nog een keer te spelen. Als hier "y" wordt ingetypt, dan zal het spel met de ingevoerde spelers opnieuw gestart worden. Als hier "n" ingevoerd wordt, dan zal het spel afsluiten.
		Voor online spelen:
			- Er wordt gevraagd om als client als server te starten. Typ "a" voor client en "b" voor server. Als u als server wil starten, lees onder in het blok verder.
			- "a" ingetypt, zal er gevraagd worden naar een naam. Vul een naam in.
			- Vervolgens wordt er gevraagd naar het adres van de server. Als de server waarmee u wilt verbinden in hetzelfde netwerk zit als u, dan kunt u ook verbinden door "a" in te typen.
			- Vervolgens wordt er gevraagd naar het poort nummer van de server. Als het poortnummer 49999 is, dan kunt u ook "a" intypen.
			- Als alles goed ingevuld is en de server aanstaat, bent u nu verbonden met de server. Op het moment dat u verbindt, krijgt u de huidige spelers in de lobby binnen.
		Voor opzetten server:
			- Er wordt gevraagd om als client als server te starten. Typ "a" voor client en "b" voor server. 
			- Typ "b".
			- Er wordt gevraagd naar welk poortnummer.
			- Voer poortnummer in.
			- Als de poort juist is ingevuld en beschikbaar is heeft u nu de server opgezet. 


Voor GUI
-	Voor het spelen via een GUI:
	In de package runnables staat een klasse RunGraphical. Deze klasse heeft een main methode, waarmee u het spel met GUI kan starten.
	Er verschijnt een venster met een titel en vier opties: “Start Local Game”, “Join Server” en “Start new Server”. 
	
	- Een lokaal spel opzetten 
		- Op het moment dat u op “Start Local Game” klikt, verandert u venster in een ander venster. In dit venster kunt u de opties voor het spel invullen. Zo kunt u de beide namen van de spelers invullen. Als u tegen een computerspeler wil spelen, dan moet u het veld onder “Select Player ” leeg laten. Verder kunt u onder “Board Settings” ook nog het bordgrootte aanpassen. Deze staan standaard op 7 breed en 6 hoog. 
		Als u klaar bent met instellen kunt u op de knop “Start Game” klikken. U venster veranderd opnieuw in een speelveld met de namen van de spelers erboven en knoppen eronder. Ook ziet u een pijltje die aangeeft welke speler aan de beurt is*. 
		Om een zet te spelen kunt u op de kolom van de desgewenste zet klikken. Mocht u niet weten welke zet u wilt spelen, dan kunt u op de “Hint” knop onder het veld drukken. Deze geeft weer welke zet u kunt zetten. LET OP: de zet is dan nog niet gezet, als u daadwerkelijk deze zet wilt spelen, dan moet u op de desbetreffende kolom klikken.
		Mocht u genoeg van het spel hebben, of met andere instellingen willen spelen dan kunt u klikken op de “Back to Menu” knop. Deze brengt u terug naar het voorgaande scherm. Hier kunt u opnieuw u instellingen wijzigen en een nieuw spel starten.
		Als u doorspeelt en op een gegeven moment is het bord vol of er is een winnaar (dit kunt u zien aan een stukje tekst tussen de “Hint” en “Restart” knop. Als deze tekst ontbreekt, dan betekent dat er nog geen winnaar is. Als deze tekst wel zichtbaar is (en het spel dus ten einde is) dan kunt u het spel herstarten door op de “Restart” knop te drukken.

	- Een server joinen:
		- U kunt een server joinen door op de “Join Server” tekst te klikken als u zich in het hoofdmenu bevindt. Dit opent een nieuw venster. In dit venster heeft u de mogelijkheid op velden in te vullen. Om met een server te verbinden, moet u het IP-adres van deze server invullen in het veld na “Enter IP”. In het veld eronder (achter “Enter Port”) moet u de poort van de server invullen. Als laatste moet u nog een naam invullen achter “Enter name”. Als u alles ingevuld hebt en de server is correct opgestart, dan zou u op de “Connect” knop kunnen klikken en wordt u verbonden met de server.

	- Een server opzetten:
		- U kunt een server opzetten door op “Start new Server” tekst te klikken als u zich in het hoofdmenu bevindt. Dit opent een nieuw venster. Hier kunt u een poort nummer invullen. Als deze poort beschikbaar is en u vervolgens op host klikt, dan is u server gestart. 

Commands:
	- Als u de server of client commandos wilt zien, dan moet u een server of client draaiend hebben en “help” typen. Dan verschijnen alle mogelijke commands.



* Er is op dit moment nog een bug in het spel. Deze bug treedt op als u een spel nadat het is afgelopen opnieuw wil spelen. Op dit moment geeft het pijltje de verkeerde speler aan die aan de beurt is. Dus na een herstart is de speler aan de beurt waar het pijltje NIET naar wijst. 
