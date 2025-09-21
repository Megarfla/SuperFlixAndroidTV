package com.superflix.app

// Model atualizado para filmes, séries, animes e TV ao vivo (campos baseados em exemplos da API)

data class Filme(
    val id: String,
    val title: String,
    val poster: String
)

data class FilmeDetalhe(
    val id: String,
    val title: String,
    val description: String,
    val poster: String,
    val link: String
)

// Séries
data class Serie(
    val id: String,
    val title: String,
    val poster: String
)

data class SerieDetalhe(
    val id: String,
    val title: String,
    val description: String,
    val poster: String,
    val seasons: List<Int>?
)

data class EpisodioDetalhe(
    val id: String,
    val title: String,
    val description: String,
    val link: String
)

// TV ao vivo
data class TvCanal(
    val id: String,
    val name: String,
    val logo: String,
    val link: String
)
