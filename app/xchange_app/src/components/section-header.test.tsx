import { render, screen } from "@testing-library/react-native"
import { Star } from "lucide-react-native"
import { SectionHeader } from "./section-header"

describe("SectionHeader", () => {
  it("renders the title", () => {
    render(<SectionHeader icon={Star} title="Favoritos" />)

    expect(screen.getByText("Favoritos")).toBeTruthy()
  })

  it("renders the subtitle when provided", () => {
    render(<SectionHeader icon={Star} title="Moedas" subtitle="Mais populares" />)

    expect(screen.getByText("Mais populares")).toBeTruthy()
  })

  it("omits the subtitle when not provided", () => {
    render(<SectionHeader icon={Star} title="Tendências" />)

    expect(screen.queryByText("Mais populares")).toBeNull()
  })
})
