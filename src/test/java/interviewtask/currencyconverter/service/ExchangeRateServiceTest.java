package interviewtask.currencyconverter.service;

import interviewtask.currencyconverter.currency_converter_tool.Currency;
import interviewtask.currencyconverter.currency_converter_tool.CurrencyAmountFromTo;
import interviewtask.currencyconverter.currency_converter_tool.CurrencyFrom;
import interviewtask.currencyconverter.currency_converter_tool.CurrencyToBuyRateSellRate;
import interviewtask.currencyconverter.model.ExchangeRate;
import interviewtask.currencyconverter.repository.ExchangeRateRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.BDDMockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@WebMvcTest(ExchangeRateService.class)
@RunWith(MockitoJUnitRunner.class)
public class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private CurrencyAmountFromTo mockCurrencyAmountFromTo;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRateService mockExchangeRateService;

    @Test
    public void getExchangingRateListTest() {
        String currencyFromString = "EUR";
        List<CurrencyToBuyRateSellRate> currencyToBuyRateSellRate = new ArrayList<>(Arrays.asList(
                new CurrencyToBuyRateSellRate("HUF", 360, 355),
                new CurrencyToBuyRateSellRate("JPY", 125, 120),
                new CurrencyToBuyRateSellRate("USD", 1.19, 1.18),
                new CurrencyToBuyRateSellRate("RUB", 92, 89)));
        List<ExchangeRate> exchangeRates = new ArrayList<>(Arrays.asList(
                new ExchangeRate(1L, Currency.EUR, Currency.HUF, 360, 355),
                new ExchangeRate(2L, Currency.EUR, Currency.JPY, 125, 120),
                new ExchangeRate(2L, Currency.EUR, Currency.USD, 1.19, 1.18),
                new ExchangeRate(2L, Currency.EUR, Currency.RUB, 92, 89)));
        given(exchangeRateService.findExchangeRateList(currencyFromString)).willReturn(exchangeRates);
        Assert.assertEquals(currencyToBuyRateSellRate.get(0).getCurrencyTo(),
                exchangeRateService.getExchangingRateList(Currency.EUR.getValue()).get(0).getCurrencyTo());
        Assert.assertEquals(currencyToBuyRateSellRate.get(2).getCurrencyTo(),
                exchangeRateService.getExchangingRateList(Currency.EUR.getValue()).get(2).getCurrencyTo());
    }

    @Test
    public void findExchangeRateListTest() {
        List<ExchangeRate> exchangeRates = new ArrayList<>(Arrays.asList(new ExchangeRate(1L, Currency.EUR, Currency.HUF, 360, 355),
                new ExchangeRate(2L, Currency.EUR, Currency.JPY, 125, 120)));
        given(exchangeRateRepository.findAllByCurrencyFrom(Currency.EUR)).willReturn(exchangeRates);
        Assert.assertEquals(exchangeRates.get(0), exchangeRateService.findExchangeRateList(Currency.EUR.getValue()).get(0));
        Assert.assertEquals(exchangeRates.get(1), exchangeRateService.findExchangeRateList(Currency.EUR.getValue()).get(1));
    }

    @Test
    public void getConvertedAmountsTest() {
        CurrencyToBuyRateSellRate currencyToBuyRateSellRate = new CurrencyToBuyRateSellRate("HUF", 36000, 35500);
        Optional<ExchangeRate> exchangeRates = Optional.of(new ExchangeRate(1L, Currency.EUR, Currency.HUF, 360, 355));
        CurrencyAmountFromTo currencyAmountFromTo = new CurrencyAmountFromTo(100, "EUR", "HUF");
        given(exchangeRateRepository.findByCurrencyFromAndCurrencyTo(Currency.EUR, Currency.HUF)).willReturn(exchangeRates);
        Assert.assertEquals(currencyToBuyRateSellRate.getBuyingRate(),
                exchangeRateService.getConvertedAmounts(currencyAmountFromTo).getBuyingRate(), 0.000001);
        Assert.assertEquals(currencyToBuyRateSellRate.getCurrencyTo(),
                exchangeRateService.getConvertedAmounts(currencyAmountFromTo).getCurrencyTo());
    }

    @Test
    public void findExchangeRateTest() {
        CurrencyAmountFromTo currencyAmountFromTo = new CurrencyAmountFromTo(100, "EUR", "HUF");
        ExchangeRate exchangeRates = new ExchangeRate(1L, Currency.EUR, Currency.HUF, 360, 355);
        given(exchangeRateRepository.findByCurrencyFromAndCurrencyTo(Currency.EUR, Currency.HUF)).willReturn(Optional.of(exchangeRates));
        Assert.assertEquals(exchangeRates, exchangeRateService.findExchangeRate(currencyAmountFromTo));
    }

    @Test
    public void isCurrencyFromAndToEqualTestTrue() {
        Mockito.when(mockCurrencyAmountFromTo.getCurrencyFrom()).thenReturn("HUF");
        Mockito.when(mockCurrencyAmountFromTo.getCurrencyTo()).thenReturn("HUF");
        Assert.assertTrue(exchangeRateService.isCurrencyFromAndToEqual(mockCurrencyAmountFromTo));
    }

    @Test
    public void isCurrencyFromAndToEqualTestFalse() {
        Mockito.when(mockCurrencyAmountFromTo.getCurrencyFrom()).thenReturn("EUR");
        Mockito.when(mockCurrencyAmountFromTo.getCurrencyTo()).thenReturn("HUF");
        Assert.assertFalse(exchangeRateService.isCurrencyFromAndToEqual(mockCurrencyAmountFromTo));
    }

    @Test
    public void currencyIsNotValidTestTrue() {
        String notValidCurrency = "Forint";
        Assert.assertTrue(exchangeRateService.currencyIsNotValid(notValidCurrency));
    }

    @Test
    public void currencyIsNotValidTestFalse() {
        String validCurrency = "HUF";
        Assert.assertFalse(exchangeRateService.currencyIsNotValid(validCurrency));
    }


    @Test
    public void converterInputIsMissingTestCurrencyFromMissing() {
        Mockito.when(mockCurrencyAmountFromTo.getCurrencyFrom()).thenReturn("");
        Assert.assertTrue(exchangeRateService.converterInputIsMissing(mockCurrencyAmountFromTo));
    }

    @Test
    public void converterInputIsMissingTestCurrencyToMissing() {
        Mockito.when(mockCurrencyAmountFromTo.getCurrencyFrom()).thenReturn("EUR");
        Mockito.when(mockCurrencyAmountFromTo.getCurrencyTo()).thenReturn("");
        Assert.assertTrue(exchangeRateService.converterInputIsMissing(mockCurrencyAmountFromTo));
    }

    @Test
    public void converterInputIsMissingTestNotMissing() {
        Mockito.when(mockCurrencyAmountFromTo.getCurrencyFrom()).thenReturn("EUR");
        Mockito.when(mockCurrencyAmountFromTo.getCurrencyTo()).thenReturn("HUF");
        Assert.assertFalse(exchangeRateService.converterInputIsMissing(mockCurrencyAmountFromTo));
    }

    @Test
    public void amountIsInvalidTestTrue() {
        double testNum = 0.001;
        Assert.assertTrue(exchangeRateService.amountIsInvalid(testNum));
    }

    @Test
    public void amountIsInvalidTestFalse() {
        double testNum = 0.01;
        Assert.assertFalse(exchangeRateService.amountIsInvalid(testNum));
    }

    @Test
    public void roundToTwoDecimalPlacesTest() {
        double originalTestNum = 10.5643623468486486;
        double roundedTestNum = exchangeRateService.roundToTwoDecimalPlaces(originalTestNum);
        Assert.assertEquals(10.56436, roundedTestNum, 0.000001);
    }

    @Test
    public void isCurrencyInputEmptyTestTrue() {
        CurrencyFrom currencyFrom = new CurrencyFrom("");
        Assert.assertTrue(exchangeRateService.isCurrencyInputEmpty(currencyFrom));
    }

    @Test
    public void isCurrencyInputEmptyTestFalse() {
        CurrencyFrom currencyFrom = new CurrencyFrom("HUF");
        Assert.assertFalse(exchangeRateService.isCurrencyInputEmpty(currencyFrom));
    }
}