package com.superflix.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerFilmes: RecyclerView
    private lateinit var recyclerSeries: RecyclerView
    private lateinit var recyclerAnimes: RecyclerView
    private lateinit var recyclerTvVivo: RecyclerView

    private lateinit var adapterFilmes: MovieAdapter
    private lateinit var adapterSeries: MovieAdapter
    private lateinit var adapterAnimes: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerFilmes = findViewById(R.id.recyclerFilmes)
        recyclerSeries = findViewById(R.id.recyclerSeries)
        recyclerAnimes = findViewById(R.id.recyclerAnimes)
        recyclerTvVivo = findViewById(R.id.recyclerTvVivo)

        recyclerFilmes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerSeries.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerAnimes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerTvVivo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapterFilmes = MovieAdapter(this, listOf()) { filme -> abrirDetalhe(filme) }
        adapterSeries = MovieAdapter(this, listOf()) { filme -> abrirSerie(filme) }
        adapterAnimes = MovieAdapter(this, listOf()) { filme -> abrirDetalhe(filme) }

        recyclerFilmes.adapter = adapterFilmes
        recyclerSeries.adapter = adapterSeries
        recyclerAnimes.adapter = adapterAnimes

        carregarTudo()
    }

    private fun carregarTudo() {
        val service = ApiClient.retrofit.create(ApiService::class.java)

        // Filmes
        service.getConteudo("movie").enqueue(object: Callback<List<Filme>> {
            override fun onResponse(call: Call<List<Filme>>, response: Response<List<Filme>>) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: listOf()
                    adapterFilmes.update(lista)
                } else {
                    Log.e("MainActivity","Erro filmes: ${'$'}{response.code()}" )
                }
            }
            override fun onFailure(call: Call<List<Filme>>, t: Throwable) {
                Log.e("MainActivity","Falha filmes: ${'$'}{t.message}" )
            }
        })

        // Séries
        service.getConteudo("serie").enqueue(object: Callback<List<Filme>> {
            override fun onResponse(call: Call<List<Filme>>, response: Response<List<Filme>>) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: listOf()
                    adapterSeries.update(lista)
                } else {
                    Log.e("MainActivity","Erro series: ${'$'}{response.code()}" )
                }
            }
            override fun onFailure(call: Call<List<Filme>>, t: Throwable) {
                Log.e("MainActivity","Falha series: ${'$'}{t.message}" )
            }
        })

        // Animes
        service.getConteudo("anime").enqueue(object: Callback<List<Filme>> {
            override fun onResponse(call: Call<List<Filme>>, response: Response<List<Filme>>) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: listOf()
                    adapterAnimes.update(lista)
                } else {
                    Log.e("MainActivity","Erro animes: ${'$'}{response.code()}" )
                }
            }
            override fun onFailure(call: Call<List<Filme>>, t: Throwable) {
                Log.e("MainActivity","Falha animes: ${'$'}{t.message}" )
            }
        })

        // TV ao vivo - usa model TvCanal
        service.getTvAoVivo().enqueue(object: Callback<List<TvCanal>> {
            override fun onResponse(call: Call<List<TvCanal>>, response: Response<List<TvCanal>>) {
                if (response.isSuccessful) {
                    val canais = response.body() ?: listOf()
                    // converter para Filme model simples para reaproveitar adapter (id/title/poster)
                    val listaSimples = canais.map { c -> Filme(c.id, c.name, c.logo) }
                    adapterFilmes.update(adapterFilmes.items + listOf()) // manter assinatura
                    // vamos criar um adapter simples para tv
                    recyclerTvVivo.adapter = TvAdapter(this@MainActivity, canais) { canal -> abrirTv(canal) }
                } else {
                    Log.e("MainActivity","Erro tv: ${'$'}{response.code()}" )
                }
            }
            override fun onFailure(call: Call<List<TvCanal>>, t: Throwable) {
                Log.e("MainActivity","Falha tv: ${'$'}{t.message}" )
            }
        })
    }

    private fun abrirDetalhe(filme: Filme) {
        val service = ApiClient.retrofit.create(ApiService::class.java)
        service.getFilme(filme.id).enqueue(object: Callback<FilmeDetalhe> {
            override fun onResponse(call: Call<FilmeDetalhe>>, response: Response<FilmeDetalhe>) {
                if (response.isSuccessful) {
                    val detalhe = response.body()
                    val url = detalhe?.link
                    if (!url.isNullOrEmpty()) {
                        val intent = Intent(this@MainActivity, PlayerActivity::class.java)
                        intent.putExtra("url", url)
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<FilmeDetalhe>>, t: Throwable) {
                Log.e("MainActivity","Erro detalhe: ${'$'}{t.message}" )
            }
        })
    }

    private fun abrirSerie(filme: Filme) {
        // Aqui usamos o mesmo id como TMDB id; poderia haver adaptação
        val intent = Intent(this@MainActivity, SerieActivity::class.java)
        intent.putExtra("serie_id", filme.id)
        startActivity(intent)
    }

    private fun abrirTv(canal: TvCanal) {
        val intent = Intent(this@MainActivity, PlayerActivity::class.java)
        intent.putExtra("url", canal.link)
        startActivity(intent)
    }
}
