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

To connect to H2 DataBase:
- in Intellij: View - Tool Windows - Database - create new; url like jdbc:h2:C:/Users/stas/Documents/evo/h2 (if localhost)
- copy h28.jar to C:\glassfish3\glassfish\lib ?
- add maven dependency;
- To use H2 with Glassfish (or Sun AS), set the Datasource Classname to org.h2.jdbcx.JdbcDataSource. You can set this in the GUI at Application Server - Resources - JDBC - Connection Pools, or by editing the file sun-resources.xml: at element jdbc-connection-pool, set the attribute datasource-classname to org.h2.jdbcx.JdbcDataSource.

var.stream().map(x -> x.method()).collect(Collectors.joining("/")));
//return builder.toString();