/*
 * This file is part of SkyStory, licensed under the Apache License 2.0.
 * see LICENSE.
 */
package com.github.shur.skystory

/**
 * 通信中に発生した例外
 *
 * @property originalException 発生した元の例外
 */
class HttpConnectionException(val originalException: Exception) : RuntimeException()