"use client"

import { Platform, Pressable, View } from "react-native"
import { Input } from "./ui/input"
import { Text } from "./ui/text"
import { Button } from "./ui/button"
import { useRef, useState, useTransition } from "react"
import { useAuthStore } from "@/hooks/use-auth-store"
import { toast } from "sonner-native"
import { router } from "expo-router"
import { twMerge } from "tailwind-merge"
import clsx from "clsx"
import DateTimePicker from "@react-native-community/datetimepicker"

interface FieldError {
  field: string,
  message: string
}

function formatCpf(value: string): string {
  const digits = value.replace(/\D/g, "").slice(0, 11)

  if (digits.length <= 3) return digits
  if (digits.length <= 6) return `${digits.slice(0, 3)}.${digits.slice(3)}`
  if (digits.length <= 9) return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6)}`
  return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6, 9)}-${digits.slice(9)}`
}

function formatDateDisplay(date: Date): string {
  const day = String(date.getDate()).padStart(2, "0")
  const month = String(date.getMonth() + 1).padStart(2, "0")
  const year = date.getFullYear()
  return `${day}/${month}/${year}`
}

function toISODate(date: Date): string {
  const day = String(date.getDate()).padStart(2, "0")
  const month = String(date.getMonth() + 1).padStart(2, "0")
  const year = date.getFullYear()
  return `${year}-${month}-${day}`
}

