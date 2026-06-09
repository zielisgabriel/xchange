const currencyFormat = new Intl.NumberFormat("en-US", {
  currency: "USD",
  style: "currency",
  maximumFractionDigits: 7,
})

export {currencyFormat}