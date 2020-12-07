package interviewtask.currencyconverter.currency_converter_tool;

public enum Currency {

    HUF("HUF"),
    EUR("EUR"),
    USD("USD"),
    JPY("JPY"),
    RUB("RUB");

    private String value;

    public String getValue() {
        return this.value;
    }

    Currency(String value) {
        this.value = value;
    }
}
