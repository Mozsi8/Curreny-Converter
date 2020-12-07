package interviewtask.currencyconverter.service;

import interviewtask.currencyconverter.currency_converter_tool.Currency;
import interviewtask.currencyconverter.currency_converter_tool.CurrencyToBuyRateSellRate;
import interviewtask.currencyconverter.currency_converter_tool.CurrencyAmountFromTo;
import interviewtask.currencyconverter.model.ExchangeRate;
import interviewtask.currencyconverter.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRateService {

    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public List<CurrencyToBuyRateSellRate> getExchangeRateList(String currencyFrom) {
        List<CurrencyToBuyRateSellRate> currencyToBuyRateSellRate = new ArrayList<>();

        List<ExchangeRate> exchangeRates = findExchangeRateList(currencyFrom);

        if (isExchangeRateListSizeValid(exchangeRates)) {
            for (ExchangeRate exchangeRate : exchangeRates) {
                currencyToBuyRateSellRate.add(new CurrencyToBuyRateSellRate(
                        exchangeRate.getCurrencyTo().getValue(),
                        exchangeRate.getBuyingRate(),
                        exchangeRate.getSellingRate()));
            }
            return currencyToBuyRateSellRate;

        } else {
            return null;
        }
    }

    public CurrencyToBuyRateSellRate getConvertedAmounts(CurrencyAmountFromTo currencyAmountFromTo) {
        CurrencyToBuyRateSellRate currencyToBuyRateSellRate = new CurrencyToBuyRateSellRate();

        if (isCurrencyFromAndToEqual(currencyAmountFromTo)) {
            currencyToBuyRateSellRate.setCurrencyTo(currencyAmountFromTo.getCurrencyTo());
            currencyToBuyRateSellRate.setBuyingRate(currencyAmountFromTo.getCurrencyAmount());
            currencyToBuyRateSellRate.setSellingRate(currencyAmountFromTo.getCurrencyAmount());

            return currencyToBuyRateSellRate;
        } else {
            ExchangeRate exchangeRate = findExchangeRate(currencyAmountFromTo);

            if (exchangeRate == null) {
                return null;
            } else {
                currencyToBuyRateSellRate.setCurrencyTo(exchangeRate.getCurrencyTo().getValue());
                currencyToBuyRateSellRate.setBuyingRate(roundToFiveDecimalPlaces
                        (currencyAmountFromTo.getCurrencyAmount() * exchangeRate.getBuyingRate()));
                currencyToBuyRateSellRate.setSellingRate(roundToFiveDecimalPlaces
                        (currencyAmountFromTo.getCurrencyAmount() * exchangeRate.getSellingRate()));
                return currencyToBuyRateSellRate;
            }
        }
    }

    public ExchangeRate findExchangeRate(CurrencyAmountFromTo currencyAmountFromTo) {
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByCurrencyFromAndCurrencyTo(
                Currency.valueOf(currencyAmountFromTo.getCurrencyFrom()),
                Currency.valueOf(currencyAmountFromTo.getCurrencyTo()));

        if (exchangeRate.isPresent()) {
            return exchangeRate.get();
        } else {
            return null;
        }
    }

    public List<ExchangeRate> findExchangeRateList(String currencyFrom) {
        return exchangeRateRepository.findAllByCurrencyFrom(Currency.valueOf(currencyFrom));
    }

    public boolean isExchangeRateListSizeValid(List<ExchangeRate> exchangeRates) {
        return exchangeRates.size() == (Currency.values().length - 1);
    }

    public boolean isCurrencyFromAndToEqual(CurrencyAmountFromTo currencyAmountFromTo) {
        return currencyAmountFromTo.getCurrencyFrom().equals(currencyAmountFromTo.getCurrencyTo());
    }

    public boolean isCurrencyInvalid(String currency) {
        for (Currency c : Currency.values()) {
            if (c.name().equals(currency)) {
                return false;
            }
        }
        return true;
    }

    public boolean isCurrencyAmountForChangeInvalid(double currencyAmount) {
        return currencyAmount < 0.01;
    }

    public double roundToFiveDecimalPlaces(double number) {
        return Math.round(number * 100000.0) / 100000.0;
    }

    public boolean isCurrencyTypeInputEmpty(String currencyType) {
        return currencyType.isEmpty();
    }
}

