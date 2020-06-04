package com.example.petland.pet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.parse.ParseQuery
import com.parse.ParseUser

class SearchCaregiversActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_caregivers)
        search()
    }

    private fun search() {
        val myPet = intent.extras?.get("pet") as Pet
        val listUsers = ParseQuery.getQuery<ParseUser>("_User")
        listUsers.orderByAscending("username")
        listUsers.whereDoesNotMatchKeyInQuery("objectId", "objectId", myPet.getCaregiversQuery())
        val list = listUsers.find()

        viewManager = LinearLayoutManager(this)
        viewAdapter = CaregiversAdapter(list, myPet)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView2).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
