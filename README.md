##EOLOPLANT

Steps to run the project:

Start docker containers for MySQL, MongoDB y RabbitMQ
	- MySQL: `docker run -p 3306:3306 --name mysql-db -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=test -e -d mysql:latest`

	- MongoDB: `docker run --rm -p 27017:27017 -d mongo:latest`

	- RabbitMQ: `docker run --rm -p 5672:5672 -p 15672:15672 rabbitmq:3-management`

Once we have the containers started, go to root project folder and run theese commands:
	- `node install.js` (*This script downloads dependencies(Node) and compiles(Java) projects*)
	- `node exec.js` (*This script runs each project so you can access the entry point and do magic*)

Now you can access `localhost:3000` and create an Eoloplant in the available cities.
