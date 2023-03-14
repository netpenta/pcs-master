package th.co.pentasol.pcs.master.dao;

import th.co.pentasol.pcs.master.entity.UserEntity;

public interface UserDao {
    UserEntity findByUserName(String userName);

    Integer updateUserLogin(UserEntity data);

    Integer updateUserLogout(UserEntity data);
}
