package ehb.maaikedupont.opdrachtwerkstuk.controllers;

import ehb.maaikedupont.opdrachtwerkstuk.dao.ProductDAO;
import ehb.maaikedupont.opdrachtwerkstuk.entities.Categorie;
import ehb.maaikedupont.opdrachtwerkstuk.entities.Dier;
import ehb.maaikedupont.opdrachtwerkstuk.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductDAO productDAO;
    // TO DO : winkelwagen niet globaal aanmaken maar een attribuut maken van user
    // of in een sessievariabele?
    private final List<Product> winkelwagen = new ArrayList<Product>();

    @Autowired
    public ProductController(ProductDAO productDAO){this.productDAO=productDAO;}

    //maakt nieuw product entity aan om de attributen naar te mappen die doorgegeven worden
    @ModelAttribute("nieuwProduct")
    public Product toSave(){
        return new Product();
    }

    //mapping van de indexpagina
    @GetMapping({"/", "/index"})
    public String getIndex(ModelMap map){
        Iterable<Product> productList = productDAO.findAll();
        map.addAttribute("all", productList);
        return "index";
    }
    @GetMapping({"/categorie/{filter}"})
    public String getByCategories(@PathVariable("filter") Categorie categorie, ModelMap map){
        Iterable<Product> productList = productDAO.findByCategorie(categorie);
        map.addAttribute("all", productList);
        map.addAttribute("filter", categorie);
        return "index";
    }

    @GetMapping({"/dier/{filter}"})
    public String getByDieren(@PathVariable("filter") Dier dier, ModelMap map){
        Iterable<Product> productList = productDAO.findByDier(dier);
        map.addAttribute("all", productList);
        map.addAttribute("filter", dier);
        return "index";
    }

    @GetMapping({"/{id}"})
    public String getAddToShoppingcart(@PathVariable("id") int id, ModelMap map){
        Optional<Product> product = productDAO.findById(id);
        if (product.isPresent())
        {
            winkelwagen.add(product.get());
        }
//        TO DO ; else return error message to page
        Iterable<Product> productList = productDAO.findAll();
        map.addAttribute("all", productList);
        return "index";
    }

    //mapping naar shoppingcart pagina
    @GetMapping({"/winkelwagen"})
    public String getShoppingcart(ModelMap map){
        map.addAttribute("cart", winkelwagen);
        return "winkelwagen";
    }
    @GetMapping({"/winkelwagen/{id}"})
    public String getDeleteShoppingcart(@PathVariable("id")int id, ModelMap map){
        winkelwagen.remove(id);
        map.addAttribute("cart", winkelwagen);
        return "winkelwagen";
    }

    // mapping van de nieuwProduct pagina
    @GetMapping({"/nieuwProduct"})
    public String getNieuwProduct(){
        return "nieuwProduct";
    }

    @PostMapping({"/nieuwProduct"})
    public String postNieuwProduct(@ModelAttribute("nieuwProduct") @Valid Product nieuwProduct,
                            BindingResult bindingResult,
                            ModelMap map){
    if(bindingResult.hasErrors()){
        // TO DO : foutmeldingen overlopen en in meerdere talen
        // moet bij elk invoerveld een span error of is hier geen error message voor nodig?
        return "/nieuwProduct";
    }
        productDAO.save(nieuwProduct);
    return "redirect:/index";
    }

}
