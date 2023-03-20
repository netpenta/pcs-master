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
        sql.append("place_th_nm = :nameTh, ");
        sql.append("group_div = :type, ");
        sql.append("outside_flg = 0, "); //set outside_flg = 0 for "Work location"
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
//        sql.append("UPDATE m_places SET ");
//        sql.append("place_nm = :name, ");
//        sql.append("place_th_nm = :nameTh, ");
//        sql.append("group_div = :type, ");
//        sql.append("effect_date = :effectDate, ");
//        sql.append("user_id = :updatedBy, ");
//        sql.append("program_id = :systemId, ");
//        sql.append("modified_datetime = :updatedDate ");
//        sql.append("WHERE place_cd = :code ");

        sql.append("UPDATE m_places SET ");
        sql.append("place_nm = :name, ");
        sql.append("place_th_nm = :nameTh, ");
//        sql.append("place_short_nm = :place_short_nm, ");
        sql.append("group_div = :type, ");
        sql.append("effect_date = :effectDate, ");
//        sql.append("exp_date = :exp_date, ");
        sql.append("deleted_flg = 0, ");
        sql.append("user_id = :updatedBy, ");
        sql.append("program_id = :systemId, ");
        sql.append("modified_datetime  = :updatedDate ");
        sql.append("WHERE place_cd = :code AND serial_no = :serialNo;");

        namedParameterJdbcTemplate.update(sql.toString(), new BeanPropertySqlParameterSource(data));
    }

    private StringBuilder selectLocation(LocationFilter filter){
        StringBuilder sql = new StringBuilder("\n");
//        sql.append("SELECT place.*, user.user_name as updated_by FROM m_places as place \n");
//        sql.append("INNER JOIN m_user as user ON user.user_name = place.user_id \n");
//        sql.append("WHERE place.deleted_flg = 0 ");
        sql.append("SELECT place.*, \n");
        sql.append("       STR_TO_DATE(place.effect_date, '%Y%m%d') as locat_effect_date, (CASE WHEN place.exp_date <> 99999999 THEN STR_TO_DATE(place.exp_date, '%Y%m%d') ELSE NULL END) as locat_exp_date, refer.reference_nm as group_div_nm, \n");
        sql.append("       (SELECT fact_abbrnm FROM m_factory WHERE fact_cd = place.factory_cd AND fact_effdate <= CAST(DATE_FORMAT(NOW(), '%Y%m%d') as Decimal) AND CAST(DATE_FORMAT(NOW(), '%Y%m%d') as Decimal) <= fact_expdate ORDER BY fact_effdate, deleted_flg ASC LIMIT 1) as fact_abbrnm, \n");
        sql.append("       (SELECT user_fname  FROM m_user WHERE user_name = place.user_id ORDER BY deleted_flg ASC LIMIT 1) as modified_by \n");
        sql.append("FROM m_places as place \n");
        sql.append("LEFT OUTER JOIN m_reference as refer ON CAST(refer.reference_cd AS UNSIGNED) = place.group_div AND reference_id = (SELECT config_2 FROM pcs_config WHERE deleted_flg = 0 AND config_define_field = 0 AND config_cd = 'REFERENCE') \n");
        sql.append("WHERE place.outside_flg = 0 AND place.deleted_flg = 0 ");
        if(!Objects.isNull(filter)) {
            if (filter.getType() > -1) sql.append("AND place.place_type = :type ");
            if (Util.isNotEmpty(filter.getFactory())) sql.append("AND place.factory_cd = :factory ");
            if (Util.isNotEmpty(filter.getCode())) sql.append("AND UPPER(place.place_cd) LIKE UPPER(:code) ");
            if (Util.isNotEmpty(filter.getName())) sql.append("AND (UPPER(place.place_nm) LIKE UPPER(:name) ");
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
    public void restore(LocationModel data) {
        String sql = "UPDATE m_places SET deleted_flg = 0, user_id = :updatedBy, modified_datetime = :updatedDate, program_id = :systemId WHERE place_cd = :code AND serial_no = 1;";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(data));
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
        if(filter.getType() > -1)   filter.setType(filter.getType());
        if(Util.isNotEmpty(filter.getFactory()))   filter.setFactory(filter.getFactory());
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

    @Override
    public boolean duplicateLocationCode(LocationModel data){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM m_places WHERE deleted_flg = 0 AND outside_flg = 0 AND UPPER(REPLACE(place_cd, ' ', '')) = UPPER(REPLACE(:code, ' ', '')) ");

        Integer found = namedParameterJdbcTemplate.queryForObject(sql.toString(), new BeanPropertySqlParameterSource(data), Integer.class);
        return found > 0 ? true : false;
    }
}
