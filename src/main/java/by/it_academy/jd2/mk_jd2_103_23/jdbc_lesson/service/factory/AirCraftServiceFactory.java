package by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.service.factory;

import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.factory.AirCraftsDAOFactory;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.service.AirCraftService;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.service.api.IAirCraftService;

public class AirCraftServiceFactory {
    private static volatile IAirCraftService instance;

    private AirCraftServiceFactory() {
    }

    public static IAirCraftService getInstance() {
        if (instance == null) {
            synchronized (AirCraftServiceFactory.class) {
                if (instance == null) {
                    instance = new AirCraftService(AirCraftsDAOFactory.getInstance());
                }
            }
        }
        return instance;
    }
}
