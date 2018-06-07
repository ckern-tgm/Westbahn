package westbahn;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@NamedQuery(name = "Bahnhof.getAll", query = "SELECT b FROM Bahnhof b")
public class Bahnhof {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ID;

    @Column(unique = true)
    @Size(min=2, max=150)
    @Pattern(regexp = "[a-zA-Z0-9\\-\\säöüÄÖÜ]+")
	private String name;

	private int absPreisEntfernung;

	private int absKmEntfernung;

	private int absZeitEntfernung;

	private boolean kopfBahnhof;

	@Override
    public boolean equals(Object obj) {
        Bahnhof other = (obj instanceof Bahnhof ? (Bahnhof) obj : null);
        return other != null && other.ID == this.ID;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAbsPreisEntfernung() {
        return absPreisEntfernung;
    }

    public void setAbsPreisEntfernung(int absPreisEntfernung) {
        this.absPreisEntfernung = absPreisEntfernung;
    }

    public int getAbsKmEntfernung() {
        return absKmEntfernung;
    }

    public void setAbsKmEntfernung(int absKmEntfernung) {
        this.absKmEntfernung = absKmEntfernung;
    }

    public int getAbsZeitEntfernung() {
        return absZeitEntfernung;
    }

    public void setAbsZeitEntfernung(int absZeitEntfernung) {
        this.absZeitEntfernung = absZeitEntfernung;
    }

    public boolean isKopfBahnhof() {
        return kopfBahnhof;
    }

    public void setKopfBahnhof(boolean kopfBahnhof) {
        this.kopfBahnhof = kopfBahnhof;
    }
}
