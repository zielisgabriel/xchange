import { Text } from "@/components/ui/text";

interface CoinItemRank {
  children: string | number
}

export function CoinItemRank({children}: CoinItemRank) {
  return (
    <Text className="text-xs text-muted-foreground/70 w-5 text-center">
      {children}
    </Text>
  )
}