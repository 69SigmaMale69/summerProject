 SnowProblems

## Rename files

```
mv src/SnowBall.java src/Snowball.java
mv src/SnowBallStack.java src/SnowballStack.java
mv src/snowmanHead.java src/SnowmanHead.java
mv src/Pieces.java src/Piece.java
```

## Install Java 25 on Ubuntu

```
sudo apt install curl zip unzip
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 25-tem
```

Verify:

```
java -version
javac -version
```

## Compile and run

```
javac -d out src/*.java
java -cp out Main
```
