




//data class Place(
//    var id: String = "",
//    var name: String = "",
//    var lat: Double = 0.0,
//    var lng: Double = 0.0,
//    var alt: Double = 0.0,
//    var heading: Double = 0.0,
//    var description: String = "",
//    var author: String = "",
//    var ardata: String = ""
//) //{
////    // Explicit no-arg constructor for Firebase
////    constructor() : this("", "", 0.0, 0.0, 0.0, 0.0, "", "", "")
////}
//


//this stored our data in firebase database
data class NewPlace(
    var name: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var alt: Double = 0.0,
    var heading: Double = 0.0,
    var description: String = "",
    var author: String = "",
    var arData: String = ""
)
