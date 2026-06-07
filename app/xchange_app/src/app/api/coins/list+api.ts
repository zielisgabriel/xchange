import { fetchClient } from "@/lib/fetch-client";

export async function GET(req: Request) {
  const response = await fetchClient({
    path: "/coins/list",
    init: {
      headers: req.headers,
      body: req.body,
      method: "GET"
    }
  })

  return Response.json(response.body, { status: response.status })
}