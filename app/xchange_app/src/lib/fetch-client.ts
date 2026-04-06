"use server"

interface FetchClientProps {
  host?: string,
  path: string,
  init: RequestInit
}

export async function fetchClient({
  init,
  path,
  host
}: FetchClientProps) {
  const apiUrl = host ?? process.env.API_URL ?? "http://localhost:8080"

  const response = await fetch(`${apiUrl + path}`, {
    ...init
  })

  let body
  try {
    body = await response.json()
  } catch {

  }

  return response
}