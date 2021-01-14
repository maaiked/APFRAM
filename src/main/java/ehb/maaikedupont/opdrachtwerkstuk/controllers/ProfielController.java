package ehb.maaikedupont.opdrachtwerkstuk.controllers;

import ehb.maaikedupont.opdrachtwerkstuk.dao.UserDAO;
import ehb.maaikedupont.opdrachtwerkstuk.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class ProfielController {

    private final UserDAO userDAO;

    @Autowired
    public ProfielController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // verwerkt het formulier op de profiel pagina, die getoont wordt wanneer de user is ingelogd
    // maar nog niet gekend is in de local db ( nodig voor de adresgegevens etc )
    @PostMapping({"/updateUser"})
    public String postUpdateUser(HttpServletRequest request,
                                 ModelMap map,
                                 @RequestParam("naam") String naam,
                                 @RequestParam("voornaam") String voornaam,
                                 @RequestParam("email") String email,
                                 @RequestParam("tel") String tel,
                                 @RequestParam("straat") String straat,
                                 @RequestParam("huisnummer") String huisnummer,
                                 @RequestParam("bus") String bus,
                                 @RequestParam("postcode") String postcode,
                                 @RequestParam("gemeente") String gemeente,
                                 @AuthenticationPrincipal OidcUser principal) {

        // haalt de auth0 gegevens van de ingelogde user op om de id in authO ook te gebruiken als id in de localdb
        var princ = principal.getClaims();
        var auth_id = princ.get("sub").toString();
        User nieuw = new User(auth_id, naam, voornaam, email, tel, straat, huisnummer, bus, postcode, gemeente);
        userDAO.save(nieuw);

        // haal de gegevens op om weer te geven op de profielview
        Optional<User> nieuweUser = userDAO.findById(auth_id);
        map.addAttribute("userprofile", nieuweUser.get());
        map.addAttribute("authprofile", principal.getClaims());

        return "profiel";
    }


    @GetMapping({"/profiel"})
    public String getProfiel(ModelMap map, @AuthenticationPrincipal OidcUser principal) {

        // get auth0 gegevens op basis van ingelogde user
        var princ = principal.getClaims();
        var id = princ.get("sub").toString();
        map.addAttribute("authprofile", principal.getClaims());
        map.addAttribute("sub", id);

        // als user ook in localdb al bestaat, haal ook deze gegevens op
        Optional<User> user = userDAO.findById(id);
        if (user.isPresent()) {
            map.addAttribute("userprofile", user.get());
        }

        return "profiel";
    }
}
