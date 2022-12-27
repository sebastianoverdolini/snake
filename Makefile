run:
	java SnakeGame.java

test:
	javac -d out Tests.java
	java -ea -cp out Tests

tcr:
	make test && git add . && git commit -m WIP || git reset --hard
