package ehb.maaikedupont.opdrachtwerkstuk.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bestelling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "auth_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "bestelling", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    private LocalDateTime datumAangemaakt;
    private String straat;
    private String nummer;
    private String bus;
    private String postcode;
    private String gemeente;
    private Double totaalbedrag;

    public Bestelling() {    }

    public Bestelling(String straat, String nummer, String bus, String postcode, String gemeente, Double totaalbedrag, User user) {
        this.datumAangemaakt = LocalDateTime.now();
        this.straat = straat;
        this.nummer = nummer;
        this.bus = bus;
        this.postcode = postcode;
        this.gemeente = gemeente;
        this.totaalbedrag = totaalbedrag;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public LocalDateTime getDatumAangemaakt() {
        return datumAangemaakt;
    }

    public void setDatumAangemaakt(LocalDateTime datumAangemaakt) {
        this.datumAangemaakt = datumAangemaakt;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }

    public Double getTotaalbedrag() {
        return totaalbedrag;
    }

    public void setTotaalbedrag(Double totaalbedrag) {
        this.totaalbedrag = totaalbedrag;
    }
}