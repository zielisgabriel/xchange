import { fetchClient } from "@/lib/fetch-client";

export async function GET(req: Request) {
  
  const response = await fetchClient({
    path: "/profile/simple",
    init: {
      headers: req.headers,
      method: "GET"
    },
  })

  return Response.json(response.body, { status: response.status })
}