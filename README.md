# Grader
Android application to help students in school.

Application is built with Kotlin language and it's using following technologies:

* Navigation component (Jetpack navigation)
* Kotlin coroutines
* View binding
* LiveData
* ViewModel
* Room

Application stores student's subjects and grades in a database. 
It simplifies grade average calculation and it contains functionality where students can set remainder for upcoming tests.

Subject creation is simplified and user friendly because student can include grades he already got.
Grades can be added with simple two step process, and deleted individually for every subject.

# Architecture

Grader uses MVVM design pattern.

<img src="https://miro.medium.com/max/960/0*r6tfHpmMPZYLstfz.png" width=700>

Underlaying layer consists of Room database with two tables for storing subjects and grades.

Through the repository, the application retrieves data into the ViewModel class which is connected to the corresponding fragment.
The application utilizes LiveData class through which it observes changes in the database layer and acts accordingly.

# Screenshots
<p float="left">
<img src="https://i.imgur.com/ydgxTrk.png" width="300">

<img src="https://i.imgur.com/N5ZlNRR.png" width="300">
</p>

<p float="left">
<img src="https://i.imgur.com/2z0MyB2.png" width="300">

<img src="https://i.imgur.com/Uep7l4B.png" width="300">
</p>

# Installation
Application is available on play store:
[Play store link](https://play.google.com/store/apps/details?id=com.electrocoder.grader)
