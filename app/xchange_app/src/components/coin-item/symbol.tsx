import { Text } from "../ui/text";

interface CoinItemSymbolProps {
  children: string
}

export function CoinItemSymbol({children}: CoinItemSymbolProps) {
  return (
    <Text className="font-semibold uppercase tracking-wide text-sm">
      {children}
    </Text>
  )
}