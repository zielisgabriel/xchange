import { Time } from "./Time";

export class Minute implements Time {
  private minutes: number

  constructor(minutes: number) {
    this.minutes = minutes
  }

  public toMilliseconds(): number {
    return this.minutes * 60 * 1000
  }
}