package ehb.maaikedupont.opdrachtwerkstuk.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;



// TODO Linken van auth0 registratie met MySQL database... Post-register hook maken waarbij koppeling
// met auth0 id gelegd wordt en waarna extra informatie gevraagd wordt.
@Entity
public class User {
    @Id
    private String auth_id;
    @NotNull(message="Je moet je familienaam opgeven")
    private String familienaam;
    @NotNull(message="Je moet je voornaam opgeven")
    private String voornaam;
    @NotNull(message="Je moet je mailadres opgeven")
    private String mail;
    @NotNull(message="Je moet je straat opgeven")
    private String straat;
    @NotNull(message="Je moet je huisnummer opgeven")
    private String huisnummer;
    private String bus;
    @NotNull(message="Je moet je postcode opgeven")
    private String postcode;
    @NotNull(message="Je moet je gemeente opgeven")
    private String gemeente;
    @NotNull(message="Je moet je telefoonnummer opgeven")
    private String telefoonnummer;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bestelling> bestellings = new ArrayList<>();

    public User(){
    };

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public void setFamilienaam(String familienaam) {
        this.familienaam = familienaam;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
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

    public String getTelefoonnummer() {
        return telefoonnummer;
    }

    public void setTelefoonnummer(String telefoonnummer) {
        this.telefoonnummer = telefoonnummer;
    }

    public List<Bestelling> getOrders() {
        return bestellings;
    }

    public void setOrders(List<Bestelling> bestellings) {
        this.bestellings = bestellings;
    }
}