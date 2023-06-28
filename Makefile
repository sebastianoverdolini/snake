run:
	javac -d out/main src/main/*.java
	java -ea -cp out/main Main

test:
	javac -d out/main src/main/*.java
	javac -d out/test -cp out/main src/test/*.java
	java -ea -cp out/test:out/main Main

tcr:
	make test && git add . && git commit -m WIP || git reset --hard
