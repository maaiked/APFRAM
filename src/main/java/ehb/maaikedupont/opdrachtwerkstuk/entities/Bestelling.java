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
    private Boolean levering;
    private Double totaalbedrag;

    public Bestelling() {    }

    public Bestelling(Boolean levering, Double totaalbedrag, User user) {
        this.datumAangemaakt = LocalDateTime.now();
        this.levering = levering;
        this.totaalbedrag = totaalbedrag;
        this.user = user;
        List<Bestelling> bestellings = user.getOrders();
        bestellings.add(this);
        user.setOrders(bestellings);
    }

    public Boolean getLevering() {
        return levering;
    }

    public void setLevering(Boolean levering) {
        this.levering = levering;
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

    public Double getTotaalbedrag() {
        return totaalbedrag;
    }

    public void setTotaalbedrag(Double totaalbedrag) {
        this.totaalbedrag = totaalbedrag;
    }
}