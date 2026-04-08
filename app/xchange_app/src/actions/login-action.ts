"use server"

import { fetchClient } from "@/lib/fetch-client"

export async function loginAction(formData: FormData) {
  const body = {
    email: formData.get("email"),
    password: formData.get("password")
  }

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

  console.log(response)

  if (response.ok) {
    return {
      "code": response.status,
      "access_token": response.body?.["access_token"] as string,
      "message": "Bem vindo(a)!"
    }
  }

  console.error(response.body)

  return {
    "code": response.status,
    "message": response.status === 409 ? response.body?.["message"] as string : "Não foi possível efetuar o login"
  }
}
