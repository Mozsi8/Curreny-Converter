package interviewtask.currencyconverter.service;

import interviewtask.currencyconverter.currency_converter_tool.Currency;
import interviewtask.currencyconverter.currency_converter_tool.CurrencyAmountFromTo;
import interviewtask.currencyconverter.currency_converter_tool.CurrencyToBuyRateSellRate;
import interviewtask.currencyconverter.model.ExchangeRate;
import interviewtask.currencyconverter.repository.ExchangeRateRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository mockExchangeRateRepository;

    @Mock
    private CurrencyAmountFromTo mockCurrencyAmountFromTo;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private static List<ExchangeRate> exchangeRateList = new ArrayList<>();
    private CurrencyAmountFromTo currencyAmountFromTo = new CurrencyAmountFromTo(
            100, "EUR", "HUF");

    @BeforeClass
    public static void setUp() {
        long id = 1L;
        double br = 100.0;
        double sr = 90.0;
        for (int i = 1; i < Currency.values().length; i++) {
            exchangeRateList.add(new ExchangeRate(id, Currency.EUR, Currency.HUF, br, sr));
            id++;
            br += 10;
            sr += 10;
        }
    }

    @Test
    public void getExchangingRateListTest() {
        Currency currency = Currency.EUR;
        List<CurrencyToBuyRateSellRate> currencyToBuyRateSellRate = new ArrayList<>(Arrays.asList(
                new CurrencyToBuyRateSellRate("HUF", 100, 90),
                new CurrencyToBuyRateSellRate("HUF", 110, 100),
                new CurrencyToBuyRateSellRate("HUF", 120, 110)));
        when(mockExchangeRateRepository.findAllByCurrencyFrom(currency)).thenReturn(exchangeRateList);
        assertEquals(currencyToBuyRateSellRate.get(0).getCurrencyTo(),
                exchangeRateService.getExchangeRateList(currency.getValue()).get(0).getCurrencyTo());
        assertEquals(currencyToBuyRateSellRate.get(2).getBuyingRate(),
                exchangeRateService.getExchangeRateList(currency.getValue()).get(2).getBuyingRate(), 0.1);
    }

    @Test
    public void getConvertedAmountsTest() {
        CurrencyToBuyRateSellRate currencyToBuyRateSellRate = new CurrencyToBuyRateSellRate(
                "HUF", 36000, 35500);
        Optional<ExchangeRate> optionalExchangeRate = Optional.of(
                new ExchangeRate(1L, Currency.EUR, Currency.HUF, 360, 355));
        when(mockExchangeRateRepository.findByCurrencyFromAndCurrencyTo(Currency.EUR, Currency.HUF))
                .thenReturn(optionalExchangeRate);
        assertEquals(currencyToBuyRateSellRate.getBuyingRate(),
                exchangeRateService.getConvertedAmounts(currencyAmountFromTo).getBuyingRate(), 0.000001);
        assertEquals(currencyToBuyRateSellRate.getCurrencyTo(),
                exchangeRateService.getConvertedAmounts(currencyAmountFromTo).getCurrencyTo());
    }

    @Test
    public void findExchangeRateTest() {
        ExchangeRate exchangeRate = new ExchangeRate(1L, Currency.EUR, Currency.HUF, 360, 355);
        when(mockExchangeRateRepository.findByCurrencyFromAndCurrencyTo(Currency.EUR, Currency.HUF))
                .thenReturn(Optional.of(exchangeRate));
        assertEquals(exchangeRate, exchangeRateService.findExchangeRate(currencyAmountFromTo));
    }

    @Test
    public void findExchangeRateListTest() {
        when(mockExchangeRateRepository.findAllByCurrencyFrom(Currency.EUR)).thenReturn(exchangeRateList);
        assertEquals(exchangeRateList.get(0), exchangeRateService.findExchangeRateList(Currency.EUR.getValue()).get(0));
        assertEquals(exchangeRateList.get(1), exchangeRateService.findExchangeRateList(Currency.EUR.getValue()).get(1));
    }

    @Test
    public void isExchangeRateListSizeValidTestTrue() {
        assertTrue(exchangeRateService.isExchangeRateListSizeValid(exchangeRateList));
    }

    @Test
    public void isExchangeRateListSizeValidTestFalse() {
        exchangeRateList.add(new ExchangeRate(
                exchangeRateList.size() + 1, Currency.EUR, Currency.HUF, 90, 80));
        assertFalse(exchangeRateService.isExchangeRateListSizeValid(exchangeRateList));
    }

    @Test
    public void isCurrencyFromAndToEqualTestTrue() {
        when(mockCurrencyAmountFromTo.getCurrencyFrom()).thenReturn("HUF");
        when(mockCurrencyAmountFromTo.getCurrencyTo()).thenReturn("HUF");
        assertTrue(exchangeRateService.isCurrencyFromAndToEqual(mockCurrencyAmountFromTo));
    }

    @Test
    public void isCurrencyFromAndToEqualTestFalse() {
        when(mockCurrencyAmountFromTo.getCurrencyFrom()).thenReturn("EUR");
        when(mockCurrencyAmountFromTo.getCurrencyTo()).thenReturn("HUF");
        assertFalse(exchangeRateService.isCurrencyFromAndToEqual(mockCurrencyAmountFromTo));
    }

    @Test
    public void isCurrencyInvalidTestTrue() {
        String notValidCurrency = "Forint";
        assertTrue(exchangeRateService.isCurrencyInvalid(notValidCurrency));
    }

    @Test
    public void isCurrencyInvalidTestFalse() {
        String validCurrency = "HUF";
        assertFalse(exchangeRateService.isCurrencyInvalid(validCurrency));
    }

    @Test
    public void isCurrencyAmountForChangeInvalidTestTrue() {
        double testNum = 0.001;
        assertTrue(exchangeRateService.isCurrencyAmountForChangeInvalid(testNum));
    }

    @Test
    public void isCurrencyAmountForChangeInvalidTestFalse() {
        double testNum = 0.01;
        assertFalse(exchangeRateService.isCurrencyAmountForChangeInvalid(testNum));
    }

    @Test
    public void roundToFiveDecimalPlacesTest() {
        double originalTestNum = 10.5643623468486486;
        double roundedTestNum = exchangeRateService.roundToFiveDecimalPlaces(originalTestNum);
        assertEquals(10.56436, roundedTestNum, 0.000001);
    }

    @Test
    public void isCurrencyInputEmptyTestTrue() {
        String currencyFrom = "";
        assertTrue(exchangeRateService.isCurrencyTypeInputEmpty(currencyFrom));
    }

    @Test
    public void isCurrencyInputEmptyTestFalse() {
        String currencyFrom = "HUF";
        assertFalse(exchangeRateService.isCurrencyTypeInputEmpty(currencyFrom));
    }
}