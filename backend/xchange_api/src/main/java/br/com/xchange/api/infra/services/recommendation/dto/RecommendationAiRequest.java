package br.com.xchange.api.infra.services.recommendation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationAiRequest {
  private List<String> ids;
}
