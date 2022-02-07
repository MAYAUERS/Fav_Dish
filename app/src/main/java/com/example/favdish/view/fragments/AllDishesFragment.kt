package com.example.favdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.R
import com.example.favdish.adapter.FavDishAdapter
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentAllDishesBinding
import com.example.favdish.view.activities.AddUpdateActivity
import com.example.favdish.viewModel.FavDishViewModel
import com.example.favdish.viewModel.FavDishViewModelFactory
//import com.example.favdish.databinding.FragmentHomeBinding

class AllDishesFragment : Fragment() {

    private lateinit var mBinding: FragmentAllDishesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    //  private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
  //  private val binding get() = _binding!!

    private val mFavDishViewModel:FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentAllDishesBinding.inflate(inflater, container, false)
        val root: View = mBinding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.reclerViewShowAddedDish.layoutManager=GridLayoutManager(requireContext(),2)

        val favDishAdapter= FavDishAdapter(this@AllDishesFragment)

        mBinding.reclerViewShowAddedDish.adapter=favDishAdapter


        mFavDishViewModel.allDisgesList.observe(viewLifecycleOwner){
            dishes->
            dishes.let {
               /* for (item in it){
                    Log.i("New added Items","${item.id}::${item.title}")
                }*/
                if (it.isNotEmpty()){
                    mBinding.reclerViewShowAddedDish.visibility=View.VISIBLE
                        mBinding.tvNoDishesAddedYet.visibility=View.GONE
                    favDishAdapter.disheList(it)
                }else{
                    mBinding.reclerViewShowAddedDish.visibility=View.GONE
                    mBinding.tvNoDishesAddedYet.visibility=View.VISIBLE
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
       // _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_add_dish->{
                startActivity(Intent(requireContext(),AddUpdateActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }


}