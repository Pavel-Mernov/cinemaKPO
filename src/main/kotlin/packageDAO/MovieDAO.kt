package packageDAO

import entity.Entity
import entity.Movie

// data access object for entities of movie
class MovieDAO : EntityDAO() {

    private val moviesFilePath = "$dataDirectory/movies.csv"

    override fun getData(): List<Movie> {
        val resultList = getDataFromFile<Movie>(moviesFilePath)

        return resultList
    }

    // saving given movie list to CSV file
    override fun saveData(entities : List<Entity>)
    {
        saveDataToFile<Movie>(entities, moviesFilePath)
    }
}