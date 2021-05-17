package main.model;

import main.DBUtils;
import main.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ChooseDSUpdateEmpModel {
    //Connection connection;

    //public ChooseDSUpdateEmpModel() {
    //    connection = SQLConnection.connect();
    //    if (connection == null)
    //        System.exit(1);
    //}

    public boolean isSeatAlreadyBookedInThatDate(int seatId, String date) throws SQLException {
        Connection connection;
        connection = SQLConnection.connect();
        String query = "select number from Booking where seat_id=? and date=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, seatId);
            preparedStatement.setString(2, date);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closePrepareStatement(preparedStatement);
            DBUtils.closeConnection(connection);
            //preparedStatement.close();
            //resultSet.close();
        }
    }

    public boolean isSeatIdBookedByUserPrevious(int seatId, int empId, String date) throws SQLException {
        Connection connection;
        connection = SQLConnection.connect();
        String query = "select id from Whitelist where seat_id=? and employee_id=? and is_locked=true and date=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, seatId);
            preparedStatement.setInt(2, empId);
            preparedStatement.setString(3, date);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = true;
            }

        } catch (Exception e) {
            return false;
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closePrepareStatement(preparedStatement);
            DBUtils.closeConnection(connection);
            //preparedStatement.close();
            //resultSet.close();
        }
        return result;
    }

    public boolean isSeatLockedDown(int seatId) throws SQLException {
        Connection connection;
        connection = SQLConnection.connect();
        String query = "select id from Seat where id=? and is_locked=true";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, seatId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closePrepareStatement(preparedStatement);
            DBUtils.closeConnection(connection);
            //preparedStatement.close();
            //resultSet.close();
        }
        return result;
    }

    public boolean updateBooking(String date, int seatId, int empId, String oldDate) throws SQLException {
        Connection connection;
        connection = SQLConnection.connect();
        String query = "update Booking set date=?,seat_id=?,has_confirmed=false where employee_id=? and date=?";
        PreparedStatement prst = null;
        boolean result = false;
        try {
            prst = connection.prepareStatement(query); // PS to SQL statement
            prst.setString(1, date);
            prst.setInt(2, seatId);
            prst.setInt(3, empId);
            prst.setString(4, oldDate);

            result = prst.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        } finally {
            DBUtils.closePrepareStatement(prst);
            DBUtils.closeConnection(connection);
            //prst.close();
        }
        return result;
    }

    /**
     * IMPORTANT if update whitelist you need to pass the old date+1day
     * whitelist need to consider all date +1 !!!
     * all operation related to date in whitelist will go with + 1 day
     */
    public boolean updateWhitelist(int seatId, String date, int empId, String oldDate) throws SQLException {
        Connection connection;
        connection = SQLConnection.connect();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate dateConverted = LocalDate.parse(date, formatter);
        dateConverted = dateConverted.plusDays(1);//add one day
        String dateString = dateConverted.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate dateConverted2 = LocalDate.parse(oldDate, formatter2);
        dateConverted2 = dateConverted2.plusDays(1);//add one day
        String newOldDate = dateConverted2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String query = "update Whitelist set seat_id=?,date=? where employee_id=? and date=?";
        PreparedStatement prst = null;
        boolean result = false;
        try {
            prst = connection.prepareStatement(query);
            prst.setInt(1, seatId);
            prst.setString(2, dateString);
            prst.setInt(3, empId);
            prst.setString(4, newOldDate);

            result = prst.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        } finally {
            DBUtils.closePrepareStatement(prst);
            DBUtils.closeConnection(connection);
            //prst.close();
        }
        return result;
    }
}
