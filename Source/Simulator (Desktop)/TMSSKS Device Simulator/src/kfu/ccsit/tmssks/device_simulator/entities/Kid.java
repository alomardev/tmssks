package kfu.ccsit.tmssks.device_simulator.entities;

public class Kid {

    private String nid;
    private String name;

    public String getNationalId() {
        return nid;
    }

    public void setNationalId(String nid) {
        this.nid = nid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Kid(String nid, String name) {
        this.nid = nid;
        this.name = name;
    }

    @Override
    public String toString() {
        return getNationalId() + " - " + getName();
    }
    
    

}
