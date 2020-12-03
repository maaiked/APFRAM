package ehb.maaikedupont.opdrachtwerkstuk.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

// TO DO : kolommen van maken voor database > eerst nog eens kijken wat nodig
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // foreign key: userid : ManyToOne

    private LocalDateTime datumAangemaakt;
    private Double bedrag;
}
