In addition, the Session.getUserProperties method provides a modifiable map to store user properties.

To store information common to all connected clients, you can use class (static) variables;
however, you are responsible for ensuring thread-safe access to them.

thread safety game

session parameters or session cookies

remove player name from cookie to gameStatus?

Game life:
- Player enter login
- click "join game" - > Game add player
- if number of players in game=2? game starts and players get random cards;

Synchronized:
methods enable a simple strategy for preventing thread interference and memory consistency errors:
if an object is visible to more than one thread, all reads or writes to that object's variables are done through
 synchronized methods.

 HashtAble is synchronized by default

Connect to H2 DataBase with glassfish:
- add h2*.jat to CLASSPATH environment var;
- copy h2*.jar to C:\glassfish3\glassfish\lib
- add maven dependency for h2;
- in glassish admin cabinet: Resources->JDBC->Conn Pools->new connection pool:
    - set the Datasource Classname to org.h2.jdbcx.JdbcDataSource.
    - set user, password, url like jdbc:h2:tcp://localhost/~/my_db_name
    -create new JDBC resource with name jdbc/basename
- add resource-ref to web.xml (<res-ref-name> =jdbc/basename)
- get Datasource object from InitContext.lookup(jdbc/basename);
- Datasource.getConnetction() returns Connection

By default, when an application calls DriverManager.getConnection(url, ...) and the database specified in the URL does not yet exist,
a new (empty) database is created. To change it, edit url in Driver.Manager.getConnection() or in JDBC Connection Pool Properties in ServerAdmin cabinet;
String url = "jdbc:h2:/data/sample;IFEXISTS=TRUE"; in this case,if the database does not already exist, an exception is thrown when trying to connect. The connection only succeeds when the database already exists.

//- in Intellij: View - Tool Windows - Database - create new; url like jdbc:h2:C:/Users/stas/Documents/evo/h2 (if localhost)


var.stream().map(x -> x.method()).collect(Collectors.joining("/")));
//return builder.toString();

JSON: to exclude: add transient or static to field or create json as GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().
The Gson instance created will exclude all fields in a class that are not marked with @Expose annotation. You can write custom ExclusionStrategy

prepared statement;
sign in and sign up;
filter to main page;
when and where close?

userdao?
security?
