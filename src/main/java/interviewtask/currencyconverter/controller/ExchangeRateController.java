package interviewtask.currencyconverter.controller;

import interviewtask.currencyconverter.currency_converter_tool.CurrencyAmountFromTo;
import interviewtask.currencyconverter.currency_converter_tool.CurrencyFrom;
import interviewtask.currencyconverter.error.ErrorMessage;
import interviewtask.currencyconverter.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The ExchangeRateController contains two Post request mapping methods.
 * Each of them receives a request body Object as JSON and sends back a ResponseEntity,
 * with a status code and one or more Objects in their body as JSONs.
 *
 * @author Dániel Mozsolics
 * @version 1.2.8
 * @since 2020-11-17
 */
@RestController
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @PostMapping("/exchange-rates")
    public ResponseEntity<?> getExchangeRates(@RequestBody CurrencyFrom currencyFrom) {
        if (exchangeRateService.isCurrencyInputEmpty(currencyFrom)) {
            return ResponseEntity.status(400).body(new ErrorMessage("Currency input is empty!"));
        } else if (exchangeRateService.currencyIsNotValid(currencyFrom.getCurrencyFrom())) {
            return ResponseEntity.status(400).body(new ErrorMessage("Currency is not valid!"));
        } else {
            if (exchangeRateService.getExchangingRateList(currencyFrom.getCurrencyFrom()) == null) {
                return ResponseEntity.status(400).body(new ErrorMessage("Data is/are missing from database!"));
            } else {
                return ResponseEntity.status(200).body(
                        exchangeRateService.getExchangingRateList(currencyFrom.getCurrencyFrom()));
            }
        }
    }

    @PostMapping("/converted-amounts")
    public ResponseEntity<?> getConvertedAmount(@RequestBody CurrencyAmountFromTo currencyAmountFromTo) {
        if (exchangeRateService.converterInputIsMissing(currencyAmountFromTo)) {
            return ResponseEntity.status(400).body(new ErrorMessage("Converter input is empty!"));
        } else if (exchangeRateService.amountIsInvalid(currencyAmountFromTo.getCurrencyAmount())) {
            return ResponseEntity.status(400).body(new ErrorMessage("Currency amount is invalid!"));
        } else if (exchangeRateService.currencyIsNotValid(currencyAmountFromTo.getCurrencyFrom()) ||
                exchangeRateService.currencyIsNotValid(currencyAmountFromTo.getCurrencyTo())) {
            return ResponseEntity.status(400).body(new ErrorMessage("Currency is not valid!"));
        } else {
            if (exchangeRateService.isGetConvertedAmountsNull(currencyAmountFromTo)) {
                return ResponseEntity.status(400).body(new ErrorMessage("Data(s) is/are missing from database!"));
            } else {
                return ResponseEntity.status(200).body(exchangeRateService.getConvertedAmounts(currencyAmountFromTo));
            }
        }
    }
}
