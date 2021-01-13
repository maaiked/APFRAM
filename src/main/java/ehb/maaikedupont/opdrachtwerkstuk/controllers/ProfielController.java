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

        var princ = principal.getClaims();
        var auth_id = princ.get("sub").toString();
        User nieuw = new User(auth_id, naam, voornaam, email, tel, straat, huisnummer, bus, postcode, gemeente);
        userDAO.save(nieuw);
        Optional<User> nieuweUser = userDAO.findById(auth_id);
        map.addAttribute("userprofile", nieuweUser.get());
        map.addAttribute("authprofile", principal.getClaims());
        return "profiel";
    }

    @GetMapping({"/profiel"})
    public String getProfiel(ModelMap map, @AuthenticationPrincipal OidcUser principal) {

        // get user op basis van ingelogde user...
        var princ = principal.getClaims();
        var id = princ.get("sub").toString();
        Optional<User> user = userDAO.findById(id);
        map.addAttribute("authprofile", principal.getClaims());
        map.addAttribute("sub", id);
        // get user in localdb op basis van ingelogde user
        if (user.isPresent()) {
            map.addAttribute("userprofile", user.get());
        }
        return "profiel";
    }
}
