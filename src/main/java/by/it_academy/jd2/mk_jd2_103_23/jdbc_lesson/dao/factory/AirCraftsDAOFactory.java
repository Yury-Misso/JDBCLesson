package by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.factory;

import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.AirCraftsDAO;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.api.IAirCraftsDAO;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.data_source.DBDataSource;

public class AirCraftsDAOFactory {
    private static volatile IAirCraftsDAO instance;

    private AirCraftsDAOFactory() {
    }

    public static IAirCraftsDAO getInstance() {
        if (instance == null) {
            synchronized (AirCraftsDAOFactory.class) {
                if (instance == null) {
                    instance = new AirCraftsDAO((DBDataSource.getInstance()));
                }
            }
        }
        return instance;
    }
}
