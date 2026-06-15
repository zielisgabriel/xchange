import { render, screen, fireEvent } from "@testing-library/react-native"
import { User } from "lucide-react-native"
import { MenuItem } from "./menu-item"

describe("MenuItem", () => {
  it("renders the label and subtitle", () => {
    render(<MenuItem icon={User} label="Informações pessoais" subtitle="Nome, e-mail" />)

    expect(screen.getByText("Informações pessoais")).toBeTruthy()
    expect(screen.getByText("Nome, e-mail")).toBeTruthy()
  })

  it("calls onPress when tapped", () => {
    const onPress = jest.fn()
    render(<MenuItem icon={User} label="Sair" onPress={onPress} />)

    fireEvent.press(screen.getByText("Sair"))

    expect(onPress).toHaveBeenCalledTimes(1)
  })
})
