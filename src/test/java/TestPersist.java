import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import westbahn.Bahnhof;
import westbahn.Benutzer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;

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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static westbahn.Main.random;

public class TestPersist {
    public EntityManager entityManager;
    public EntityManagerFactory factory;

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("westbahn");
        entityManager = factory.createEntityManager();

        // Create first demo user
        Benutzer benutzer = new Benutzer();
        benutzer.setVorName("Michael");
        benutzer.setNachName("Borko");
        benutzer.setVerbuchtePraemienMeilen(1);
        benutzer.seteMail("mborko@tgm.ac.at");
        benutzer.setPasswort("sew");
        benutzer.setSmsNummer("+1234");

        entityManager.getTransaction().begin();
        entityManager.persist(benutzer);
        entityManager.getTransaction().commit();


        Bahnhof bahnhof = new Bahnhof();
        bahnhof.setName("Hauptbahnhof");
        bahnhof.setAbsKmEntfernung(random(0, 1000));
        bahnhof.setAbsPreisEntfernung(random(0, 1000));
        bahnhof.setAbsZeitEntfernung(random(0, 1000));
        entityManager.getTransaction().begin();
        entityManager.persist(bahnhof);
        entityManager.getTransaction().commit();
    }

    @AfterEach
    public void tearDown() {
        entityManager.close();
        factory.close();
    }


    @Test
    public void testPersistBenutzer()
    {
        try {
            Benutzer benutzer = new Benutzer();
            benutzer.setVorName("Max");
            benutzer.setNachName("Mustermann");
            benutzer.setVerbuchtePraemienMeilen(5);
            benutzer.seteMail("mmustermann@gmail.com");
            benutzer.setPasswort("123");
            benutzer.setSmsNummer("+123456");

            entityManager.getTransaction().begin();
            entityManager.persist(benutzer);
            entityManager.getTransaction().commit();
            assert(true);
        } catch (Exception ex) {
            assert(false);
        }
    }

    @Test
    public void testPersistInvalidEmailShouldThrowValidationError()
    {
        Benutzer benutzer = new Benutzer();
        benutzer.setVorName("Max");
        benutzer.setNachName("Mustermann");
        benutzer.setVerbuchtePraemienMeilen(5);
        benutzer.seteMail("INVALID EMAIL");
        benutzer.setPasswort("123");
        benutzer.setSmsNummer("+123456");

        Executable code = () -> {
            entityManager.getTransaction().begin();
            entityManager.persist(benutzer);
            entityManager.getTransaction().commit();
        };
        assertThrows(ConstraintViolationException.class, code, "Invalid Email");
    }

    @Test
    public void testBenutzerExists()
    {
        Benutzer benutzer = entityManager.find(Benutzer.class, 1L);
        assertNotNull(benutzer);
    }

    @Test
    public void testBenutzerNotExists()
    {
        Benutzer benutzer = entityManager.find(Benutzer.class, 99999L);
        assert(benutzer == null);
    }

    @Test
    public void testPersistBahnhof(){
        try{
            Bahnhof bahnhof = new Bahnhof();
            bahnhof.setName("Westbahnhof");
            bahnhof.setAbsKmEntfernung(random(0, 1000));
            bahnhof.setAbsPreisEntfernung(random(0, 1000));
            bahnhof.setAbsZeitEntfernung(random(0, 1000));
            entityManager.getTransaction().begin();
            entityManager.persist(bahnhof);
            entityManager.getTransaction().commit();
            assert(true);
        } catch (Exception ex) {
            assert(false);
        }
    }

    @Test
    public void testBahnhofExists()
    {
        Bahnhof bahnhof = entityManager.find(Bahnhof.class, 1L);
        assertNotNull(bahnhof);
    }

    @Test
    public void testBahnhofNotExists()
    {
        Bahnhof bahnhof = entityManager.find(Bahnhof.class, 99999L);
        assert(bahnhof == null);
    }

}