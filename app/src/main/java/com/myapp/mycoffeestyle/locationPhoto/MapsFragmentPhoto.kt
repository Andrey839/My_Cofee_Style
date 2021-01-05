package com.myapp.mycoffeestyle.locationPhoto

import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toIcon
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.myapp.mycoffeestyle.R
import com.myapp.mycoffeestyle.database.PhotoInDatabase
import com.myapp.mycoffeestyle.database.getDatabase

class MapsFragmentPhoto : Fragment() {

    private var photoLocation: List<PhotoInDatabase>? = null

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        photoLocation?.let {
            for (i in photoLocation!!){
                googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            i.latitude.toDouble(),
                            i.longitude.toDouble()
                        )
                    ).title(i.typeCoffee)
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(i.latitude.toDouble(),i.longitude.toDouble())))
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val database = getDatabase(requireContext())

        val mapsViewModel =
            ViewModelProvider(
                this,
                MapsViewModelFactory(database)
            ).get(ViewModelMapsFragment::class.java)

        mapsViewModel.listPhotoLocation.observe(viewLifecycleOwner, {
            photoLocation = it
        })
    }

    private fun photoToIcon(uri: String): Bitmap? {
        var icon: Bitmap? = null
        Glide.with(requireContext()).asBitmap().load(uri).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                icon = resource
            }

        })
        return icon
    }
}