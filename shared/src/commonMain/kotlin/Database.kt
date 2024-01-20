import java.sql.Connection
import java.sql.DriverManager

class Database private constructor() {

  companion object {
    private val dbInstance: Database by lazy { Database() }
    val instance: Database
      get() = dbInstance
  }

  private var dbConnection: Connection? = null
  val connection: Connection?
    get() = dbConnection

  fun connect(host: String, port: Int, database: String, username: String, password: String): Boolean {
    isConnected().run { dbConnection?.close() }
    dbConnection = DriverManager.getConnection("jdbc:mysql://$host:$port/$database", username, password);
    return dbConnection?.isClosed?.not()?: false    
  }

  fun disconnect(): Boolean {
    isConnected().run { dbConnection?.let { conn -> conn.close(); dbConnection = null } }
    return true;
  }

  fun isConnected(): Boolean {
    return dbConnection?.isClosed?.not() ?: false
  }

}