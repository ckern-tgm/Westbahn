package westbahn;

import javax.persistence.Entity;

@Entity
public class Einzelticket extends Ticket {
	private TicketOption ticketOption;

	public TicketOption getTicketOption() {
		return ticketOption;
	}

	public void setTicketOption(TicketOption ticketOption) {
		this.ticketOption = ticketOption;
	}
}
 // TicketTyp.MONAT = 1