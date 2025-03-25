package repositories.database;

import domain.Patient;
import repositories.base.IRepository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PatientDatabaseRepository implements IRepository<Integer, Patient> {
    private final String url = "jdbc:sqlite:/Users/criss/Documents/Semester 3/MAP/labMAP/DB/mydatabase.db";

    public PatientDatabaseRepository() {}

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    @Override
    public void add(Integer id, Patient entity) {
        String sql = "INSERT INTO patients (id, name, address, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getAddress());
            ps.setString(4, entity.getPhone());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding patient to database", e);
        }
    }

    @Override
    public Optional<Patient> delete(Integer id) {
        String selectSql = "SELECT * FROM patients WHERE id = ?";
        String deleteSql = "DELETE FROM patients WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement selectPs = conn.prepareStatement(selectSql)) {

            selectPs.setInt(1, id);
            try (ResultSet rs = selectPs.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    String phone = rs.getString("phone");

                    Patient patient = new Patient(id, name, address, phone);

                    try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                        deletePs.setInt(1, id);
                        deletePs.executeUpdate();
                    }
                    return Optional.of(patient);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting patient from database", e);
        }
        return Optional.empty();
    }


    @Override
    public void modify(Integer id, Patient entity) {
        String sql = "UPDATE patients SET name = ?, address = ?, phone = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getAddress());
            ps.setString(3, entity.getPhone());
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error modifying patient in database", e);
        }
    }

    @Override
    public Optional<Patient> findById(Integer id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM patients WHERE id = ?")) {
            System.out.println("Executing findById for ID: " + id);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Patient found.");
                return Optional.of(new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error in findById: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }



    @Override
    public Map<Integer, Patient> getAll() {
        String sql = "SELECT * FROM patients";
        Map<Integer, Patient> patients = new HashMap<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient patient = new Patient(rs.getInt("id"), rs.getString("name"), rs.getString("address"), rs.getString("phone"));
                patients.put(patient.getID(), patient);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all patients from database", e);
        }
        return patients;
    }
}
