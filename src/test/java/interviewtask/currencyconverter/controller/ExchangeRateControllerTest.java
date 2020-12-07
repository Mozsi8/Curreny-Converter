package interviewtask.currencyconverter.controller;

import interviewtask.currencyconverter.currency_converter_tool.CurrencyToBuyRateSellRate;
import interviewtask.currencyconverter.service.ExchangeRateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.*;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(ExchangeRateController.class)
@RunWith(MockitoJUnitRunner.class)
public class ExchangeRateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private ExchangeRateService mockExchangeRateService;

    @InjectMocks
    ExchangeRateController exchangeRateController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(exchangeRateController)
                .build();
    }

    private List<CurrencyToBuyRateSellRate> currencyToBuyRateSellRateList = new ArrayList<>();
    private CurrencyToBuyRateSellRate currencyToBuyRateSellRate = new CurrencyToBuyRateSellRate();
    private String jsonExchangeRates = "{\"currencyFrom\": \"HUF\"}";
    private String jsonConvertedAmounts = "{\"currencyAmount\": \"100\",\"currencyFrom\": \"HUF\", \"currencyFrom\": \"EUR\"}";

    @Test
    public void getExchangeRatesTestInputIsEmpty() throws Exception {

        when(mockExchangeRateService.isCurrencyTypeInputEmpty(anyString())).thenReturn(true);

        mockMvc.perform(post("/exchange-rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonExchangeRates))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Currency input is empty!")));
    }

    @Test
    public void getExchangeRatesTestCurrencyIsInvalid() throws Exception {

        when(mockExchangeRateService.isCurrencyTypeInputEmpty(anyString())).thenReturn(false);
        when(mockExchangeRateService.isCurrencyInvalid(anyString())).thenReturn(true);

        mockMvc.perform(post("/exchange-rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonExchangeRates))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Currency is not valid!")));
    }

    @Test
    public void getExchangeRatesTestExchangeRateListIsNull() throws Exception {

        when(mockExchangeRateService.isCurrencyTypeInputEmpty(anyString())).thenReturn(false);
        when(mockExchangeRateService.isCurrencyInvalid(anyString())).thenReturn(false);
        currencyToBuyRateSellRateList = null;
        when(mockExchangeRateService.getExchangeRateList(anyString())).thenReturn(currencyToBuyRateSellRateList);

        mockMvc.perform(post("/exchange-rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonExchangeRates))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Data is/are missing from database!")));
    }

    @Test
    public void getExchangeRatesTestOK() throws Exception {

        when(mockExchangeRateService.isCurrencyTypeInputEmpty(anyString())).thenReturn(false);
        when(mockExchangeRateService.isCurrencyInvalid(anyString())).thenReturn(false);
        when(mockExchangeRateService.getExchangeRateList(anyString())).thenReturn(currencyToBuyRateSellRateList);

        mockMvc.perform(post("/exchange-rates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonExchangeRates))
                .andExpect(status().isOk());
    }

    @Test
    public void getConvertedAmountsTestInputIsEmpty() throws Exception {

        when(mockExchangeRateService.isCurrencyTypeInputEmpty(anyString())).thenReturn(true);

        mockMvc.perform(post("/converted-amounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConvertedAmounts))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Converter input is empty!")));
    }

    @Test
    public void getConvertedAmountsTestAmountForChangeIsInvalid() throws Exception {

        when(mockExchangeRateService.isCurrencyTypeInputEmpty(anyString())).thenReturn(false);
        when(mockExchangeRateService.isCurrencyAmountForChangeInvalid(anyDouble())).thenReturn(true);

        mockMvc.perform(post("/converted-amounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConvertedAmounts))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Currency amount is invalid!")));
    }

    @Test
    public void getConvertedAmountsTestCurrencyIsInvalid() throws Exception {

        when(mockExchangeRateService.isCurrencyTypeInputEmpty(anyString())).thenReturn(false);
        when(mockExchangeRateService.isCurrencyAmountForChangeInvalid(anyDouble())).thenReturn(false);
        when(mockExchangeRateService.isCurrencyInvalid(anyString())).thenReturn(true);

        mockMvc.perform(post("/converted-amounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConvertedAmounts))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Currency is not valid!")));
    }

    @Test
    public void getConvertedAmountsTestConvertedAmountIsNull() throws Exception {

        when(mockExchangeRateService.isCurrencyTypeInputEmpty(anyString())).thenReturn(false);
        when(mockExchangeRateService.isCurrencyAmountForChangeInvalid(anyDouble())).thenReturn(false);
        when(mockExchangeRateService.isCurrencyInvalid(anyString())).thenReturn(false);
        currencyToBuyRateSellRate = null;
        when(mockExchangeRateService.getConvertedAmounts(any())).thenReturn(currencyToBuyRateSellRate);


        mockMvc.perform(post("/converted-amounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConvertedAmounts))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Data is/are missing from database!")));
    }

    @Test
    public void getConvertedAmountsTestOK() throws Exception {

        when(mockExchangeRateService.isCurrencyTypeInputEmpty(anyString())).thenReturn(false);
        when(mockExchangeRateService.isCurrencyAmountForChangeInvalid(anyDouble())).thenReturn(false);
        when(mockExchangeRateService.isCurrencyInvalid(anyString())).thenReturn(false);
        when(mockExchangeRateService.getConvertedAmounts(any())).thenReturn(currencyToBuyRateSellRate);

        mockMvc.perform(post("/converted-amounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConvertedAmounts))
                .andExpect(status().isOk());
    }
}