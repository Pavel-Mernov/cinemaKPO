package packageDAO

import entity.Entity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import kotlin.reflect.typeOf

abstract class EntityDAO {
    companion object {
        @JvmStatic
        protected val dataDirectory : String = "src/main/resources/data"
    }

    // reading movies from CSV file
    public abstract fun getData() : List<Entity>

    // save given entity list to CSV file
    public abstract fun saveData(entities : List<Entity>)

    // tries adding given entity to the database
    fun add(entity: Entity) {

        if (find(entity) != null) {
            throw IllegalStateException("Movie $entity has been added before.")
        }

        val list = getData().toMutableList()

        list.add(entity)

        saveData(list)
    }

    // finds entity e given file
    private fun find(entity: Entity) : Entity?
    {
        val list = getData()

        for (curEntity in list) {
            if (curEntity == entity)
            {
                return curEntity
            }
        }

        return null
    }

    fun remove(entity: Entity)
    {
        if (find(entity) == null) {
            throw IllegalStateException("Entity $entity not found.")
        }

        val list = getData().toMutableList()

        list.remove(entity)

        saveData(list)
    }

    // reads data from given file path
    protected inline fun <reified TEntity> getDataFromFile(filePath : String) : List<TEntity> where TEntity : Entity
    {
        val file : File
        try {
            file = File(filePath)
        }
        catch (fileEx : FileNotFoundException)
        {
            return emptyList()
        }

        val fileReader  : FileReader
        try {
            fileReader = FileReader(file)
        }
        catch (fileEx : FileNotFoundException)
        {
            return emptyList()
        }

        val lines = emptyList<String>().toMutableList()

        fileReader.use { reader ->
            reader.forEachLine {
                lines.add(it)
            }
        }

        val resultList = lines.map{ Entity.fromCsv<TEntity>(it) }

        return resultList
    }

    protected inline fun <reified TEntity> saveDataToFile(entities : List<Entity>, filePath : String) where TEntity : Entity
    {
        val file = File(filePath)

        val fileWriter = FileWriter(file)

        val stringBuilder = StringBuilder()
        entities.forEach{
            if (it is TEntity) {
                stringBuilder.appendLine(it.toCSV())
            }
            else {
                throw RuntimeException("$it is not ${typeOf<TEntity>()}")
            }
        }

        fileWriter.use { writer ->
            writer.write(stringBuilder.toString())
        }
    }
}