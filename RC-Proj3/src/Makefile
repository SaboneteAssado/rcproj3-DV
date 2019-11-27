all: Packet.class Payload.class Link.class Event.class Router.class Config.class Simulator.class RoutingAlgorithm.class RoutingPacket.class RoutingTableEntry.class DV.class 

everything : all javadoc tests

javadoc:  
	mkdir -p docs
	javadoc -Xdoclint:none -quiet -d docs Payload.java Packet.java Link.java Event.java Router.java Config.java Simulator.java RoutingAlgorithm.java RoutingPacket.java RoutingTableEntry.java

gcj:  
	gcj --main=Simulator -o Simulator DVsolution.so Packet.java Payload.java Link.java Event.java Router.java Config.java Simulator.java RoutingAlgorithm.java RoutingPacket.java RoutingTableEntry.java
	gcj -C DV.java
	strip Simulator

clean:
	rm -rf docs Simulator *.class

tests: all test0 test1 test2 test3 test4 test5

test0: all
	@echo "running test0.cfg and outputing results to test0Output.txt"
	java Simulator test0.cfg > test0Output.txt
test1: all
	@echo "running test1.cfg and outputing results to test1Output.txt"
	java Simulator test1.cfg > test1Output.txt
test2: all
	@echo "running test2.cfg and outputing results to test2Output.txt"
	java Simulator test2.cfg > test2Output.txt
test3: all
	@echo "running test3.cfg and outputing results to test3Output.txt"
	java Simulator test3.cfg > test3Output.txt
test4: all
	@echo "running test4.cfg and outputing results to test4Output.txt"
	java Simulator test4.cfg > test4Output.txt
test5: all
	@echo "running test5.cfg and outputing results to test5Output.txt"
	java Simulator test5.cfg > test5Output.txt

Packet.class: Packet.java
	javac Packet.java

Payload.class: Payload.java
	javac Payload.java

Link.class: Link.java
	javac Link.java

Event.class : Event.java
	javac Event.java

Router.class : Router.java
	javac Router.java

Config.class : Config.java
	javac Config.java

Simulator.class : Simulator.java
	javac Simulator.java

RoutingAlgorithm.class : RoutingAlgorithm.java
	javac RoutingAlgorithm.java

RoutingPacket.class : RoutingPacket.java
	javac RoutingPacket.java

RoutingTableEntry.class : RoutingTableEntry.java
	javac RoutingTableEntry.java

DV.class : DV.java
	javac DV.java

help :
	@echo "Makefile options"
	@echo ""
	@echo "all : compiles all classes"
	@echo "javadoc : generates javadoc documentation in docs directory"
	@echo "tests : runs all tests"
	@echo "test0 : runs test0 and generates test0Output.txt"
	@echo "test1 : runs test1 and generates test1Output.txt"
	@echo "test2 : runs test2 and generates test2Output.txt"
	@echo "test3 : runs test3 and generates test3Output.txt"
	@echo "test4 : runs test4 and generates test4Output.txt"
	@echo "test5 : runs test5 and generates test5Output.txt"
	@echo "clean : removes class files and javadoc"
	@echo "everything : compiles everything, builds docs and runs all tests"
	@echo ""
