package ehb.maaikedupont.opdrachtwerkstuk.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Naam mag niet leeg zijn")
    private String naam;
    @NotNull(message = "Je moet een prijs opgeven")
    private Double prijs;
    private Categorie categorie;
    private Dier dier;
    @Column(columnDefinition = "text")
    private String omschrijving;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Double getPrijs() {
        return prijs;
    }

    public void setPrijs(Double prijs) {
        this.prijs = prijs;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Dier getDier() {
        return dier;
    }

    public void setDier(Dier dier) {
        this.dier = dier;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }
}
