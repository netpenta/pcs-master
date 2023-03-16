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
import th.co.pentasol.pcs.master.model.SelectedModel;
import th.co.pentasol.pcs.master.model.filter.LocationFilter;
import th.co.pentasol.pcs.master.util.MySqlUtil;
import th.co.pentasol.pcs.master.util.Util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    public void update(LocationModel data) {
        StringBuilder sql = new StringBuilder("\n");
        sql.append("UPDATE m_places SET ");
        sql.append("place_nm = :name, ");
        sql.append("user_id = :updatedBy, ");
        sql.append("program_id = :systemId, ");
        sql.append("modified_datetime = :updatedDate ");
        sql.append("WHERE place_cd = :code ");
        namedParameterJdbcTemplate.update(sql.toString(), new BeanPropertySqlParameterSource(data));
    }

    private StringBuilder selectLocation(LocationFilter filter){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("SELECT place.*, user.user_name as updated_by FROM m_places as place\n");
        sql.append("INNER JOIN m_user as user  ON user.user_name = place.user_id\n");
        sql.append("WHERE place.deleted_flg = 0 AND place.place_type = 'LOCATION' ");
        if(!Objects.isNull(filter)) {
            if (Util.isNotEmpty(filter.getName())) sql.append("AND place.place_nm LIKE :name ");
            if (Util.isNotEmpty(filter.getCode())) sql.append("AND place.place_cd LIKE :code ");
        }
        return sql;
    }

    @Override
    public LocationEntity findByLocation(String locationCode){
        LocationEntity data = null;
        StringBuilder sql = selectLocation(null).append("AND place.place_cd = ?");
        if(jdbcTemplate.queryForObject(MySqlUtil.rowCountSql(sql), Integer.class, locationCode) > 0) {
            data = jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(LocationEntity.class), locationCode);
        }
        return data;
    }

    @Override
    public Integer delete(SelectedModel data) {
        String sql = "UPDATE m_places SET deleted_flg = 1, user_id = :userId, modified_datetime = :modifiedDateTime, program_id = :programId WHERE place_cd IN (:selectedList) ;";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(data));
    }

    @Override
    public Long rowCountByCondition(LocationFilter filter) {
        filter = setConditionValue(filter);
        return namedParameterJdbcTemplate.queryForObject(MySqlUtil.rowCountSql(selectLocation(filter)), new BeanPropertySqlParameterSource(filter), Long.class);
    }

    @Override
    public List<LocationEntity> findByCondition(LocationFilter filter) {
        StringBuilder sql = selectLocation(filter);
        sql.append(MySqlUtil.orderBy(sortField(filter.getSortBy()), filter.getSortOrder()));
        sql.append(MySqlUtil.limit(filter.getPageNo(), filter.getPageSize()));

        filter = setConditionValue(filter);
        return namedParameterJdbcTemplate.query(sql.toString(), new BeanPropertySqlParameterSource(filter), new BeanPropertyRowMapper<>(LocationEntity.class));
    }

    private LocationFilter setConditionValue(LocationFilter filter){
        if(Util.isNotEmpty(filter.getCode()))   filter.setCode(MySqlUtil.valueLike(filter.getCode()));
        if(Util.isNotEmpty(filter.getName()))   filter.setName(MySqlUtil.valueLike(filter.getName()));
        return filter;
    }

    private String sortField(String sortBy){
        Map<String, String> sortByMap = new LinkedHashMap<>();
        sortByMap.put("code".toUpperCase(), "place.place_cd");
        sortByMap.put("name".toUpperCase(), "place.place_nm");
        if(!Objects.isNull(sortBy)) {
            if (sortByMap.containsKey(sortBy.trim().toUpperCase())) {
                return sortByMap.get(sortBy.trim().toUpperCase());
            }
        }
        return "place.created_datetime";
    }
}
