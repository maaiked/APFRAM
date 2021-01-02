package ehb.maaikedupont.opdrachtwerkstuk.controllers;

import ehb.maaikedupont.opdrachtwerkstuk.dao.BestellingDAO;
import ehb.maaikedupont.opdrachtwerkstuk.dao.OrderDetailDAO;
import ehb.maaikedupont.opdrachtwerkstuk.dao.ProductDAO;
import ehb.maaikedupont.opdrachtwerkstuk.dao.UserDAO;
import ehb.maaikedupont.opdrachtwerkstuk.entities.Bestelling;
import ehb.maaikedupont.opdrachtwerkstuk.entities.OrderDetail;
import ehb.maaikedupont.opdrachtwerkstuk.entities.Product;
import ehb.maaikedupont.opdrachtwerkstuk.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfielController {

    private final UserDAO userDAO;
    private final BestellingDAO bestellingDAO;
    private final OrderDetailDAO orderDetailDAO;

    @Autowired
    public ProfielController(UserDAO userDAO, BestellingDAO bestellingDAO, OrderDetailDAO orderDetailDAO)
    {
        this.userDAO=userDAO;
        this.bestellingDAO = bestellingDAO;
        this.orderDetailDAO = orderDetailDAO;
    }

    @GetMapping({"/profiel"})
    public String getProfiel(ModelMap map, @AuthenticationPrincipal OidcUser principal){
        Optional<User> user = userDAO.findById("1");
        map.addAttribute("authprofile", principal.getClaims());
        if (user.isPresent())
        {
           map.addAttribute("userprofile", user.get());
        }
        else map.addAttribute("userprofile", "geen user gevonden met deze id");

        // get orders
/*        Iterable<Bestelling> bestelling = bestellingDAO.findAll();
        Iterable<OrderDetail> orderDetail = orderDetailDAO.findAll();
        map.addAttribute("orders", bestelling);
        map.addAttribute("orderdetail", orderDetail);*/
        return "profiel";
    }
}
