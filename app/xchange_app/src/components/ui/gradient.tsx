import { LinearGradient } from "expo-linear-gradient"
import { withUniwind } from "uniwind"

/**
 * `LinearGradient` do `expo-linear-gradient` com suporte a `className` (Uniwind).
 *
 * O Uniwind só intercepta `className` nos componentes core do React Native
 * (View, Text, ScrollView, ...). Componentes de terceiros como o `LinearGradient`
 * ignoram `className` no nativo (na web funciona porque o react-native-web repassa
 * a classe pro DOM). `withUniwind` resolve isso convertendo `className` em `style`,
 * mantendo a paridade entre web e nativo.
 */
export const Gradient = withUniwind(LinearGradient)
