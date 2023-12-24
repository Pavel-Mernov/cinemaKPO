package packageDAO

import entity.Entity
import entity.MovieShow

// data access object for movie shows
class MovieShowDAO : EntityDAO()
{
    private val movieShowPath = "$dataDirectory/movieShows.csv"

    public override fun getData(): List<MovieShow> {
        val dataFromFile = getDataFromFile<MovieShow>(movieShowPath)

        return dataFromFile
    }

    public override fun saveData(entities: List<Entity>) {
        saveDataToFile<MovieShow>(entities, movieShowPath)
    }
}