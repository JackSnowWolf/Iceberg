package com.iceberg.entity;

public class Privilege {
    private Integer ID;
    private String privilegeNumber;
    private String privilegeName;

    public Privilege() {

    }

    public Integer getID() {
        return this.ID;
    }

    public void setID(Integer iD) {
        this.ID = iD;
    }

    public String getPrivilegeNumber() {
        return this.privilegeNumber;
    }

    public void setPrivilegeNumber(String privilegeNumber) {
        this.privilegeNumber = privilegeNumber;
    }

    public String getPrivilegeName() {
        return this.privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }
}
