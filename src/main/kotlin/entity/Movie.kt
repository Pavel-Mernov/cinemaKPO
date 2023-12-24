package entity

data class Movie(val name : String, val year: Int) : Entity() {

    override fun toString(): String {
        return "Movie : $name, year $year"
    }

    // function responsible for CSV serialization
    override fun toCSV() : String
    {
        return "$name, $year"
    }
}