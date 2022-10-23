package com.potus.potus_front.data.remote

import com.potus.potus_front.data.remote.dto.PostRequest
import com.potus.potus_front.data.remote.dto.PostResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

class PostsServiceImpl(
    private val client: HttpClient
) : PostsService {

    override suspend fun getPosts(): List<PostResponse> {
        return try {
            client.get { url(HttpRoutes.POSTS) }
        } catch(e: RedirectResponseException) {
            // RESPONSE CODE: 3xx
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ClientRequestException) {
            // RESPONSE CODE: 4xx
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ServerResponseException) {
            // RESPONSE CODE: 5xx
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: Exception) {
            // GENERIC EXCEPTION
            println("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun createPosts(postRequest: PostRequest): PostResponse? {
        return try {
            client.post<PostResponse> {
                url(HttpRoutes.POSTS)
                contentType(ContentType.Application.Json)
                body = postRequest
            }
        } catch(e: RedirectResponseException) {
            // RESPONSE CODE: 3xx
            println("Error: ${e.response.status.description}")
            null
        } catch(e: ClientRequestException) {
            // RESPONSE CODE: 4xx
            println("Error: ${e.response.status.description}")
            null
        } catch(e: ServerResponseException) {
            // RESPONSE CODE: 5xx
            println("Error: ${e.response.status.description}")
            null
        } catch(e: Exception) {
            // GENERIC EXCEPTION
            println("Error: ${e.message}")
            null
        }
    }
}