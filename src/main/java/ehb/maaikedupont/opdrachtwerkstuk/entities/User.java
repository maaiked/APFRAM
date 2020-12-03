package ehb.maaikedupont.opdrachtwerkstuk.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// TODO : kolommen van maken > eerst kijken naar Spring Security!
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String familienaam;
    private String voornaam;
    private String mail;
    private String password;
    private String adres;
    private String gemeente;
    private String telefoonnummer;
    private Double prijs;
}
