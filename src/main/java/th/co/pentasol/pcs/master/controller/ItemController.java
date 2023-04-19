package th.co.pentasol.pcs.master.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.pentasol.pcs.master.component.Message;
import th.co.pentasol.pcs.master.exception.ServiceException;
import th.co.pentasol.pcs.master.model.*;
import th.co.pentasol.pcs.master.model.api.ApiResponse;
import th.co.pentasol.pcs.master.model.filter.CustomerFilter;
import th.co.pentasol.pcs.master.model.filter.ItemFilter;
import th.co.pentasol.pcs.master.service.ItemService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/item")
public class ItemController extends AbsController{
    @Autowired
    ItemService itemService;
    @Autowired
    Message message;
    public ItemController(ItemService itemService) {this.itemService = itemService ;}
    @GetMapping(value = "/{itemCustCode}/{itemCode}/{itemRevNo}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> view(HttpServletRequest request, @RequestParam(value = "itemCustCode", required = false) String itemCustCode, @RequestParam(value = "itemCode") String itemCode, @RequestParam(value = "itemRevNo") String itemRevNo) throws ServiceException{
        UserInfo userInfo = userService.getUserInfo(request);
        return responseOK(itemService.getItemByCode(itemCustCode, itemCode, itemRevNo, userInfo));
    }
    @PostMapping(value = "/search", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> search(HttpServletRequest request, @RequestBody ItemFilter filter) throws  ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        Long rowCount = itemService.getRowCountByCondition(filter, userInfo);
        if(rowCount > 0){
            List<ItemListModel> dataList = itemService.getItemListByCondition(filter, userInfo);
            return responseOK(dataList, rowCount, filter.getPageNo(), filter.getPageSize());
        }
        return responseOK();
    }
    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> create(HttpServletRequest request, @Valid @RequestBody ItemModel data) throws ServiceException {
        UserInfo userInfo = userService.getUserInfo(request);
        ItemFilter filter = new ItemFilter();
        filter.setItemCustCode(data.getItemCustCode());
        filter.setItemCode(data.getItemCode());
        filter.setItemRevNo("0");
        Long rowCount = itemService.getRowCountByCondition(filter, userInfo);
        Boolean restored = itemService.foundDataDeleted(data, userInfo);
        if(rowCount > 0){
            return responseOK(message.getMsgDuplicateName(userInfo.getLocale()));
        }else {
            if(restored){
                System.out.println("---- DISPLAY BUTTON RESTORE");
                return responseOK(message.getRestoreMessage(userInfo.getLocale())); // return button
            }else{
                System.out.println("----- SAVE");
                return responseOK(itemService.save(data, userInfo), message.getSavedMessage(userInfo.getLocale()));
            }
        }
    }
    @GetMapping(value = "/delete/{itemCustCode}/{itemCode}/{itemRevNo}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> delete(HttpServletRequest request , @RequestParam(value = "itemCustCode", required = false) String itemCustCode, @RequestParam(value = "itemCode") String itemCode, @RequestParam(value = "itemRevNo") String itemRevNo) throws ServiceException{
        UserInfo userInfo = userService.getUserInfo(request);
        ItemModel data = new ItemModel();
        data.setItemCustCode(itemCustCode != null ? itemCustCode:"");
        data.setItemCode(itemCode);
        data.setItemRevNo(itemRevNo);
        return responseOK(itemService.delete(data, userInfo));
    }
    @PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> deleteList(HttpServletRequest request, @RequestBody List<ItemListModel> dataList) throws ServiceException{
        System.out.println("----- TEST DELETE LIST -----");
        System.out.println(dataList.size());
        return responseOK();
    }
    @GetMapping(value = "/restore", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> restore(HttpServletRequest request, @RequestParam(value = "itemCustCode", required = false) String itemCustCode, @RequestParam(value = "itemCode") String itemCode, @RequestParam(value = "itemRevNo") String itemRevNo) throws ServiceException{
        UserInfo userInfo = userService.getUserInfo(request);
        ItemModel data = new ItemModel();
        data.setItemCustCode(itemCustCode != null ? itemCustCode:"");
        data.setItemCode(itemCode);
        data.setItemRevNo(itemRevNo);
        return responseOK(itemService.restore(data, userInfo));
    }
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> update(HttpServletRequest request, @RequestBody ItemModel data) throws ServiceException{
        UserInfo userInfo = userService.getUserInfo(request);
        return responseOK(itemService.update(data, userInfo));
    }

    @PostMapping(value = "/copy/{itemCustCode}/{itemCode}/{itemRevNo}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<ApiResponse> copy(HttpServletRequest request, @RequestParam(value = "itemCustCode", required = false) String itemCustCode, @RequestParam(value = "itemCode") String itemCode, @RequestParam(value = "itemRevNo") String itemRevNo) throws ServiceException{
        UserInfo userInfo = userService.getUserInfo(request);
        return responseOK(itemService.getItemCopyByCode(itemCustCode, itemCode, itemRevNo, userInfo));
    }
}
