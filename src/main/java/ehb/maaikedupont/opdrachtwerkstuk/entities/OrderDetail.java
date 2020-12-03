package ehb.maaikedupont.opdrachtwerkstuk.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

// TO DO: kolommen maken > eerst kijken wat nodig
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // orderid: foreign key - manytoone
    // productid: foreign key - manytoone
    private Double prijs; // in kader van prijswijzigingen op producten wil ik deze niet dynamisch ophalen maar de prijs op moment van verkoop registreren
}
