package westbahn;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;

@Entity
public class Strecke {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ID;
	@ManyToOne
	private Bahnhof start;
	@ManyToOne
	private Bahnhof ende;

    @AssertTrue(message="Start und End Bahnhof können nicht die selben sein!")
    public boolean validBahnhof() {
        return !start.equals(ende);
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Bahnhof getStart() {
        return start;
    }

    public void setStart(Bahnhof start) {
        this.start = start;
    }

    public Bahnhof getEnde() {
        return ende;
    }

    public void setEnde(Bahnhof ende) {
        this.ende = ende;
    }
}
