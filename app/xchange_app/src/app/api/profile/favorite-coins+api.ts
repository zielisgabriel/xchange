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

  return Response.json(response.body, { status: response.status })
}