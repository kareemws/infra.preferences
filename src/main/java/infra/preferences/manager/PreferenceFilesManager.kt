package infra.preferences.manager

import android.content.Context
import infra.preferences.contract.PreferencesFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object PreferenceFilesManager {

    var context: Context? = null
        set(value) {
            field = value
            initializePreferenceFiles()
        }

    private val preferenceFiles: ArrayList<PreferencesFile> = ArrayList()

    private fun initializePreferenceFiles() {
        preferenceFiles.forEach {
            it.initializeFile(context!!)
        }
    }

    fun initializePreferenceFile(preferencesFile: PreferencesFile) {
        context?.let {
            preferencesFile.initializeFile(it)
        } ?: kotlin.run {
            preferenceFiles.add(preferencesFile)
        }
    }
}