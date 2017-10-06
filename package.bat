call mvn clean compile assembly:single
del *.jar
copy .\target\*jar-with-dependencies.jar .

