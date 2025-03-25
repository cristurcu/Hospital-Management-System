package domain;

import java.io.Serializable;


public class Patient implements Identifiable<Integer> , Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id = -1;
    private String name = "";
    private String address = "";
    private String phone = "";


    public Patient(int id, String name, String address, String phone){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
    public Patient() {}

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String toString() {
        return "Patient id=" + id + ", name=" + name + ", address=" + address + ", phone=" + phone;
    }

    @Override
    public Integer getID() {
        return id;
    }


}
