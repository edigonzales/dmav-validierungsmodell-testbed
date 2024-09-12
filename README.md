# dmav-validierungsmodell-testbed

```
./gradlew runProgram -PilivalidatorVersion=1.14.3 -Pdata=./src/test/data -Pconfig=./src/test/data/models/DMAV_V1_0_Validierung.ini -PmodelDir=./src/test/data/models
```

```
java -jar /Users/stefan/apps/ilivalidator-1.14.3/ilivalidator-1.14.3.jar --config ilidata:DMAV_V1_0_Validierung --xtflog CH031151.log.xtf  CH031151.xtf
```

```
./gradlew runProgram -PilivalidatorVersion=1.14.3 -Pdata=/Users/stefan/sources/dmav-validierungsmodell/data/Validierungsmodell -Pconfig=/Users/stefan/sources/dmav-validierungsmodell/model/DMAV_V1_0_Validierung.ini -Pmodeldir=/Users/stefan/sources/dmav-validierungsmodell/model
```

```
java -jar /Users/stefan/apps/ilivalidator-1.14.3/ilivalidator-1.14.3.jar --config ../../model/DMAV_V1_0_Validierung.ini --xtflog CH033551.log.xtf  CH033551.xtf
```
