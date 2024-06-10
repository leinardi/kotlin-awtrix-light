import org.quartz.Trigger
import org.quartz.TriggerBuilder
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

fun <T : Trigger> TriggerBuilder<T>.startAt(zonedDateTime: ZonedDateTime): TriggerBuilder<T> = this.startAt(Date.from(zonedDateTime.toInstant()))

fun <T : Trigger> TriggerBuilder<T>.startAt(localTime: LocalTime): TriggerBuilder<T> =
    startAt(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()))
