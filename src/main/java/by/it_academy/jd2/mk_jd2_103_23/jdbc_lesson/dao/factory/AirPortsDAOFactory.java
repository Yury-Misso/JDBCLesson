package by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.factory;

import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.AirPortsDAO;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.api.IAirPortsDAO;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.data_source.DBDataSource;

public class AirPortsDAOFactory {
    private static volatile IAirPortsDAO instance;

    private AirPortsDAOFactory() {
    }

    public static IAirPortsDAO getInstance() {
        if (instance == null) {
            synchronized (AirPortsDAOFactory.class) {
                if (instance == null) {
                    instance = new AirPortsDAO((DBDataSource.getInstance()));
                }
            }
        }
        return instance;
    }
}
