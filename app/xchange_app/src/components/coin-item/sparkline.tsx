import { Image } from "expo-image";

interface CoinItemSparklineProps {
  sparklineUri: string
}

export function CoinItemSparkline({sparklineUri}: CoinItemSparklineProps) {
  const sparklineStyle = { width: 80, height: 32 }

  return (
    <Image
      source={{ uri: sparklineUri }}
      style={sparklineStyle}
      contentFit="contain"
      transition={200}
      cachePolicy="memory-disk"
    />
  )
}