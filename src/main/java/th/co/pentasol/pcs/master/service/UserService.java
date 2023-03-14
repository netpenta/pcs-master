package th.co.pentasol.pcs.master.service;

import lombok.extern.slf4j.Slf4j;
import th.co.pentasol.pcs.master.component.Message;
import th.co.pentasol.pcs.master.configuration.JwtUtils;
import th.co.pentasol.pcs.master.dao.UserDao;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.util.ApiStatus;
import th.co.pentasol.pcs.master.util.Util;
import th.co.pentasol.pcs.master.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("ALL")
@Slf4j
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserDao userDao;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    Message message;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userDao.findByUserName(userName);
        if(!Objects.isNull(userEntity)){
            return new User(userEntity.getUserName(), "", new ArrayList<>());
        }else{
            throw new UsernameNotFoundException(message.getMsg("msg.invalid.username.password", new Locale("en")));
        }
    }


    public UserInfo getUserInfo(HttpServletRequest request) throws ServiceException {
        try{
            String jwtToken = request.getHeader("Authorization");
            String username = jwtUtils.getUsernameFromToken(jwtToken);

            UserEntity userEntity = userDao.findByUserName(username);
            UserInfo user = new UserInfo();
            user.setUserId(userEntity.getUserId());
            user.setUserName(username);
            user.setLocale(request.getLocale());
//            user.setIpAddress(request.getRemoteAddr());
            return user;
        }catch(Exception ex){
            log.error(Util.logErrorMsg(this.getClass().getName(), ex.getMessage()));
            throw new ServiceException(ApiStatus.STATUS_INTERNAL_SERVER_ERROR, message.getInternalErrorMessage(request.getLocale()));
        }
    }

}