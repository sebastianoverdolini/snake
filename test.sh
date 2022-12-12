#!/bin/sh

rm -f -r out
javac -d out Tests.java
java -ea -cp out Tests