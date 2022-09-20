/*
 * *******************************************************************************************
 *  * Created By Debojyoti Singha
 *  * Copyright (c) 2022.
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 * ******************************************************************************************
 */

package com.swing.githubmanager.data.utils

import com.google.gson.JsonSyntaxException
import com.swing.githubmanager.data.constants.GMRemoteConstants.CONNECT_EXCEPTION
import com.swing.githubmanager.data.constants.GMRemoteConstants.PROTOCOL_EXCEPTION
import com.swing.githubmanager.data.constants.GMRemoteConstants.SOCKET_TIME_OUT_EXCEPTION
import com.swing.githubmanager.data.constants.GMRemoteConstants.UNKNOWN_NETWORK_EXCEPTION
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.*

object GMExceptionHandler {
    fun extractErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is ConnectException -> CONNECT_EXCEPTION
            is UnknownHostException -> {
                CONNECT_EXCEPTION
            }
            is SocketTimeoutException -> SOCKET_TIME_OUT_EXCEPTION
            is UnknownServiceException -> throwable.localizedMessage
                ?: UNKNOWN_NETWORK_EXCEPTION
            is ProtocolException -> PROTOCOL_EXCEPTION
            is HttpException -> {
                val response = throwable.response()
                try {
                    val json = JSONObject(response?.errorBody()?.string()!!)
                    json["message"] as String
                } catch (exception: JSONException) {
                    exception.localizedMessage
                }
            }
            is JsonSyntaxException -> throwable.message ?: ""
            else -> UNKNOWN_NETWORK_EXCEPTION
        }
    }
}

class CustomException(override val message: String, val throwable: Throwable? = null) :
    Exception(message)
