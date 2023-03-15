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
import th.co.pentasol.pcs.master.model.CustomerFilter;
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
                sql.append(")");
            }

            if(Util.isNotEmpty(filter.getEffectDate())){
                sql.append("AND mcs.effect_date <= DATE_FORMAT(:effectDate, '%Y%m%d') ");
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
    public Long countByCondition(CustomerFilter filter){
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
    public int save(CustomerModel data){
        String sql = "INSERT INTO m_customers SET " +
                "cust_cd = :code," +
                "branch_cd = :branchCode, " +
                "serial_no = IFNULL((SELECT MAX(serial_no) FROM m_customers WHERE cust_cd = :code AND branch_cd = :branchCode), 0) + 1, " +
                "effect_date = :effectiveDate, " +
                "exp_date = :expiredDate, " +
                "tax_id = :taxId, " +
                "cust_nm = :nameEn, " +
                "cust_nm_th = :nameTh, " +
                "cust_short_nm = :shortNameEn, " +
                "cust_short_nm_th = :shortNameTh, " +
                "cust_branch_id = :branchNo, " +
                "cust_branch_nm = :branchName, " +
                "group_div = :groupId, " +
                "delivery_location = :deliveryLocation, " +
                "address_1 = :addressEn1, " +
                "address_2 = :addressEn2, " +
                "address_1_th = :addressTh1, " +
                "address_2_th = :addressTh2, " +
                "zip_cd = :postCode, " +
                "tel_no = :telephone, " +
                "fax_no = :faxNo, " +
                "deleted_flg = 0, " +
                "user_id = :userName, " +
                "program_id = :systemId, " +
                "created_datetime = NOW();";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(data));
    }

    @Override
    public int update(CustomerModel data){
        String sql = "UPDATE m_customers SET " +
                "effect_date = :effectiveDate, " +
                "exp_date = :expiredDate, " +
                "tax_id = :taxId, " +
                "cust_nm = :nameEn, " +
                "cust_nm_th = :nameTh, " +
                "cust_short_nm = :shortNameEn, " +
                "cust_short_nm_th = :shortNameTh, " +
                "cust_branch_id = :branchNo, " +
                "cust_branch_nm = :branchName, " +
                "group_div = :groupId, " +
                "delivery_location = :deliveryLocation, " +
                "address_1 = :addressEn1, " +
                "address_2 = :addressEn2, " +
                "address_1_th = :addressTh1, " +
                "address_2_th = :addressTh2, " +
                "zip_cd = :postCode, " +
                "tel_no = :telephone, " +
                "fax_no = :faxNo, " +
                "deleted_flg = 0, " +
                "user_id = :userName, " +
                "program_id = :systemId, " +
                "modified_datetime = NOW() " +
                "WHERE cust_cd = :code AND branch_cd = :branchCode AND serial_no = :serialNo;";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(data));
    }

    @Override
    public int deleteOne(CustomerEntity entity){
        String sql = "UPDATE m_customers SET " +
                "deleted_flg = 1, " +
                "modified_datetime = NOW(), "+
                "user_id = :user_id, " +
                "program_id = :program_id " +
                "WHERE cust_cd = :cust_cd AND branch_cd = :branch_cd AND serial_no = :serial_no;";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(entity));
    }
}
