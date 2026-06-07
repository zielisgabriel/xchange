import { Text } from "../ui/text";

interface CoinItemPriceProps {
  children: string
}

export function CoinItemPrice({children}: CoinItemPriceProps) {
  return (
    <Text className="text-sm font-medium">
      {children}
    </Text>
  )
}