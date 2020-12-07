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

@RestController
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @PostMapping("/exchange-rates")
    public ResponseEntity<?> getExchangeRates(@RequestBody CurrencyFrom currencyFrom) {
        if (exchangeRateService.isCurrencyTypeInputEmpty(currencyFrom.getCurrencyFrom())) {
            return ResponseEntity.status(400).body(new ErrorMessage("Currency input is empty!"));
        } else if (exchangeRateService.isCurrencyInvalid(currencyFrom.getCurrencyFrom())) {
            return ResponseEntity.status(400).body(new ErrorMessage("Currency is not valid!"));
        } else {
            if (exchangeRateService.getExchangeRateList(currencyFrom.getCurrencyFrom()) == null) {
                return ResponseEntity.status(501).body(new ErrorMessage("Data is/are missing from database!"));
            } else {
                return ResponseEntity.status(200).body(
                        exchangeRateService.getExchangeRateList(currencyFrom.getCurrencyFrom()));
            }
        }
    }

    @PostMapping("/converted-amounts")
    public ResponseEntity<?> getConvertedAmounts(@RequestBody CurrencyAmountFromTo currencyAmountFromTo) {
        if (exchangeRateService.isCurrencyTypeInputEmpty(currencyAmountFromTo.getCurrencyFrom()) ||
                exchangeRateService.isCurrencyTypeInputEmpty(currencyAmountFromTo.getCurrencyTo())) {
            return ResponseEntity.status(400).body(new ErrorMessage("Converter input is empty!"));
        } else if (exchangeRateService.isCurrencyAmountForChangeInvalid(currencyAmountFromTo.getCurrencyAmount())) {
            return ResponseEntity.status(400).body(new ErrorMessage("Currency amount is invalid!"));
        } else if (exchangeRateService.isCurrencyInvalid(currencyAmountFromTo.getCurrencyFrom()) ||
                exchangeRateService.isCurrencyInvalid(currencyAmountFromTo.getCurrencyTo())) {
            return ResponseEntity.status(400).body(new ErrorMessage("Currency is not valid!"));
        } else {
            if (exchangeRateService.getConvertedAmounts(currencyAmountFromTo) == null) {
                return ResponseEntity.status(501).body(new ErrorMessage("Data is/are missing from database!"));
            } else {
                return ResponseEntity.status(200).body(exchangeRateService.getConvertedAmounts(currencyAmountFromTo));
            }
        }
    }
}
