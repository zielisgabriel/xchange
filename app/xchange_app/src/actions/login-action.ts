"use server"

import { fetchClient } from "@/lib/fetch-client"

export async function loginAction(formData: FormData) {
  const body = {
    email: formData.get("email"),
    password: formData.get("password")
  }

  const response = await fetchClient({
    init: {
      body: JSON.stringify(body),
      headers: {
        "Content-Type": "application/json"
      }
    },
    path: "/auth/login"
  })

  console.log(response)

  if (response.ok) {
    const data = await response.json()

    return {
      "status": response.statusText,
      "code": response.status,
      "access_token": data["access_token"] as string,
      "message": "Bem vindo(a)!"
    }
  }

  return {
    "status": response.statusText,
    "code": response.status,
    "message": "Não foi possível efetuar a autenticação"
  }
}