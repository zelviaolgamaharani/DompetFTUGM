package com.teknikugm.dompetft

import android.app.AlertDialog
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.teknikugm.dompetft.retrofit.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Response

class Profile : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtusername_profile.text = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "None")
        txtname_profile.text = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.name, "None")

        btn_logout.setOnClickListener(){

            AlertDialog.Builder(context)
                .setMessage("Close?")
                .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                    context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.edit{clear()}
                    startActivity(Intent(context, Login::class.java))
                }
                .setNegativeButton(android.R.string.cancel) { dialog, whichButton ->

                }
                .show()
        }

        val a = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "None")

        nampilinSaldo_Profile(a.toString())
    }

    fun nampilinSaldo_Profile (key : String){
        lateinit var myAPI: API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)

        myAPI.getsaldo(key).enqueue(object : retrofit2.Callback<ResponseSaldo>{

            override fun onFailure(call: Call<ResponseSaldo>, t: Throwable) {
                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseSaldo>, response: Response<ResponseSaldo>) {
                val a = response.body()?.balance.toString().toInt()
                txtsaldo_profile.text = Currency.toRupiahFormat2(a).replace("$", "Rp ").replace(",", ".")
            }
        })
    }
}