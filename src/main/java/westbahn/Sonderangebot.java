package westbahn;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Sonderangebot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;

	private int kontingent = 999;

    @Future
	private Date startZeit;

	private int dauer = 12;

	private float preisNachlass = 0.5f;

	@ManyToMany(cascade=CascadeType.ALL)
	private List<Ticket> tickets = new ArrayList<Ticket>();

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public int getKontingent() {
        return kontingent;
    }

    public void setKontingent(int kontingent) {
        this.kontingent = kontingent;
    }

    public Date getStartZeit() {
        return startZeit;
    }

    public void setStartZeit(Date startZeit) {
        this.startZeit = startZeit;
    }

    public int getDauer() {
        return dauer;
    }

    public void setDauer(int dauer) {
        this.dauer = dauer;
    }

    public float getPreisNachlass() {
        return preisNachlass;
    }

    public void setPreisNachlass(float preisNachlass) {
        this.preisNachlass = preisNachlass;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }
}
