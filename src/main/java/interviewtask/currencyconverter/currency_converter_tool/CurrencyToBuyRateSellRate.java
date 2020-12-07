package interviewtask.currencyconverter.currency_converter_tool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyToBuyRateSellRate {

    private String currencyTo;
    private double buyingRate;
    private double sellingRate;
}
