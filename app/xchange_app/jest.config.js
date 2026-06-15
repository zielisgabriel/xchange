/** @type {import('jest').Config} */
module.exports = {
  preset: "jest-expo",
  setupFilesAfterEnv: ["<rootDir>/jest.setup.js"],
  moduleNameMapper: {
    "^@/(.*)$": "<rootDir>/src/$1",
  },
  transformIgnorePatterns: [
    "node_modules/(?!(" +
      [
        "(jest-)?react-native",
        "@react-native.*",
        "react-native-.*",
        "expo.*",
        "@expo.*",
        "@expo-google-fonts/.*",
        "@react-navigation/.*",
        "@rn-primitives/.*",
        "uniwind",
        "lucide-react-native",
        "sonner-native",
        "class-variance-authority",
        "tailwind-variants",
        "nativewind",
        "react-native-css-interop",
      ].join("|") +
      ")/)",
  ],
}
