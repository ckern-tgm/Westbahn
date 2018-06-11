package westbahn;

import javax.persistence.*;

@Entity
@NamedQuery(
        name = "Ticket.getAllTicketsWithoutReservierung",
        query = "SELECT t FROM Ticket t " +
                "LEFT JOIN Reservierung r ON r.strecke.ID=t.strecke.ID " +
                "WHERE t.strecke.ID=:streckeID"
)
public abstract class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long ID;

	@ManyToOne
	protected Strecke strecke;

	@Transient
	protected Zahlung zahlung;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Strecke getStrecke() {
        return strecke;
    }

    public void setStrecke(Strecke strecke) {
        this.strecke = strecke;
    }

    public Zahlung getZahlung() {
        return zahlung;
    }

    public void setZahlung(Zahlung zahlung) {
        this.zahlung = zahlung;
    }
}
