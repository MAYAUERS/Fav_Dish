package com.example.favdish.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.favdish.R
import com.example.favdish.databinding.ActivityAddUpdateDishBinding
import com.example.favdish.databinding.DialogCustomPhotoCameraBinding
import com.karumi.dexter.Dexter
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.favdish.adapter.CustomListItemAdapter
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.DialogCustomListBinding
import com.example.favdish.model.FavDish
import com.example.favdish.util.Constants
import com.example.favdish.viewModel.FavDishViewModel
import com.example.favdish.viewModel.FavDishViewModelFactory
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateActivity : AppCompatActivity(),View.OnClickListener {

    companion object{
       private const val CAMERA=1
        private const val GALLERY=2
        private const val IMAGE_DIRECTORY="FavDishImage"
    }

    private val mFavDishViewModel :FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as FavDishApplication).repository)
    }

    private var mImagePath:String=" "

    private lateinit var mBinding:ActivityAddUpdateDishBinding

    private lateinit var mCustomListDialog :Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpActionBar()
        mBinding.ivAddDishImage.setOnClickListener(this)

        mBinding.etTitel.setOnClickListener(this)
        mBinding.etCategory.setOnClickListener(this)
        mBinding.etCookingTime.setOnClickListener(this)
        mBinding.btnAddDish.setOnClickListener(this)
        mBinding.etType.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK){
            if (requestCode== CAMERA)
            {
                data?.extras?.let {
                    val thumbnail:Bitmap=data.extras?.get("data") as Bitmap
                  //  mBinding.ivDishImage.setImageBitmap(thumbnail)
                  //  mBinding.ivDishImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_edit))
                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .into(mBinding.ivDishImage)

                    mImagePath= saveImageToInternalStorage(thumbnail)

                }

            }
            if (requestCode== GALLERY)
            {
                data?.let {

                    val selectedPhotoUri=data.data
                   // mBinding.ivDishImage.setImageURI(selectedPhotoUri)

                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable>{
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                               Log.e("TAG","Error Loading Image",e)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap:Bitmap =resource.toBitmap()
                                    mImagePath=saveImageToInternalStorage(bitmap)
                                    Log.e("ImagePath",mImagePath)
                                }
                              return false
                            }

                        })
                        .into(mBinding.ivDishImage)
                 //   mBinding.ivDishImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_edit))
                }

            }
        }else if(resultCode==Activity.RESULT_CANCELED){
            Log.e("canceled","User canceled image selection")
        }
    }

    private fun setUpActionBar(){
        setSupportActionBar(mBinding.toolbarAddUpdateDish)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddUpdateDish.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.ivAddDishImage->{
                  customImageSelectionDialog()
                    return
                   // Toast.makeText(this@AddUpdateActivity,"Hi",Toast.LENGTH_SHORT).show()
                }

                R.id.et_type->
                {
                    customItemsDialog(resources.getString(R.string.select_dish_type),Constants.dishTypes(),Constants.DISH_TYPE)
                    return
                }

                R.id.et_category->
                {
                    customItemsDialog(resources.getString(R.string.select_dish_type),Constants.dishCategory(),Constants.DISH_CATEGORY)
                    return
                }
                R.id.et_cooking_time->
                {
                    customItemsDialog(resources.getString(R.string.select_dish_type),Constants.dishCookingTime(),Constants.DISH_COOKING_TIME)
                    return
                }
                //add this code on add dish button click
                R.id.btn_add_dish->{
                    val title=mBinding.etTitel.text.toString().trim{it <=' '}
                    val type=mBinding.etType.text.toString().trim{it <=' '}
                    val category=mBinding.etCategory.text.toString().trim{it <=' '}
                    val ingredients=mBinding.etIngredients.text.toString().trim{it <=' '}
                    val cookingtime=mBinding.etCookingTime.text.toString().trim{it <=' '}
                    val cookingDirection=mBinding.etCookingDirection.text.toString().trim{it <=' '}
                    
                    when{
                        TextUtils.isEmpty(mImagePath)->{
                            Toast.makeText(this@AddUpdateActivity,"Select Dish Image",Toast.LENGTH_SHORT).show()
                        }
                        TextUtils.isEmpty(title)->{
                            Toast.makeText(this@AddUpdateActivity,"Add title",Toast.LENGTH_SHORT).show()
                        }
                        TextUtils.isEmpty(type)->{
                            Toast.makeText(this@AddUpdateActivity,"Add Type",Toast.LENGTH_SHORT).show()
                        }
                        TextUtils.isEmpty(category)->{
                            Toast.makeText(this@AddUpdateActivity,"Add Category",Toast.LENGTH_SHORT).show()
                        }
                        TextUtils.isEmpty(ingredients)->{
                            Toast.makeText(this@AddUpdateActivity,"Add Ingredients",Toast.LENGTH_SHORT).show()
                        }
                        TextUtils.isEmpty(cookingtime)->{
                            Toast.makeText(this@AddUpdateActivity,"Add Cooking Time",Toast.LENGTH_SHORT).show()
                        }
                        TextUtils.isEmpty(cookingDirection)->{
                            Toast.makeText(this@AddUpdateActivity,"Add Cooking Direction",Toast.LENGTH_SHORT).show()
                        }
                        else->{
                           // Toast.makeText(this@AddUpdateActivity,"Add Dish succesfully",Toast.LENGTH_SHORT).show()
                            val faveDishDetails:FavDish= FavDish(
                                mImagePath,title,type,category,ingredients,cookingtime,cookingDirection,false
                            )
                            mFavDishViewModel.insert(faveDishDetails)

                            Toast.makeText(this@AddUpdateActivity,"Add Dish succesfully",Toast.LENGTH_SHORT).show()
                            Log.i("Insertion","Success")
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun customImageSelectionDialog(){
        mCustomListDialog= Dialog(this)
        val binding :DialogCustomPhotoCameraBinding=
            DialogCustomPhotoCameraBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
          //  Toast.makeText(this@AddUpdateActivity,"Camera",Toast.LENGTH_SHORT).show()

            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    report?.let {
                        if (report.areAllPermissionsGranted()){
                           // Toast.makeText(this@AddUpdateActivity,"You Have Camera Permission Now",Toast.LENGTH_SHORT).show()
                            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, CAMERA)
                        }
                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationaleDialogForPermissions()
                }

            }).onSameThread().check()



            mCustomListDialog.dismiss()
        }
        binding.tvGallery.setOnClickListener {
           // Toast.makeText(this@AddUpdateActivity,"Gallery",Toast.LENGTH_SHORT).show()
            Dexter.withContext(this@AddUpdateActivity)
                .withPermission(
                   /* Manifest.permission.WRITE_EXTERNAL_STORAGE,*/
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : PermissionListener{

                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                        val galleryIntent=Intent(Intent.ACTION_PICK)
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        startActivityForResult(galleryIntent, GALLERY)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(this@AddUpdateActivity,"You Have denied the storage Permission to select image",Toast.LENGTH_SHORT).show()

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                       showRationaleDialogForPermissions()
                    }

                }).onSameThread().check()


            mCustomListDialog.dismiss()
        }

        mCustomListDialog.show()
    }

    private fun showRationaleDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("Its Look Like you have turned off Permissions," +
                "require for this feature.It can be enabled under Application Settings")
            .setPositiveButton("Go TO SETTINGS")
            { _,_->
                try {
                    val  intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri=Uri.fromParts("package",packageName,null)
                    intent.data=uri
                    startActivity(intent)
                }
                catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                }
            }

            .setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap) :String{

        val wrapper=ContextWrapper(applicationContext)
        var file=wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        file= File(
            file,"${UUID.randomUUID()}.jpg"
        )
        try {
            val stream :OutputStream=FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }
        catch (e: IOException){
            e.printStackTrace()
        }
        return file.absolutePath
    }


    private fun customItemsDialog(title:String,itemsList:List<String>,selection:String){
        mCustomListDialog= Dialog(this)
        val binding: DialogCustomListBinding= DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvCustomeTitel.text=title
        binding.recyclerlist.layoutManager=LinearLayoutManager(this)

        val adapter=CustomListItemAdapter(this,itemsList,selection)
        binding.recyclerlist.adapter=adapter
        mCustomListDialog.show()
    }

    fun selectedListItem(item:String,selection: String){
        when(selection){
            Constants.DISH_TYPE->{
                mCustomListDialog.dismiss()
                mBinding.etType.setText(item)
            }
            Constants.DISH_CATEGORY->{
                mCustomListDialog.dismiss()
                mBinding.etCategory.setText(item)
            }
            Constants.DISH_COOKING_TIME->{
                mCustomListDialog.dismiss()
                mBinding.etCookingTime.setText(item)
            }
        }
    }
}