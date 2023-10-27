package by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao;

import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.core.dto.AirPort;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.api.IAirPortsDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirPortsDAO implements IAirPortsDAO {
    private final static String SQL_QUERY_GET_ALL_AirPorts = "select airport_code,\n" +
            "       airport_name,\n" +
            "       city,\n" +
            "       coordinates,\n" +
            "       timezone\n" +
            "from airports_data;";
    private final DataSource dataSource;

    public AirPortsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<AirPort> getAirPortsList() {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_QUERY_GET_ALL_AirPorts);
             ResultSet resultSet = statement.executeQuery()) {

            List<AirPort> airPortList = new ArrayList<>();

            while (resultSet.next()) {
                AirPort airPort = new AirPort();
                airPort.setAirportCode(resultSet.getString("airport_code"));
                airPort.setAirportName(resultSet.getString("airport_name"));
                airPort.setCity(resultSet.getString("city"));
                airPort.setCoordinates(resultSet.getString("coordinates"));
                airPort.setTimeZone(resultSet.getString("timezone"));
                airPortList.add(airPort);
            }
            return airPortList;

        } catch (SQLException e) {
            throw new IllegalStateException("Error getting AirPort information", e);
        }
    }
}
