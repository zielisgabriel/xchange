import { fetchClient } from "@/lib/fetch-client";

export async function GET(req: Request, { coinId }: { coinId: string }) {
  const response = await fetchClient({
    path: `/coins/chart/${coinId}`,
    init: {
      method: "GET",
      headers: req.headers,
      body: req.body
    }
  })

  return Response.json(response.body, { status: response.status })
}
