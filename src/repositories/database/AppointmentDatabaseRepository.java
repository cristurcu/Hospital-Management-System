package repositories.database;

import domain.Appointment;
import repositories.base.IRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AppointmentDatabaseRepository implements IRepository<Integer, Appointment> {
    private final String url = "jdbc:sqlite:/Users/criss/Documents/Semester 3/MAP/labMAP/DB/mydatabase.db";

    public AppointmentDatabaseRepository() {}

    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }
    @Override
    public void add(Integer id, Appointment entity) {
        String sql = "INSERT INTO appointments (id, patientId, date) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, entity.getPatientId());
            ps.setString(3, entity.getDate().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding appointment to database", e);
        }
    }

    @Override
    public Optional<Appointment> delete(Integer id) {
        String findSql = "SELECT * FROM appointments WHERE id = ?";
        String deleteSql = "DELETE FROM appointments WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement findPs = conn.prepareStatement(findSql)) {

            findPs.setInt(1, id);
            ResultSet rs = findPs.executeQuery();

            if (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patientId"),
                        LocalDate.parse(rs.getString("date"))  // Fixed here
                );
                try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
                    deletePs.setInt(1, id);
                    deletePs.executeUpdate();
                }
                return Optional.of(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment from database", e);
        }
        return Optional.empty();
    }

    @Override
    public void modify(Integer id, Appointment entity) {
        String sql = "UPDATE appointments SET patientId = ?, date = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getPatientId());
            ps.setString(2, entity.getDate().toString());
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error modifying appointment in database", e);
        }
    }

    @Override
    public Optional<Appointment> findById(Integer id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Appointment appointment = new Appointment(
                            rs.getInt("id"),
                            rs.getInt("patientId"),
                            LocalDate.parse(rs.getString("date"))
                    );
                    return Optional.of(appointment);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment in database", e);
        }
        return Optional.empty();
    }

    @Override
    public Map<Integer, Appointment> getAll() {
        String sql = "SELECT * FROM appointments";
        Map<Integer, Appointment> appointments = new HashMap<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int patientId = rs.getInt("patientId");
                String dateStr = rs.getString("date");
                LocalDate date = LocalDate.parse(dateStr);
                appointments.put(id, new Appointment(id, patientId, date));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all appointments from database", e);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing appointment data", e);
        }
        return appointments;
    }

}