export function RegisterForm() {
  const firstNameRef = useRef("")
  const lastNameRef = useRef("")
  const emailRef = useRef("")
  const passwordRef = useRef("")
  const birthDateRef = useRef("")
  const cpfRef = useRef("")
  const { logIn } = useAuthStore()
  const [isPeding, startTransition] = useTransition()
  const [fieldErrors, setFieldErrors] = useState<FieldError[] | null>(null);

  const [cpfDisplay, setCpfDisplay] = useState("")
  const [birthDate, setBirthDate] = useState<Date | null>(null)
  const [showDatePicker, setShowDatePicker] = useState(false)

  function handleLogin() {
    startTransition(async () => {
      const firstName = firstNameRef.current
      const lastName = lastNameRef.current
      const email = emailRef.current
      const password = passwordRef.current
      const birth_date = birthDateRef.current
      const cpf = cpfRef.current

      const response = await fetch("/api/register", {
        method: "POST",
        body: JSON.stringify({
          first_name: firstName,
          last_name: lastName,
          email,
          password,
          birth_date,
          cpf
        }),
        headers: {
          "Content-Type": "application/json"
        }
      })
      const data = await response.json()

      if (data.code == 201) {
        toast.success(data.message)
        router.push("/login")
        return
      }

      if (data.code == 422) {
        setFieldErrors(data.fieldErrors)
      }

      console.log(fieldErrors)

      toast.error(data.message)

      console.log(data)
    })
  }

  function hasErrorByInputFieldName(inputName: string): boolean {
    if (!fieldErrors) return false

    return fieldErrors.some(error => error.field.includes(inputName))
  }

  function getErrorMessage(inputName: string): string | null {
    if (!fieldErrors) return null

    const error = fieldErrors.find(error => error.field.includes(inputName))
    return error?.message ?? null
  }

  function handleCpfChange(value: string) {
    const formatted = formatCpf(value)
    setCpfDisplay(formatted)
    cpfRef.current = formatted.replace(/\D/g, "")
  }

  function handleBirthDateChange(_event: any, selectedDate?: Date) {
    if (Platform.OS !== "web") {
      setShowDatePicker(false)
    }

    if (selectedDate) {
      setBirthDate(selectedDate)
      birthDateRef.current = toISODate(selectedDate)
    }
  }

  return (
    <View className="flex gap-4">
      <View className="flex flex-row gap-2">
        <View className="flex-1 gap-1">
          <Input
            placeholder="Nome"
            className={twMerge(clsx("placeholder:text-sm", {
              "border-destructive": hasErrorByInputFieldName("firstName")
            }))}
            placeholderTextColor={"#FFF"}
            textContentType="name"
            autoComplete="off"
            autoCorrect={false}
            onChangeText={value => firstNameRef.current = value}
          />
          {hasErrorByInputFieldName("firstName") && (
            <Text className="text-destructive text-xs">
              {getErrorMessage("firstName")}
            </Text>
          )}
        </View>
        <View className="flex-1 gap-1">
          <Input
            placeholder="Sobrenome"
            className={twMerge(clsx("placeholder:text-sm", {
              "border-destructive": hasErrorByInputFieldName("lastName")
            }))}
            placeholderTextColor={"#FFF"}
            textContentType="name"
            autoComplete="off"
            autoCorrect={false}
            onChangeText={value => lastNameRef.current = value}
          />
          {hasErrorByInputFieldName("lastName") && (
            <Text className="text-destructive text-xs">
              {getErrorMessage("lastName")}
            </Text>
          )}
        </View>
      </View>

      <View className="gap-1">
        <Input
          placeholder="E-mail"
          className={twMerge(clsx("placeholder:text-sm", {
            "border-destructive": hasErrorByInputFieldName("email")
          }))}
          placeholderTextColor={"#FFF"}
          textContentType="emailAddress"
          autoComplete="off"
          autoCorrect={false}
          onChangeText={value => emailRef.current = value}
        />
        {hasErrorByInputFieldName("email") && (
          <Text className="text-destructive text-xs">
            {getErrorMessage("email")}
          </Text>
        )}
      </View>

      <View className="gap-1">
        <Input
          placeholder="Senha"
          className={twMerge(clsx("placeholder:text-sm", {
            "border-destructive": hasErrorByInputFieldName("password")
          }))}
          placeholderTextColor={"#FFF"}
          textContentType="password"
          secureTextEntry={true}
          autoComplete="off"
          autoCorrect={false}
          onChangeText={value => passwordRef.current = value}
        />
        {hasErrorByInputFieldName("password") && (
          <Text className="text-destructive text-xs">
            {getErrorMessage("password")}
          </Text>
        )}
      </View>

      <View className="gap-1">
        {Platform.OS === "web" ? (
          <>
            <Input
              placeholder="Data de aniversário"
              className={twMerge(clsx("placeholder:text-sm", {
                "border-destructive": hasErrorByInputFieldName("birth_date")
              }))}
              placeholderTextColor={"#FFF"}
              textContentType="birthdate"
              autoComplete="off"
              autoCorrect={false}
              onChangeText={value => birthDateRef.current = value}
            />
            {hasErrorByInputFieldName("birth_date") && (
              <Text className="text-destructive text-xs">
                {getErrorMessage("birth_date")}
              </Text>
            )}
          </>
        ) : (
          <>
            <Pressable onPress={() => setShowDatePicker(true)}>
              <Input
                placeholder="Data de nascimento"
                className={twMerge(clsx("placeholder:text-sm", {
                  "border-destructive": hasErrorByInputFieldName("birthDate")
                }))}
                placeholderTextColor={"#FFF"}
                value={birthDate ? formatDateDisplay(birthDate) : ""}
                editable={false}
                pointerEvents="none"
              />
            </Pressable>
            {showDatePicker && (
              <DateTimePicker
                value={birthDate ?? new Date()}
                mode="date"
                maximumDate={new Date()}
                onChange={handleBirthDateChange}
              />
            )}
          </>
        )}
        {hasErrorByInputFieldName("birthDate") && (
          <Text className="text-destructive text-xs">
            {getErrorMessage("birthDate")}
          </Text>
        )}
      </View>

      <View className="gap-1">
        <Input
          placeholder="CPF"
          className={twMerge(clsx("placeholder:text-sm", {
            "border-destructive": hasErrorByInputFieldName("cpf")
          }))}
          placeholderTextColor={"#FFF"}
          value={cpfDisplay}
          keyboardType="numeric"
          maxLength={14}
          autoComplete="off"
          autoCorrect={false}
          onChangeText={handleCpfChange}
        />
        {hasErrorByInputFieldName("cpf") && (
          <Text className="text-destructive text-xs">
            {getErrorMessage("cpf")}
          </Text>
        )}
      </View>

      <Button
        onPress={handleLogin}
        disabled={isPeding}
      >
        <Text className="font-bold">
          {isPeding ? "Carregando..." : "Entrar"}
        </Text>
      </Button>
    </View>
  )
}