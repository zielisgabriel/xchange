import pandas as pd

def load_data(raw_data_path: str) -> pd.DataFrame:
    return pd.read_csv(raw_data_path)

def clean_data(df: pd.DataFrame) -> pd.DataFrame:
    return df.drop(columns=[
        "image",
        "roi",
        "last_updated",
        "ath_date",
        "atl_date",
    ])

def generate_features(df: pd.DataFrame) -> pd.DataFrame:
    # liquidez: quanto do market cap está sendo negociado
    df["volume_marketcap_ratio"] = df["total_volume"] / df["market_cap"]

    # volatilidade do dia: range entre máxima e mínima
    df["high_low_range_pct"] = (df["high_24h"] - df["low_24h"]) / df["low_24h"] * 100

    # % do supply já em circulação (escassez)
    df["circulating_supply_ratio"] = df["circulating_supply"] / df["max_supply"]

    # distância do topo histórico (quão longe do ATH)
    df["dist_ath_pct"] = df["ath_change_percentage"]  # já vem negativo

    return df

def create_labels(df: pd.DataFrame, column: str = "price_change_percentage_24h") -> pd.DataFrame:
    df = df.dropna(subset=[column])
    
    p33 = df[column].quantile(0.33)
    p66 = df[column].quantile(0.66)

    df["label"] = pd.cut(
        df[column],
        bins=[-float("inf"), p33, p66, float("inf")],
        labels=["baixa", "lateral", "alta"]
    )

    return df

def main():
    df = load_data("data/raw/raw_data.csv")
    df = clean_data(df)
    df = generate_features(df)
    df = create_labels(df)
    df.to_csv("data/processed/features.csv", index=False)

if __name__ == "__main__":
    main()