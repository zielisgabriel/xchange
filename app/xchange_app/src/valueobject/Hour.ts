import { Time } from "./Time";

export class Hour implements Time {
  private hours: number

  constructor(hours: number) {
    this.hours = hours
  }
  
  public toMilliseconds(): number {
    return this.hours * 60 * 60 * 1000
  }
}