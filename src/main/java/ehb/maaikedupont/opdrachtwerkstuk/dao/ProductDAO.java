package ehb.maaikedupont.opdrachtwerkstuk.dao;

import ehb.maaikedupont.opdrachtwerkstuk.entities.Categorie;
import ehb.maaikedupont.opdrachtwerkstuk.entities.Dier;
import ehb.maaikedupont.opdrachtwerkstuk.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductDAO extends CrudRepository<Product, Integer> {

    //Iterable<Product> productList = productDAO.findByCategorie(Categorie.voeding);
    Iterable<Product> findByCategorie(Categorie categorie);
    Iterable<Product> findByDier(Dier dier);
}
