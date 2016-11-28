package de.tobiasbruns.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import javax.transaction.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class DatabaseITCase {

    @Autowired
    private DataSource dataSource;

    @Test
    public void runQuery() {
        JdbcTemplate templ = new JdbcTemplate(dataSource);
        System.out.println(templ.queryForList("show tables"));
        System.out.println(templ.queryForList("show columns from USER_AUTHORITIES"));
        System.out.println(templ.queryForList("Select * from USER_AUTHORITIES"));

    }
}
