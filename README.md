# mixtape

# How to build and run the app

mixtape is a Java based app using the Gradle package manager. Currently, there are 2 ways to run it

## Running using docker

If you want to run the app through a docker container, please use the following steps
1) `cd` into the project directory
2) Run `./gradlew clean && ./gradlew` - This will clean any previous build jars, build the app, create the service jar and build the docker image. After this command you should see mixtape:0.0.1 in your local docker registry
3) Run `./run.sh` - `run.sh` is a bash script which will run the docker image that was built in the previous step

See the `Dockerfile` and `run.sh` script for more details

**NOTE**: If this method is used, the application assumes that `mixtape.json` and `changes.json` are located in the directory `local/data` inside the project. See https://github.com/VinayLondhe14/mixtape/tree/master/local/data. The `output.json` will also be created in the `local/data` directory 

## Running directly through main

The other way is to run the app directly through main like so `./gradlew run --args='mixtape.json changes.json output.json'`. If using this method, the application assumes that `mixtape.json` and `changes.json` are at the same directory level as the application i.e. if this app is at `Workspace/mixtape`, the json files should be located at `Workspace/mixtape.json` and `Workspace/changes.json`. The output file will also be created at `Workspace/output.json`

# How to scale the application to deal with large input and/or change files
