package th.co.pentasol.pcs.master.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.pentasol.pcs.master.component.Message;
import th.co.pentasol.pcs.master.configuration.PcsConfig;
import th.co.pentasol.pcs.master.dao.LocationDao;
import th.co.pentasol.pcs.master.entity.LocationEntity;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.LocationModel;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.util.ApiStatus;
import th.co.pentasol.pcs.master.util.DateTimeUtil;
import th.co.pentasol.pcs.master.util.Util;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class LocationService {
    @Autowired
    Message message;

    @Autowired
    LocationDao locationDao;
    @Autowired
    PcsConfig config;

    private Map<String, Object> mapLocationResult(Integer rowNo, LocationEntity entity){
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("rowNo", rowNo);
        data.put("code", entity.getPlaceCd());
        data.put("name", entity.getPlaceNm());
        data.put("updatedBy", entity.getUpdatedBy());
        data.put("updatedDate", Objects.isNull(entity.getModifiedDatetime()) ? DateTimeUtil.convertDateToddMMyyyyHHmmss(entity.getCreatedDatetime()) : DateTimeUtil.convertDateToddMMyyyyHHmmss(entity.getModifiedDatetime()));
        return data;
    }
    public Map<String, Object> getLocation(String locationCode, UserInfo userInfo) throws ServiceException {
        try {
            LocationEntity entity = locationDao.findByLocation(locationCode);
            return mapLocationResult(1, entity);
        } catch (Exception ex) {
            log.error(Util.logErrorMsg(this.getClass().getName(), ex.getMessage()));
            throw new ServiceException(ApiStatus.STATUS_INTERNAL_SERVER_ERROR, message.getInternalErrorMessage(userInfo.getLocale()));
        }
    }
    public Map<String, Object> save(LocationModel data, UserInfo userInfo) throws ServiceException{
         try {
            data.setSerialNo(1);
            data.setEffectDate(config.getEffectDate());
            data.setUpdatedBy(userInfo.getUserName());
            data.setUpdatedDate(new Date());
            data.setSystemId(this.getClass().getName() + ".save");
            locationDao.insert(data);

            return getLocation(data.getCode(), userInfo);
        } catch (Exception ex){
            log.error(this.getClass().getName() + ".save", ex);
            throw new ServiceException(ApiStatus.STATUS_INTERNAL_SERVER_ERROR, message.getInternalErrorMessage(userInfo.getLocale()));
        }
    }
}
