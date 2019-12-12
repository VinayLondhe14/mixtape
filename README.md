# mixtape

# How to build and run the app

mixtape is a Java+Gradle based app. Currently, there are 2 ways to run it

## Running using docker

If you want to run the app through a docker container, please use the following steps
1) `cd` into the project directory
2) Run `./gradlew clean && ./gradlew` - This will clean any previous build jars, build the app, create the service jar and build the docker image. After this command you should see mixtape:0.0.1 in your local docker registry
3) Run `./run.sh` - `run.sh` is a bash script which will run the docker image that was built in the previous step

See the `Dockerfile` and `run.sh` script for more details

**NOTE**: If this method is used, the application assumes that `mixtape.json` and `changes.json` are located in the directory `local/data` inside the project. We already have the 2 files there. See https://github.com/VinayLondhe14/mixtape/tree/master/local/data. Feel free to update them as necessary. The `output.json` will also be created in the `local/data` directory. 

## Running directly through main

The other way is to run the app directly through main like so `./gradlew run --args='mixtape.json changes.json output.json'`. If using this method, the application assumes that `mixtape.json` and `changes.json` are at the same directory level as the application i.e. if this app is at `Workspace/mixtape`, the json files should be located at `Workspace/mixtape.json` and `Workspace/changes.json`. The output file will also be created at `Workspace/output.json`. **This method assumes Java is installed on your system**

# changes.json format

The changes file should be a JSON file. It contains arrays with a key for each operation `add_song`, `add_playlist` and `remove_playlist`. 

For eg:
```
{
  "add_song" : [
    {
      "playlist_id" : "2",
      "song_id" : "25"
    },
    {
      "playlist_id" : "3",
      "song_id" : "24"
    }
 ],
 "add_playlist" : [
    {
      "playlist_id" : "12",
      "user_id" : "2",
      "song_ids" : [
        "8",
        "32"
      ]
    }
 ],
 "remove_playlist": [
    {
      "playlist_id" : "3"
    }
  ]
}  
```

This example changes file does the following:
1) adds songs with ids 25 and 24 to playlists with ids 2 and 3 respectively
2) adds playlist with id 12 for user_id 2 containing songs with ids 8 and 32
3) removes playlist with id 3

An example changes file is located at https://github.com/VinayLondhe14/mixtape/blob/master/local/data/changes.json

# Assumptions and Working Behavior

1) All ids are strings
2) If playlist_id is not set while adding a playlist, then the playlist is created with a random UUID
3) All operations are done using the respective ids
4) If the changes file is invalid, then no changes are applied and an IllegalArgumentException is thrown with all the violations. This decision was taken so that it is easier to reason about failures

# When is the changes file invalid?

The changes file is invalid if any of the following is true:

**For an `add_song` call**
1) song_id is empty
2) playlist_id is empty
3) song_id does not exist in the mixtape file
4) playlist_id does not exist in the mixtape file
5) song_id already exists in the songIds array of the playlist in which we want the songId added

**For an `add_playlist` call**
1) playlist_id already exists in the mixtape.json file
2) user_id is empty
3) user_id does not exist in the mixtape file
4) song_ids is not null or empty
5) song_ids has duplicate songs
6) all songs in song_ids exist in the mixtape file

**For a `remove_playlist` call**
1) playlist_id is empty
2) playlist_id does not exist in the mixtape file

Take a look at https://github.com/VinayLondhe14/mixtape/blob/master/src/main/java/com/coding/exercise/ChangesValidator.java for more details

# How to scale the application to deal with large input and/or change files

If we are dealing with large files, then we cannot load them up in memory since we will run out of memory. 

I can see a couple of solutions to this problem

1) One of the solutions is to stream the mixtape file(as opposed to loading it directly in memory) and create users, playlists and songs in a data store. Then do the same with the changes file and apply the changes to the relevant objects in the data store. Finally, stream all the contents of the data store back to the client. 

2) The other way would be to create a RESTful web service instead and expose endpoints to add songs and add/remove playlists. Then instead of dealing with json files, the client can directly make PUT/POST requests against the web service. 
