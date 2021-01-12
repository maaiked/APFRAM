package ehb.maaikedupont.opdrachtwerkstuk.controllers;

import ehb.maaikedupont.opdrachtwerkstuk.dao.UserDAO;
import ehb.maaikedupont.opdrachtwerkstuk.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;

@Controller
public class ProfielController {

    private final UserDAO userDAO;

    @Autowired
    public ProfielController(UserDAO userDAO)
    {
        this.userDAO=userDAO;
    }

    @GetMapping({"/profiel"})
    public String getProfiel(ModelMap map, @AuthenticationPrincipal OidcUser principal){

        // get user in localdatabase op basis van ingelogde user...
        // TODO: map attributen opruimen en profielpagina mooi maken :)
        var princ = principal.getClaims();
        var id = princ.get("sub").toString();
        Optional<User> user = userDAO.findById(id);
        map.addAttribute("authprofile", principal.getClaims());

        if (user.isPresent())
        {
           map.addAttribute("userprofile", user.get());
           map.addAttribute("sub", princ.get("sub").toString());
        }
        else map.addAttribute("userprofile", "geen user gevonden met deze id");

        return "profiel";
    }
}
