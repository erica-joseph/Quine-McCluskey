# P1
Final project for course.

Main.java contains my attempt at writing a code for a QM algorithm.
I found that I struggled quite a bit with visualizing what I was doing, which led me to storing everything in lists and arraylists.
While I got the project to run, it didn't run particularly well. It can handle most cases and achieve a good output such as,
.i 4
.o 1
.ilb A B C D
0010 1
0101 1
0110 1
0111 1
1000 1
1010 1
1100 1
1101 1
1110 1
.e

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
