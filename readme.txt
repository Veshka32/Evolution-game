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
- copy h28.jar to C:\glassfish3\glassfish\lib ?
- add maven dependency;
- in glassish admin cabinet: Resources->JDBC->Conn Pools->new connection pool:
    - set the Datasource Classname to org.h2.jdbcx.JdbcDataSource.
    - set user, password, url like jdbc:h2:tcp://localhost/~/my_db_name

- add resource-ref to web.xml


//- in Intellij: View - Tool Windows - Database - create new; url like jdbc:h2:C:/Users/stas/Documents/evo/h2 (if localhost)


var.stream().map(x -> x.method()).collect(Collectors.joining("/")));
//return builder.toString();

JSON: to exclude: add transient or static to field or create json as GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().
The Gson instance created will exclude all fields in a class that are not marked with @Expose annotation. You can write custom ExclusionStrategy