import { fetchClient } from "@/lib/fetch-client";

export async function GET() {
  const response = await fetchClient({
    path: "/coins/global",
    init: {
      headers: {
        "Content-Type": "application/json"
      },
      method: "GET"
    }
  })

  return Response.json(response.body, { status: response.status })
}