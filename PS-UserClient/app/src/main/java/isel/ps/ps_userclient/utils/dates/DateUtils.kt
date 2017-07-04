package isel.ps.ps_userclient.utils.dates

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
    }
}
