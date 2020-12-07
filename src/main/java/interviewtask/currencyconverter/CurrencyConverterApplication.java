package interviewtask.currencyconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The CurrencyConverter
 *
 * @author Dániel Mozsolics
 * @version 1.2.8
 * @since 2020-11-17
 */
@SpringBootApplication
public class CurrencyConverterApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyConverterApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application;
    }
}
