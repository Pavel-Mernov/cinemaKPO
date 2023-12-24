package startApp

import entity.Movie
import entity.MovieShow
import packageDAO.MovieDAO
import packageDAO.MovieShowDAO
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.system.exitProcess

// class that processes requests from the user
class StartApp {

    companion object
    {
        private val movieDAO = MovieDAO()
        private val movieShowDAO = MovieShowDAO()

        // list of all actions you can process
        private val actionList = listOf(
            Action("Sell Ticket", action = { sellTicket() }),
            Action("Refund ticket", action = { refundTicket() }),
            Action("Show free and ordered seats", action = { showPlaces() }),
            Action("Add new movie", action = { addNewMovie() } ),
            Action("Remove movie", action = { removeMovie() }),
            Action("Add new movie show", action = { addMovieShow() }),
            Action("Remove movie show", action = { removeMovieShow() }),
            Action("Exit", action = { exit() })
        )

        private fun readEnter() {
            val pressEnterString : String = "Press Enter to return to main menu. "

            print(pressEnterString)

            readln()
        }

        fun run()
        {
            var actionNumber : Int
            while (true)
            {
                println("Choose your next action from shown below:")

                for (i in actionList.indices)
                {
                    println("${i + 1}. ${actionList[i]}.")
                }

                print("Enter the number of your action: ")
                actionNumber = readln().toInt()

                try {
                    processAction(actionNumber)
                }
                catch (numEx : NumberFormatException)
                {
                    println(numEx.message)
                }
            }
        }

        private fun processAction(actionNumber : Int)
        {

            if (actionNumber <= 0 || actionNumber > actionList.size)
            {
                throw NumberFormatException("Incorrect number of action : $actionNumber.")
            }

            actionList[actionNumber - 1].action.invoke()
        }

        private  fun sellTicket()
        {
            val movieShowList : MutableList<MovieShow>

            try {
                movieShowList = movieShowDAO.getData().toMutableList()
            }
            catch (runEx : RuntimeException) {
                println(runEx.message)
                println("The process of selling a ticket cancelled.")

                readEnter()
                return
            }

            val optionsList : List<String> = mutableListOf("Cancel selling") + movieShowList.map{ it.toString() }

            println("Choose the number of movie show to sell a ticket from below:")

            for (i in optionsList.indices)
            {
                println("${i}. ${optionsList[i]}.")
            }

            print("Enter the number of movie show for selling a ticket: ")

            val actionNumber : Int

            try {
                actionNumber = readln().toInt()
            }
            catch (numEx : NumberFormatException)
            {
                println("Incorrect number of action.")

                readEnter()
                return
            }

            if (actionNumber < 0 || actionNumber >= optionsList.size)
            {
                println("Incorrect number of action.")

                readEnter()
                return
            }

            if (actionNumber == 0)
            {
                println("The process of selling a ticket cancelled.")

                readEnter()
                return
            }

            val selectedMovieShow = movieShowList[actionNumber - 1]

            val curDateTime = LocalDateTime.now()
            val selectedDateTime = selectedMovieShow.startDateTime

            if (curDateTime > selectedDateTime)
            {
                println("Cannot sell or refund tickets to $selectedDateTime.")
                println("The movie show has already started.")

                readEnter()
                return
            }

            val seatsForShow = selectedMovieShow.seats

            println("Choose the number of place to buy a ticket for (from shown below).")

            for (i in seatsForShow.indices) {
                val curSeatStatus = if ( seatsForShow[i] ) "BOUGHT" else "FREE"

                println("${i + 1}. $curSeatStatus.")
            }

            print("Choose the number of seat you want to buy a ticket for: ")

            val seatNumber : Int
            try {
                seatNumber = readln().toInt()
            }
            catch (numEx : NumberFormatException) {
                println(numEx.message)

                println("The process of buying a ticket is cancelled.")

                readEnter()
                return
            }

            if (seatNumber <= 0 || seatNumber > seatsForShow.size) {
                println("No seat with such a number: $seatNumber.")
                println("The process of buying a ticket is cancelled.")

                readEnter()
                return
            }

            val selectedSeatIsBought = seatsForShow[seatNumber - 1]

            if (selectedSeatIsBought) {
                println("The seat number $seatNumber is already bought.")

                println("The process of buying a ticket is cancelled.")

                readEnter()
                return
            }

            selectedMovieShow.buyTicketAt(seatNumber - 1)

            movieShowDAO.saveData(movieShowList)

            println("Ticket for seat $seatNumber was bought successfully.")

            readEnter()
        }

        private fun refundTicket()
        {
            val movieShowList : MutableList<MovieShow>

            try {
                movieShowList = movieShowDAO.getData().toMutableList()
            }
            catch (runEx : RuntimeException) {
                println(runEx.message)
                println("The process of refunding a ticket cancelled.")

                readEnter()
                return
            }

            val optionsList : List<String> = mutableListOf("Cancel refunding") + movieShowList.map{ it.toString() }

            println("Choose the number of movie show to refund a ticket from below:")

            for (i in optionsList.indices)
            {
                println("${i}. ${optionsList[i]}.")
            }

            print("Enter the number of movie show for refunding a ticket: ")

            val actionNumber : Int

            try {
                actionNumber = readln().toInt()
            }
            catch (numEx : NumberFormatException)
            {
                println("Incorrect number of action.")

                readEnter()
                return
            }

            if (actionNumber < 0 || actionNumber >= optionsList.size)
            {
                println("Incorrect number of action.")

                readEnter()
                return
            }

            if (actionNumber == 0)
            {
                println("The process of refunding a ticket cancelled.")

                readEnter()
                return
            }

            val selectedMovieShow = movieShowList[actionNumber - 1]

            val curDateTime = LocalDateTime.now()
            val selectedDateTime = selectedMovieShow.startDateTime

            if (curDateTime > selectedDateTime)
            {
                println("Cannot sell or refund tickets to $selectedDateTime.")
                println("The movie show has already started.")

                readEnter()
                return
            }

            val seatsForShow = selectedMovieShow.seats

            println("Choose the number of place to refund a ticket for " +
                    "(from shown below).")

            for (i in seatsForShow.indices) {
                val curSeatStatus = if ( seatsForShow[i] ) "BOUGHT" else "FREE"

                println("${i + 1}. $curSeatStatus.")
            }

            print("Choose the number of seat you want to refund a ticket for: ")

            val seatNumber : Int
            try {
                seatNumber = readln().toInt()
            }
            catch (numEx : NumberFormatException) {
                println(numEx.message)

                println("The process of refunding a ticket is cancelled.")

                readEnter()
                return
            }

            if (seatNumber <= 0 || seatNumber > seatsForShow.size) {
                println("No seat with such a number: $seatNumber.")
                println("The process of refunding a ticket is cancelled.")

                readEnter()
                return
            }

            val selectedSeatIsBought = seatsForShow[seatNumber - 1]

            if (!selectedSeatIsBought) {
                println("The seat number $seatNumber is not bought.")

                println("The process of refunding a ticket is cancelled.")

                readEnter()
                return
            }

            selectedMovieShow.refundTicketAt(seatNumber - 1)

            movieShowDAO.saveData(movieShowList)

            println("Ticket for seat $seatNumber was refunded successfully.")

            readEnter()
        }

        // this function shows places in the cinema hall
        private fun showPlaces()
        {
            val movieShowList : MutableList<MovieShow>

            try {
                movieShowList = movieShowDAO.getData().toMutableList()
            }
            catch (runEx : RuntimeException) {
                println(runEx.message)
                println("The process of refunding a ticket cancelled.")

                readEnter()
                return
            }

            val optionsList : List<String> = mutableListOf("Cancel refunding") + movieShowList.map{ it.toString() }

            println("Choose the number of movie show to refund a ticket from below:")

            for (i in optionsList.indices)
            {
                println("${i}. ${optionsList[i]}.")
            }

            print("Enter the number of movie show for refunding a ticket: ")

            val actionNumber : Int

            try {
                actionNumber = readln().toInt()
            }
            catch (numEx : NumberFormatException)
            {
                println("Incorrect number of action.")

                readEnter()
                return
            }

            if (actionNumber < 0 || actionNumber >= optionsList.size)
            {
                println("Incorrect number of action.")

                readEnter()
                return
            }

            if (actionNumber == 0)
            {
                println("The process of refunding a ticket cancelled.")

                readEnter()
                return
            }

            val selectedMovieShow = movieShowList[actionNumber - 1]

            val curDateTime = LocalDateTime.now()
            val selectedDateTime = selectedMovieShow.startDateTime

            if (curDateTime > selectedDateTime)
            {
                println("Cannot sell or refund tickets to $selectedDateTime.")
                println("The movie show has already started.")

                readEnter()
                return
            }

            val seatsForShow = selectedMovieShow.seats

            println("Printing seats from $selectedMovieShow.")

            for (i in seatsForShow.indices) {
                val curSeatStatus = if ( seatsForShow[i] ) "BOUGHT" else "FREE"

                println("${i + 1}. $curSeatStatus.")
            }

            readEnter()
        }

        private fun addNewMovie()
        {
            print("Enter movie name: ")

            val movieName = readln()

            if (movieName.isEmpty())
            {
                println("The movie name is empty.")

                readEnter()
                return
            }

            print("Enter the year of the movie: ")

            val movieYear : Int
            try {
                movieYear = readln().toInt()
            }
            catch (numEx : NumberFormatException)
            {
                println("Incorrect format of the movie year.")

                readEnter()
                return
            }

            val movie = Movie(movieName, movieYear)

            try {
                movieDAO.add(movie)
                println("$movie was added successfully.")

                readEnter()
                return
            }
            catch (ex : Exception) {
                println(ex.message)

                readEnter()
                return
            }
        }

        private fun removeMovie()
        {
            val movieList : MutableList<Movie>
            try {
                movieList = movieDAO.getData().toMutableList()
            }
            catch (runtimeEx : RuntimeException)
            {
                println(runtimeEx.message)
                println("Deletion cancelled.")
                print("Press Enter to return to main menu. ")

                readln()
                return
            }

            val optionsList : List<String> = mutableListOf("Cancel deletion") + movieList.map{ it.toString() }

            println("Choose the number of movie to delete from below:")

            for (i in optionsList.indices)
            {
                println("${i}. ${optionsList[i]}.")
            }

            print("Enter the number of movie for deletion: ")

            val actionNumber : Int

            try {
                actionNumber = readln().toInt()
            }
            catch (numEx : NumberFormatException)
            {
                println("Incorrect number of action.")

                readEnter()
                return
            }

            if (actionNumber < 0 || actionNumber >= optionsList.size)
            {
                println("Incorrect number of action.")

                readEnter()
                return
            }

            if (actionNumber == 0)
            {
                println("Deletion cancelled.")

                readEnter()
                return
            }

            val movieForRemove = movieList[actionNumber - 1]

            movieDAO.remove(movieForRemove)

            println("$movieForRemove was removed successfully.")

            readEnter()
        }

        private fun addMovieShow()
        {
            println("Choose movie from presented below:")

            val movieList = movieDAO.getData()

            if (movieList.isEmpty())
            {
                println("No movies added to the data base.")
                println("Adding movie show was cancelled.")

                readEnter()
                return
            }

            for (i in movieList.indices)
            {
                println("${i + 1}. ${movieList[i]}.")
            }

            print("Enter the number of movie for adding a movie show: ")

            val movieNumber : Int
            try {
                movieNumber = readln().toInt()


            }
            catch (numEx : NumberFormatException) {
                println("Incorrect movie number.")

                readEnter()
                return
            }

            if (movieNumber <= 0 || movieNumber > movieList.size)
            {
                println("Movie number is either too big or too small: $movieNumber.")

                readEnter()
                return
            }

            val movie = movieList[movieNumber - 1]

            print("Enter the date of the movie show. Format dd : mm : yyyy. ")
            val localDate : LocalDate

            try {
                val dateString = readln()

                val dateArgs = dateString.split(" ", ":", ",", ".", ";").filter{ it.isNotEmpty() }

                if (dateArgs.size > 3)
                {
                    throw RuntimeException("Too many arguments for local date.")
                }

                val dayOfMonth : Int = dateArgs[0].toInt()
                val month : Int = dateArgs[1].toInt()
                val year : Int = dateArgs[2].toInt()

                localDate = LocalDate.of(year, month, dayOfMonth)
            }
            catch (runEx : RuntimeException) {
                println(runEx.message)
                println("Incorrect data format.")

                readEnter()
                return
            }

            print("Enter the time of start time. Format : hh : mm. ")

            val localTime : LocalTime

            try {
                val timeString : String = readln()
                val timeArgs = timeString.split(" ", ":", ",", ";", ".").filter { it.isNotEmpty() }

                if (timeArgs.size > 2)
                {
                    throw RuntimeException("Too many arguments for local time. ")
                }

                val hours : Int = timeArgs[0].toInt()
                val minutes : Int = timeArgs[1].toInt()

                localTime = LocalTime.of(hours, minutes)
            }
            catch (runEx : RuntimeException) {
                println(runEx.message)
                println("Cannot create a local time.")

                readEnter()
                return
            }

            val movieShow = MovieShow(movie, localDate, localTime)

            try {
                movieShowDAO.add(movieShow)
                println("$movieShow was added successfully.")

                readEnter()
            }
            catch (ex : Exception)
            {
                println(ex.message)
                print("Press Enter to return to main menu. ")

                readln()
                return
            }
        }

        private fun removeMovieShow()
        {
            val movieShowList : MutableList<MovieShow>

            try {
                movieShowList = movieShowDAO.getData().toMutableList()
            }
            catch (runEx : RuntimeException) {
                println(runEx.message)
                println("Deletion cancelled.")

                readEnter()
                return
            }

            val optionsList : List<String> = mutableListOf("Cancel deletion") + movieShowList.map{ it.toString() }

            println("Choose the number of movie show to delete from below:")

            for (i in optionsList.indices)
            {
                println("${i}. ${optionsList[i]}.")
            }

            print("Enter the number of movie show for deletion: ")

            val actionNumber : Int

            try {
                actionNumber = readln().toInt()
            }
            catch (numEx : NumberFormatException)
            {
                println("Incorrect number of action.")

                readEnter()
                return
            }

            if (actionNumber < 0 || actionNumber >= optionsList.size)
            {
                println("Incorrect number of action.")

                readEnter()
                return
            }

            if (actionNumber == 0)
            {
                println("Deletion cancelled.")
                println("Returning to main menu.")
                return
            }

            val movieShowForRemove = movieShowList[actionNumber - 1]

            movieShowDAO.remove(movieShowForRemove)

            println("Movie show $movieShowForRemove was removed successfully.")

            readEnter()
        }

        private fun exit()
        {
            exitProcess(0)
        }
    }
}