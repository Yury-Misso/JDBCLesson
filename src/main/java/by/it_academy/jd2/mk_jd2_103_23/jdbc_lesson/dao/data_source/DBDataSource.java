package by.it_academy.jd2.mk_jd2_103_23.jdbc_lesson.dao.data_source;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DBDataSource {

    private static final HikariConfig config = new HikariConfig("/hikari.properties");
    private static final DataSource ds = new HikariDataSource(config);

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private DBDataSource() {
    }

    public static DataSource getInstance(){
        return ds;
    }
}
