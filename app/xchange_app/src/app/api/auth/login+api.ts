import { fetchClient } from "@/lib/fetch-client"

export async function POST(req: Request) {
  const { email, password } = await req.json()

  const body = { email, password }

  const response = await fetchClient({
    init: {
      method: "POST",
      body: JSON.stringify(body),
      headers: {
        "Content-Type": "application/json"
      }
    },
    path: "/auth/login"
  })

  if (response.ok) {
    return Response.json({
      "code": response.status,
      "access_token": response.body?.["access_token"] as string,
      "refresh_token": response.body?.["refresh_token"] as string,
      "message": "Bem vindo(a)!"
    })
  }

  return Response.json({
    "code": response.status,
    "message": response.status === 409 ? response.body?.["message"] as string : "Não foi possível efetuar o login"
  })
}