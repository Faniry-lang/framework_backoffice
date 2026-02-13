mvn install:install-file ^
  -Dfile="C:\Users\ME-PC\Documents\GitHub\ProjetFrameworkS5\backoffice\libs\framework.jar" ^
  -DgroupId=com.itu.framework ^
  -DartifactId=framework ^
  -Dversion=1.0-SNAPSHOT ^
  -Dpackaging=jar

mvn install:install-file ^
  -Dfile="C:\Users\ME-PC\Documents\GitHub\ProjetFrameworkS5\backoffice\libs\legacy-orm-1.0-SNAPSHOT.jar" ^
  -DgroupId=com.itu.legacy ^
  -DartifactId=legacy ^
  -Dversion=1.0-SNAPSHOT ^
  -Dpackaging=jar
