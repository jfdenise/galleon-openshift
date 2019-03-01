Postgresql feature-pack
=======================

Build a feature-pack for postgresql driver

- You must copy postgresql driver binary jar file to project root dir.
- Update org.demo.postgredriver.Main src with the name of jar file.
- To build the feature-pack: mvn clean package exec:java
- feature-pack zip file is generated in local-repo/org/jboss/galleon/demo/postgresql/1.0/postgresql-1.0.zip
