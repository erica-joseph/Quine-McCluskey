# P1
Final project for course.

Main.java contains my attempt at writing a code for a QM algorithm.
I found that I struggled quite a bit with visualizing what I was doing, which led me to storing everything in lists and arraylists.
While I got the project to run, it didn't run particularly well. It can handle most cases and achieve a good output such as,
<table>
<td>.i 4</td>
<td>.o 1</td>
<td>.ilb A B C D</td>
<td>0010 1</td>
<td>0101 1</td>
<td>0110 1</td>
<td>0111 1</td>
<td>1000 1</td>
<td>1010 1</td>
<td>1100 1</td>
<td>1101 1</td>
<td>1110 1</td>
<td>.e</td>
</table>

Which was an example implemented in class. The output expression is,
b'd' + ad' + cd' + bd

.i 4
.o 1
.m 4
.d 0
-0-0 1
1--0 1
--10 1
-1-1 1

Which is the output that was calculated in class.
However, when I start to throw more complex concepts into the code, it starts to get a bit finicky, such as dropping prime implicants properly between combining rounds.
The most difficult part was keeping track of the variables. While, naturally, it was not as difficult as doing it by hand, I found that myself creating new lists for almost every action.
If I had given myself more time, I think I would have come up with something much cleaner, but so much time was spent on trying to visualize the steps as they were happening.

Overall, it was a great learning experience.
