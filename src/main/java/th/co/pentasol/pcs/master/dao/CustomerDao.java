package th.co.pentasol.pcs.master.dao;

import th.co.pentasol.pcs.master.entity.CustomerEntity;
import th.co.pentasol.pcs.master.model.filter.CustomerFilter;
import th.co.pentasol.pcs.master.model.CustomerModel;

import java.util.List;

public interface CustomerDao {
    List<CustomerEntity> findAll();

    Long rowcountByCondition(CustomerFilter filter);

    List<CustomerEntity> findAllByCondition(CustomerFilter filter);

    CustomerEntity findOneByCondition(CustomerFilter filter);

    CustomerEntity findOneByCode(String code);

    CustomerEntity findOneByFilter(CustomerFilter filter);

    boolean duplicateCustomerCode(CustomerModel data);

    boolean duplicateCustomerName(CustomerModel data);

    Integer insert(CustomerModel data);

    Integer update(CustomerModel data);

    Integer delete(CustomerEntity entity);

    Integer getMaxSerialNo(String code, String branchCode);

    Integer restore(CustomerEntity entity);

    Integer updateRestore(CustomerEntity entity);
}
