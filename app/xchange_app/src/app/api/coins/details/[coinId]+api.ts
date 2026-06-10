import { fetchClient } from "@/lib/fetch-client";

export async function GET(req: Request, { coinId }: Record<string, string>) {
  const response = await fetchClient({
    path: `/coins/detail/${coinId}`,
    init: {
      headers: req.headers,
      body: req.body,
      method: "GET"
    }
  })
  
  return Response.json(response.body, { status: 200 })
}