# Location API

This is an API created using [Scala](https://www.scala-lang.org/) and the [Play framework](https://www.playframework.com/).

## Pre-requesites

This API uses the `sbt` build tool. To install SBT, follow the guide on the reference manual for your OS.

[SBT Reference Manual](https://www.scala-sbt.org/1.x/docs/Setup.html)

### Getting Started

To run the API, simply clone/download the repository and run the command:

`sbt run`

Once the application has compiled, it will then default to port `9000`.

You can specify a desired port instead of 9000 by in the command:

`sbt "run <port-number>"`

***
## Endpoints

The available endpoints for this API are:

| Method | URL | Description |
|---|---|---|
|GET| `/`| Returns a JSON list of users who live within a 50 mile radius of London |
|GET| `/users`| Returns a JSON list of users who live within a 50 mile radius of London |

## Running unit tests

To run the unit tests for the project, run the following command:

`sbt test`
***