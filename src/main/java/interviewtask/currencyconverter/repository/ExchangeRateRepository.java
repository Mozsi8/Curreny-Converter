package interviewtask.currencyconverter.repository;

import interviewtask.currencyconverter.currency_converter_tool.Currency;
import interviewtask.currencyconverter.model.ExchangeRate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findByCurrencyFromAndCurrencyTo(Currency from, Currency to);

    List<ExchangeRate> findAllByCurrencyFrom(Currency from);
}
