package com.example.pazitelj.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pazitelj.R
import com.example.pazitelj.databinding.FragmentAvailableAdsBinding
import com.example.pazitelj.models.AdFilter
import com.example.pazitelj.models.User
import com.example.pazitelj.ui.CurrentUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import java.text.FieldPosition


class AvailableAdsFragment : Fragment(),IAdListAdapter {

    private var _binding: FragmentAvailableAdsBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val currentUser: CurrentUserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvailableAdsBinding.inflate(inflater, container, false)

        val root: View = binding.root
        Log.d("created","crated")

        val userObserver = Observer<User> {getAds()}
        currentUser.user.observe(viewLifecycleOwner,userObserver)

        return root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser.user.value?.let {
            getAds()
        }
        val userObserver = Observer<User>{user -> getAds()}
        currentUser.user.observe(viewLifecycleOwner,userObserver)

        binding.petRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.petRecyclerView.adapter = PetAdAdapter(this)

        val filterObserver = Observer<Boolean> { newFilter -> changeAdapter(newFilter)  }
        homeViewModel.filter.observe(viewLifecycleOwner, filterObserver)

        binding.refreshBtn.setOnClickListener {
            getAds()
        }
        //homeViewModel.filter.observe(viewLifecycleOwner, Observer { changeAdapter() })
    }

    override fun onResume() {
        super.onResume()
        Log.d("resume","resume")
        Log.d("fragads",binding.homeModel.toString())
        Log.d("adapterdata",binding.petRecyclerView.adapter.toString())
        Log.d("adapter",binding.petRecyclerView.adapter.toString())
        homeViewModel.reset()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        Log.d("des","des")
    }

    fun getAds()
    {
        if(currentUser.user.value != null){
            CoroutineScope(Dispatchers.Main).launch {
                homeViewModel.getModelAds(AdFilter(1,currentUser.user.value!!.Id))
                binding.lifecycleOwner = super.getParentFragment()
                binding.homeModel = homeViewModel
            }
        }

    }
    fun changeAdapter(filter: Boolean){
        if(currentUser.user.value != null){
            if(filter){
                homeViewModel.getModelAds(AdFilter(1,currentUser.user.value!!.Id))
                binding.petRecyclerView.adapter = PetAdAdapter(this)
                //homeViewModel.reset()
            }
            else{
                homeViewModel.getModelAds(AdFilter(0,currentUser.user.value!!.Id))
                binding.petRecyclerView.adapter = CarerAdAdapter(this)
            }
            }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.more -> {
                Log.d("notify","notify")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

    override fun openAd(adId: String, type: Int) {
        if(type == 1){
            val action = HomeFragmentDirections.actionFragmentHomeToFullOwnerAdFragment(id = adId)
            findNavController().navigate(action)
        }
        else{
            val action = HomeFragmentDirections.actionFragmentHomeToFullCarerAdFragment(id = adId)
            findNavController().navigate(action)
        }
    }

    override fun deleteAd(id: String,position: Int) {
        TODO("Not yet implemented")
    }

    override fun cancelAd(adId: String, position: Int) {
        TODO("Not yet implemented")
    }

    override fun openUser(userId: String) {
        TODO("Not yet implemented")
    }

    override fun concludeAd(
        AdId: String,
        UserId: String,
        Type: Int,
        IsPoster: Boolean,
        Position: Int
    ) {
        TODO("Not yet implemented")
    }


}