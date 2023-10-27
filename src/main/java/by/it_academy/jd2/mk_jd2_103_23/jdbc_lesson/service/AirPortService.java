package by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.service;

import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.core.dto.AirPort;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.api.IAirPortsDAO;
import by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.service.api.IAirPortService;

import java.util.List;

public class AirPortService implements IAirPortService {
    private final IAirPortsDAO dao;

    public AirPortService(IAirPortsDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<AirPort> getAll() {
        return this.dao.getAirPortsList();
    }
}
