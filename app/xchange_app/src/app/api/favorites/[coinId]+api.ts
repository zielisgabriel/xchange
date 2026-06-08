import { fetchClient } from "@/lib/fetch-client";

export async function DELETE(req: Request, { coinId }: Record<string, string>) {
  const response = await fetchClient({
    path: `/api/favorites/${coinId}`,
    init: {
      headers: req.headers,
      method: "DELETE"
    }
  })

  if (response.ok) {
    return new Response(null, { status: 204 });
  }

  return Response.json(response.body, { status: response.status })
}
