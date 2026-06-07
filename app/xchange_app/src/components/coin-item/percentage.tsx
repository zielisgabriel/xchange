import { ChevronDown, ChevronUp } from "lucide-react-native"
import { Text } from "../ui/text"
import { View } from "react-native"

interface CoinItemPercentageProps {
  children: number
}

export function CoinItemPercentage({children}: CoinItemPercentageProps) {
  const isPositive = (value: number) => value >= 0

  const percentageFormat = new Intl.NumberFormat("pt-BR", {
    style: "percent",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  })

  return (
    <View className="flex-row items-center">
      {isPositive(children) ? (
        <ChevronUp size={12} color="#22c55e" />
      ) : (
        <ChevronDown size={12} color="#ef4444" />
      )}
      <Text
        className="text-xs font-medium"
        style={{
          color: isPositive(children)
            ? "#22c55e"
            : "#ef4444",
        }}
      >
        {percentageFormat.format(
          Math.abs(children)
        )}
      </Text>
    </View>
  )
}