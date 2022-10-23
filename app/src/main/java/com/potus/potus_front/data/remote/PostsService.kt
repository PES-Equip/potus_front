package com.potus.potus_front.data.remote

import com.potus.potus_front.data.remote.dto.PostRequest
import com.potus.potus_front.data.remote.dto.PostResponse
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*

interface PostsService {

    suspend fun getPosts(): List<PostResponse>

    suspend fun createPosts(PostRequest: PostRequest): PostResponse?

    companion object {
        fun create(): PostsService {
            return PostsServiceImpl (
                client = HttpClient(Android) {
                    //NOT NECESSARY
                    //install(Logging) {
                    //    level = LogLevel.ALL
                    //}
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}