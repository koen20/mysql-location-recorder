import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.lang.Exception
import java.sql.SQLException

fun Route.addPhoneTrackLocation(mysql: Mysql){
    route("/add") {
        get {
            try {
                val tst = Mqtt.getMysqlDateString(call.parameters["timestamp"]!!.toLong())
                val stmt = mysql.conn.createStatement()
                val alt = call.parameters["alt"]!!
                if (alt == "") {
                    stmt.executeUpdate(
                        "INSERT INTO data VALUES (NULL, '" + tst + "', '"
                                + call.parameters["lat"]!! + "', '" + call.parameters["lon"]!! + "'" +
                                ", NULL, '" + call.parameters["acc"]!!
                                + "',  '" + call.parameters["batt"]!! + "', '" + call.parameters["tid"]!! + "')"
                    )
                } else {
                    stmt.executeUpdate(
                        ("INSERT INTO data VALUES (NULL, '" + tst + "', '"
                                + call.parameters["lat"]!! + "', '" + call.parameters["lon"]!! + "'" +
                                ", '" + alt + "', '" + call.parameters["acc"]!!
                                + "',  '" + call.parameters["batt"]!! + "', '" + call.parameters["tid"]!! + "')")
                    )
                }
                stmt.close()
                call.respondText("added")
            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    println(mysql.conn.isValid(3000))
                } catch (ex: SQLException) {
                    ex.printStackTrace()
                }
                call.respondText("Insert failed", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}