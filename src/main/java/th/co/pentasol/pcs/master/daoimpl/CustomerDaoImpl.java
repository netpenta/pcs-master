package th.co.pentasol.pcs.master.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import th.co.pentasol.pcs.master.dao.CustomerDao;
import th.co.pentasol.pcs.master.entity.CustomerEntity;
import th.co.pentasol.pcs.master.model.filter.CustomerFilter;
import th.co.pentasol.pcs.master.model.CustomerModel;
import th.co.pentasol.pcs.master.util.MySqlUtil;
import th.co.pentasol.pcs.master.util.Util;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

@Component
public class CustomerDaoImpl implements CustomerDao {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private StringBuilder selectCustomerSql(boolean count, CustomerFilter filter){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("SELECT * FROM m_customers as mcs\n");
        sql.append("WHERE mcs.deleted_flg = 0 ");
        if(!Objects.isNull(filter)){
            if(Util.isNotEmpty(filter.getSearch())) {
                sql.append("AND (");
                sql.append("mcs.cust_cd LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("mcs.branch_cd LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("mcs.cust_nm LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("mcs.cust_nm_th LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("mcs.cust_short_nm LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("mcs.cust_short_nm_th LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("mcs.tax_id LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' OR ");
                sql.append("mcs.cust_branch_nm LIKE '" + MySqlUtil.valueLike(filter.getSearch()) + "' ");
                if (Util.isNotEmpty(filter.getEffectDate())) {
                    sql.append("AND mcs.effect_date <= DATE_FORMAT(:effectDate, '%Y%m%d') ");
                }
                sql.append(")");
            }else {
                if (Util.isNotEmpty(filter.getCustomerCode())) {
                    sql.append("AND mcs.cust_cd = :customerCode ");
                }
                if (Util.isNotEmpty(filter.getBranchCode())) {
                    sql.append("AND mcs.branch_cd = :branchCode ");
                }
                if (!Objects.isNull(filter.getSerialNo())) {
                    sql.append("AND mcs.serial_no = :serialNo ");
                }
                if (Util.isNotEmpty(filter.getEffectDate())) {
                    sql.append("AND mcs.effect_date >= DATE_FORMAT(:effectDate, '%Y%m%d') ");
                }
            }
            if(!count) sql.append(MySqlUtil.limit("", filter.getPageNo(), filter.getPageSize()));
        }
        return sql;
    }

    @Override
    public List<CustomerEntity> findAll(){
        String sql = selectCustomerSql(false,null).append("ORDER BY mcs.cust_cd, mcs.branch_cd ASC").toString();
        try{
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CustomerEntity.class));
        }catch (EmptyResultDataAccessException ignored){
            return null;
        }
    }

    @Override
    public Long rowcountByCondition(CustomerFilter filter){
        String sql = MySqlUtil.rowCountSql(selectCustomerSql(true, filter)).toString();
        return namedParameterJdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(filter), Long.class);
    }

