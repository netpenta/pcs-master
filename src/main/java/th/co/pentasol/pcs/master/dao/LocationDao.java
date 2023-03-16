package th.co.pentasol.pcs.master.dao;

import th.co.pentasol.pcs.master.entity.LocationEntity;
import th.co.pentasol.pcs.master.model.LocationModel;
import th.co.pentasol.pcs.master.model.SelectedModel;
import th.co.pentasol.pcs.master.model.filter.LocationFilter;

import java.util.List;

public interface LocationDao {
    void insert(LocationModel data);

    void update(LocationModel data);

    LocationEntity findByLocation(String locationCode);

    Integer delete(SelectedModel data);

    Long rowCountByCondition(LocationFilter filter);

    List<LocationEntity> findByCondition(LocationFilter filter);
}
