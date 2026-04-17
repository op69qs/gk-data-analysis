package org.seo.model;

public class DataSourceInfoEntity {

    private String ID;
    private String DATASOURCE_NAME;
    private String DRIVERCLASS_NAME;
    private String DATASOURCE_URL;
    private String DATASOURCE_USERNAME;
    private String DATASOURCE_PASSWORD;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDATASOURCE_NAME() {
        return DATASOURCE_NAME;
    }

    public void setDATASOURCE_NAME(String DATASOURCE_NAME) {
        this.DATASOURCE_NAME = DATASOURCE_NAME;
    }

    public String getDRIVERCLASS_NAME() {
        return DRIVERCLASS_NAME;
    }

    public void setDRIVERCLASS_NAME(String DRIVERCLASS_NAME) {
        this.DRIVERCLASS_NAME = DRIVERCLASS_NAME;
    }

    public String getDATASOURCE_URL() {
        return DATASOURCE_URL;
    }

    public void setDATASOURCE_URL(String DATASOURCE_URL) {
        this.DATASOURCE_URL = DATASOURCE_URL;
    }

    public String getDATASOURCE_USERNAME() {
        return DATASOURCE_USERNAME;
    }

    public void setDATASOURCE_USERNAME(String DATASOURCE_USERNAME) {
        this.DATASOURCE_USERNAME = DATASOURCE_USERNAME;
    }

    public String getDATASOURCE_PASSWORD() {
        return DATASOURCE_PASSWORD;
    }

    public void setDATASOURCE_PASSWORD(String DATASOURCE_PASSWORD) {
        this.DATASOURCE_PASSWORD = DATASOURCE_PASSWORD;
    }
}
