import { fetchClient } from "@/lib/fetch-client";

export async function POST(req: Request) {
  const body = await req.json();

  const response = await fetchClient({
    path: "/api/favorites",
    init: {
      headers: req.headers,
      body: JSON.stringify(body),
      method: "POST"
    }
  })

  // Retorna vazio ou com status original se for ok
  if (response.ok) {
    return new Response(null, { status: 201 });
  }

  return Response.json(response.body, { status: response.status })
}
