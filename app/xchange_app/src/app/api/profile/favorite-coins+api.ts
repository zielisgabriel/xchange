import { fetchClient } from "@/lib/fetch-client";

export async function GET(req: Request) {
  const url = new URL(req.url)
  const predictionParam = url.searchParams.get("prediction")

  const response = await fetchClient({
    path: `/profile/favorite-coins${predictionParam ? `?prediction=${predictionParam}` : ""}`,
    init: {
      headers: req.headers,
      body: req.body,
      method: "GET"
    },
  })

  return Response.json(response.body ?? null, { status: response.status })
}

export async function POST(req: Request) {
  const body = await req.json()

  const response = await fetchClient({
    path: "/profile/favorite-coins",
    init: {
      method: "POST",
      headers: req.headers,
      body: JSON.stringify(body),
    },
  })

  return Response.json(response.body ?? null, { status: response.status })
}