import { fetchClient } from "@/lib/fetch-client";

export async function POST(req: Request) {
  
  const response = await fetchClient({
    path: "/profile/onboarding",
    init: {
      headers: req.headers,
      body: req.body,
      method: "POST"
    },
  })

  return Response.json(null, { status: response.status })
}