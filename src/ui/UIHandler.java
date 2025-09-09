package ui;

import service.*;
import java.sql.Date;
import java.util.Scanner;

public class UIHandler {
    private final BoracService boracService;
    private final TimService timService;
    private final TurnirService turnirService;
    private final SponzorService sponzorService;
    private final Scanner scanner;
    private final TransakcijaService transakcijaService;
    public UIHandler() {
        this.boracService = new BoracService();
        this.timService = new TimService();
        this.turnirService = new TurnirService();
        this.sponzorService = new SponzorService();
        this.transakcijaService = new TransakcijaService();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n===== KICKBOX TURNIR MENI =====");
            System.out.println("1. Dodaj borca");
            System.out.println("2. Prikazi sve borce");
            System.out.println("3. Dodaj tim");
            System.out.println("4. Prikazi sve timove");
            System.out.println("5. Dodaj turnir");
            System.out.println("6. Prikazi sve turnire");
            System.out.println("7. Dodaj sponzora");
            System.out.println("8. Prikazi sve sponzore");
            System.out.println("9. Izvještaji");
            System.out.println("10. Transakcija - Dodaj borca i ažuriraj tim");
            System.out.println("11. Izlaz");
            System.out.print("Izaberite opciju: ");

            int opcija = scanner.nextInt();
            scanner.nextLine(); // Čišćenje bafera

            switch (opcija) {
                case 1 -> addBorac();
                case 2 -> prikaziBorce();
                case 3 -> addTim();
                case 4 -> prikaziTimove();
                case 5 -> addTurnir();
                case 6 -> prikaziTurnire();
                case 7 -> addSponzor();
                case 8 -> prikaziSponzore();
                case 9 -> prikaziIzvestaje();
                case 10 -> transakcijaDodajBoracIAzurirajTim();
                case 11 -> {
                    System.out.println("✅ Zatvaranje aplikacije...");
                    return;
                }
                default -> System.out.println("❌ Pogrešan unos! Pokušajte ponovo.");
            }
        }
    }

    private void addBorac() {
        System.out.print("Unesite ime: ");
        String ime = scanner.nextLine();
        System.out.print("Unesite prezime: ");
        String prezime = scanner.nextLine();
        System.out.print("Unesite ID tima: ");
        int idTim = scanner.nextInt();
        scanner.nextLine();
        boracService.addBorac(ime, prezime, idTim);
    }

    private void prikaziBorce() {
        boracService.getAllBorci().forEach(System.out::println);
    }

    private void addTim() {
        System.out.print("Unesite ID tima: ");
        int idTim = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Unesite naziv tima: ");
        String naziv = scanner.nextLine();
        System.out.print("Unesite ime trenera: ");
        String nazivTrener = scanner.nextLine();
        System.out.print("Unesite broj boraca: ");
        int brojBoraca = Integer.parseInt(scanner.nextLine());
        timService.addTim(idTim, naziv, nazivTrener, brojBoraca);
    }

    private void prikaziTimove() {
        timService.getAllTimovi().forEach(System.out::println);
    }

    private void addTurnir() {
        System.out.print("Unesite ID turnira: ");
        int idTurnir = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Unesite naziv turnira: ");
        String naziv = scanner.nextLine();
        System.out.print("Unesite lokaciju: ");
        String lokacija = scanner.nextLine();
        System.out.print("Unesite datum turnira (YYYY-MM-DD): ");
        Date datum = Date.valueOf(scanner.nextLine());
        System.out.print("Unesite trajanje turnira (u satima): ");
        int trajanje = scanner.nextInt();
        scanner.nextLine();
        turnirService.addTurnir(idTurnir, naziv, lokacija, datum, trajanje);
    }

    private void prikaziTurnire() {
        turnirService.getAllTurniri().forEach(System.out::println);
    }

    private void addSponzor() {
        System.out.print("Unesite ID sponzora: ");
        int idSponzor = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Unesite naziv sponzora: ");
        String naziv = scanner.nextLine();
        System.out.print("Unesite vrstu sponzora: ");
        String vrsta = scanner.nextLine();
        sponzorService.addSponzor(idSponzor, naziv, vrsta);
    }

    private void prikaziSponzore() {
        sponzorService.getAllSponzori().forEach(System.out::println);
    }

    private void prikaziIzvestaje() {
        System.out.println("\n📊 IZVJEŠTAJI 📊");
        System.out.println("1. Takmičari po kategoriji (kompleksan upit)");
        System.out.println("2. Broj boraca po timu i kategoriji (kompleksan upit)");
        System.out.println("3. Broj boraca po timu (jednostavan upit)");
        System.out.print("Izaberite opciju: ");
        int opcija = scanner.nextInt();
        scanner.nextLine();

        switch (opcija) {
            case 1 -> boracService.prikaziBorcePoKategoriji();
            case 2 -> prikaziBrojBoracaPoTimuIKategoriji();
            case 3 -> timService.prikaziBrojBoracaPoTimuJednostavan();
            default -> System.out.println("❌ Pogrešan unos!");
        }
    }

    private void prikaziBrojBoracaPoTimuIKategoriji() {
        System.out.print("Unesite naziv kategorije (ili ENTER za bez filtera): ");
        String kategorija = scanner.nextLine().trim();
        if (kategorija.isEmpty()) {
            kategorija = null; // ili ostaviti prazan string, po želji
        }

        System.out.print("Unesite naziv tima (ili ENTER za bez filtera): ");
        String tim = scanner.nextLine().trim();
        if (tim.isEmpty()) {
            tim = null;
        }

        timService.prikaziBrojBoracaPoTimuIKategoriji(kategorija, tim);
    }

    private void transakcijaDodajBoracIAzurirajTim() {
        System.out.print("Unesite ime: ");
        String ime = scanner.nextLine();
        System.out.print("Unesite prezime: ");
        String prezime = scanner.nextLine();
        System.out.print("Unesite ID tima: ");
        int idTim = scanner.nextInt();
        scanner.nextLine();

        transakcijaService.dodajBorcaIAzurirajTim(ime, prezime, idTim);

        System.out.println("✅ Borac dodat i broj boraca u timu ažuriran (transakcija)!");
    }
}
