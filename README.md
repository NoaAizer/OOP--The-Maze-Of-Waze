# The Maze of Waze

***Authors:** Noa Aizer & Lior Samuel-Levy*

In this project we used the infrastructure of assignment 2 (graph).<br />
We have developed a logic for a game where a group of robots has to perform tasks by moving along the graph according to the direction of the side.<br />
Each edge has a weight and a "coin" collection, for each robot we have planned movement so that at the allotted time the total "coin" will be maximum.<br />

The coins are represented by fruits - banana and apple.<br />
That we move from low vertex to high we will collect - apple and if we move from high vertex to low we will collect - banana.<br />
There are 0-23 stages, each with a different number of robots, fruits and allotted time.<br />
Each fruit collected creates a new fruit at a random location.<br />

When playing the game the user chooses the stage and whether he wants automatic or manual play.<br />

In this project we received data from a server (jar file) on which the game is performed.<br />
Obtaining the information was based on strings represented as JSON.<br />

You can also see the graph, bots, and fruits in the KML file.<br />

**An example for a stage 10 in play:**<br />

![An Example:](https://github.com/NoaAizer/OOP_Ex3/blob/master/10.png)

Enjoy!
