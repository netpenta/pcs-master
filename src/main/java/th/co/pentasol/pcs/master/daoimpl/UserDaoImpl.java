package th.co.pentasol.pcs.master.daoimpl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import th.co.pentasol.pcs.master.dao.UserDao;
import th.co.pentasol.pcs.master.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


@Component
public class UserDaoImpl implements UserDao {
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    private StringBuilder selectUserSql(){
        StringBuilder sql = new StringBuilder("\n");
        sql.append("SELECT mus.*, mrl.role_nm, mft.fact_ennm as factory, mst.sect_nm as section, mpl.place_nm as workPlace, (SELECT user_fname FROM m_user WHERE user_name = mus.modified_id) as modified_by  FROM m_user as mus\n");
        sql.append("INNER JOIN m_roles   as mrl ON mrl.role_id = mus.role_id\n");
        sql.append("INNER JOIN m_factory as mft ON mft.fact_cd = mus.user_fact_cd\n");
        sql.append("LEFT JOIN m_section as mst ON mst.sect_cd = mus.user_sect_cd AND mst.sect_fact_cd = mus.user_fact_cd\n");
        sql.append("LEFT JOIN m_places  as mpl ON mpl.place_cd   = mus.user_place_cd AND DATE_FORMAT(NOW(), '%Y%m%d') >= mpl.effect_date  AND DATE_FORMAT(NOW(), '%Y%m%d') <= mpl.exp_date\n");
        sql.append("WHERE mus.deleted_flg = 0 AND mst.deleted_flg = 0 ");
        return sql;
    }

    @Override
    public UserEntity findByUserName(String userName){
        StringBuilder sql = selectUserSql().append("AND mus.user_name = ? ");
        try{
            return jdbcTemplate.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(UserEntity.class), userName);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    @Override
    public Integer updateUserLogin(UserEntity data){
        String sql = "UPDATE m_user SET " +
                "token = :token, " +
                "token_expired = :tokenExpiredDate, " +
                "user_last_login = :lastLogin, " +
                "modified_id = :modifiedId, "+
                "modified_datetime = :modifiedDatetime, " +
                "program_id = :programId " +
                "WHERE user_id = :userId; ";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(data));
    }

    @Override
    public Integer updateUserLogout(UserEntity data){
        String sql = "UPDATE m_user SET token = null, " +
                "token_expired = null, " +
                "user_last_logout = :lastLogout, " +
                "modified_id = :modifiedId, " +
                "modified_datetime = :modifiedDatetime, " +
                "program_id = :programId " +
                "WHERE user_id = :userId; ";
        return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(data));
    }
}
