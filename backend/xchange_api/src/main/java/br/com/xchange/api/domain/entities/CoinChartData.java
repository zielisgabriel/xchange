package br.com.xchange.api.domain.entities;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CoinChartData {
  List<List<BigDecimal>> prices;
}
