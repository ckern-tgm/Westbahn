package westbahn;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.GregorianCalendar;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class.getName());
    private static long BAHNHOF_ID = 1L;

    public static void main(String[] args) {
        logger.info("Starting EntityManagerFactory..");

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("westbahn");
        EntityManager entityManager = factory.createEntityManager();

        createStations(entityManager);
        createStrecken(entityManager);
        createUsers(entityManager);
        createSonderangebote(entityManager);
        //update(entityManager);
        //remove(entityManager);

        List<Benutzer> benutzerMitMonatskarte = entityManager.createNamedQuery("Benutzer.getAllWithMonatskarte").getResultList();
        for (Benutzer benutzer : benutzerMitMonatskarte) {
            System.out.println("Benutzer mit Monatkarte: " + benutzer);
        }

        String email = "ckern@gmail.com";
        List<Reservierung> benutzerMitReservierungen = entityManager.createNamedQuery("Reservierung.getReservierungenForUser")
                .setParameter("email", email)
                .getResultList();
        for (Reservierung reservierung : benutzerMitReservierungen) {
            System.out.println("Reservierung für " + email + ": " + reservierung);
        }

        long streckeId = 1;
        List<Ticket> ticketsOhneReservierung = entityManager.createNamedQuery("Ticket.getAllTicketsWithoutReservierung")
                .setParameter("streckeID", streckeId)
                .getResultList();
        for (Ticket ticket : ticketsOhneReservierung) {
            System.out.println("Strecke ohne Reservierung für Strecke#" + streckeId + ": " + ticket);
        }

        entityManager.close();
        factory.close();
        logger.info("Finished!");
    }

    private static void createStations(EntityManager entityManager) {
        try {
            Bahnhof spittelau = new Bahnhof();
            spittelau.setName("Wien Spittelau");
            create(entityManager, spittelau);
            BAHNHOF_ID = spittelau.getID();

            String[] names = new String[]{"Wien Westbhf", "Wien Hütteldorf", "St Pölten", "Amstetten", "Linz", "Wels", "Attnang-Puchheim", "Salzburg"};
            for (String name : names) {
                Bahnhof bahnhof = new Bahnhof();
                bahnhof.setName(name);
                System.out.println(name);
                bahnhof.setAbsKmEntfernung(random(0, 1000));
                bahnhof.setAbsPreisEntfernung(random(0, 1000));
                bahnhof.setAbsZeitEntfernung(random(0, 1000));
                create(entityManager, bahnhof);
            }
        } catch (Exception ex) {
            logger.error("Failed to create Stations!", ex);
        }
    }

    private static void createStrecken(EntityManager entityManager) {
        try {
            List<Bahnhof> results = entityManager.createNamedQuery("Bahnhof.getAll").getResultList();
            for (Bahnhof start : results) {
                for (Bahnhof ende : results) {
                    if (start.getID() != ende.getID()) {
                        // strecken
                        Strecke strecke = new Strecke();
                        strecke.setStart(start);
                        strecke.setEnde(ende);
                        create(entityManager, strecke);

                        // Ticket
                        createEinzelTicket(entityManager, strecke);
                        createZeitkarte(entityManager, strecke);

                        // züge
                        Zug zug = new Zug();
                        zug.setStart(start);
                        zug.setEnde(ende);
                        zug.setFahrradStellplaetze(random(0, 100));
                        zug.setRollStuhlPlaetze(random(0, 10));
                        zug.setSitzPlaetze(random(50, 400));
                        zug.setStartZeit(randomDate());
                        create(entityManager, zug);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Failed to create Strecken!", ex);
        }
    }

    private static void createEinzelTicket(EntityManager entityManager, Strecke strecke) {
        try {
            Einzelticket einzel = new Einzelticket();
            einzel.setTicketOption(TicketOption.values()[random(0, 1)]);
            einzel.setStrecke(strecke);
            einzel.setZahlung(new Maestro());
            create(entityManager, einzel);
        } catch (Exception ex) {
            logger.error("Failed to create Tickets!", ex);
        }
    }

    private static void createSonderangebote(EntityManager entityManager) {
        try {
            for (int i = 0; i < 10; i++) {
                Sonderangebot angebot = new Sonderangebot();
                angebot.setDauer(random(0, 50));
                angebot.setKontingent(random(1, 10));
                angebot.setPreisNachlass((float) random(0, 50));
                angebot.setStartZeit(randomDate());
                for (int ii = 0; ii < 3; ii++) {
                    Ticket rndTicket = entityManager.find(Ticket.class, randomL(1, 5));
                    angebot.addTicket(rndTicket);
                }
                create(entityManager, angebot);
            }
        } catch (Exception ex) {
            logger.error("Failed to create Sonderangebote!", ex);
        }
    }

    private static void createZeitkarte(EntityManager entityManager, Strecke strecke) {
        try {
            Zeitkarte zeitkarte = new Zeitkarte();
            zeitkarte.setGueltigAb(randomDate());
            zeitkarte.setTyp(ZeitkartenTyp.values()[random(0, 2)]);
            zeitkarte.setStrecke(strecke);
            zeitkarte.setZahlung(new Praemienmeilen());
            create(entityManager, zeitkarte);
        } catch (Exception ex) {
            logger.error("Failed to create Tickets!", ex);
        }
    }

    private static void createUsers(EntityManager entityManager) {
        try {
            String[] names = new String[]{"Marc", "Max", "David", "Mike"};
            for (String name : names) {
                Benutzer benutzer = new Benutzer();
                benutzer.setVorName(name);
                benutzer.setVerbuchtePraemienMeilen(random(0, 10000));
                benutzer.setNachName("Mustermann");
                benutzer.seteMail(name + "@gmail.com");
                benutzer.setPasswort(Integer.toString(random(0, 1000)));
                benutzer.setSmsNummer("+431234");
                populateUser(entityManager, benutzer);
                create(entityManager, benutzer);
            }
        } catch (Exception ex) {
            logger.error("Failed to create Users!", ex);
        }
    }

    private static void populateUser(EntityManager entityManager, Benutzer benutzer) {
        try {
            Reservierung reservierung = new Reservierung();
            reservierung.setBenutzer(benutzer);
            reservierung.setDatum(randomDate());
            reservierung.setPraemienMeilenBonus(random(0, 1000));
            reservierung.setPreis(random(0, 100));
            reservierung.setStatus(StatusInfo.values()[random(0, 2)]);
            Strecke rndStrecke = entityManager.find(Strecke.class, randomL(1, 10));
            reservierung.setStrecke(rndStrecke);
            Zug rndZug = entityManager.find(Zug.class, randomL(1, 10));
            reservierung.setZug(rndZug);
            Zahlung kreditkarte = new Kreditkarte();
            reservierung.setZahlung(kreditkarte);
            benutzer.addReservierung(reservierung);

            for (int i = 0; i < 5; i++) {
                Ticket rndTicket = entityManager.find(Ticket.class, randomL(1, 10));
                logger.info("Adding ticket " + rndTicket);
                benutzer.addTicket(rndTicket);
            }
        } catch (Exception ex) {
            logger.error("Failed to populate Reservierungen!", ex);
        }
    }

    private static void create(EntityManager entityManager, Object object) {
        logger.info("Creating " + object.toString() + "..");
        entityManager.getTransaction().begin();
        entityManager.persist(object);
        entityManager.getTransaction().commit();
        logger.info(object.toString() + " created!");
    }

    private static void update(EntityManager entityManager) {
        logger.info("Updating Bahnhof Spittelau -> Heiligenstadt..");
        entityManager.getTransaction().begin();
        Bahnhof bahnhof = entityManager.find(Bahnhof.class, BAHNHOF_ID);
        bahnhof.setName("Wien Heiligenstadt");
        entityManager.getTransaction().commit();
        logger.info("Bahnhof Spittelau updated to Heiligenstadt!");
    }

    private static void remove(EntityManager entityManager) {
        logger.info("Removing Bahnhof Heiligenstadt..");
        entityManager.getTransaction().begin();
        Bahnhof bahnhof = entityManager.find(Bahnhof.class, BAHNHOF_ID);
        entityManager.remove(bahnhof);
        entityManager.getTransaction().commit();
        logger.info("Bahnhof Heiligenstadt removed!");
    }

    public static int random(int min, int max) {
        return (int)(Math.round(Math.random() * max)) + min;
    }

    private static long randomL(int min, int max) {
        return Math.round(Math.random() * max) + min;
    }

    private static java.util.Date randomDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        int year = random(2019, 2030);
        calendar.set(GregorianCalendar.YEAR, year);
        int dayOfYear = random(1, calendar.getActualMaximum(GregorianCalendar.DAY_OF_YEAR));
        calendar.set(GregorianCalendar.DAY_OF_YEAR, dayOfYear);
        return calendar.getTime();
    }
}
