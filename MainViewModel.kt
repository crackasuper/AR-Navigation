

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.morhenn.ar_navigation.model.ChatRequest
import de.morhenn.ar_navigation.model.ChatResponse
import de.morhenn.ar_navigation.model.ChatMessage
import de.morhenn.ar_navigation.pages.RetrofitClient

import de.morhenn.ar_navigation.persistance.AppDatabase
import de.morhenn.ar_navigation.persistance.NewPlace
import de.morhenn.ar_navigation.persistance.Place

import de.morhenn.ar_navigation.persistance.PlaceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    enum class NavState {
        NONE,
        CREATE_TO_AR_TO_TRY,
        MAPS_TO_AR_NEW,
        MAPS_TO_AR_NAV,
        MAPS_TO_AR_SEARCH,
        MAPS_TO_EDIT,

    }

    var navState = NavState.NONE

    private val db: AppDatabase = AppDatabase.getInstance()
    private val placeRepository = PlaceRepository.getInstance()
    var currentPlace: Place? = null
//var currentPlace: Place? = Place(
//    "12345", "Eiffel Tower", 48.8584, 2.2945, 324.0, 180.0, "A famous landmark in Paris, France.", "John Doe", "{ '3dModel': 'eiffel_tower.glb', 'scale': 1.0 }"
//
//)
    var arDataString: String = ""

    private var lastLatLng: LatLng? = null

    var geoLat = 0.0
    var geoLng = 0.0
    var geoAlt = 0.0
    var geoHdg = 0.0

    //replaces local database with firebase database

//    val places = placeRepository.getPlaces().asLiveData()
//    val placesMap = HashMap<Marker, Place>()

    //firebase backed database
    val places = MutableLiveData<List<Place>>()
    val placesMap = mutableMapOf<Marker, Place>()

   // var placesInRadius = placeRepository.getPlacesAroundLocation(0.0, 0.0, 1.0).asLiveData() commented for firebase

    //firebase logic for newplaces fetch and other functions
    val firebasePlaces = MutableLiveData<List<Place>>() // All places
    val placesInRadius = MutableLiveData<List<Place>>() // Filtered by radius


    fun uploadPlace(place: NewPlace) {
        //uploading places into firebase database
        val database = FirebaseDatabase.getInstance()
        val placesRef = database.getReference("places")



        // Generate a new unique ID
        val newPlaceRef = placesRef.push()
        newPlaceRef.setValue(place)
            .addOnSuccessListener {
                Log.d("Firebase", "Place uploaded successfully")
                fetchPlaces() //refresh place lists
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to upload place", e)
            }





       // placeRepository.newPlace(place)
    }

    fun updatePlace(place: Place) {
        placeRepository.updatePlace(place)
    }

//    fun deletePlace(place: Place) {
//        //deleting places from firebase database
//
//        placeRepository.deletePlace(place)
//    }

    fun deletePlace(place: Place) {
        val placeId = place.id
        if (placeId.isNullOrEmpty()) {
            Log.e("DeletePlace", "Cannot delete place: ID is null or empty.")
            return
        }

        val database = FirebaseDatabase.getInstance()
        val placesRef = database.getReference("places")

        placesRef.child(placeId).removeValue()
            .addOnSuccessListener {
                Log.d("DeletePlace", "Place deleted successfully")
                fetchPlaces() // Refresh the list
            }
            .addOnFailureListener { e ->
                Log.e("DeletePlace", "Failed to delete place", e)
            }

        placeRepository.deletePlace(place)
    }



    fun updateCurrentPlace(marker: Marker) {
        currentPlace = placesMap[marker]
        arDataString = currentPlace?.ardata ?: ""
    }

    fun clearCurrentPlace() {
        currentPlace = null
        arDataString = ""
    }

    fun fetchPlaces() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("places")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fetchedPlaces = mutableListOf<Place>()
                for (placeSnapshot in snapshot.children) {
                    val place = placeSnapshot.getValue(Place::class.java)
                    place?.let {
                        fetchedPlaces.add(it)
                    }
                }
                places.value = fetchedPlaces
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainViewModel", "Failed to fetch places: ${error.message}")
            }
        })

        placeRepository.getPlaces()
    }




    fun clearGeo() {
        geoHdg = 0.0
        geoAlt = 0.0
        geoLat = 0.0
        geoLng = 0.0
    }
//   changing from local database into firebase database
//    fun fetchPlacesAroundLocation(latLng: LatLng, searchRadius: Double) {
//        lastLatLng = latLng
//        placesInRadius = placeRepository.getPlacesAroundLocation(latLng.latitude, latLng.longitude, searchRadius).asLiveData()
//    }

    //firebase backed fetchPlacesAroundLocation method
    fun fetchPlacesAroundLocation(latLng: LatLng, searchRadius: Double) {
        val database = FirebaseDatabase.getInstance()
        val placesRef = database.getReference("places")

        placesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filteredList = mutableListOf<Place>()
                for (child in snapshot.children) {
                    val place = child.getValue(Place::class.java)
                    place?.let {
                        val distance = FloatArray(1)
                        Location.distanceBetween(
                            latLng.latitude, latLng.longitude,
                            it.lat, it.lng,
                            distance
                        )
                        val distanceInKm = distance[0] / 1000
                        if (distanceInKm <= searchRadius) {
                            filteredList.add(it)
                        }
                    }
                }
                placesInRadius.value = filteredList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Failed to fetch nearby places: ${error.message}")
            }
        })
    }
//this property also changed into firebase database
//
//    fun fetchPlacesAroundLastLocation(searchRadius: Double) {
//        lastLatLng?.let {
//            placesInRadius = placeRepository.getPlacesAroundLocation(it.latitude, it.longitude, searchRadius).asLiveData()
//        }
//    }

    //firebase to check last location
    fun fetchPlacesAroundLastLocation(searchRadius: Double) {
        lastLatLng?.let {
            placeRepository.getPlacesAroundLocationFirebase(it.latitude, it.longitude, searchRadius) { places ->
                placesInRadius.value = places
            }
        }
    }


//    //for OpenAI chatbot configuration
//    fun askChatGPT(userInput: String, onResponse: (String) -> Unit) {
//        val messages = listOf(
//            ChatMessage("system", "You are a helpful assistant for ASTU campus navigation and events."),
//            ChatMessage("user", userInput)
//        )
//        val request = ChatRequest(messages = messages)
//        val call = RetrofitClient.apiService.sendMessage("Bearer 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\n", request)
//
//        call.enqueue(object : Callback<ChatResponse> {
//            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
//                if (response.isSuccessful) {
//                    val reply = response.body()?.choices?.firstOrNull()?.message?.content
//                    onResponse(reply ?: "No response")
//                } else {
//                    onResponse("Error: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
//                onResponse("Failed: ${t.message}")
//            }
//        })
//    }



}
