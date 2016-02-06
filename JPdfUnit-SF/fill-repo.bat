mvn deploy:deploy-file -Durl=file://D:\projects\jPDFUnit\workspace\i-jpdfunit\etc\Homepage\repo -DrepositoryId=local -Dfile=./target/jpdfunit-1.2.jar -DpomFile=pom.xml
mvn deploy:deploy-file -Durl=file://D:\projects\jPDFUnit\workspace\i-jpdfunit\etc\Homepage\repo -DrepositoryId=local -Dfile=./target/jpdfunit-1.2-sources.jar -DpomFile=pom.xml -Dclassifier=sources
mvn deploy:deploy-file -Durl=file://D:\projects\jPDFUnit\workspace\i-jpdfunit\etc\Homepage\repo -DrepositoryId=local -Dfile=./target/jpdfunit-1.2-javadoc.jar -DpomFile=pom.xml -Dclassifier=javadoc
