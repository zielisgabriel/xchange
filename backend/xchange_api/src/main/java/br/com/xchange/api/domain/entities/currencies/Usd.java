package br.com.xchange.api.domain.entities.currencies;

import br.com.xchange.api.domain.entities.Currency;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Usd extends Currency {
    public Usd(BigDecimal value) {
        super(value);
    }

    public Usd(String value) {
        super(value);
    }

    @Override
    public String getSymbol() {
        return "$";
    }

    @Override
    public String getCode() {
        return "USD";
    }

    @Override
    public String formatted(int minimumFractionDigits, int maximumFractionDigits) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        formatter.setMinimumFractionDigits(minimumFractionDigits);
        formatter.setMaximumFractionDigits(maximumFractionDigits);

        return formatter.format(value);
    }

    @Override
    protected BigDecimal parseFromString(String value) {
        String cleaned = value.replace("$", "").replace(",", "").trim();
        return new BigDecimal(cleaned);
    }
}
