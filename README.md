# parser

This program parses webserver log file and save it in DB for further queries. The motive of program to create JAR and run JAR with predefined parameters. You can get DB schema and webserver log file in project resourse folder. Program executes using command.

java -cp "parser.jar" com.ef.Parser --accesslog=/<dir-path>/access.log --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500

This program written in Java 8.
