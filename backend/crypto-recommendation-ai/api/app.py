import pickle
import pandas as pd
from fastapi import FastAPI
from apscheduler.schedulers.background import BackgroundScheduler
from contextlib import asynccontextmanager

from src.features import clean_data, generate_features, create_labels
from src.collector import collector

# ─────────────────────────────────────────
# ESTADO GLOBAL (cache em memória)
# ─────────────────────────────────────────

cache: dict = {"recommendations": [], "last_updated": None}

with open("models/random_forest.pkl", "rb") as f:
    model = pickle.load(f)


# ─────────────────────────────────────────
# JOB: coleta + predição
# ─────────────────────────────────────────

def update_recommendations():

    try:
        df_raw = collector()                 
        if df_raw.empty:
            return

        df = clean_data(df_raw)
        df = generate_features(df)
        df = df.dropna()

        drop_cols = ["id", "symbol", "name", "label", "price_change_percentage_24h"]
        X = df.drop(columns=[c for c in drop_cols if c in df.columns])

        df["prediction"] = model.predict(X)

        # retorna só as recomendadas como "alta"
        altas = (
            df[df["prediction"] == "alta"][["id", "symbol", "name", "current_price", "price_change_percentage_24h", "prediction"]]
            .sort_values("price_change_percentage_24h", ascending=False)
            .to_dict(orient="records")
        )

        from datetime import datetime
        cache["recommendations"] = altas
        cache["last_updated"] = datetime.now().isoformat()
        print(f"  {len(altas)} criptos recomendadas.")

    except Exception as e:
        print(f"  Erro no job: {e}")


# ─────────────────────────────────────────
# APP
# ─────────────────────────────────────────

@asynccontextmanager
async def lifespan(app: FastAPI):
    # roda uma vez na inicialização
    update_recommendations()

    scheduler = BackgroundScheduler()
    scheduler.add_job(update_recommendations, "interval", minutes=15)
    scheduler.start()

    yield

    scheduler.shutdown()


app = FastAPI(lifespan=lifespan)


@app.get("/recommend")
def recommend():
    return {
        "last_updated": cache["last_updated"],
        "total": len(cache["recommendations"]),
        "data": cache["recommendations"],
    }


@app.get("/health")
def health():
    return {"status": "ok", "last_updated": cache["last_updated"]}