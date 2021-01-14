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

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import javax.validation.Valid;
import java.util.*;


@Controller
public class ProductController {

    private final ProductDAO productDAO;
    private final BestellingDAO bestellingDAO;
    private final OrderDetailDAO orderDetailDAO;
    private final UserDAO userDAO;

    @Autowired
    public ProductController(UserDAO userDAO, ProductDAO productDAO, BestellingDAO bestellingDAO, OrderDetailDAO orderDetailDAO) {
        this.productDAO = productDAO;
        this.bestellingDAO = bestellingDAO;
        this.orderDetailDAO = orderDetailDAO;
        this.userDAO = userDAO;
    }

    // variabelen die in meerdere functies terugkomen
    private List<Product> winkelwagen = new ArrayList<Product>();
    private Iterable<Product> productList;
    private String filter;

    /* --- METHOD : herhalende code ---
    code die bij bijna elke mapping werd herhaald, dus functie van gemaakt dubbele code te vermijden
    */
    private void mapAll(ModelMap map) {
        map.addAttribute("filter", filter);
        map.addAttribute("all", productList);
        map.addAttribute("dieren", Dier.values());
        map.addAttribute("categories", Categorie.values());
    }

/* --- INDEX ---  */
    /* --- OPHALEN indexpagina ---  */
    @GetMapping({"/", "/home"})
    public String getIndex(ModelMap map) {
        productList = productDAO.findAll();
        filter = null;
        mapAll(map);
        return "index";
    }

    /* --- OPHALEN indexpagina met FILTER categorie ---  */
    @GetMapping({"/categorie/{filter}"})
    public String getByCategories(@PathVariable("filter") Categorie categorie, ModelMap map) {
        productList = productDAO.findByCategorie(categorie);
        filter = categorie.toString();
        mapAll(map);
        return "index";
    }

    /* --- OPHALEN indexpagina met FILTER dier ---  */
    @GetMapping({"/dier/{filter}"})
    public String getByDieren(@PathVariable("filter") Dier dier, ModelMap map) {
        productList = productDAO.findByDier(dier);
        filter = dier.toString();
        mapAll(map);
        return "index";
    }


/* --- WINKELWAGEN ---  */
    /* --- TOEVOEGEN artikel aan Winkelwagen[] ---  */
    @GetMapping({"/winkelwagen/{id}"})
    public String getAddToShoppingcart(@PathVariable("id") int id, ModelMap map) {
        Optional<Product> product = productDAO.findById(id);
        if (product.isPresent()) {
            winkelwagen.add(product.get());
        }
        mapAll(map);
        return "index";
    }

    /* --- VERWIJDEREN artikel van Winkelwagen[] ---  */
    @GetMapping({"/winkelwagen/delete/{id}"})
    public String getDeleteShoppingcart(@PathVariable("id") int id, ModelMap map) {
        winkelwagen.remove(id);
        // herbereken de totaalprijs van de winkelwagen
        Double totaalprijs = 0.0;
        for (Product p : winkelwagen
        ) {
            totaalprijs += p.getPrijs();
        }
        map.addAttribute("totaalprijs", totaalprijs);
        map.addAttribute("cart", winkelwagen);
        return "winkelwagen";
    }

    /* --- OPHALEN winkelwagenpagina ---  */
    @GetMapping({"/winkelwagen"})
    public String getShoppingcart(ModelMap map, @AuthenticationPrincipal OidcUser principal) {
        // uitvoeren als de winkelwagen array niet leeg is
        if (!winkelwagen.isEmpty()) {
            if (principal != null) {
                // als user is ingelogd met AuthO
                var princ = principal.getClaims();
                var auth_id = princ.get("sub").toString();
                Optional<User> nieuweUser = userDAO.findById(auth_id);
                if (nieuweUser.isPresent()) {
                    // als user is geregistreerd in local db > als "userprofile" niet bestaat kan hij geen bestelling bevestigen
                    map.addAttribute("userprofile", nieuweUser.get());
                }
            }
            // bereken totaalprijs winkelwagen
            Double totaalprijs = 0.0;
            for (Product p : winkelwagen
            ) {
                totaalprijs += p.getPrijs();
            }
            map.addAttribute("totaalprijs", totaalprijs);
            map.addAttribute("cart", winkelwagen);
        }
        return "winkelwagen";
    }



/* --- ORDERBEVESTIGING ---  */
    /* --- POSTEN bevestigen aankoop ---  */
    @PostMapping({"/orderbevestiging"})
    public String postOrderbevestiging(HttpServletRequest request,
                                       @RequestParam("leveroptie") Boolean leveroptie,
                                       @AuthenticationPrincipal OidcUser principal) {

        // get ingelogde user
        var princ = principal.getClaims();
        var auth_id = princ.get("sub").toString();
        Optional<User> nieuweUser = userDAO.findById(auth_id);
        // bereken totaalprijs order
        Double totaalprijs = 0.0;
        for (Product p : winkelwagen
        ) {
            totaalprijs += p.getPrijs();
        }
        // maak nieuwe bestelling aan
        Bestelling bestelling = new Bestelling(leveroptie, totaalprijs, nieuweUser.get());
        bestellingDAO.save(bestelling);
        // maak voor elk product een nieuwe orderdetail aan
        for (Product p : winkelwagen
        ) {
            OrderDetail orderDetail = new OrderDetail(bestelling, p.getNaam(), p.getOmschrijving(), p.getPrijs());
            orderDetailDAO.save(orderDetail);
        }
        // maak winkelwagen array leeg want alles is besteld
        winkelwagen.clear();
        // voeg attribuut toe aan request om deze te kunnen ophalen in de getfunctie
        request.setAttribute("bestelling", bestelling);
        return "orderbevestiging";
    }

    /* --- OPHALEN orderbevestigingspagina ---  */
    @GetMapping({"/orderbevestiging"})
    public String getOrderbevestiging(ModelMap map,
                                      HttpServletRequest request) {
        // get attribuut die vanuit de postOrderbevestiging komt
        String bestelling = (String) request.getAttribute("bestelling");
        map.addAttribute("bestelling", bestelling);
        return "orderbevestiging";
    }

/* --- NIEUW PRODUCT ---  */
    /* --- OPHALEN nieuwProductpagina ---  */
    @GetMapping({"/nieuwProduct"})
    public String getNieuwProduct(ModelMap map) {
        // voeg attributen toe om de select-opties mee te bevolken
        map.addAttribute("dieren", Dier.values());
        map.addAttribute("categories", Categorie.values());
        return "nieuwProduct";
    }

    /* --- ATTRIBUUT die product entity aanmaakt om attributen naar te mappen ---  */
    @ModelAttribute("nieuwProduct")
    public Product toSaveProduct() {
        return new Product();
    }

    /* --- AANMAKEN nieuw product via formulierinput ---  */
    @PostMapping({"/nieuwProduct"})
    public String postNieuwProduct(@ModelAttribute("nieuwProduct") @Valid Product nieuwProduct,
                                   BindingResult bindingResult,
                                   ModelMap map) {
        if (bindingResult.hasErrors()) {
            return "/nieuwProduct";
        }
        productDAO.save(nieuwProduct);
        return "redirect:/home";
    }
}