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
import th.co.pentasol.pcs.master.model.SelectedModel;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.model.api.ApiMessage;
import th.co.pentasol.pcs.master.model.filter.LocationFilter;
import th.co.pentasol.pcs.master.util.ApiStatus;
import th.co.pentasol.pcs.master.util.DateTimeUtil;
import th.co.pentasol.pcs.master.util.NumberUtil;
import th.co.pentasol.pcs.master.util.Util;

import java.util.*;

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
        // <Column name, value>
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("rowNo", rowNo);
        data.put("code", entity.getPlaceCd());
        data.put("name", entity.getPlaceNm());
        data.put("nameTh", entity.getPlaceThNm());
        data.put("effectDate", entity.getEffectDate());
        data.put("updatedBy", entity.getUserId());
        data.put("updatedDate", Objects.isNull(entity.getModifiedDatetime()) ? DateTimeUtil.convertDateToddMMyyyyHHmmss(entity.getCreatedDatetime()) : DateTimeUtil.convertDateToddMMyyyyHHmmss(entity.getModifiedDatetime()));
        return data;
    }
    public Map<String, Object> getLocation(String locationCode, UserInfo userInfo) throws ServiceException {
        LocationEntity entity = locationDao.findByLocation(locationCode);
        //Same FJM
        return mapLocationResult(1, entity);
    }
    public Map<String, Object> save(LocationModel data, UserInfo userInfo) throws ServiceException{
        if(locationDao.duplicateLocationCode(data)){
            throw new ServiceException(ApiStatus.STATUS_BAD_REQUEST, message.getApiMessageWarning("msg.duplicate.location.code", userInfo.getLocale()));
        }
        data.setSerialNo(1);
        data.setEffectDate(Util.isNotEmpty(data.getEffectDate()) ? data.getEffectDate() : config.getEffectDate().toString());
        data.setUpdatedBy(userInfo.getUserName());
        data.setUpdatedDate(new Date());
        data.setSystemId(this.getClass().getName() + ".save");
        locationDao.insert(data);

        return getLocation(data.getCode(), userInfo);
    }

    public ApiMessage delete(SelectedModel data, UserInfo userInfo) throws ServiceException {
        if(!Objects.isNull(data.getSelectedList()) && data.getSelectedList().size() > 0){
            data.setUserId(userInfo.getUserName());
            data.setModifiedDateTime(new Date());
            data.setProgramId(this.getClass().getName() + ".delete");

            locationDao.delete(data);
        }
        return message.getDeletedMessage(true, userInfo.getLocale());
    }


    public Map<String, Object> update(LocationModel data, UserInfo userInfo) throws ServiceException{
        data.setEffectDate(Util.isNotEmpty(data.getEffectDate()) ? data.getEffectDate() : config.getEffectDate().toString());
        data.setUpdatedBy(userInfo.getUserName());
        data.setUpdatedDate(new Date());
        data.setSystemId(this.getClass().getName() + ".update");
        locationDao.update(data);

        return getLocation(data.getCode(), userInfo);
    }

    public Long getRowCountLocationByCondition(LocationFilter filter, UserInfo userInfo) throws ServiceException {
        return locationDao.rowCountByCondition(filter);
    }

    public List<Map<String, Object>> getLocationListByCondition(LocationFilter filter, UserInfo userInfo) throws ServiceException {
        List<Map<String, Object>> result = new ArrayList<>();
        //Found data with filter
        List<LocationEntity> entityList = locationDao.findByCondition(filter);
        // rowNo = (0*10)+1 = 1 , rowNo = (1*10)+1 = 11
        Integer rowNo = (NumberUtil.convertToInteger(filter.getPageNo()) * NumberUtil.convertToInteger(filter.getPageSize())) + 1;
        for(LocationEntity entity : entityList){
            result.add(mapLocationResult(rowNo, entity));
            rowNo++;
        }
        return result;
    }
    public Map<String, Object> restore(String code, UserInfo userInfo) throws ServiceException {
        LocationModel data = new LocationModel();
        data.setCode(code);
        data.setUpdatedBy(userInfo.getUserName());
        data.setUpdatedDate(new Date());
        data.setSystemId(this.getClass().getName() + ".restore");
        locationDao.restore(data);

        return getLocation(data.getCode(), userInfo);
    }
}
