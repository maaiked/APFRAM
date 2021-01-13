package ehb.maaikedupont.opdrachtwerkstuk.dao;

import ehb.maaikedupont.opdrachtwerkstuk.entities.User;
import org.springframework.data.repository.CrudRepository;

import javax.management.Query;

public interface UserDAO extends CrudRepository <User, String> {

}
