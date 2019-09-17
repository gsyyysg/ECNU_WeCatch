package com.baidu.ar.pro.Task;

public class PoiSearchResults {

    private String mname;
    private String maddress;
    private Double mlatitude;
    private Double mlongitude;

    public String getMname() {
        return mname;
    }
    public void setMname(String mname) {
        this.mname = mname;
    }
    public String getMaddress() {
        return maddress;
    }
    public void setMaddress(String maddress) {
        this.maddress = maddress;
    }
    public Double getMlatitude() {
        return mlatitude;
    }
    public void setMlatitude(Double mlatitude) {
        this.mlatitude = mlatitude;
    }
    public Double getMlongitude() {
        return mlongitude;
    }
    public void setMlongitude(Double mlongitude) {
        this.mlongitude = mlongitude;
    }

    public PoiSearchResults(String mname, String maddress, Double mlatitude, Double mlongitude){
        super();
        this.mname = mname;
        this.maddress = maddress;
        this.mlatitude = mlatitude;
        this.mlongitude = mlongitude;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