    @Override
    public List<CustomerEntity> findAllByCondition(CustomerFilter filter){
        String sql = selectCustomerSql(false, filter).toString();
        try{
            return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(filter), new BeanPropertyRowMapper<>(CustomerEntity.class));
        }catch (EmptyResultDataAccessException ignored){
            return null;
        }
    }

    @Override
    public CustomerEntity findOneByCondition(CustomerFilter filter){
        String sql = selectCustomerSql(false, filter).toString();
        try{
            return namedParameterJdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(filter), new BeanPropertyRowMapper<>(CustomerEntity.class));
        }catch (EmptyResultDataAccessException ignored){
            return null;
        }
    }

    @Override
    public boolean duplicateCustomerCode(CustomerModel data){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM m_customers WHERE deleted_flg = 0 AND UPPER(REPLACE(cust_cd, ' ', '')) = UPPER(REPLACE(:code, ' ', '')) ");
        Integer found = namedParameterJdbcTemplate.queryForObject(sql.toString(), new BeanPropertySqlParameterSource(data), Integer.class);
        return found > 0 ? true : false;
    }

    @Override
    public  boolean duplicateCustomerName(CustomerModel data){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM m_customers WHERE deleted_flg = 0 AND UPPER(REPLACE(cust_nm, ' ', '')) = UPPER(REPLACE(:nameEn, ' ', '')) ");
        Integer found = namedParameterJdbcTemplate.queryForObject(sql.toString(), new BeanPropertySqlParameterSource(data), Integer.class);
        return  found > 0 ? true : false;
    }

    @Override
    public Integer insert(CustomerModel data){
        String sql = "INSERT INTO m_customers SET " +
                "cust_cd = :code," +
                "branch_cd = :branchCode, " +
                "serial_no = :serialNo, " +
                "effect_date = :effectDate, " +
                "exp_date = :expDate, " +
                "tax_id = :taxId, " +
                "cust_nm = :nameEn, " +
                "cust_nm_th = :nameTh, " +
                "cust_short_nm = :shortNameEn, " +
                "cust_short_nm_th = :shortNameTh, " +
                "cust_branch_id = :branchNo, " +
                "cust_branch_nm = :branchName, " +
                "group_div = :groupId, " +
                "delivery_location = :deliveryLocation, " +
                "address_1 = :address1, " +
                "address_2 = :address2, " +
//                "address_1_th = :addressTh1, " +
//                "address_2_th = :addressTh2, " +
//                "zip_cd = :postCode, " +
                "tel_no = :telephoneNo, " +
                "fax_no = :faxNo, " +
                "deleted_flg = 0, " +
                "user_id = :userName, " +
                "program_id = :systemId, " +
                "created_datetime = NOW();";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(data));
    }

    @Override
    public Integer update(CustomerModel data){
        String sql = "UPDATE m_customers SET " +
                "effect_date = :effectDate, " +
                "exp_date = :expDate, " +
                "tax_id = :taxId, " +
                "cust_nm = :nameEn, " +
                "cust_nm_th = :nameTh, " +
                "cust_short_nm = :shortNameEn, " +
                "cust_short_nm_th = :shortNameTh, " +
                "cust_branch_id = :branchNo, " +
                "cust_branch_nm = :branchName, " +
                "group_div = :groupId, " +
                "delivery_location = :deliveryLocation, " +
                "address_1 = :address1, " +
                "address_2 = :address2, " +
//                "address_1_th = :addressTh1, " +
//                "address_2_th = :addressTh2, " +
//                "zip_cd = :postCode, " +
                "tel_no = :telephoneNo, " +
                "fax_no = :faxNo, " +
                "deleted_flg = 0, " +
                "user_id = :userName, " +
                "program_id = :systemId, " +
                "modified_datetime = NOW() " +
                "WHERE cust_cd = :code AND branch_cd = :branchCode AND serial_no = :serialNo ;";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(data));
    }

    @Override
    public Integer updateRestore(CustomerEntity data){
        String sql = "UPDATE m_customers SET " +
                "exp_date = 99999999, " +
                "deleted_flg = 0, " +
                "user_id = :user_id, " +
                "program_id = :program_id, " +
                "modified_datetime = NOW() " +
                "WHERE cust_cd = :cust_cd AND branch_cd = :branch_cd AND effect_date = :effect_date ;";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(data));
    }

    @Override
    public Integer delete(CustomerEntity entity){
        String sql = "UPDATE m_customers SET " +
                "exp_date = :exp_date, " +
                "deleted_flg = 1, " +
                "modified_datetime = NOW(), "+
                "user_id = :user_id, " +
                "program_id = :program_id " +
                "WHERE cust_cd = :cust_cd AND branch_cd = :branch_cd AND effect_date = :effect_date;";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(entity));
    }

    @Override
    public Integer restore(CustomerEntity entity){
        String sql = "UPDATE m_customers SET " +
                "deleted_flg = 0, " +
                "modified_datetime = NOW(), "+
                "user_id = :user_id, " +
                "program_id = :program_id " +
                "WHERE cust_cd = :cust_cd AND branch_cd = :branch_cd AND effect_date = :effect_date;";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(entity));
    }

    @Override
    public  CustomerEntity findOneByCode(String code){
        StringBuilder sql = selectCustomerSql(false, null).append("AND mcs.cust_cd = '" + code + "' ORDER BY serial_no DESC LIMIT 1 ");
        try{
            return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(CustomerEntity.class));
        }catch (EmptyResultDataAccessException ignored){
            return null;
        }
    }

    @Override
    public  CustomerEntity findOneByFilter(CustomerFilter filter){
        StringBuilder sql = selectCustomerSql(false, filter);
        try{
            return namedParameterJdbcTemplate.queryForObject(sql.toString(), new BeanPropertySqlParameterSource(filter), new BeanPropertyRowMapper<>(CustomerEntity.class));
        }catch (EmptyResultDataAccessException ignored){
            return null;
        }
    }

    @Override
    public Integer getMaxSerialNo(String code, String branchCode){
        StringBuilder sql = new StringBuilder("SELECT IFNULL(MAX(serial_no),0) + 1 FROM m_customers WHERE cust_cd = ? AND branch_cd = ? ");
        Integer serial_no = jdbcTemplate.queryForObject(sql.toString(), new Object[]{code, branchCode}, Integer.class);
        return serial_no;
    }
}
