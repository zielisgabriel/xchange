package br.com.xchange.api.domain.entities;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CoinDetailData extends CoinWithMarketData {
    private String description;
    private String hashingAlgorithm;
    private String genesisDate;
    private Integer marketCapRank;
    private Long watchlistPortfolioUsers;
    private BigDecimal sentimentVotesUpPercentage;
    private BigDecimal sentimentVotesDownPercentage;

    private String high24h;
    private String low24h;
    private String priceChange24h;
    private String fullyDilutedValuation;

    private BigDecimal priceChangePercentage1h;
    private BigDecimal priceChangePercentage7d;
    private BigDecimal priceChangePercentage14d;
    private BigDecimal priceChangePercentage30d;
    private BigDecimal priceChangePercentage60d;
    private BigDecimal priceChangePercentage200d;
    private BigDecimal priceChangePercentage1y;

    private BigDecimal circulatingSupply;
    private BigDecimal totalSupply;
    private BigDecimal maxSupply;

    private String ath;
    private BigDecimal athChangePercentage;
    private String athDate;
    private String atl;
    private BigDecimal atlChangePercentage;
    private String atlDate;

    private List<Double> sparkline7d;
}