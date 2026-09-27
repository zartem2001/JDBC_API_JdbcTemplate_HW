package ru.netology.jdbc_api_jdbctemplate_hw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private final String sqlQuery;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String allProductsQuery = "SELECT DISTINCT product_name FROM ORDERS";

    @Autowired
    public ProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlQuery = read("query.sql");
    }

    public List<String> getAllProductNames() {
        return jdbcTemplate.query(
                allProductsQuery,
                (rs, rowNum) -> rs.getString("product_name")
        );
    }

    public List<String> getProductName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);

        return jdbcTemplate.query(
                sqlQuery,
                params,
                (rs, rowNum) -> rs.getString("product_name")
        );
    }

    private static String read(String scriptFileName) {
        try (InputStream is = new ClassPathResource(scriptFileName).getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
