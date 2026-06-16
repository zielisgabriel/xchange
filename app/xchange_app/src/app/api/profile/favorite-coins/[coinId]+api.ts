import { fetchClient } from "@/lib/fetch-client";

export async function DELETE(req: Request, { coinId }: Record<string, string>) {
  const response = await fetchClient({
    path: `/profile/favorite-coins/${coinId}`,
    init: {
      headers: req.headers,
      method: "DELETE",
    },
  })

  return Response.json(response.body ?? null, { status: response.status })
}
