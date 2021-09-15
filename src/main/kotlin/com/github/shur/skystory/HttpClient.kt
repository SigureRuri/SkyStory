/*
 * This file is a part of SkyStory, licensed under the Apache License 2.0.
 * see LICENSE.
 */
package com.github.shur.skystory

import java.io.IOException
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL


typealias Headers = Map<String, String>
typealias Parameters = Map<String, String>


/**
 * 簡易的なHTTPクライアント
 *
 * @author ShuR
 */
class HttpClient {

    /**
     * GETメソッドのHTTPリクエストを送信する
     *
     * @param baseUrl 送信するURL
     * @param headers ヘッダー
     * @param parameters パラメータ
     * @throws HttpConnectionException 通信中にエラーが発生したとき
     */
    @Throws(HttpConnectionException::class)
    fun get(
        baseUrl: URL,
        headers: Headers,
        parameters: Parameters,
    ): HttpResponse {
        val stringUrl = parameters.map { "${it.key}=${it.value}" }.joinToString("&", "${baseUrl}?")
        val url = URL(stringUrl)
        return getDirectly(url, headers)
    }

    /**
     * POSTメソッドのHTTPリクエストを送信する
     *
     * @param url 送信するURL
     * @param headers ヘッダー
     * @param parameters パラメータ
     * @throws HttpConnectionException 通信中にエラーが発生したとき
     */
    @Throws(HttpConnectionException::class)
    fun post(
        url: URL,
        headers: Headers,
        parameters: Parameters
    ): HttpResponse {
        return postDirectly(
            url,
            headers,
            parameters.map { "${it.key}=${it.value}" }.joinToString("&")
        )
    }

    /**
     * POSTメソッドのHTTPリクエストをjsonと共に送信する
     *
     * @param url 送信するURL
     * @param headers ヘッダー
     * @param json json
     * @throws HttpConnectionException 通信中にエラーが発生したとき
     */
    @Throws(HttpConnectionException::class)
    fun postWithJson(
        url: URL,
        headers: Headers,
        json: String
    ): HttpResponse {
        return postDirectly(
            url,
            headers.toMutableMap().apply { put("Content-Type", "application/json; charset=utf-8") },
            json
        )
    }
    
    /**
     * PUTメソッドのHTTPリクエストを送信する
     *
     * @param url 送信するURL
     * @param headers ヘッダー
     * @param parameters パラメータ
     * @throws HttpConnectionException 通信中にエラーが発生したとき
     */
    @Throws(HttpConnectionException::class)
    fun put(
        url: URL,
        headers: Headers,
        parameters: Parameters
    ): HttpResponse {
        return putDirectly(
            url,
            headers,
            parameters.map { "${it.key}=${it.value}" }.joinToString("&")
        )
    }

    /**
     * PUTメソッドのHTTPリクエストをjsonと共に送信する
     *
     * @param url 送信するURL
     * @param headers ヘッダー
     * @param json json
     * @throws HttpConnectionException 通信中にエラーが発生したとき
     */
    @Throws(HttpConnectionException::class)
    fun putWithJson(
        url: URL,
        headers: Headers,
        json: String
    ): HttpResponse {
        return putDirectly(
            url,
            headers.toMutableMap().apply { put("Content-Type", "application/json; charset=utf-8") },
            json
        )
    }

    /**
     * DELETEメソッドのHTTPリクエストを送信する
     *
     * @param url 送信するURL
     * @param headers ヘッダー
     * @param parameters パラメータ
     * @throws HttpConnectionException 通信中にエラーが発生したとき
     */
    @Throws(HttpConnectionException::class)
    fun delete(
        url: URL,
        headers: Headers,
        parameters: Parameters
    ): HttpResponse {
        return deleteDirectly(
            url,
            headers,
            parameters.map { "${it.key}=${it.value}" }.joinToString("&")
        )
    }

    /**
     * DELETEメソッドのHTTPリクエストをjsonと共に送信する
     *
     * @param url 送信するURL
     * @param headers ヘッダー
     * @param json json
     * @throws HttpConnectionException 通信中にエラーが発生したとき
     */
    @Throws(HttpConnectionException::class)
    fun deleteWithJson(
        url: URL,
        headers: Headers,
        json: String
    ): HttpResponse {
        return deleteDirectly(
            url,
            headers.toMutableMap().apply { put("Content-Type", "application/json; charset=utf-8") },
            json
        )
    }


    private fun getDirectly(
        url: URL,
        headers: Headers
    ): HttpResponse {
        try {
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                headers.forEach { setRequestProperty(it.key, it.value) }
                doInput = true
                doOutput = false
                useCaches = false
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT
            }
            connection.connect()

            val response = createResponse(connection)

            connection.disconnect()

            return response
        } catch (e: Exception) {
            throw HttpConnectionException(e)
        }
    }

    private fun putDirectly(
        url: URL,
        headers: Headers,
        rawBody: String
    ): HttpResponse {
        try {
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "PUT"
                headers.forEach { setRequestProperty(it.key, it.value) }
                doInput = true
                doOutput = true
                useCaches = false
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT

                val printWriter = PrintWriter(outputStream).apply {
                    print(rawBody)
                }
                printWriter.close()
            }
            connection.connect()

            val response = createResponse(connection)

            connection.disconnect()

            return response
        } catch (e: Exception) {
            throw HttpConnectionException(e)
        }
    }

    private fun postDirectly(
        url: URL,
        headers: Headers,
        rawBody: String
    ): HttpResponse {
        try {
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                headers.forEach { setRequestProperty(it.key, it.value) }
                doInput = true
                doOutput = true
                useCaches = false
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT

                val printWriter = PrintWriter(outputStream).apply {
                    print(rawBody)
                }
                printWriter.close()
            }
            connection.connect()

            val response = createResponse(connection)

            connection.disconnect()

            return response
        } catch (e: Exception) {
            throw HttpConnectionException(e)
        }
    }

    private fun deleteDirectly(
        url: URL,
        headers: Headers,
        rawBody: String
    ): HttpResponse {
        try {
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "DELETE"
                headers.forEach { setRequestProperty(it.key, it.value) }
                doInput = true
                doOutput = true
                useCaches = false
                connectTimeout = CONNECT_TIMEOUT
                readTimeout = READ_TIMEOUT

                val printWriter = PrintWriter(outputStream).apply {
                    print(rawBody)
                }
                printWriter.close()
            }
            connection.connect()

            val response = createResponse(connection)

            connection.disconnect()

            return response
        } catch (e: Exception) {
            throw HttpConnectionException(e)
        }
    }

    private fun createResponse(connection: HttpURLConnection): HttpResponse {
        val url = connection.url
        val responseCode = connection.responseCode
        val responseMessage = connection.responseMessage
        val headers = mutableMapOf<String, String>().apply {
            connection.headerFields
                .filter { it.key != null }
                .forEach {
                    put(it.key, it.value.joinToString(" "))
                }
        }
        val result = try {
            val bytes = connection.inputStream.readBytes()
            String(bytes)
        } catch (e: IOException) {
            val bytes = connection.errorStream.readBytes()
            String(bytes)
        }

        return HttpResponse(url, responseCode, responseMessage, headers, result)
    }

    companion object {

        private const val CONNECT_TIMEOUT = 20 * 1000

        private const val READ_TIMEOUT = 20 * 1000

    }

}