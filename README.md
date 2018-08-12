# Add Review To Place On Map


## Description

When user opens the app, it will check network state first
If network is connected to Internet it will check if GPS is enabled or not<br>
Else it will inform user to check `Internet Conection`<br><br>

If `GPS` is Enabled it will track user gps and display user location on the map <br>
Else it will request user to enable `GPS`<br><br>

If user added reviews before, it will be displayed as Markers on the Map.<br><br>


If user clicks on a place on the map it will navigate to `Pick Place Builder`<br>
to choose place sepcifiaclly with it's details,<br>
then it will back to Parent ActivityForResult method to handle returned place.<br><br>

When the selected place is returend the app will show a `Review & Rating Dialog`<br>
to help user to add review and rate the picked place.

When user submit the review it's added to Firebase Database and A marker added to displayed Map<br><br>

## Libraries and Tools
- Firebase (Database)
- Google Map SDK API
- Fragment Activity
- Interface and Callbacks
- GPS

## Screenshots

<img src="ScreenShots/Screenshot_2018-08-12-19-44-17.jpeg" height="400" alt="Screenshot"/> <img src="ScreenShots/Screenshot_2018-08-12-19-44-42.jpeg" height="400" alt="Screenshot"/> <img src="ScreenShots/Screenshot_2018-08-12-19-44-51.jpeg" height="400" alt="Screenshot"/><br>
<img src="ScreenShots/Screenshot_2018-08-12-19-45-55.jpeg" height="400" alt="Screenshot"/> <img src="ScreenShots/Screenshot_2018-08-12-19-46-07.jpeg" height="400" alt="Screenshot"/>
