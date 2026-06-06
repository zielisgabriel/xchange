package br.com.xchange.api.application.dto.response;

import java.util.List;

public record SimpleCoinsListResponse(
    List<SimpleCoinsListWrapper> coins
) {
    public record SimpleCoinsListWrapper(
        String id,
        String name,
        String symbol,
        String imageUrl
    ) {};
}
