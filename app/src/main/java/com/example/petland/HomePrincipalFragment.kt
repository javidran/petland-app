package com.example.petland

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petland.image.ImageActivity
import com.example.petland.image.ImageUtils
import com.parse.ParseUser
import kotlinx.android.synthetic.main.fragment_user_profile.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePrincipalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePrincipalFragment : Fragment() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       rootView = inflater.inflate(R.layout.fragment_home_principal, container, false)
      //  rootView.profileImage.setOnClickListener { seeImage() }

        rootView.recyclerView.layoutManager = layoutManager
        return rootView
    }
    override fun onResume() {
        super.onResume()
        setPetInfo()

    }
    private fun setPetInfo() {
        val user = ParseUser.getCurrentUser()

        val petNameText: TextView = rootView.findViewById(R.id.petName)
        petNameText.text = "hola"

        val birthDayText: TextView = rootView.findViewById(R.id.birthday)
        birthDayText.text = "hola"

      //  val imageUtils = ImageUtils()
       // imageUtils.retrieveImage(user, rootView.profileImage, this)
    }
    private fun seeImage() {
        val intent = Intent(context, ImageActivity::class.java).apply {}
        val user = ParseUser.getCurrentUser()
        intent.putExtra("object", user)
        startActivity(intent)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
           HomePrincipalFragment().apply {}
    }
}
