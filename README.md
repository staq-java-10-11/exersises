# Preparation
## Install Java 10 on Ubuntu
```
sudo add-apt-repository ppa:linuxuprising/java
sudo apt-get update
sudo apt-get install oracle-java10-installer
```

## Install Java 10 on Windows
Download the installer from the Oracle website.

## Compile with Maven
```
export JAVA_HOME=/path/to/jdk-10
mvn clean compile
```

# Exercises
The exercises in this project guide you through the high-impact *Local-Variable Type Inference* language feature of Java 10, and a useful but currently incubating *HTTP Client* API addition that is to be standardized in Java 11.  

## Local-Variable Type Inference
The `localvar` module should help you get familiar with the possibilities and constraints of Local-Variable Type Inference.
The `LocalVar` class contains an extensive overview of example scenarios in which the `var` declaration may or may not be used.
Try to think of which ones will compile (when uncommented), and which ones will not. Did you get them all correct?

## HTTP Client
The `httpclient-demo` module should get you started on using the Java HTTP Client. Both "plain" HTTP and WebSockets are touched.

### Plain HTTP
The plain HTTP exercises consist of four different classes which demonstrate different aspects of the HTTP Client API, such as HTTP/2 support, response code handling and Flow API Subscriber integration.
For your convenience, unit tests are provided to check your implementation. Try not to cheat!
To run these tests:
```
mvn clean verify
```

### WebSockets
In the WebSocketChatExercise you will find yourself implementing a chat client using WebSockets with the Java HTTP Client.
If all goes well, you should be able to chat with your fellow participants!
Try to not only follow the instructions, but use the exercise to learn more about the HTTP Client API.