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

<img src="ScreenShots/Screenshot_2018-08-12-19-44-17_framed.png" height="400" alt="Screenshot"/> <img src="ScreenShots/Screenshot_2018-08-12-19-44-42_framed.png" height="400" alt="Screenshot"/> <img src="ScreenShots/Screenshot_2018-08-12-19-44-51_framed.png" height="400" alt="Screenshot"/><br>
<img src="ScreenShots/Screenshot_2018-08-12-19-45-55_framed.png" height="400" alt="Screenshot"/> <img src="ScreenShots/Screenshot_2018-08-12-19-46-07_framed.png" height="400" alt="Screenshot"/>

## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2018 Ali Ussama

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
