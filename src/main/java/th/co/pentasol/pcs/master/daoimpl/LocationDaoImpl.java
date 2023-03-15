package th.co.pentasol.pcs.master.daoimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.pentasol.pcs.master.dao.LocationDao;
import th.co.pentasol.pcs.master.entity.LocationEntity;
import th.co.pentasol.pcs.master.model.LocationModel;
import th.co.pentasol.pcs.master.model.filter.LocationFilter;
import th.co.pentasol.pcs.master.util.MySqlUtil;
import th.co.pentasol.pcs.master.util.Util;

import java.util.Objects;

@Slf4j
@Repository
public class LocationDaoImpl implements LocationDao {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insert(LocationModel data){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("INSERT INTO m_places SET ");
        sql.append("place_cd = :code, ");
        sql.append("serial_no = :serialNo, ");
        sql.append("effect_date = :effectDate, ");
        sql.append("exp_date = 99999999, ");
        sql.append("place_nm = :name, ");
        sql.append("place_type = 'LOCATION', ");
        sql.append("outside_flg = 0, ");
        sql.append("division_cd = '01', ");
        sql.append("factory_cd = '01', ");
        sql.append("vat_div = 0, ");
        sql.append("round_div = 1, ");
        sql.append("deleted_flg = 0, ");
        sql.append("user_id = :updatedBy, ");
        sql.append("program_id = :systemId, ");
        sql.append("created_datetime = :updatedDate; ");
        namedParameterJdbcTemplate.update(sql.toString(), new BeanPropertySqlParameterSource(data));
    }

    @Override
    public LocationEntity findByLocation(String locationCode){
        LocationEntity data = null;
        StringBuilder sql = selectLocation(null).append("AND mpl.place_cd = ?");
        if(jdbcTemplate.queryForObject(MySqlUtil.rowCountSql(sql), Integer.class, locationCode) > 0) {
            data = jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(LocationEntity.class), locationCode);
        }
        return data;
    }
    private StringBuilder selectLocation(LocationFilter filter){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("SELECT mpl.*, mus.name as updated_by FROM m_places as mpl\n");
        sql.append("INNER JOIN m_user as mus  ON mus.user_name = mpl.user_id\n");
        sql.append("WHERE mpl.deleted_flg = 0 AND mpl.place_type = 'LOCATION' ");
        if(!Objects.isNull(filter)) {
            if (Util.isNotEmpty(filter.getName())) sql.append("AND mpl.place_nm LIKE :name ");
            if (Util.isNotEmpty(filter.getCode())) sql.append("AND mpl.place_cd LIKE :code ");
        }
        return sql;
    }
}
