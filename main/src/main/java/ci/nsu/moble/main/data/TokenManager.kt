package ci.nsu.moble.main.data

import android.content.Context
import android.content.SharedPreferences

object TokenManager {

    private const val PREF_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"
    private const val USER_ID_KEY = "user_id"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var token: String?
        get() = prefs.getString(TOKEN_KEY, null)
        set(value) {
            prefs.edit().putString(TOKEN_KEY, value).apply()
        }

    var userId: Long
        get() = prefs.getLong(USER_ID_KEY, 0L)
        set(value) {
            prefs.edit().putLong(USER_ID_KEY, value).apply()
        }

    fun clearToken() {
        prefs.edit().clear().apply()
    }
}