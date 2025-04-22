# QM
Final project for course.

Main.java contains my attempt at writing a code for a QM algorithm.
I found that I struggled quite a bit with visualizing what I was doing, which led me to storing everything in lists and arraylists.
While I got the project to run, it didn't run particularly well. It can handle most cases and achieve a good output such as,
<br>
.i 4<br>
.o 1<br>
.ilb A B C D<br>
0000 1<br>
0010 1<br>
0101 1<br>
0110 1<br>
0111 1<br>
1000 1<br>
1010 1<br>
1100 1<br>
1101 1<br>
1110 1<br>
1111 1<br>
.e<br>

Which was an example implemented in class. The output expression is,
b'd' + ad' + cd' + bd
<br>
.i 4<br>
.o 1<br>
.m 4<br>
.d 0<br>
-0-0 1<br>
1--0 1<br>
--10 1<br>
-1-1 1<br>
<br>

Which is the output that was calculated in class.
However, when I start to throw more complex concepts into the code, it starts to get a bit finicky, such as dropping prime implicants properly between combining rounds.
The most difficult part was keeping track of the variables. While, naturally, it was not as difficult as doing it by hand, I found that myself creating new lists for almost every action.
If I had given myself more time, I think I would have come up with something much cleaner, but so much time was spent on trying to visualize the steps as they were happening.

I ran into the most trouble with row and column dominance, and consulted with ChatGPT to solve a few issues, especially in the variable handling. I asked it for an idea on how to approach the code, and it gave a roundabout response that I had to piece and idea out of.

Overall, it was a great learning experience.
