package domain;

public class Doctor implements Identifiable<Integer> {
    private Integer id;
    private final String name;
    private final String speciality;
    private final String address;
    private final String phone;

    Doctor(int id, String name, String speciality, String address, String phone) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String toString(){
        return "Doctor id=" + id + ", name=" + name + ", specialty: " + speciality+ ", address=" + address + ", phone=" + phone;
    }

    @Override
    public Integer getID() {
        return id;
    }

}
