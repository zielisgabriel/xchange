package br.com.xchange.api.domain.entities;

import java.math.BigDecimal;

public abstract class Currency {
    protected BigDecimal value;

    public Currency(String value) {
        this.value = parseFromString(value);
    }

    public Currency(BigDecimal value) {
        this.value = value;
    }

    public abstract String getSymbol();

    public abstract String getCode();

    public abstract String formatted(int minimumFractionDigits, int maximumFractionDigits);

    protected abstract BigDecimal parseFromString(String value);

    public BigDecimal getValue() {
        return value;
    }
}
