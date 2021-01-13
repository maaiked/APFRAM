package ehb.maaikedupont.opdrachtwerkstuk.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailid;

    // een orderdetail behoort tot 1 order. Een order kan meerdere orderdetails (productlijnen) hebben.
    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Bestelling bestelling;

    // in kader van prijswijzigingen, productwijzigingen, producten die op termijn terug verwijderd worden
    // wil ik deze niet dynamisch ophalen maar de productgegevens op moment van verkoop registreren
    // zodat orderbevestigingen na aanmaak niet meer gewijzigd kunnen worden.
    private String naam;
    private String omschrijving;
    private Double prijs;

    public OrderDetail() { }

    public OrderDetail(Bestelling bestelling, String naam, String omschrijving, Double prijs) {
        this.bestelling = bestelling;
        this.naam = naam;
        this.omschrijving = omschrijving;
        this.prijs = prijs;
        List<OrderDetail> orderdetails = bestelling.getOrderDetails();
        orderdetails.add(this);
        bestelling.setOrderDetails(orderdetails);
    }

    public int getDetailid() {
        return detailid;
    }

    public void setDetailid(int detailid) {
        this.detailid = detailid;
    }

    public Bestelling getBestelling() {
        return bestelling;
    }

    public void setBestelling(Bestelling bestelling) {
        this.bestelling = bestelling;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public Double getPrijs() {
        return prijs;
    }

    public void setPrijs(Double prijs) {
        this.prijs = prijs;
    }
}