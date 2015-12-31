package com.zibilal.model;

import java.util.Date;

/**
 * Created by Bilal on 12/31/2015.
 */
public class ContactModel implements IDataAdapter {

    public String DisplayName;
    public String FirstName;
    public String LastName;
    public String MiddleName;
    public String NickName;
    public String Title;
    public Integer ContactId;
    public Long DeviceContactId;
    public Date CreateDate;

    public ContactModel(){}
}
