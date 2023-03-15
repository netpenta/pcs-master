package th.co.pentasol.pcs.master.dao;

import th.co.pentasol.pcs.master.entity.LocationEntity;
import th.co.pentasol.pcs.master.model.LocationModel;

public interface LocationDao {
    void insert(LocationModel data);
    LocationEntity findByLocation(String locationCode);

}
