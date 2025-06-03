


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
//data class Place(
//    @PrimaryKey
//    var id: String,
//    var name: String,
//    var lat: Double,
//    var lng: Double,
//    var alt: Double,
//    var heading: Double,
//    var description: String,
//    var author: String,
//    var ardata: String
//)

data class Place(

    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var alt: Double = 0.0,
    var heading: Double = 0.0,
    var description: String = "",
    var author: String = "",
    var ardata: String = ""
)
