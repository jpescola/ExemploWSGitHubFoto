package com.example.exemplowsgithubfoto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

class MainActivity : AppCompatActivity() {

    var usuario = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buscar(view: View){
        // recupera o login digitado pelo usuário
        var txtLogin = findViewById<EditText>(R.id.txtLogin)
        usuario = txtLogin.text.toString()

        // cria o objeto 'service', especificando a URL e a interface
        val service = Retrofit.Builder()
            .baseUrl("https://api.github.com/") // URL usada
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UserService::class.java) // nossa interface

        service.getUser("/users/"+usuario).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val dadosRecebidos = response.body()
                val nome = dadosRecebidos?.name
                val foto = dadosRecebidos?.avatar_url
                findViewById<TextView>(R.id.txtNome).text = nome
                val imgFoto = findViewById<ImageView>(R.id.imgFoto)
                Glide.with(applicationContext).load(foto).into(imgFoto);

            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}


// controller (dados que queremos buscar no WS)
data class UserResponse(val login: String, val name: String, val avatar_url: String)

// interface de mapeamento do WS
interface UserService {
    @GET
    fun getUser(@Url url: String): Call<UserResponse> // método que chamará o nosso controller
}