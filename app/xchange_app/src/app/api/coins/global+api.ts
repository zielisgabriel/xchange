import { fetchClient } from "@/lib/fetch-client";

export async function GET(req: Request) {
  const response = await fetchClient({
    path: "/coins/global",
    init: {
      method: "GET",
      headers: req.headers,
      body: req.body
    }
  })

  return Response.json(response.body, { status: response.status })
}