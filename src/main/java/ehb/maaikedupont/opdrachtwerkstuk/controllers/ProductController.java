package ehb.maaikedupont.opdrachtwerkstuk.controllers;

import ehb.maaikedupont.opdrachtwerkstuk.dao.BestellingDAO;
import ehb.maaikedupont.opdrachtwerkstuk.dao.OrderDetailDAO;
import ehb.maaikedupont.opdrachtwerkstuk.dao.ProductDAO;
import ehb.maaikedupont.opdrachtwerkstuk.dao.UserDAO;
import ehb.maaikedupont.opdrachtwerkstuk.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
public class ProductController {

    private final ProductDAO productDAO;
    private final BestellingDAO bestellingDAO;
    private final OrderDetailDAO orderDetailDAO;
    private final UserDAO userDAO;
    // TO DO : winkelwagen niet globaal aanmaken maar een attribuut maken van user
    // of in een sessievariabele?
    private final List<Product> winkelwagen = new ArrayList<Product>();
    private Iterable<Product> productList ;
    private String filter;

    @Autowired
    public ProductController(UserDAO userDAO,ProductDAO productDAO, BestellingDAO bestellingDAO, OrderDetailDAO orderDetailDAO)
    {
        this.productDAO=productDAO;
        this.bestellingDAO=bestellingDAO;
        this.orderDetailDAO=orderDetailDAO;
        this.userDAO = userDAO;
    }


    /* --- METHOD : herhalende code ---
    code die bij bijna elke mapping werd herhaald, dus functie van gemaakt dubbele code te vermijden
    */
    private void mapAll(ModelMap map) {
        map.addAttribute("filter", filter);
        map.addAttribute("all", productList);
        map.addAttribute("dieren", Dier.values());
        map.addAttribute("categories", Categorie.values());
    }


    /* --- OPHALEN indexpagina ---  */
    @GetMapping({"/", "/home"})
    public String getIndex(ModelMap map, @AuthenticationPrincipal OidcUser principal){
        productList= productDAO.findAll();
        filter = null;
        map.addAttribute("profile", principal.getClaims());
        mapAll(map);
        return "index";
    }

    /* --- OPHALEN indexpagina FILTER categorie ---  */
    @GetMapping({"/categorie/{filter}"})
    public String getByCategories(@PathVariable("filter") Categorie categorie, ModelMap map){
        productList = productDAO.findByCategorie(categorie);
        filter = categorie.toString();
        mapAll(map);
        return "index";
    }

    /* --- OPHALEN indexpagina FILTER dier ---  */
    @GetMapping({"/dier/{filter}"})
    public String getByDieren(@PathVariable("filter") Dier dier, ModelMap map){
        productList = productDAO.findByDier(dier);
        filter = dier.toString();
        mapAll(map);
        return "index";
    }

    /* --- TOEVOEGEN artikel aan Winkelwagen[] ---  */
    @GetMapping({"/{id}"})
    public String getAddToShoppingcart(@PathVariable("id") int id, ModelMap map){
        Optional<Product> product = productDAO.findById(id);
        if (product.isPresent())
        {
            winkelwagen.add(product.get());
        }
//        TO DO ; else return error message to page
        mapAll(map);
        return "index";
    }

    /* --- OPHALEN winkelwagenpagina ---  */
    @GetMapping({"/winkelwagen"})
    public String getShoppingcart(ModelMap map){
        if (!winkelwagen.isEmpty())
        {
            map.addAttribute("cart", winkelwagen);
            Double totaalprijs = 0.0;
            for (Product p: winkelwagen
            ) {
                totaalprijs += p.getPrijs();
            }
            map.addAttribute("totaalprijs", totaalprijs);
        }
        return "winkelwagen";
    }

    /* --- VERWIJDEREN artikel van Winkelwagen[] ---  */
    @GetMapping({"/winkelwagen/{id}"})
    public String getDeleteShoppingcart(@PathVariable("id")int id, ModelMap map){
        winkelwagen.remove(id);
        Double totaalprijs = 0.0;
        for (Product p: winkelwagen
        ) {
            totaalprijs += p.getPrijs();
        }
        map.addAttribute("totaalprijs", totaalprijs);
        map.addAttribute("cart", winkelwagen);
        return "winkelwagen";
    }

    /* --- ATTRIBUUT die Bestelling entity aanmaakt om attributen naar te mappen ---  */
    @ModelAttribute("nieuweBestelling")
    public Bestelling toSaveBestelling(){
        return new Bestelling();
    }

    @ModelAttribute("nieuweOrderDetail")
    public OrderDetail toSaveOrderDetail(){ return new OrderDetail(); }

    /* --- OPHALEN orderbevestigingspagina ---  */
    @GetMapping({"/orderbevestiging"})
    public String getOrderbevestiging(ModelMap map){
        /* inhoud van winkelwagen array wordt overgezet naar andere array
        * omdat na het bevestigen van de aankoop de winkelwagen gecleared wordt.
        * Maar ik wel graag het overzicht van de aangekochte producten wil weergeven op
        * de bevestigingspagina. */
        List<Product> aankoop = new ArrayList<>();
        Double totaalprijs = 0.0;
        for (Product p: winkelwagen
             ) {
            totaalprijs += p.getPrijs();
            aankoop.add(p);
        }
        map.addAttribute("totaalprijs", totaalprijs);
        map.addAttribute("cart", aankoop);
        winkelwagen.clear();
        // leveradres vragen
        // bestelling wegschrijven naar database
        // public Bestelling(String straat, String nummer, String bus, String postcode,
        // String gemeente, Double totaalbedrag, User user) {
        //
        Optional<User> nieuweUser = userDAO.findById("1");
        if (nieuweUser.isPresent())
        {
            Bestelling bestelling = new Bestelling("straat", "5", "", "7690", "gemeente", totaalprijs, nieuweUser.get());
            bestellingDAO.save(bestelling);
            for (Product p: aankoop
            ) {
                //public OrderDetail(Bestelling bestelling, String naam,
                // String omschrijvin, Double prijs)
                OrderDetail orderDetail = new OrderDetail(bestelling, p.getNaam(), p.getOmschrijving(), p.getPrijs());
                orderDetailDAO.save(orderDetail);
            }
        }

        return "orderbevestiging";
    }

    /* --- OPHALEN nieuwProductpagina ---  */
    @GetMapping({"/nieuwProduct"})
    public String getNieuwProduct(){
        return "nieuwProduct";
    }

    /* --- ATTRIBUUT die product entity aanmaakt om attributen naar te mappen ---  */
    @ModelAttribute("nieuwProduct")
    public Product toSaveProduct(){
        return new Product();
    }

    /* --- AANMAKEN nieuw product via formulierinput ---  */
    @PostMapping({"/nieuwProduct"})
    public String postNieuwProduct(@ModelAttribute("nieuwProduct") @Valid Product nieuwProduct,
                            BindingResult bindingResult,
                            ModelMap map){
    if(bindingResult.hasErrors()){
        return "/nieuwProduct";
    }
        productDAO.save(nieuwProduct);
    return "redirect:/home";
    }
}