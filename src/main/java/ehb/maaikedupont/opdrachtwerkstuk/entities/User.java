package ehb.maaikedupont.opdrachtwerkstuk.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    private String auth_id;
    private String familienaam;
    private String voornaam;
    private String mail;
    private String straat;
    private String huisnummer;
    private String bus;
    private String postcode;
    private String gemeente;
    private String telefoonnummer;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // een user kan meerdere bestellingen hebben
    private List<Bestelling> orders = new ArrayList<>();

    public User(){}

    public User(String auth_id, String naam, String voornaam, String email, String tel, String straat, String huisnummer, String bus, String postcode, String gemeente) {
        this.auth_id = auth_id;
        this.familienaam = naam;
        this.voornaam = voornaam;
        this.mail = email;
        this.telefoonnummer = tel;
        this.straat = straat;
        this.huisnummer = huisnummer;
        this.bus = bus;
        this.postcode = postcode;
        this.gemeente = gemeente;
    }

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
        return orders;
    }

    public void setOrders(List<Bestelling> orders) {
        this.orders = orders;
    }
}