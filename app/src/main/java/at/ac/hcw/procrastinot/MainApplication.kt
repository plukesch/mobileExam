/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.ac.hcw.procrastinot

import android.app.Application
import at.ac.hcw.procrastinot.data.TaskRepository
import at.ac.hcw.procrastinot.di.ApplicationScope
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

/**
 * Application that sets up Timber in the DEBUG BuildConfig.
 * Read Timber's documentation for production setups.
 */
@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        applicationScope.launch {
            seedDatabaseIfEmpty()
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        if (taskRepository.getTasks().isEmpty()) {
            taskRepository.createTask(
                title = "Buy groceries",
                description = "Get milk, eggs, and bread from the store"
            )
            taskRepository.createTask(
                title = "Learn MAD!",
                description = "Study Jetpack Compose and Android architecture"
            )
            taskRepository.createTask(
                title = "Finish the Android App",
                description = "Complete all remaining exam tasks"
            )
            taskRepository.createTask(
                title = "Read the documentation",
                description = "Go through the official Android developer docs"
            )
        }
    }
}

