import { fetchClient } from "@/lib/fetch-client";

export async function GET(request: Request) {
  const url = new URL(request.url);
  const query = url.searchParams.get('query');

  const response = await fetchClient({
    path: "/coins/search?query=" + encodeURIComponent(query || ""),
    init: {
      headers: request.headers,
      method: "GET"
    }
  })

  return Response.json(response.body, {status: response.status});
}
