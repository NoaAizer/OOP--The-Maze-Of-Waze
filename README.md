# The Maze of Waze 

***Authors:** Noa Aizer & Lior Samuel-Levy*

## Description:
In this project we used the infrastructure of assignment 2 - [Graph](https://github.com/NoaAizer/Graphs/wiki).<br />

We have developed a logic for a game where a group of robots has to perform tasks by moving along the graph according to the direction of the side.<br /> Each edge has a weight and a "coin" collection, for each robot we have planned movement so that at the allotted time the total "coin" will be maximum.<br />
The coins are represented by fruits - banana and apple.<br />
That we move from low vertex to high we will collect - apple and if we move from high vertex to low we will collect - banana.<br />

There are 0-23 stages, each with a different amount of robots, fruits and allotted time.<br />
Each fruit collected creates a new fruit at a random location.<br />

When playing the game the user chooses the stage and whether he wants automatic or manual play:<br />
* **Manual management-** The client can choose the location of the robots and decide how to move them at each step with the mouse.

* **Automatic management-** Our algorithm decide the robots locations and move them toward the fruits in order to achieve as many points as possible.The algorithm finds the nearest fruit with the highest value and moves there- according to the dijkstra algorithm.

In this project we received data from a server (jar file) on which the game is performed.<br />
Obtaining the information was based on strings represented as JSON.<br />

## Our Packages & Classes:
In the project we have 8 packages:<br />
**Algorithms:**
* Game_algo- This class has several algorithms that are used during the game such as: Location and moving the automatic robot, creating the elements, etc.<br />
* Graph_Algo- A class with some algorithms can be applied on a DGraph. We used the same class from the previous exercise - (for more information [Graph](https://github.com/NoaAizer/Graphs/wiki)).<br />

**Data Structures**<br />
* DGraph- This class representing a grpah with nodes and edges. We used the same class from the previous exercise ([Graph](https://github.com/NoaAizer/Graphs/wiki)), but we add 2 more methods for creating a DGraph from a JSON file and converting to a JSON file.<br />
(*Node & Edges- Same as the previous exercise.)

**Elements:**<br />
* Fruit<br />
* Robot<br />
* Arena<br />

**GameClient:**<br />
* Ex4_Client- this class IS the "main" method running the whole project. In order to start playing the game you need to run this class.<br />
* KML_Logger- Converting the game to a KML file.<br />
* MoveManual- A thread used for moving the robots manually (The manually moving was also conducted in StdDraw class).<br />
* MyGameGUI- Displaying of the game in JAVA (Using StdDraw).<br />
* SimpleDB- This class represents a simple example of using MySQL Data-Base.<br />

**Tests:**<br />
We have created 5 JUNIT test (using JUNIT 5 version).<br />

**Utils:**<br />
* StdDraw - We add an option to add the robot manually in this class. Also used for diplaying the game.<br />
( *Range & Point3D).

**Data:**<br />
In this package you will find the KML files and the elemnents images.<br />

**Lib:**<br />
GameServer_v0.27.jar<br />
GameServer_v0.32.jar<br />
GameServer_v0.33.jar<br />
java-json.jar<br />
gson-2.8.6.jar<br />
mysql-connector-java-5.1.47.jar<br />

An explanation of the game and an extension of each package in the project can be found at WIKI.<br />

## Example of stage 10 in the game:<br />

**Game GUI JAVA** <br />

![An Example:](https://github.com/NoaAizer/OOP_Ex3/blob/master/stage%2010%20.png)<br />

**Game GUI Google Earth** <br />


You can also see the graph, robots, and fruits in the KML file.<br />
The KML file can be opened in Google Earth, the view you will receive -<br />
![An Example:](https://github.com/NoaAizer/OOP_Ex3/blob/master/level%2010%20-%20kml.jpeg)

## Our Best Grades:<br />

![An Example:](https://github.com/NoaAizer/OOP_Ex3/blob/master/Best%20Grades.jpg)




# Enjoy!
