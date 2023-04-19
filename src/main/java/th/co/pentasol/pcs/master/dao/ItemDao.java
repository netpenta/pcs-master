package th.co.pentasol.pcs.master.dao;

import th.co.pentasol.pcs.master.entity.ItemEntity;
import th.co.pentasol.pcs.master.entity.ItemLocationEntity;
import th.co.pentasol.pcs.master.entity.ItemProcessEntity;
import th.co.pentasol.pcs.master.model.ItemLocationModel;
import th.co.pentasol.pcs.master.model.ItemModel;
import th.co.pentasol.pcs.master.model.ItemProcessModel;
import th.co.pentasol.pcs.master.model.UserInfo;
import th.co.pentasol.pcs.master.model.filter.ItemFilter;

import java.util.List;
import java.util.Map;

public interface ItemDao {
    ItemEntity findOneItemByCondition(String itemCustCode, String itemCode, String itemRevNo);
    ItemProcessEntity findOneItemProcessByCondition(String itemCustCode, String itemCode, String itemRevNo);
    ItemLocationEntity findOneItemLocationByCondition(String itemCustCode, String itemCode, String itemRevNo);
    Long rowCountByCondition(ItemFilter filter);
    Long rowCountDataDeleted(ItemFilter filter);
    List<ItemEntity> findAllByCondition(ItemFilter filter);
    Map<String, Integer> save(ItemModel data, ItemProcessModel process, ItemLocationModel location);
    Map<String, Integer> delete(ItemModel data);
    Map<String, Integer> restore(ItemModel data);
    Map<String, Integer> update(ItemModel data, UserInfo userInfo);
}
