package com.superflix.app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("lista")
    fun getConteudo(
        @Query("category") category: String,
        @Query("format") format: String = "json"
    ): Call<List<Filme>>

    @GET("filme/{id}")
    fun getFilme(@Path("id") imdbId: String): Call<FilmeDetalhe>

    @GET("serie/{id}")
    fun getSerie(@Path("id") tmdbId: String): Call<SerieDetalhe>

    @GET("serie/{id}/{season}/{episode}")
    fun getEpisodio(
        @Path("id") tmdbId: String,
        @Path("season") season: Int,
        @Path("episode") episode: Int
    ): Call<EpisodioDetalhe>

    @GET("tv")
    fun getTvAoVivo(@Query("format") format: String = "json"): Call<List<TvCanal>>
}
