package isel.ps.ps_userclient.utils.async

import android.content.Context
import android.os.AsyncTask
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.utils.dates.CalendarProviderUtils

class CalendarOpAsync(val ctx:Context) : AsyncTask<AsyncData,Void,Long>() {
    lateinit var data : AsyncData

    override fun doInBackground(vararg params: AsyncData): Long {
        data = params[0]
        val eventId = CalendarProviderUtils.getEventIdFromCalendar(ctx,data.event)
        return eventId
    }

    override fun onPostExecute(eventId: Long) {
        if (eventId>0) {
            CalendarProviderUtils.deleteEventFromCalendar(ctx,eventId)
            data.btn.text = ctx.getString(R.string.btn_add_event_calendar)
        }
        else {
            CalendarProviderUtils.insertEventIntoCalendar(ctx,data.event)
            data.btn.text = ctx.getString(R.string.btn_remove_event_calendar)
        }
    }
}