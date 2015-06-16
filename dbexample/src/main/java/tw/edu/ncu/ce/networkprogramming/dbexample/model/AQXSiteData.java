package tw.edu.ncu.ce.networkprogramming.dbexample.model;

public class AQXSiteData {

    private String SiteName;
    private String SiteEngName;
    private String AreaName;
    private String County;
    private String Township;
    private String SiteAddress;
    private String TWD97Lon;
    private String TWD97Lat;
    private String SiteType;

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getSiteEngName() {
        return SiteEngName;
    }

    public void setSiteEngName(String siteEngName) {
        SiteEngName = siteEngName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getCounty() {
        return County;
    }

    public void setCounty(String county) {
        County = county;
    }

    public String getTownship() {
        return Township;
    }

    public void setTownship(String township) {
        Township = township;
    }

    public String getSiteAddress() {
        return SiteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        SiteAddress = siteAddress;
    }

    public String getTWD97Lon() {
        return TWD97Lon;
    }

    public void setTWD97Lon(String TWD97Lon) {
        this.TWD97Lon = TWD97Lon;
    }

    public String getTWD97Lat() {
        return TWD97Lat;
    }

    public void setTWD97Lat(String TWD97Lat) {
        this.TWD97Lat = TWD97Lat;
    }

    public String getSiteType() {
        return SiteType;
    }

    public void setSiteType(String siteType) {
        SiteType = siteType;
    }




}
