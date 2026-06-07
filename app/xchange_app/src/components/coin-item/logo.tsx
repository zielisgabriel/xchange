import { Avatar, AvatarImage } from "../ui/avatar";

interface CoinItemLogoProps {
  name: string,
  imageUrl: string
}

export function CoinItemLogo({name, imageUrl}: CoinItemLogoProps) {
  return (
    <Avatar alt={name} className="w-9 h-9">
      <AvatarImage source={{ uri: imageUrl }} />
    </Avatar>
  )
}