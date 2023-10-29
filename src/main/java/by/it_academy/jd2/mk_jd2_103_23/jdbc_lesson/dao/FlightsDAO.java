package by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao;

import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.core.dto.Flight;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.core.dto.FlightFilters;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.core.dto.PageParam;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.api.IFlightsDAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class FlightsDAO implements IFlightsDAO {

    private final static String SQL_QUERY_GET_ALL_FLIGHTS = "select flight_id,\n" +
            "       flight_no,\n" +
            "       scheduled_departure,\n" +
            "       scheduled_arrival,\n" +
            "       departure_airport,\n" +
            "       arrival_airport,\n" +
            "       status,\n" +
            "       aircraft_code,\n" +
            "       actual_departure,\n" +
            "       actual_arrival\n" +
            "from flights\n";

    private final static String SQL_QUERY_GET_COUNT = "SELECT count(flight_id) as count\n" +
            "FROM flights;";
    private final DataSource dataSource;

    public FlightsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Flight> getFlightsList() {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_QUERY_GET_ALL_FLIGHTS);
             ResultSet resultSet = statement.executeQuery()) {
            List<Flight> flightList = new ArrayList<>();
            while (resultSet.next()) {
                flightList.add(getFlightByResultSet(resultSet));
            }
            return flightList;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting list flights", e);
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_QUERY_GET_COUNT);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error getting count of flights");
        }


        return count;
    }

    @Override
    public List<Flight> getFlightsList(PageParam pageParam, FlightFilters flightFilters) {

        StringBuilder sql = new StringBuilder();

        StringJoiner sqlFilterBuilder = new StringJoiner(" and ",
                " where ", "");

        List<Object> params = new ArrayList<>();

        if (flightFilters != null) {

            if (flightFilters.getFlightId() != null) {
                sqlFilterBuilder.add("flight_id = ?");
                params.add(flightFilters.getFlightId());
            }
            if (flightFilters.getFlightNo() != null) {
                sqlFilterBuilder.add("flight_no = ?");
                params.add(flightFilters.getFlightNo());
            }

            if (flightFilters.getScheduledDeparture() != null) {
                sqlFilterBuilder.add("scheduled_departure >= ? and scheduled_departure < ?");
                params.add(flightFilters.getScheduledDeparture());
                params.add(flightFilters.getScheduledDeparture().plusDays(1));
            }

            if (flightFilters.getScheduledArrival() != null) {
                sqlFilterBuilder.add("scheduled_arrival >= ? and scheduled_arrival < ?");
                params.add(flightFilters.getScheduledArrival());
                params.add(flightFilters.getScheduledArrival().plusDays(1));
            }

            if (flightFilters.getDepartureAirport() != null) {
                sqlFilterBuilder.add("departure_airport = ?");
                params.add(flightFilters.getDepartureAirport());
            }

            if (flightFilters.getArrivalAirport() != null) {
                sqlFilterBuilder.add("arrival_airport = ?");
                params.add(flightFilters.getArrivalAirport());
            }

            if (flightFilters.getStatus() != null) {
                sqlFilterBuilder.add("status = ?");
                params.add(flightFilters.getStatus());
            }

            if (flightFilters.getAircraftCode() != null) {
                sqlFilterBuilder.add("aircraft_code = ?");
                params.add(flightFilters.getAircraftCode());
            }

            if (flightFilters.getActualDeparture() != null) {
                sqlFilterBuilder.add("actual_departure >= ? and actual_departure < ?");
                params.add(flightFilters.getActualDeparture());
                params.add(flightFilters.getActualDeparture().plusDays(1));
            }

            if (flightFilters.getActualArrival() != null) {
                sqlFilterBuilder.add("actual_arrival >= ? and actual_arrival < ?");
                params.add(flightFilters.getActualArrival());
                params.add(flightFilters.getActualArrival().plusDays(1));
            }

        }

        if (!sqlFilterBuilder.toString().equalsIgnoreCase(" where ")) {
            sql.append(SQL_QUERY_GET_ALL_FLIGHTS).append(sqlFilterBuilder);
        } else {
            sql.append(SQL_QUERY_GET_ALL_FLIGHTS);
        }

        sql.append("ORDER BY scheduled_departure");

        int pageNo = pageParam.getPageNo();
        int sizePage = pageParam.getSizePage();

        int offSet;

        StringBuilder sqlCount = new StringBuilder();
        sqlCount.append("select count(flight_id) from (").append(sql).append(") as count");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlCount.toString())) {

            int index = 1;
            for (Object param : params) {
                statement.setObject(index++, param);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int maxItem = resultSet.getInt("count");
                    int maxPage = (int) Math.ceil((double) maxItem / sizePage);
                    pageParam.setMaxPage(maxPage);
                }
            } catch (SQLException e) {
                throw new IllegalStateException("Error getting count", e);
            }
        } catch (SQLException ee) {
            throw new IllegalStateException("Error getting count", ee);
        }


        if (pageParam != null) {
            offSet = (pageNo - 1) * sizePage;

            sql.append(" limit ? offset ?");
            params.add(sizePage);
            params.add(offSet);
        } else {
            throw new IllegalStateException("Error in 'PageParam' data");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object param : params) {
                statement.setObject(index++, param);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Flight> flightList = new ArrayList<>();
                while (resultSet.next()) {
                    flightList.add(getFlightByResultSet(resultSet));
                }
                return flightList;
            } catch (SQLException e) {
                throw new RuntimeException("Error getting list flights", e);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error getting Flight List", e);
        }
    }

    private Flight getFlightByResultSet(ResultSet resultSet) throws SQLException {
        Flight flight = new Flight();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        flight.setFlightId(resultSet.getInt("flight_id"));
        flight.setFlightNo(resultSet.getString("flight_no"));

        try {
            flight.setScheduledDeparture(OffsetDateTime.parse(resultSet.getString("scheduled_departure"), formatter).toLocalDate());
        } catch (RuntimeException e) {
            //Error for parse date because 'scheduled_departure' in database is null
        }

        try {
            flight.setScheduledArrival(OffsetDateTime.parse(resultSet.getString("scheduled_arrival"), formatter).toLocalDate());
        } catch (RuntimeException e) {
            //Error for parse date because 'scheduled_departure' in database is null
        }

        flight.setDepartureAirport(resultSet.getString("departure_airport"));
        flight.setArrivalAirport(resultSet.getString("arrival_airport"));
        flight.setStatus(resultSet.getString("status"));
        flight.setAircraftCode(resultSet.getString("aircraft_code"));

        try {
            flight.setActualDeparture(OffsetDateTime.parse(resultSet.getString("actual_departure"), formatter).toLocalDate());
        } catch (RuntimeException e) {
            //Error for parse date because 'scheduled_departure' in database is null
        }

        try {
            flight.setActualArrival(OffsetDateTime.parse(resultSet.getString("actual_arrival"), formatter).toLocalDate());
        } catch (RuntimeException e) {
            //Error for parse date because 'scheduled_departure' in database is null
        }

        return flight;
    }
}
