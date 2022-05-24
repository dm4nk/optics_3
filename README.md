# optics_3

Project crated for learning purposes for optical informatics course
in the university made with
[Java 17](https://jdk.java.net/17/),
[Spring Boot](https://spring.io/projects/spring-boot), 
[Vaadin](https://vaadin.com/) and Python for better visual demonstration.

Programm is used for comparing [Fast Fourier](https://en.wikipedia.org/wiki/Fast_Fourier_transform) and [Hankel](https://en.wikipedia.org/wiki/Hankel_transform) transformations.

## One Dimension

Here we can see how Function looks.
![image](https://user-images.githubusercontent.com/80630476/170111479-4d98c8de-f62e-435f-9c20-5f9ea3f580f0.png)

And here how it looks after Hankel transformation.
![image](https://user-images.githubusercontent.com/80630476/170111625-e547aa89-986b-4141-a177-2b171ad180d7.png)

## Three Dimension
We can spin this function around (0, 0) and get a 3 dimetional proejction. Here's what we got.

![image](https://user-images.githubusercontent.com/80630476/170111975-9e725a9c-0f1b-4ee1-93ff-b046597336c6.png)

And same with Hankel transformation.
![image](https://user-images.githubusercontent.com/80630476/170112087-f1b62f30-c7d5-4d43-a8c7-3b346d7eab26.png)

So, Hankel and Fast Fourier have the same purposes, and we expect same result from 3-dimentional Fast Fourier Transform.
![image](https://user-images.githubusercontent.com/80630476/170112274-b1fdb388-c949-4db2-98c5-c5d279750d2a.png)

As we can see, the results are the same, except that Hankel ransform spent 3 milliseconds, while FFT - 2 seconds. 

Hankel Transform seems to be 60 times more efficient.
