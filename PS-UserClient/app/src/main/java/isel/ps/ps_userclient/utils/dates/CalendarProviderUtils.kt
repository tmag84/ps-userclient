package isel.ps.ps_userclient.utils.dates

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import isel.ps.ps_userclient.models.ListEvents
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
import android.app.Activity

class CalendarProviderUtils {
    companion object {
        private fun CheckPermission(ctx: Context, Permission: String): Boolean {
            return ContextCompat.checkSelfPermission(ctx,Permission) == PackageManager.PERMISSION_GRANTED
        }

        private fun RequestPermission(ctx: Context, permissions: Array<String>, Code: Int) {
            if (ContextCompat.checkSelfPermission(ctx, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((ctx as Activity),permissions[0])) {
                }
                else {
                    ActivityCompat.requestPermissions(ctx, permissions, Code)
                }
            }
        }

        fun getEventIdFromCalendar(ctx:Context, event: ListEvents) : Long {
            var eventId : Long = 0
            val CALENDAR_PROJECTION = arrayOf(
                    Events._ID
            )

            val selection = "${Events.DTSTART}=? AND ${Events.DTEND}=? AND ${Events.DESCRIPTION}=? AND ${Events.EVENT_LOCATION}=?"
            val selectionArgs = arrayOf(
                    (event.event_begin*1000L).toString(),
                    (event.event_end*1000L).toString(),
                    event.text,
                    event.service_location
            )

            val permissions = arrayOf(
                    android.Manifest.permission.READ_CALENDAR
            )

            if (!CheckPermission(ctx, android.Manifest.permission.READ_CALENDAR)) {
                RequestPermission(ctx, permissions, 123)
            }

            ctx.contentResolver.query(
                        Events.CONTENT_URI,
                        CALENDAR_PROJECTION,
                        selection,
                        selectionArgs,
                        null
                )?.use { cursor ->
                while(cursor.moveToFirst()) {
                    eventId = cursor.getLong(0)
                    break
                }
            }
            return eventId
        }

        fun insertEventIntoCalendar(ctx:Context, event:ListEvents) {
            val calendar_intent = Intent(Intent.ACTION_INSERT)
                    .setData(Events.CONTENT_URI)
                    .putExtra(Events.TITLE, "Evento do serviço ${event.service_name}")
                    .putExtra(Events.EVENT_LOCATION,event.service_location)
                    .putExtra(Events.DESCRIPTION, event.text)
                    .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY,false)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,event.event_begin * 1000L)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,event.event_end * 1000L)

            ctx.startActivity(calendar_intent)
        }

        fun deleteEventFromCalendar(ctx:Context, eventID:Long) {
            val uri = ContentUris.withAppendedId(Events.CONTENT_URI,eventID)
            ctx.contentResolver.delete(uri,null,null)
        }
    }
}
