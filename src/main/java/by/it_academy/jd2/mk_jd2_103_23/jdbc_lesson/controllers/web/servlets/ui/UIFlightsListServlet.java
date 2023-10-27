package by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.controllers.web.servlets.ui;

import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.core.dto.Flight;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.core.dto.FlightFilters;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.core.dto.PageParam;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.service.api.IFlightsService;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.service.factory.FlightsServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "FlightsListServlet", urlPatterns = "/flightslist")
public class UIFlightsListServlet extends HttpServlet {

    private final IFlightsService service;

    public UIFlightsListServlet() {
        this.service = FlightsServiceFactory.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int pageNo = 1;
        int sizePage = 50;
        int maxPage = (int) Math.ceil((double) service.getCount() / sizePage);

        PageParam pageParam = new PageParam(pageNo, sizePage, maxPage);
        FlightFilters flightFilters = null;

        List<Flight> flightsList = this.service.getPage(pageParam, flightFilters);

        req.setAttribute("pageParam", pageParam);
        req.setAttribute("flightFilters", flightFilters);
        req.setAttribute("flights", flightsList);
        req.getRequestDispatcher("/view/flightslist.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PageParam pageParam = getPageParam(req);
        FlightFilters flightFilters = getFlightFilters(req);

        req.setAttribute("pageParam", pageParam);
        req.setAttribute("flightFilters", flightFilters);
        List<Flight> flightsList = this.service.getPage(pageParam, flightFilters);
        req.setAttribute("flights", flightsList);
        req.getRequestDispatcher("/view/flightslist.jsp").forward(req, resp);
    }

    private PageParam getPageParam(HttpServletRequest req) {
        PageParam pageParam = new PageParam();

        int sizePage = Integer.parseInt(req.getParameter("pageSize"));
        pageParam.setSizePage(sizePage);

        String pageNoRaw = req.getParameter("pageNo");
        int maxPage = (int) Math.ceil((double) service.getCount() / sizePage);
        int pageNo = 0;

        try {
            pageNo = Integer.parseInt(pageNoRaw);
        } catch (NumberFormatException e) {
            String[] split = pageNoRaw.split("#");
            try {
                int pageNoOld = Integer.parseInt(split[1]);
                if (split[0].equalsIgnoreCase("prev")) {
                    pageNo = pageNoOld - 1;
                }
                if (split[0].equalsIgnoreCase("next")) {
                    pageNo = pageNoOld + 1;
                }
                if (pageNo < 1) {
                    pageNo = 1;
                } else if (pageNo > maxPage) {
                    pageNo = maxPage;
                }
            } catch (NumberFormatException ee) {
                throw new RuntimeException("Error parse pageNo", ee);
            }
        }

        pageParam.setPageNo(pageNo);
        pageParam.setMaxPage(maxPage);

        return pageParam;
    }

    private FlightFilters getFlightFilters(HttpServletRequest req) {
        FlightFilters flightFilters = new FlightFilters();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            String flightId = req.getParameter("flightId");
            if (flightId != null && !flightId.isBlank()) {
                flightFilters.setFlightId(Integer.parseInt(flightId));
            } else {
                flightFilters.setFlightId(null);
            }

            String flightNo = req.getParameter("flightNo");
            if (flightNo != null && !flightNo.isBlank()) {
                flightFilters.setFlightNo(flightNo);
            } else {
                flightFilters.setFlightNo(null);
            }

            String scheduledDeparture = req.getParameter("scheduledDeparture");
            if (scheduledDeparture != null && !scheduledDeparture.isBlank()) {
                flightFilters.setScheduledDeparture(LocalDate.parse(
                        scheduledDeparture, dateFormatter));
            } else {
                flightFilters.setScheduledDeparture(null);
            }

            String scheduledArrival = req.getParameter("scheduledArrival");
            if (scheduledArrival != null && !scheduledArrival.isBlank()) {
                flightFilters.setScheduledArrival(LocalDate.parse(
                        scheduledArrival, dateFormatter));
            } else {
                flightFilters.setScheduledArrival(null);
            }

            String departureAirport = req.getParameter("departureAirport");
            if (departureAirport != null && !departureAirport.isBlank()) {
                flightFilters.setDepartureAirport(departureAirport);
            } else {
                flightFilters.setDepartureAirport(null);
            }

            String arrivalAirport = req.getParameter("arrivalAirport");
            if (arrivalAirport != null && !arrivalAirport.isBlank()) {
                flightFilters.setArrivalAirport(arrivalAirport);
            } else {
                flightFilters.setArrivalAirport(null);
            }

            String status = req.getParameter("status");
            if (status != null && !status.isBlank()) {
                flightFilters.setStatus(status);
            } else {
                flightFilters.setStatus(null);
            }

            String aircraftCode = req.getParameter("aircraftCode");
            if (aircraftCode != null && !aircraftCode.isBlank()) {
                flightFilters.setAircraftCode(aircraftCode);
            } else {
                flightFilters.setAircraftCode(null);
            }

            String actualDeparture = req.getParameter("actualDeparture");
            if (actualDeparture != null && !actualDeparture.isBlank()) {
                flightFilters.setActualDeparture(
                        LocalDate.parse(actualDeparture, dateFormatter));
            } else {
                flightFilters.setActualDeparture(null);
            }

            String actualArrival = req.getParameter("actualArrival");
            if (actualArrival != null && !actualArrival.isBlank()) {
                flightFilters.setActualArrival(
                        LocalDate.parse(actualArrival, dateFormatter));
            } else {
                flightFilters.setActualArrival(null);
            }

            return flightFilters;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating flight_filter", e);
        }
    }
}
