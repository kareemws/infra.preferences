package infra.preferences.manager

import android.content.Context
import infra.preferences.contract.PreferencesFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object PreferenceFilesManager {

    private var context: Context? = null
        set(value) {
            field = value
            initializePreferenceFiles()
        }

    private val preferenceFiles: ArrayList<PreferencesFile> = ArrayList()

    private fun initializePreferenceFiles() {
        GlobalScope.launch(Dispatchers.Default) {
            preferenceFiles.forEach {
                it.initializeFile(context!!)
            }
        }
    }

    fun attachPreferenceFile(preferencesFile: PreferencesFile) {
        preferenceFiles.add(preferencesFile)
        context?.let {
            GlobalScope.launch(Dispatchers.Default) {
                preferencesFile.initializeFile(it)
            }
        }
    }

    fun detachPreferenceFile(preferencesFile: PreferencesFile) {
        preferenceFiles.remove(preferencesFile)
    }
}