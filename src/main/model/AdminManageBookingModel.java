package main.model;

import main.DBUtils;
import main.SQLConnection;

import java.sql.*;
import java.util.LinkedList;

public class AdminManageBookingModel {
    //Connection connection;
    LinkedList<Integer> seatsLockedByAdmin = new LinkedList<>();
    LinkedList<Integer> seatsBookedByOther = new LinkedList<>();
    //public AdminManageBookingModel() {
    //    connection = SQLConnection.connect();
    //    if (connection == null)
    //        System.exit(1);
    //}
    public LinkedList getSeatIdLockedByAdmin() throws SQLException {
        Connection connection;
        connection = SQLConnection.connect();
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "select id from Seat where is_locked=true";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                seatsLockedByAdmin.add(resultSet.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closeStatement(statement);
            DBUtils.closeConnection(connection);
            //statement.close();
            //resultSet.close();
        }
        return seatsLockedByAdmin;
    }
    public LinkedList getSeatIDBookedByOther(String date) throws SQLException { //the date is what user chosed at the start
        Connection connection;
        connection = SQLConnection.connect();
        String query = "select seat_id from Booking where date=? and is_booked=true";//that date which seat has been booked
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, date);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                seatsBookedByOther.add(resultSet.getInt("seat_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.closeResultSet(resultSet);
            DBUtils.closePrepareStatement(preparedStatement);
            DBUtils.closeConnection(connection);
            //preparedStatement.close();
            //resultSet.close();
        }
        return seatsBookedByOther;
    }
}
