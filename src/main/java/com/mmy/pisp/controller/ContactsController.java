package com.mmy.pisp.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmy.pisp.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mmy.pisp.entity.Contacts;
import com.mmy.pisp.service.ContactsService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
@Api(tags="Contact API接口文档")
@RestController
@RequestMapping("/contact")
public class ContactsController {

    private final ContactsService contactsService;

    @Autowired
    public ContactsController(ContactsService contactsService){ this.contactsService=contactsService; }

    @PostMapping("/getList")
    public ResultVO getContactList() {
        List<Contacts> contacts=contactsService.getContactsByUserId();
        return ResultVO.success(200, "获取通讯录列表成功", contacts);
    }

    @ApiOperation(value = "分页查找联系人列表")
    @PostMapping("/getPage")
    public ResultVO getContactPage(@RequestBody Map<String,Object> map) {
        int currentPage=(int) map.get("currentPage");
        int pageSize=(int) map.get("pageSize");
        Page<Contacts> contacts=contactsService.getContactsPageByUserId(currentPage,pageSize);
        return ResultVO.success(200, "获取通讯录列表成功", contacts);
    }


    @ApiOperation(value = "批量删除选中联系人")
    @PostMapping("/deleteSelected")
    public ResultVO deleteSelectedContact(@RequestBody List<Integer> list){
        if(list.isEmpty()){
            return ResultVO.fail(101,"请先选择要删除的联系人",null);
        }
        for(Integer i:list){
            if(contactsService.deleteContactByContactId(i)<0){
                return ResultVO.fail(201,"删除联系人失败",null);
            }
        }
        return ResultVO.success(200,"删除联系人成功",null);
    }

    @ApiOperation(value = "删除联系人")
    @PostMapping("/delete")
    public ResultVO deleteContact(@RequestBody Map<String, Object> map){
        Integer contactId=(Integer) map.get("contactId");
        System.out.println(contactId);
        if(contactsService.deleteContactByContactId(contactId)>0){
            return ResultVO.success(200,"删除联系人成功",null);
        }else {
            return ResultVO.fail(201,"删除联系人失败",null);
        }
    }


    @ApiOperation(value = "添加联系人")
    @PostMapping("/add")
    public ResultVO addSite(@RequestBody Map<String, Object> map){
        Integer userId=StpUtil.getLoginIdAsInt();
        String contactName=(String) map.get("contactName");
        String contactGender=(String) map.get("contactGender");
        String contactEmail=(String) map.get("contactEmail");
        String contactPhone=(String) map.get("contactPhone");
        String contactAddress=(String) map.get("contactAddress");
        Contacts contact=new Contacts();
        contact.setContactName(contactName);
        contact.setContactGender(contactGender);
        contact.setContactEmail(contactEmail);
        contact.setContactPhone(contactPhone);
        contact.setContactAddress(contactAddress);
        contact.setUserId(userId);
        if(contactsService.addContact(contact)>0){
            return ResultVO.success(200,"添加联系人成功",null);
        }else {
            return ResultVO.fail(201,"添加联系人失败",null);
        }
    }

    @ApiOperation(value = "更改联系人信息")
    @PostMapping("/edit")
    public ResultVO editSite(@RequestBody Map<String, Object> map){
        Integer userId=StpUtil.getLoginIdAsInt();
        Integer contactId=(Integer) map.get("contactId");
        String contactName=(String) map.get("contactName");
        String contactGender=(String) map.get("contactGender");
        String contactEmail=(String) map.get("contactEmail");
        String contactPhone=(String) map.get("contactPhone");
        String contactAddress=(String) map.get("contactAddress");
        Contacts contact=new Contacts();
        contact.setContactId(contactId);
        contact.setContactName(contactName);
        contact.setContactGender(contactGender);
        contact.setContactEmail(contactEmail);
        contact.setContactPhone(contactPhone);
        contact.setContactAddress(contactAddress);
        contact.setUserId(userId);
        if(contactsService.editContact(contact)>0){
            return ResultVO.success(200,"修改联系人成功",null);
        }else {
            return ResultVO.fail(201,"修改联系人失败",null);
        }
    }


    @ApiOperation(value = "查找联系人")
    @PostMapping("/search")
    public ResultVO search(@RequestBody Map<String,Object> map) {
        String contactName=(String) map.get("contactName");
        String contactGender=(String) map.get("contactGender");
        String contactEmail=(String) map.get("contactEmail");
        String contactPhone=(String) map.get("contactPhone");
        String contactAddress=(String) map.get("contactAddress");
        int currentPage=(int) map.get("currentPage");
        int pageSize=(int) map.get("pageSize");
        Page<Contacts> contacts=contactsService.searchContacts(currentPage,pageSize,contactName,contactGender,contactEmail,contactPhone,contactAddress);
        return ResultVO.success(200, "查找成功", contacts);
    }

}
