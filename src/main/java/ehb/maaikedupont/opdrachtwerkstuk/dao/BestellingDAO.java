package ehb.maaikedupont.opdrachtwerkstuk.dao;

import ehb.maaikedupont.opdrachtwerkstuk.entities.Bestelling;
import org.springframework.data.repository.CrudRepository;

public interface BestellingDAO extends CrudRepository <Bestelling, Integer> {
}
