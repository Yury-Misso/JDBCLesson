package by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.factory;

import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.FlightsDAO;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.api.IFlightsDAO;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.data_source.DBDataSource;

public class FlightsDAOFactory {
    private static volatile IFlightsDAO instance;

    private FlightsDAOFactory() {
    }

    public static IFlightsDAO getInstance() {
        if (instance == null) {
            synchronized (FlightsDAOFactory.class) {
                if (instance == null) {
                    instance = new FlightsDAO(DBDataSource.getInstance());
                }
            }
        }
        return instance;
    }
}
