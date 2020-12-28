package ehb.maaikedupont.opdrachtwerkstuk.dao;

import ehb.maaikedupont.opdrachtwerkstuk.entities.OrderDetail;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailDAO extends CrudRepository <OrderDetail, Integer> {
}
