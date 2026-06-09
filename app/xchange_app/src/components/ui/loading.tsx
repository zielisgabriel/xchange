import { useEffect, useRef } from "react";
import { Animated, Easing, View } from "react-native";
import { Loader } from "lucide-react-native";
import { cn } from "@/lib/utils";

interface LoadingProps {
  size?: number;
  color?: string;
  className?: string;
}

export function Loading({ size = 24, color = "currentColor", className }: LoadingProps) {
  const spinValue = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    const startAnimation = () => {
      spinValue.setValue(0);
      Animated.timing(spinValue, {
        toValue: 1,
        duration: 1000,
        easing: Easing.linear,
        useNativeDriver: true,
      }).start(({ finished }) => {
        if (finished) {
          startAnimation();
        }
      });
    };
    
    startAnimation();
  }, [spinValue]);

  const spin = spinValue.interpolate({
    inputRange: [0, 1],
    outputRange: ["0deg", "360deg"],
  });

  return (
    <View className={cn("items-center justify-center", className)}>
      <Animated.View style={{ transform: [{ rotate: spin }] }}>
        <Loader size={size} color={color} className="text-foreground" />
      </Animated.View>
    </View>
  );
}