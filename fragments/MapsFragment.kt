



import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.morhenn.ar_navigation.MainViewModel
import de.morhenn.ar_navigation.R
import de.morhenn.ar_navigation.adapter.MyInfoWindowAdapter
import de.morhenn.ar_navigation.databinding.FragmentMapsBinding
import de.morhenn.ar_navigation.databinding.InfoWindowBinding
import de.morhenn.ar_navigation.persistance.Place
import de.morhenn.ar_navigation.util.Utils

////

class MapsFragment : Fragment(), OnMapReadyCallback {

    //for recording and displaying user estimated time and distance location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private var destinationLatLng: LatLng? = null // Update this when the user selects a marker


    //best practise for using binding
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var infoWindowAdapter: MyInfoWindowAdapter
    private var selectedMarker: Marker? = null
    private var locationProvider: FusedLocationProviderClient? = null

    private val viewModel: MainViewModel by navGraphViewModels(R.id.nav_graph_xml)
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private var centered = true

    private var map: GoogleMap? = null


    private lateinit var geofenceHelper: GeofenceHelper


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        infoWindowAdapter = MyInfoWindowAdapter(InfoWindowBinding.inflate(inflater, container, false), viewModel)



        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
        return binding.root
    }

    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
    val adminEmails = listOf("admin@astu.edu.et", "sdmh725@gmail.com")
    val isAdmin = currentUserEmail in adminEmails

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPlaces()


        val filter = IntentFilter("GEOFENCE_TRIGGERED")
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(geofenceUIReceiver, filter)

        loadBuildingsAndAddGeofences()
        geofenceHelper = GeofenceHelper(requireContext())



        //to show destination and distance calculation
        locationRequest = LocationRequest.create().apply {
            interval = 5000 // 5 seconds
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation ?: return
                destinationLatLng?.let { destination ->
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    val distance = calculateDistance(currentLatLng, destination) // In meters
                    val estimatedTimeMin = distance / 83.33 // Approx 5 km/h -> 83.33 m/min

                    val distanceText = if (distance >= 1000) {
                        String.format("%.2f km", distance / 1000)
                    } else {
                        String.format("%.0f m", distance)
                    }

                    val timeText = String.format("%.1f min", estimatedTimeMin)

                    // Update UI
                    binding.distanceText.text = "Distance: $distanceText | ETA: $timeText"
                }
            }
        }


        locationProvider = LocationServices.getFusedLocationProviderClient(requireContext())

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
            } else {
                Utils.toast("The app requires location permission to function, please enable them")
            }
        }
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Utils.toast(getString(R.string.location_rationale))
            }
            else -> {
                (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
            }
        }
        //search bar listener code

        binding.searchButton.setOnClickListener {
            val query = binding.placeSearchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchPlace(query)
            } else {
                Toast.makeText(requireContext(), "Enter a place name", Toast.LENGTH_SHORT).show()
            }
        }



        binding.mapAddFab.setOnClickListener {

            //logic to prevent users and guests from creating routes
            if (!isAdmin) {
                Toast.makeText(requireContext(), "Only admins can create or edit locations.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedMarker != null) {
                viewModel.updateCurrentPlace(selectedMarker!!)
                viewModel.navState = MainViewModel.NavState.MAPS_TO_EDIT
                findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToCreateFragment())
            } else {
                viewModel.clearCurrentPlace()
                viewModel.navState = MainViewModel.NavState.MAPS_TO_AR_NEW
                findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToArFragment())
            }
        }
        binding.mapRouteFab.setOnClickListener {
            viewModel.updateCurrentPlace(selectedMarker!!)
            viewModel.navState = MainViewModel.NavState.MAPS_TO_AR_NAV
            findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToArFragment())
        }
        binding.mapArSearchFab.setOnClickListener {
            viewModel.navState = MainViewModel.NavState.MAPS_TO_AR_SEARCH
            findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToArFragment())
        }
        binding.mapMyLocationFab.setImageResource(R.drawable.ic_baseline_gps_not_fixed_24)
        binding.mapMyLocationFab.setOnClickListener { zoomOnMyLocation() }
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
//
//
    private fun calculateDistance(start: LatLng, end: LatLng): Float {
        val result = FloatArray(1)
        Location.distanceBetween(
            start.latitude, start.longitude,
            end.latitude, end.longitude,
            result
        )
        return result[0] // distance in meters
    }

    //this to also deleted
    private val geofenceUIReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val buildingName = intent?.getStringExtra("building_name") ?: return
            binding.tvBuildingName.text = "Nearby: $buildingName"
            binding.tvBuildingName.visibility = View.VISIBLE
        }
    }



    //buildings name
    private fun loadBuildingsAndAddGeofences() {
        val database = FirebaseDatabase.getInstance().getReference("buildings")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buildingSnapshot in snapshot.children) {
                    val name = buildingSnapshot.child("name").getValue(String::class.java)
                    val lat = buildingSnapshot.child("latitude").getValue(Double::class.java)
                    val lng = buildingSnapshot.child("longitude").getValue(Double::class.java)

                    if (name != null && lat != null && lng != null) {
                        val latLng = LatLng(lat, lng)
                        geofenceHelper.addGeofence(name, latLng, 100f) // radius = 100 meters
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching buildings: ${error.message}")
            }
        })
    }






    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {

            setInfoWindowAdapter(infoWindowAdapter)

            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isCompassEnabled = true
            isMyLocationEnabled = true
            mapType = GoogleMap.MAP_TYPE_SATELLITE
            isIndoorEnabled = true
            setOnCameraMoveStartedListener {
                if (centered) {
                    centered = false
                } else {
                    binding.mapMyLocationFab.setImageResource(R.drawable.ic_baseline_gps_not_fixed_24)
                }
            }
            setOnMarkerClickListener { marker ->
                if (marker.isInfoWindowShown) {
                    marker.hideInfoWindow()
                } else {
                    selectedMarker?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    selectedMarker = marker
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    marker.showInfoWindow()
                    binding.mapRouteFab.visibility = View.VISIBLE
                    binding.mapAddFab.setImageResource(R.drawable.ic_baseline_edit_location_alt_24)

                    //calculate distance
                    destinationLatLng = marker.position
                    locationProvider?.lastLocation?.addOnSuccessListener { location ->
                        location?.let {
                            val userLatLng = LatLng(location.latitude, location.longitude)
                            val distanceInMeters = calculateDistance(userLatLng, marker.position)
                            val distanceText = if (distanceInMeters >= 1000) {
                                String.format("%.2f km", distanceInMeters / 1000)
                            } else {
                                String.format("%.0f m", distanceInMeters)
                            }

                            // Show distance to user
                            binding.distanceText.text = "Distance: $distanceText"

                            Toast.makeText(
                                requireContext(),
                                "Distance: $distanceText",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                true
            }
            setOnInfoWindowClickListener { marker ->
                viewModel.updateCurrentPlace(marker)
                viewModel.navState = MainViewModel.NavState.MAPS_TO_AR_NAV
                findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToArFragment())
            }
            setOnInfoWindowLongClickListener { marker ->

                //preventing users and guests from editing locations
                if (!isAdmin) {
                    Toast.makeText(requireContext(), "Only admins can edit this location.", Toast.LENGTH_SHORT).show()
                    return@setOnInfoWindowLongClickListener
                }

                viewModel.updateCurrentPlace(marker)
                viewModel.navState = MainViewModel.NavState.MAPS_TO_EDIT
                findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToCreateFragment())
            }
            setOnMapClickListener {
                selectedMarker?.let {
                    it.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    selectedMarker = null
                    binding.mapRouteFab.visibility = View.GONE
                    binding.mapAddFab.setImageResource(R.drawable.ic_baseline_add_location_alt_24)

                }
            }
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationProvider?.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }

        }

        viewModel.places.observe(viewLifecycleOwner) {
            binding.mapRouteFab.visibility = View.GONE
            binding.mapAddFab.setImageResource(R.drawable.ic_baseline_add_location_alt_24)
            map?.clear()
            selectedMarker = null
            viewModel.placesMap.clear()

                for (place in it) {
                    val marker =
                        map?.addMarker(MarkerOptions().position(LatLng(place.lat, place.lng)))
                    viewModel.placesMap[marker!!] = place
                }
            }
            viewModel.fetchPlaces()
            zoomOnMyLocation()

            map?.let { map ->
                binding.mapMyLocationFab.setOnLongClickListener {
                    if (map.mapType == GoogleMap.MAP_TYPE_NORMAL) {
                        map.mapType = GoogleMap.MAP_TYPE_SATELLITE
                    } else {
                        map.mapType = GoogleMap.MAP_TYPE_NORMAL
                    }
                    true
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        map?.isIndoorEnabled = false
        map?.clear()
        map = null
        _binding = null
        selectedMarker = null

        locationProvider?.removeLocationUpdates(locationCallback)


    }

    @SuppressLint("MissingPermission")
    private fun zoomOnMyLocation() {
        viewModel.fetchPlaces()
        locationProvider?.let {
            it.lastLocation.addOnSuccessListener { l ->
                l?.let { location ->
                    centered = true
                    val target = LatLng(location.latitude, location.longitude)
                    val builder = CameraPosition.builder()
                        .zoom(18f)
                        .target(target)
                    map?.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()))
                    binding.mapMyLocationFab.setImageResource(R.drawable.ic_baseline_gps_fixed_24)
                }
            }
        }
    }

    //search place function code
    private fun searchPlace(query: String) {
        val matchedEntry = viewModel.placesMap.entries.find { entry ->
            entry.value.name.contains(query, ignoreCase = true)
        }

        if (matchedEntry != null) {
            val marker = matchedEntry.key
            val place = matchedEntry.value
            selectedMarker?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            selectedMarker = marker
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            marker.showInfoWindow()
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 18f))
            binding.mapRouteFab.visibility = View.VISIBLE
            binding.mapAddFab.setImageResource(R.drawable.ic_baseline_edit_location_alt_24)
        } else {
            Toast.makeText(requireContext(), "Place not found", Toast.LENGTH_SHORT).show()
        }
    }

}

