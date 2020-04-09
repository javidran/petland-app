package com.example.petland

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_view_pet_profile.*
import java.text.SimpleDateFormat
import java.util.*

/*
    -VER TODA LA INFO DEL ANIMAL                                                 1ª VISTA
    -DARLE UN BOTON Y PODER EDITAR TODA LA INFO DEL ANIMAL                       1ª VISTA -> 2ª VISTA
    -PODER EDITAR LA INFO                                                        2ª VISTA
    -DAR A GUARDAR PARA PASARLO AL SERVER                                        2ª VISTA -> 1º VISTA
    -DAR CANCELAR PARA VOLVER A VER LA INFO DEL ANIMAL                           2ª VISTA -> 1º VISTA

    -EJEMPLO DE EDITAR CONTACTOS DE TELEFONO

    PARA ENVIAR:
        Intent intent = new Intent(Activity_Origen.this, Activity_Destino.class);
        intent.putExtra("parametro", "string");
        startActivity(intent);

    RECIBIR:
        getIntent().getExtras().getString("parametro");
 */
class ViewPetProfileActivity : AppCompatActivity() {

    private lateinit var pett:ParseObject
    private val TAG = "Petland EditPetProfile"
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pet_profile)
        setData()
    }

    private fun setData() {
        val pets = ParseQuery.getQuery<ParseObject>("Pet")
        pets.whereEqualTo("name", "lia")
        pett = pets.first
        val caregivers: ParseRelation<ParseUser> = pett.getRelation<ParseUser>("caregivers")
        val listCaregivers = caregivers.query

        textViewName.text = pett.get("name").toString()
        textViewOwner.text = listCaregivers.first.username
        val dateb = sdf.format(pett.get("birthday"))
        textViewBirth.text = dateb.toString()
        textViewChip.text = pett.get("chip").toString()

        val list = findViewById<TextView>(R.id.petCaregiversList)
        list.text = ""
        listCaregivers.findInBackground { result, e ->
            if (e == null) {
                for(el in result) {
                    list.text = (list.text as String).plus("- ".plus(el.username).plus("\n"))
                }
            } else {
                Log.d(TAG, "PetQuery not completed")
            }
        }
    }

    fun editPetProfile(view: View) {

        val intent = Intent(this, EditPetProfileActivity::class.java).apply {
        }
        intent.putExtra("petName", "lia");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }



}
