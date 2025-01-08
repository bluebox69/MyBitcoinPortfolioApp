# MyBitcoinPortfolioApp

Diese App ist das Abschlussprojekt der Lerveranstaltung Android-Advanced der FH-Technikum Wien

Author: Paul Kolger

Infos zur App:
Mit der "BitInvest" - App kann man ein virtuelles Portfolio erstellen. Bei erstmaligen Starten wird ein Portfolio mit 20.000$ in der Datenbank erstellt. Man kann damit Bicoins kaufen und auch wieder verkaufen, dafür wird der aktuelle Bitcoinkurs über eine API geladen. Die Kauf- Verkaufsorders werden in einer Liste angezeigt. Unter den Menüpunkt "Settings" kann mehr Cash auf das Konto geladen werden oder man kann das Portfolio zurücksetzen auf die Ausgangswerte.

Das Design wurde selbst mit Figma erstellt und liegt als PDF bei.

Technische Punke:
    -> Die Android App wurde mit Jetpack Compose umgesetzt.
    
        Dazu wurden die Lehrvideos von Philipp Lackner zu hilfe genommen:
        https://www.youtube.com/playlist?list=PLQkwcJG4YTCSpJ2NLhDTHhi6XBNfk9WiC
        Generell sind seine Videos richtig gut und lehrreich!!
    
    -> Es wurde "Koin" für die Dependency Injection verwendet.
    
    -> Android Room dient als Datenbank
    
    -> gson
    
    -> retrofit wurde als http-Client verwented
    
    -> API Coin Paprica
        https://api.coinpaprika.com/v1/tickers/btc-bitcoin
        in der Free Version updatet sich der Bitcoin Kurs alle 5 min!
        
        
Blöcke         
  
Base Block: Projektsetup und ViewModels
Projektsetup:

    Das Projekt wurde in Android Studio erstellt und nutzt minSdk = 24, targetSdk = 34.
    Kotlin wurde als Programmiersprache verwendet.
    Jetpack Compose wird statt XML und Navigation Components.

ViewModels:

    ViewModels für verschiedene Screens (CoinViewModel, BuyViewModel, SellViewModel, SettingsViewModel) wurden implementiert.
    Zugriff auf Repository-Klassen zur Datenlogik.

Repository und Datenzugriff:

    Repositories (PortfolioRepositoryImpl, InvestmentRepositoryImpl, CoinRepositoryImpl) für Lese- und Schreiboperationen wurden erstellt.
    ViewModels nutzen diese Repositories, um Investments und Portfolio-Daten zu verwalten.

Navigation:

    Navigation wurde zwischen Screens (HomeScreen, BuyScreen, SellScreen, SettingsScreen) mit Jetpack Compose's NavController implementiert.

Datenhandling:

    Funktionen wie getCoin, refreshPortfolio, addInvestment, resetPortfolio wurden für den Datenzugriff und die UI-Integration verwendet.

Block 1: LiveData und Lifecycle Components

StateFlow statt LiveData

    StateFlow wird genutzt, um State-Änderungen von Repositorys zu ViewModels und UI weiterzugeben.

Datenänderungen:

    State-Updates im ViewModel führen automatisch zu UI-Aktualisierungen.
    Init-Block: Initialisierung von Daten in initializePortfolio() und loadInvestments() anstelle von onResume.

Block 2: SQL mit Google Room

Setup:

    Room-Datenbank (AppDatabase) wurde erstellt und eingebunden.
    
Entities und DAOs:

    Entitäten: PortfolioEntity, InvestmentEntity, CoinEntity.
    DAO-Klassen: PortfolioDao, InvestmentDao, CoinDao, inkl. SQL-Statements wie SELECT * FROM.

Integration:

    Datenbank mit Room-Builder in Koin (AppModule.kt) eingebunden.
    Repositories rufen DAO-Funktionen auf, z. B. getPortfolio() und getInvestments().

Block 3: API Access und Serialization mit Retrofit

API-Zugriff:

    Retrofit wurde statt Ktor (basierend auf Tutorial von Philipp Lackner) verwendet.
    API-Modelle mit @SerializedName annotiert.

INTERNET-Permission:

    Erforderliche Berechtigung im Manifest vorhanden.

Datenfluss:

    Suspend-Funktionen wie getCoin() rufen API-Daten ab.
    Ergebnisse werden durch Repositorys und ViewModels an die UI weitergegeben.

Block 4: Dependency Injection mit Koin

Koin-Module: 

    AppModule definiert und ViewModels mit viewModel registriert.

Singleton-Repositories:

    Repositories in Koin als Singletons registriert.
    MyApplication: Klasse erstellt und in AndroidManifest.xml eingetragen, um Koin zu initialisieren.

Block 5: Dependency Injection mit Koin (Google Room)

    Room-Datenbank wurde über Koin registriert.
    Room-Builder korrekt in Koin eingebunden, mit get() für den Android-Kontext.
    MainActivity wurde von der Room-Initialisierung befreit.

Block 6: Concurrency mit Coroutines und Flow

    Coroutines: API- und Room-Datenbankzugriff erfolgt in Coroutines auf Neben-Threads.
    Suspend-Funktionen: Repositories implementieren mehrere suspend-Funktionen.
    UI-Integration: Lifecycle-Handling erfolgt über StateFlow, wodurch UI automatisch bei Datenänderungen aktualisiert wird.
