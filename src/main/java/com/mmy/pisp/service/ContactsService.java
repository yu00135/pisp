package com.mmy.pisp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmy.pisp.entity.Contacts;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
public interface ContactsService extends IService<Contacts> {

    List<Contacts> getContactsByUserId();

    Page<Contacts> getContactsPageByUserId(int currentPage, int pageSize);

    int addContact(Contacts contact);

    int editContact(Contacts contact);

    int deleteContactByContactId(Integer contactId);

    Page<Contacts> searchContacts(int currentPage, int pageSize,String contactName,String contactGender,String contactPhone,String contactEmail,String contactAddress);
}
