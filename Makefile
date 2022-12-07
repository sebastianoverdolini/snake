run:
	java Snake.java

test:
	rm -f -r out
	javac -d out Tests.java
	java -ea -cp out Tests