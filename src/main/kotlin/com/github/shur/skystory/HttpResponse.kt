/*
 * This file is a part of SkyStory, licensed under the Apache License 2.0.
 * see LICENSE.
 */
package com.github.shur.skystory

import java.net.URL

/**
 * HTTPレスポンス
 *
 * @property url URL
 * @property responseCode ステータスコード
 * @property responseMessage レスポンスメッセージ
 * @property headers ヘッダー
 * @property result 結果
 */
data class HttpResponse(
    val url: URL,
    val responseCode: Int,
    val responseMessage: String,
    val headers: Headers,
    val result: String
) {

    /**
     * このレスポンスが成功したかどうかを返す
     *
     * @return このレスポンスが成功したかどうか
     */
    fun isSuccess() = responseCode >= 200 || responseCode < 400

}