package by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao;

import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.core.dto.Aircraft;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.api.IAirCraftsDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AirCraftsDAO implements IAirCraftsDAO {
    private static final String SQL_QUERY_GET_ALL_AirCrafts = "SELECT aircraft_code,model,range FROM aircrafts_data;";
    private final DataSource dataSource;

    public AirCraftsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Aircraft> getAirCraftsList() {

        List<Aircraft> aircraftEntityList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(SQL_QUERY_GET_ALL_AirCrafts);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                aircraftEntityList.add(new Aircraft(rs.getString("aircraft_code"),
                        rs.getString("model"),
                        rs.getInt("range")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error getting aircraft information", e);
        }
        return aircraftEntityList;
    }
}
