package com.example.petland

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.parse.ParseUser
import kotlinx.android.synthetic.main.content_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {

    private val TAG = "Petland EditProfile"
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    lateinit var date: Date


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_edit_profile, container, false)


        val cal = Calendar.getInstance()
        val user = ParseUser.getCurrentUser()
        view.editTextUsername.setText(user.get("username").toString())
        view.editTextEmail.setText(user.get("email").toString())
        val dateb = sdf.format(user.get("birthday"))
        date = user.get("birthday") as Date
        view.editTextBirthday.setText(dateb.toString())
        view.editTextName.setText(user.get("name").toString())
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                date = cal.time
                view.editTextBirthday.setText(sdf.format(cal.time))
            }

        view.editTextBirthday.setOnClickListener {
            val dialog = context?.let { it1 ->
                DatePickerDialog(
                    it1, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
            }
            if (dialog != null) {
                dialog.datePicker.maxDate = Date().time
                dialog.show()
            }
        }

        return view
    }

    fun edit (view: View) {
        val user= ParseUser.getCurrentUser()
        if (user != null) {
            user.username = editTextUsername.text.toString()
            user.email = editTextEmail.text.toString()
            user.put("name", editTextName.text.toString())
            user.put("birthday", date)
            user.save()
            Log.d(TAG, getString(R.string.profileEditedCorrectly))
            Toast.makeText(context, getString(R.string.profileEditedCorrectly), Toast.LENGTH_LONG).show()
        } else {
            Log.d(TAG, getString(R.string.userNotLogged))
        }
    }
//
//    fun volver(view: View) {
//        val intent = Intent(this, HomePrincipalActivity::class.java).apply {
//        }
//        startActivity(intent)
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
//    }
//
//    fun changepassword(view: View) {
//        val intent = Intent(this, ChangePasswordActivity::class.java).apply {
//        }
//        startActivity(intent)
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            EditProfileFragment().apply {}
    }
}
