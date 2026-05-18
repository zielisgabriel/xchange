import { fetchClient } from "@/lib/fetch-client";

export async function POST(req: Request) {
  const body = await req.json()

  const response = await fetchClient({
    path: "/auth/refresh",
    init: {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(body)
    }
  })

  if (response.ok) {
    return Response.json({
      "code": response.status,
      "access_token": response.body?.["access_token"] as string,
      "refresh_token": response.body?.["refresh_token"] as string
    }, { status: 200 })
  }

  console.error(response.body)

  return Response.json({
    "code": response.status,
    "message": response.status === 401 ? response.body?.["message"] as string : "Erro interno na autenticação do usuário"
  }, { status: 401 })
}