package isel.ps.ps_userclient.utils.async

import android.content.Context
import android.os.AsyncTask
import isel.ps.ps_userclient.R
import isel.ps.ps_userclient.utils.dates.CalendarProviderUtils

class CalendarQueryAsync(val ctx:Context) : AsyncTask<AsyncData,String,Long>() {
    lateinit var data : AsyncData

    override fun doInBackground(vararg params: AsyncData): Long {
        data = params[0]
        val eventId = CalendarProviderUtils.getEventIdFromCalendar(ctx,data.event)
        return eventId
    }

    override fun onPostExecute(eventId: Long) {
        data.btn.isEnabled = true
        if (eventId>0) {
            data.btn.text = ctx.getString(R.string.btn_remove_event_calendar)}
        else {
            data.btn.text = ctx.getString(R.string.btn_add_event_calendar)
        }
    }
}