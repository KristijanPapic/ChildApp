package com.example.pazitelj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pazitelj.models.Pet
import com.google.android.material.button.MaterialButton

class PetListAdapter(
    private val pets: List<Pet>,
    private final val iPetListAdapter: IPetListAdapter
) : RecyclerView.Adapter<PetListAdapter.PetItemViewHolder>() {

    class PetItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val petName = view.findViewById<TextView>(R.id.pet_item_name)
        val petType = view.findViewById<TextView>(R.id.pet_item_type)
        val petBreed = view.findViewById<TextView>(R.id.pet_item_breed)
        val petIcon = view.findViewById<ImageView>(R.id.pet_icon)
        val petDelete = view.findViewById<MaterialButton>(R.id.pet_item_delete_button)
    }

    override fun getItemCount(): Int {
        return pets.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetItemViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.pet_item, parent,false)

        return PetItemViewHolder(layout)
    }

    override fun onBindViewHolder(holder: PetItemViewHolder, position: Int) {
        val item = pets[position]
        holder.petName.text = item.Name
        holder.petType.text = item.Kind
        holder.petBreed.text = item.Breed
        when(item.Kind){
            "Male" -> holder.petIcon.setImageResource(R.drawable.boy)
            "Female" -> holder.petIcon.setImageResource(R.drawable.girl)
        }
        holder.petDelete.setOnClickListener {
            iPetListAdapter.onPetDelete(position)
        }
    }
}