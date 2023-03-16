package th.co.pentasol.pcs.master.dao;

import th.co.pentasol.pcs.master.entity.CustomerEntity;
import th.co.pentasol.pcs.master.model.CustomerFilter;
import th.co.pentasol.pcs.master.model.CustomerModel;

import java.util.List;

public interface CustomerDao {
    List<CustomerEntity> findAll();

    Long countByCondition(CustomerFilter filter);

    List<CustomerEntity> findAllByCondition(CustomerFilter filter);

    int save(CustomerModel data);

    int update(CustomerModel data);

    int deleteOne(CustomerEntity entity);

}
