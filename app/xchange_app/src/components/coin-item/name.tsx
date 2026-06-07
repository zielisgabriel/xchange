import { Text } from "../ui/text"

interface CoinItemNameProps {
  children: string
}

export function CoinItemName({children}: CoinItemNameProps) {
  return (
    <Text className="text-xs text-muted-foreground truncate w-22">
      {children}
    </Text>
  )
}