========================================================================
EXTERNAL JAR DEPENDENCIES DIRECTORY (WebContent/WEB-INF/lib/)
========================================================================

Since this project does NOT use Maven, third-party libraries must be placed
in this folder.

REQUIRED JAR:
1. MySQL Connector/J (JDBC Driver)
   - Filename: mysql-connector-j-8.3.0.jar (or mysql-connector-java-8.0.x.jar)
   - Download from: https://dev.mysql.com/downloads/connector/j/ 
     or https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar

HOW TO USE IN ECLIPSE / STS / INTELLIJ:
1. Copy the downloaded 'mysql-connector-j-8.x.x.jar' into this folder:
   `WebContent/WEB-INF/lib/`
2. In Eclipse: Right-click project -> Properties -> Java Build Path -> Libraries -> Add JARs -> select this jar.
3. In Tomcat: Any JAR placed in `WEB-INF/lib/` is automatically added to the web application runtime classpath.
========================================================================
