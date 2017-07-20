package isel.ps.ps_userclient.utils.dates

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        private fun getDateAsCalendarInMili(dt:Long) : Calendar {
            val cal_date = Calendar.getInstance()
            cal_date.timeInMillis = dt*1000
            return cal_date
        }

        private fun getTodayCalendar() = Calendar.getInstance()

        fun addSecondsToCurrentTime(value:Int) : Long {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND,value)
            return calendar.getTime().time
        }

        fun isDateAfterCurrentDay(dt:Long) = getDateAsCalendarInMili(dt).after(getTodayCalendar())

        fun unixToDate(dt:Long?) :String {
            if (dt==null) return ""
            val sdf = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.getDefault())
            val date = Date(dt*1000)
            return sdf.parse(date.toString()).toString()
        }

        fun getDurationAsString(dt:Long) : String
        {
            val hours = dt / 3600
            val minutes = (dt%3600) / 60

            if (hours==0L && minutes==0L) {
                return "Sem tempo definido"
            }

            var duration = ""

            if (hours>0) {
                duration+="${hours}horas"
                if (minutes>0) {
                    duration+=" e ${minutes}minutos"
                }
            }
            else {
                if (minutes>0) {
                    duration+="${minutes}minutos"
                }
            }
            return duration
        }
    }
}
