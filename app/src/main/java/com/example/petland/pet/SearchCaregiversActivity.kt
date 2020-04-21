package com.example.petland.pet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petland.R
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseRelation
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
        val myPet = intent.extras?.get("pet") as ParseObject
        val caregivers: ParseRelation<ParseUser> = myPet.getRelation<ParseUser>("caregivers")
        val listCaregivers = caregivers.query
   
        val listUsers = ParseQuery.getQuery<ParseUser>("_User")
        listUsers.orderByAscending("username")
        listUsers.whereDoesNotMatchKeyInQuery("objectId", "objectId", listCaregivers)

        val list = listUsers.find()

        if (list != null) {
            viewManager = LinearLayoutManager(this)
            viewAdapter = CaregiversAdapter(list.toList(), myPet)
            recyclerView = findViewById<RecyclerView>(R.id.recyclerView2).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // use a linear layout manager
                layoutManager = viewManager

                // specify an viewAdapter (see also next example)
                adapter = viewAdapter

            }
        }
    }
}
