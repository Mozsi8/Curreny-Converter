package interviewtask.currencyconverter.model;

import interviewtask.currencyconverter.currency_converter_tool.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private Currency currencyFrom;
    @Enumerated(EnumType.STRING)
    private Currency currencyTo;
    private double buyingRate;
    private double sellingRate;
}
