# The Stick Game
This system was developed following the premises below:
```console
We have a new game called "The Stick Game" hosted on its own web site. Its working really nice so far. To learn more about our users and use some viral features of the platform we connected our web site with Facebook.
Tasks
(1) The game provides statistics about your won and lost games. Currently they are stored in the local storage of the browser if there is one available. To encourage people to sign-in with Facebook we want to provide some features based on Facebook login like sharing results with other gamers/bragging about won games etc. For the new features we need a possibility to store the statistics in a database shared by all users so that they e.g. can see each other’s won games.
(a) Please create a REST API for retrieving and storing the statistics from a database. You should use Java as implementation language, but you can chose whatever framework you find most appropriate to solve the task
(b) You can use an in-memory DB or a real one to maintain the data - please ensure that we can easily setup your created application for deploying/testing - provide setup hints if necessary

￼(2) Additional to the user specific games statistics, maintain a global (persistent) statistic about total games won, lost and the average win/loss ratio. In other words, this statistic reflects the outcome of all played games regardless of the actual user who played.

(3) Whenever the user wins a game there should be a post to the users Facebook wall saying that the current user won x games and there should be an action link back to the game where any Facebook user that clicks on this link will get back to the game and see the corresponding user's full statistics and then be able to play himself.
(a) Setup the posting to the users wall with the appropriate link
(b) Provide a way to show the original user's statistics and let him then play afterwards
```

## Technologies
At this section I'll list and explain which technologies I've chosen to work on:
* Java - the required language for this application
* Redis - Key/Value database with easily integration using Jedis library. I've decided to use this database to implement a simple layer of access and to get a better performance throughput. I've stored keys with no ttl expiration and using default policy of writing into disk to persist, this way I've got a key/value database with disk persistency
* RestEasy - Lightweight framework to create REST APIs [http://resteasy.jboss.org/]
* Google Guice - This library manages object creation and instantiation and it's very lightweight in order to avoid heavy footprints during bootstrap application [https://github.com/google/guice]
* GSON - This library was used to convert API's response endpoint into objects from the system. Again, it's very simple and lightweight [https://github.com/google/gson]
* Logback - Async logging library [http://logback.qos.ch/]
* JUnit - unit tests framework to test each class and method individually [http://junit.org/junit4/]
* Mockito - Framework to create objects mocks helping unit tests to segregate layers and objects dependencies [http://mockito.org/]

## Motivations
The system was designed to be simple but at the same time using approaches to help easy understanding, maintain and improve the system like some other production should have. Below there's a list of mindsets that guided implementation:
* Rest API - I've created a simple REST API using RestEasy. The main goal of this is to create semantic APIs and returning correct response including HTTP Status code and so on.
* Key/Value database - I've implemented this application using a key/value database to get a easy data access using userId or globalStatistic depending on the flow. Every time a user win/lost the total amount is updated in database and returned in the API.
* Logging - every production application must have logging for troubleshooting and to know used parameters on each flow and so on.
* Exceptions handling - the code is supported by an exception mechanism controlling the flow. This approach needs more code (creating custom exceptions and check all exceptions) but lets the system more robust.
* IoC - Using IoC (Inversion of Control) mechanism helps to not creating a lot of objects in memory and avoiding large footprints besides code gets simpler as well. 
* Unit tests - To make sure all the corner cases were coverage including code coverage. I've implemented some unit tests to cover flows in DAO and Service classes ;)

## APIs Documentation
* GET /service/statistics/ - returns global statistics data, including total amount of win, lost and win ratio
```console
{
	"wonRatio": 72.22,
	"totalWon": 13,
	"totalLost": 5
}
```

* GET /service/statistics/{userId}/ - this API receives a userId as pathParam and returning the win and lost amounts for the informed user
```console
{
	"totalWon": 5,
	"totalLost": 2
}
```

* PUT /service/statistics/{userId}/ - this API receives a JSON input in the following format and updates win/lost data for the informed user:
```console
{
	"totalWon": 1,
	"totalLost": 0
}
```
Note: this API was designed to receive at same time totalWon and totalLost because depending on the frontend implementation, it can avoid calling backend in every game, so frontend can accumulate some played games and call when it should be a good time passing all sum values. The implementation I've made in frontend is calling backend every time a game is ended, though.

## How to run
To run the system it's mandatory to have installed and running Redis [http://redis.io/]. After installed and started, Redis should be running at localhost:6379. This is the default URL and PORT the application will try to connect at startup. If your Redis was installed in a different url and port, you can provide this information at startup using environment parameters. Below I'll show an example of providing them.

The system produces a war file and can be easily started by calling embedded jetty into maven, so to run you just need Maven and Java installed on machine. After setting up them, just run the following command in the ROOT application directory:
> `mvn clean install jetty:run`

```console
[INFO] Started ServerConnector@36570936{HTTP/1.1}{0.0.0.0:8080}
[INFO] Started Jetty Server
```
When the lines above appear in the console the application is up and running to start receiving requests.

To provide alternate Redis URL and PORT during startup, you can run the following command:
> `mvn clean install -Dgameduell.redis.server.url=localhost -Dgameduell.redis.server.port=6379 jetty:run` 


## FrontEnd
To start using the application without calling REST APIs directly, you can access the frontend, just access the follow url:
* http://localhost:8080/stickgame/

Note: I'm not so experienced with frontend applications, so I've made just a few changes in the files you've sent me including a modal to capture login, a label in the header to show logged user and Ajax implementation to calling REST APIs in the backend. I've tried to implement a facebook post, but I've not concluded this step, sorry about that.

## API Requests examples
* http://localhost:8080/stickgame/service/statistics/

This GET call will retrieve a global statistics about winning and losses including win ratio. If there is no global statistic, the application will return 404 status code with a message "global statistics not found"

* http://localhost:8080/stickgame/service/statistics/{userId}

This GET call will retrieve a statistics about winning and losses for provided userId. If there is no statistic related to this user, the application will inform it in log and return a response with zero values in json in order to help frontend avoid doing additional validations.

* PUT http://localhost:8080/stickgame/service/statistics/{userId}

This PUT method will receive a json with totalWon and totalLost values, retrieve an already stored values for this user (if exists), sum and update key on redis database. Below an example of calling this API with curl:
> `curl -v -H "Content-Type: application/json" -X PUT http://localhost:8080/stickgame/service/statistics/leo -d'{"totalWon":1,"totalLost":0}'`



