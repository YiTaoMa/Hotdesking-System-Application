package main.model;

import main.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CancelBookingConfirmModel {
    Connection connection;

    public CancelBookingConfirmModel() {
        connection = SQLConnection.connect();
        if (connection == null)
            System.exit(1);
    }

    public boolean deleteBookingRecord(int empId, String date) throws SQLException {
        String query = "delete from Booking where employee_id=? and date=?";
        PreparedStatement prst = null;
        boolean result = false;
        try {
            prst = connection.prepareStatement(query); // PS to SQL statement
            prst.setInt(1, empId);
            prst.setString(2, date); // why because we check is check the original +1
            result = prst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            prst.close();
        }
        return result;
    }

    public boolean deleteWhitelistRecord(int empId, String date) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate dateConverted = LocalDate.parse(date, formatter);
        dateConverted = dateConverted.plusDays(1);//add one day
        String dateString = dateConverted.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String query = "delete from Whitelist where employee_id=? and date=?";
        PreparedStatement prst = null;
        boolean result = false;
        try {
            prst = connection.prepareStatement(query); // PS to SQL statement
            prst.setInt(1, empId);
            prst.setString(2, dateString);

            result = prst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            prst.close();
        }
        return result;
    }
}
