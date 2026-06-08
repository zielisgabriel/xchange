import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
import pickle

def load_data(features_path: str) -> pd.DataFrame:
    return pd.read_csv(features_path)

def split_data(df: pd.DataFrame):
    df = df.dropna()

    drop_cols = ["id", "symbol", "name", "label", "price_change_percentage_24h"]
    X = df.drop(columns=[c for c in drop_cols if c in df.columns])
    y = df["label"]
    return train_test_split(X, y, test_size=0.2, random_state=42)

def train(X_train, y_train) -> RandomForestClassifier:
    model = RandomForestClassifier(n_estimators=100, random_state=42)
    model.fit(X_train, y_train)
    return model

def evaluate(model, X_test, y_test):
    y_pred = model.predict(X_test)
    print(classification_report(y_test, y_pred))

def save_model(model, path: str = "models/random_forest.pkl"):
    with open(path, "wb") as f:
        pickle.dump(model, f)
    print(f"Modelo salvo em {path}")

def main():
    df = load_data("data/processed/features.csv")

    X_train, X_test, y_train, y_test = split_data(df)

    model = train(X_train, y_train)
    evaluate(model, X_test, y_test)
    save_model(model)

if __name__ == "__main__":
    main()