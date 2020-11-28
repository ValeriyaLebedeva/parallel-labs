# parallel-labs
## - Akka application with akka http designed for remote testing of JS applications.
#####State: Done
Compile and start:
```
cd laba4
mvn compile
mvn exec:java -Dexec.mainClass="laba4.JsScriptTester"
```
####Example:
```
curl -H "Content-type: application/json" -X POST -d '{"packageId":"13", "jsScript":"var divideFn = function(a,b) { return a/b} ", "functionName":"divideFn", "tests": [{"testName":"test1", "expectedResult":"2.0", "params":[2,1]}, {"testName":"test2", "expectedResult":"2.0", "params":[5,2]}]}' http://localhost:8082/test/execute
```
Output: Executed
```
curl -X GET localhost:8082/test/result/13
```
Output 