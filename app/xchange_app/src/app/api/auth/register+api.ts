import { fetchClient } from "@/lib/fetch-client"

export async function POST(req: Request) {
  const {
    first_name: firstName,
    last_name: lastName,
    email,
    password,
    birth_date: birthDate,
    cpf
  } = await req.json()

  const body = {
    first_name: firstName,
    last_name: lastName,
    email,
    password,
    birth_date: birthDate,
    cpf
  }

  const response = await fetchClient({
    init: {
      method: "POST",
      body: JSON.stringify(body),
      headers: {
        "Content-Type": "application/json"
      }
    },
    path: "/auth/register"
  })

  console.log(response)

  if (response.ok) {
    return Response.json({
      "code": response.status,
      "access_token": response.body?.["access_token"] as string,
      "message": "Cadastro com sucesso!"
    })
  }

  if (response.status === 422) {
    return Response.json({
      "code": response.status,
      "fieldErrors": response.body?.["fieldErrors"],
      "message": response.status === 422 ? response.body?.["message"] as string : "Não foi possível efetuar o cadastro",
    })
  }

  return Response.json({
    "code": response.status,
    "message": response.status === 409 ? response.body?.["message"] as string : "Não foi possível efetuar o cadastro"
  })
}