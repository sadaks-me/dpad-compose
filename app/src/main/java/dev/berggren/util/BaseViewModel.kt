package dev.berggren.util

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.berggren.util.coroutines.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    app: Application,
    coroutineContextProvider: CoroutineContextProvider
) : AndroidViewModel(app), CoroutineScope {

    private val jobs = mutableListOf<Job>()
    override val coroutineContext: CoroutineContext = coroutineContextProvider.io

    protected fun load(job: Job) {
        job.apply {
            jobs.add(this)
            this.invokeOnCompletion { jobs.remove(this) }
        }
    }
}
