package entity

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

// immutable data class which represents a movie show (rus. сеанс)
data class MovieShow(val movie : Movie, val date : LocalDate,
                     val startTime : LocalTime,
                     private val _seats : MutableList<Boolean>) : Entity()
{
    // list showing whether _seats[i] is
    // FALSE = free (no-one has bought a ticket for this seat)
    // TRUE  = someone has bought a ticket for this place
    // we do not want to mutate the list outside the class
    val seats : List<Boolean>
        get() = _seats

    constructor(movie : Movie, date : LocalDate, startTime : LocalTime) :
            this(movie, date, startTime,
                MutableList<Boolean>(SEATS_COUNT, { false })
            )

    constructor(movie : Movie, dateTime : LocalDateTime)
            : this(movie, dateTime.toLocalDate(), dateTime.toLocalTime())

    companion object {
        // date formatter needed for converting movie show parsing a csv line
        val dateFormatter: DateTimeFormatter
            get() = DateTimeFormatter.
            ofPattern("dd-MMMM-yyyy", Locale.US)

        private const val SEATS_COUNT = 20

        // time formatter needed for converting movie to a csv line
        // and parsing a csv line and getting a movie show from the line
        val timeFormatter: DateTimeFormatter
            get() = DateTimeFormatter.
            ofPattern("hh-mm-a")
    }

    val startDateTime : LocalDateTime
        get() = LocalDateTime.of(date, startTime)

    override fun toString(): String {
        val consoleDateFormatter = DateTimeFormatter
            .ofPattern("dd MMMM yyyy", Locale.US)
        val dateForConsole = date.format(consoleDateFormatter)

        return "Movie show: $movie, Date = $dateForConsole, " +
                "Start time = $startTime"
    }

    override fun toCSV(): String {
        val formattedDate = date.format(dateFormatter)
        val formattedTime = startTime.format(timeFormatter)

        val boughtSeatsString = _seats.joinToString("-")
        return "${movie.toCSV()}, $formattedDate, $formattedTime, $boughtSeatsString"
    }

    fun buyTicketAt(index : Int) {
        if (_seats[index]) {
            throw  RuntimeException("The ticket for seat number ${index + 1} is already bought.")
        }

        _seats[index] = true
    }

    fun refundTicketAt(index : Int) {
        if (!_seats[index]) {
            throw  RuntimeException("The ticket for seat number ${index + 1} is not bought.")
        }

        _seats[index] = false
    }

    // we compare only by movie and start date time,
    // because we want each movie show to be appropriate only one placement
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        return (other is MovieShow && this.movie == other.movie &&
                this.startDateTime == other.startDateTime)
    }
}