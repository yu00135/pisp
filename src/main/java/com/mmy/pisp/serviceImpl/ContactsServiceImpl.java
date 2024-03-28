package com.mmy.pisp.serviceImpl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mmy.pisp.entity.Contacts;
import com.mmy.pisp.mapper.ContactsMapper;
import com.mmy.pisp.service.ContactsService;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 马鸣宇
 * @since 2022-01-17
 */
@Service
public class ContactsServiceImpl extends ServiceImpl<ContactsMapper, Contacts> implements ContactsService {

    private final ContactsMapper contactsMapper;

    @Autowired
    public ContactsServiceImpl(ContactsMapper contactsMapper){ this.contactsMapper=contactsMapper; }

    @Override
    public List<Contacts> getContactsByUserId(){
        Integer userId= StpUtil.getLoginIdAsInt();
        return contactsMapper.selectList(new QueryWrapper<Contacts>().eq("user_id",userId));
    }

    @Override
    public Page<Contacts> getContactsPageByUserId(int currentPage, int pageSize){
        Integer userId= StpUtil.getLoginIdAsInt();
        Page<Contacts> page=new Page<>(currentPage,pageSize);
        contactsMapper.selectPage(page,new QueryWrapper<Contacts>().eq("user_id",userId));
        return page;
    }

    @Override
    public int addContact(Contacts contact){
        return contactsMapper.insert(contact);
    }

    @Override
    public int editContact(Contacts contact){return contactsMapper.updateById(contact);}

    @Override
    public int deleteContactByContactId(Integer contactId){ return contactsMapper.deleteById(contactId); }

    @Override
    public Page<Contacts> searchContacts(int currentPage, int pageSize,String contactName, String contactGender, String contactPhone, String contactEmail,String contactAddress) {
        Integer userId= StpUtil.getLoginIdAsInt();
        QueryWrapper<Contacts> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        if(!StringUtil.isNullOrEmpty(contactName)){
            qw.like("contact_name",contactName);
        }
        if(!StringUtil.isNullOrEmpty(contactGender)){
            qw.like("contact_gender",contactGender);
        }
        if(!StringUtil.isNullOrEmpty(contactPhone)){
            qw.like("contact_phone",contactPhone);
        }
        if(!StringUtil.isNullOrEmpty(contactEmail)){
            qw.like("contact_email",contactEmail);
        }
        if(!StringUtil.isNullOrEmpty(contactAddress)){
            qw.like("contact_address",contactAddress);
        }
        Page<Contacts> page=new Page<>(currentPage,pageSize);
        contactsMapper.selectPage(page,qw);
        return page;
    }


}
