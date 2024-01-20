import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.thekdub.Database
import com.unboundid.ldap.sdk.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.sql.SQLException

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val greeting = remember { Greeting().greet() }
        
        Database.instance.let { db ->
            println("Connected? " + db.isConnected())
            try {
                println("Connecting: " + db.connect("192.168.1.82", 3306, "lampr", "lampr", "lampr"))
            }
            catch (e: SQLException) {
                println("Exception on connection: " + e.localizedMessage)
            }
            println("Connected? " + db.isConnected())
            db.isConnected().run {
                db.connection?.prepareStatement("SELECT * FROM test")?.executeQuery().let { results ->


                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        while (results?.next() == true) {
                            Text(results.getString("name"))
                        }  
                    } 
                }
            }
            try {
                println("Disconnecting? " + db.disconnect())
            }
            catch (e: SQLException) {
                println("Exception on disconnection: " + e.localizedMessage)
            }
            println("Connected? " + db.isConnected())
        }
        
        val ldapHost = "192.168.1.80"
        val ldapPort = 389
        val bindDN = "cn=lampr,ou=service accounts,ou=company,dc=company1,dc=local"
        val bindPw = "xpDkY4B5Zw94"
        
        try {
            val ldapOpts = LDAPConnectionOptions()
            ldapOpts.connectTimeoutMillis = 30000

            val ldapCon = LDAPConnection(ldapOpts)
            ldapCon.connect(ldapHost, ldapPort);

            if (ldapCon.isConnected) {
                println("Connection established!")
                
                val bindResult = ldapCon.bind(bindDN, bindPw)
                
                val baseDN = "ou=company,dc=company1,dc=local"
                val userFilter = Filter.createEqualityFilter("cn", "lampr")
                val allFilter = Filter.createORFilter(
                    Filter.createEqualityFilter("objectClass", "user"),
                    Filter.createEqualityFilter("objectClass", "group")
                )
                
                val searchScope = SearchScope.SUB
                
                if (bindResult.resultCode == ResultCode.SUCCESS) {
                    val userResult = ldapCon.search(baseDN, searchScope, userFilter)
                    val allResult = ldapCon.search(baseDN, searchScope, allFilter)

                  userResult.searchEntries.forEach { entry ->
                    println("DN: ${entry.dn}")
                    for (attribute in entry.attributes) {
                      println("\t${attribute.name}: ${attribute.values.joinToString(", ")}")
                    }
                    println()
                  }

                  allResult.searchEntries.forEach { entry ->
                    println("DN: ${entry.dn}")
                    for (attribute in entry.attributes) {
                      println("\t${attribute.name}: ${attribute.values.joinToString(", ")}")
                    }
                    println()
                  }
                }
                else {
                    println("Bind failed!")
                }
                
                ldapCon.close()
            }
            else {
                println("Connection failed!")
            }
        }
        catch (e: LDAPException) {
            e.printStackTrace()
        }
        
        
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource("compose-multiplatform.xml"), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}