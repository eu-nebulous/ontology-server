# Ontology Server

The ontology server is responsible for handling the NebulOuS ontology utilised by the SLA Generator and Brokerage Quality Assurance.

# Building and Running the Server

Maven is required to build this project.
Java is required to run this project.

1. clone the repository;
2. you may specify the port the server will be running on by editing the value `src/main/resources/application.properties`. The default is `server.port=80`;
3. run `mvn package -Dmaven.test.skip` on the root folder;
4. an executable `nebulous-ont-x.y.z.jar` file will be created in the `target` folder;
5. run the executable by using the `java -jar nebulous-ont-x.y.z.jar {{ontology file}}` command. You need to pass the ontology file location as a parameter.

Terminating the program is done by aborting the task (CTRL + c in most command lines). 
Currently, the ontology server has an in-memory implementation and terminating the program will erase the current ontology.

# Using the Server

Other component owners should have little reason to directly use the Ontology Server. Nevertheless, a list of all the endpoints follows:

## Get Requests

| URL | Request Parameters | Returns | Description |
| --- | ------------------ | ------- | ----------- |
| /countInstances | dlQuery | int | Returns the number of instances that match the specified DL query |
| /get/instances | dlQuery | List<String> |Returns the instances that match the specified DL query |
| /exists/dataProperty | dataProperty | boolean | Returns whether the named data property exists in the ontology |
| /get/dataProperty | individualName, dataProperty | List<Object> | Returns all data properties of the specified named related with the specified individual |
| /get/superclasses | dlQuery | List<String> | Return all the superclasses of the instances described by the DL Query |

## Post Requests

| URL | Request Body | Returns | Description |
| --- | ------------ | ------- | ----------- |
| /save | --- | --- | Save the ontology in a file named `output.ttl` in the same directory as the .jar |
| /create/individual | {"individualURI": "xxx", "classURI": "yyy"} | --- | create an individual of the specified class with the specified URI |
| /create/objectProperty | {"objectPropertyURI": "xxx", "domainURI": "yyy", "rangeURI": "zzz"} | --- | Create an object property assertion using the specified object property between the two specified instances |
| /create/dataProperty | {"dataPropertyURI": "xxx", "domainURI": "yyy", "dataPropertyURI": "zzz"} | --- | Create a data property assertion using the specified data property on the specified instance with the specified value |

# DL Queries

DL Queries are a way to describe an OWL Object (e.g. Classes, Instances) and ask the reasoner to return all those that match that description. They are written in the [class expression syntax](http://protegeproject.github.io/protege/class-expression-syntax/).