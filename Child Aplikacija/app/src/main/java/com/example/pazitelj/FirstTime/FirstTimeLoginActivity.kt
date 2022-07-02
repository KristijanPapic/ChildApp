package com.example.pazitelj.FirstTime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.pazitelj.IPetListAdapter
import com.example.pazitelj.MainActivity
import com.example.pazitelj.models.Pet
import com.example.pazitelj.PetListAdapter
import com.example.pazitelj.R
import com.example.pazitelj.databinding.ActivityFirstTimeLoginBinding
import com.example.pazitelj.databinding.AddPetPopupBinding
import com.example.pazitelj.ui.CurrentUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirstTimeLoginActivity : AppCompatActivity(),IPetListAdapter {
    private val currentUser: CurrentUserViewModel by viewModels()
    private lateinit var binding: ActivityFirstTimeLoginBinding

    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstTimeLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent?.extras?.getString("userId").toString()
        CoroutineScope(Dispatchers.Main).launch {
            currentUser.getUser(userId)
            updatePetList()
        }
        binding.firstAddPetButton.setOnClickListener {
            createNewPetDialog()
        }
        binding.proceedButton.setOnClickListener {
            if(PhoneNumberUtils.isGlobalPhoneNumber(binding.phoneInput.text.toString())){
                proceed()
            }
            else{
                binding.phoneInputLayout.error = "Not a valid phone number"
            }
        }


    }

    fun createNewPetDialog(){
        dialogBuilder = AlertDialog.Builder(this)
        val binding: AddPetPopupBinding = AddPetPopupBinding.inflate(LayoutInflater.from(this))
        dialogBuilder.setView(binding.root)
        dialog = dialogBuilder.create()
        dialog.show()

        binding.cancelButton.setOnClickListener{
            dialog.dismiss()
        }
        binding.addButton.setOnClickListener {
            var checkedType = ""
            when(binding.radioGroup.checkedRadioButtonId){
                R.id.dog_radio -> checkedType = "Male"
                R.id.cat_radio -> checkedType = "Female"
            }
            addPet(binding.petNameInput.text.toString(),checkedType,binding.petBreedInput.text.toString())
            dialog.dismiss()
        }
    }

    private fun addPet(name: String,type: String,breed: String){
        CoroutineScope(Dispatchers.Main).launch {
            currentUser.addPet(Pet(null,name,type,breed,currentUser.user.value!!.Id))
            updatePetList()
        }

    }

    private fun proceed(){
        currentUser.setPhone(binding.phoneInput.text.toString())
        currentUser.setBio(binding.bioInput.text.toString())
        currentUser.updateUser()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    override fun onPetDelete(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            currentUser.deletePet(position)
            binding.petList.adapter!!.notifyItemRemoved(position)
            //binding.petList.adapter=PetListAdapter(currentUser.user.value!!.Pets)
        }

    }
    private  fun updatePetList(){
        binding.petList.adapter = PetListAdapter(currentUser.user.value!!.Pets,this)
    }


}