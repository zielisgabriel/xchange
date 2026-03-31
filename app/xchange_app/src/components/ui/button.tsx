import { ReactNode } from "react";
import { Text, Pressable, type ViewStyle } from "react-native";
import { type VariantProps, tv } from "tailwind-variants";
import { twMerge } from "tailwind-merge";

const buttonVariants = tv({
  slots: {
    container: "rounded-full transition-all ease-in-out items-center justify-center",
    text: "font-semibold"
  },
  variants: {
    variant: {
      default: {
        container: "bg-primary active:bg-primary-muted",
        text: "text-primary-foreground"
      },
      secondary: {
        container: "bg-foreground active:bg-foreground-muted",
        text: "text-background"
      },
      outline: {
        container: "border-2 border-primary bg-transparent active:bg-background-muted",
        text: "text-primary"
      },
      ghost: {
        container: "bg-transparent active:bg-background-muted",
        text: "text-foreground"
      }
    },
    size: {
      sm: { container: "px-3 py-1.5", text: "text-sm" },
      md: { container: "px-4 py-2", text: "text-base" },
      lg: { container: "px-5 py-3", text: "text-lg" }
    }
  },
  defaultVariants: {
    variant: "default",
    size: "md"
  }
})

type ButtonProps = VariantProps<typeof buttonVariants> & { 
  children: ReactNode; 
  className?: string;
  textClassName?: string;
  style?: ViewStyle;
  onPress?: () => void;
};

export function Button({ children, variant, size, className, textClassName, style, onPress }: ButtonProps) {
  const { container, text } = buttonVariants({ variant, size });

  return (
    <Pressable className={twMerge(container({ className }))} style={style} onPress={onPress}>
        <Text className={twMerge(text({ className: textClassName }))}>
          {children}
        </Text>
    </Pressable>
  )
}