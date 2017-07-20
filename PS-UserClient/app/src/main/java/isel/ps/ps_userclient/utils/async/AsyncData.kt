package isel.ps.ps_userclient.utils.async

import android.widget.Button
import isel.ps.ps_userclient.models.ListEvents

data class AsyncData(
        val event:ListEvents,
        val btn:Button
)