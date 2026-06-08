import pandas as pd
import requests 

def collector():
    """
    function for insert to data in data/raw from coin gecko API.
    
    """

    # variable to save all cryptos
    all_data = []

    # requesting all cryptos from coin gecko api
    response: dict = requests.get("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd")
    
    # inserting data in all data
    all_data.extend(response.json())

    # dataframe where data will be
    df = pd.DataFrame(all_data)

    # salving raw data in data/raw
    df.to_csv("data/raw/raw_data.csv", index=False)

    return df

if __name__ == "__main__":
    collector()