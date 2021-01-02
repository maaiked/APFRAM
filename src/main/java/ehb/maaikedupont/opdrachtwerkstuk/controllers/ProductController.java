package ehb.maaikedupont.opdrachtwerkstuk.controllers;

import ehb.maaikedupont.opdrachtwerkstuk.dao.BestellingDAO;
import ehb.maaikedupont.opdrachtwerkstuk.dao.OrderDetailDAO;
import ehb.maaikedupont.opdrachtwerkstuk.dao.ProductDAO;
import ehb.maaikedupont.opdrachtwerkstuk.dao.UserDAO;
import ehb.maaikedupont.opdrachtwerkstuk.entities.*;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
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

    // TO DO : winkelwagen niet globaal aanmaken maar een attribuut maken van user
    // of in een sessievariabele?
    private final List<Product> winkelwagen = new ArrayList<Product>();
    private Iterable<Product> productList ;
    private String filter;
    private Optional<User> user;

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

        // check if user is instantiated in local database


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
//        TODO ; else return error message to page
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

    /* --- POSTEN bevestigen aankoop ---  */
    @PostMapping({"/orderbevestiging"})
    public String postOrderbevestiging(HttpServletRequest request, @RequestParam("leveroptie") Boolean leveroptie){

        Optional<User> nieuweUser = userDAO.findById("1");
        if (nieuweUser.isPresent())
        {
            Double totaalprijs = 0.0;
            for (Product p: winkelwagen
            ) {
                totaalprijs += p.getPrijs();
            }
            Bestelling bestelling = new Bestelling(leveroptie, totaalprijs, nieuweUser.get());
            bestellingDAO.save(bestelling);
            for (Product p: winkelwagen
            ) {
                OrderDetail orderDetail = new OrderDetail(bestelling, p.getNaam(), p.getOmschrijving(), p.getPrijs());
                orderDetailDAO.save(orderDetail);
            }
            winkelwagen.clear();
            request.setAttribute("bestelling", bestelling);
        }
        return "orderbevestiging";
    }

    /* --- OPHALEN orderbevestigingspagina ---  */
    @GetMapping({"/orderbevestiging"})
    public String getOrderbevestiging(ModelMap map, HttpServletRequest request){
        String bestelling = (String) request.getAttribute("bestelling");
        map.addAttribute("bestelling", bestelling);
        return "orderbevestiging";
    }

    /* --- OPHALEN nieuwProductpagina ---  */
    @GetMapping({"/nieuwProduct"})
    public String getNieuwProduct(ModelMap map){
        map.addAttribute("dieren", Dier.values());
        map.addAttribute("categories", Categorie.values());
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