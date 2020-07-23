# LandmarkRemark
## Landmark Remark Android App

**The applications lets users do the following:**
1. View the current location on a map
2. Save a text note at a location/landmark
3. View all text notes that were saved at a location/landmark 
4. View all landmarks on a map
5. Search notes by text in a note or by the user's name who added the note

## Implementation 
1. Facebook Login feature to create a user, better than tradition way of long user forms. 
2. Checking for Runtime Permissions to get the current location of the device.
3. Check if the location settings are ON, redirect to the settings page to enable location on the device. 
4. Implemented View Binding to avoid NullPointer Exceptions and Type safety.
5. NavigationView header displays the Name and Email address of the logged in User.
6. Displaying the current location using Google Maps and allowing to save note.
7. Implemented Firebase Realtime Database to save notes.
8. User can view his/her notes on 'My Landmarks' and all notes on 'All Landmarks On Maps' as markers on Google Maps and more information can be viewed by clicking on a specific marker.
9. Used Consumer Lambdas Object to pass code as asynchronous callback functions to find or load data from Firebase Database. 
10. Designed Custom Dialog to view notes.
11. All Notes are also displayed as a List using Recyclerview.
12. SearchView to find the specific note by Name or Title.
13. Logout functionality, logs out from Facebook and also clears Shared Preference data.

## Technologies & Tool used
1. Android Studio 4.0
2. Android API 29
2. Java 8
3. Firebase Realtime Database
4. Google Maps
5. Facebook Login

## Testing
1. Application is tested on Google Pixel 2.
2. On launch, Login page is loaded and Facebook login is used.
3. Runtime location permission is requested to get the current location of the user.
4. User can see the logged In user details on Navigation Header, i.e Username and Email.
5. Current location is Marked on Google Maps and Marker is used to add Notes at current location.
6. After successfully saving the Note in the Firebase Realtime Database, the user gets a Toast message.
7. If no note is added an alert is displayed as no notes found.
8. User's notes are displayed on Google Maps, can be viewed at 'My Landmarks' tab in Navigation Drawer.
9. All users notes are also displayed on Google Maps, can be viewed at 'All Landmarks on Map' tab in Navigation View. 
10. More information of notes can be viewed by clicking on the marker.
11. List of notes are displayed from all users.
12. Search option is provided to filter this list by 'Name' of the user or 'Title' of the note.
13. Logout option is provided.

## Limitations
1. Location permission is mandatory to use the application.
2. Note can only be added at the current location.
3. Search by name and title only.

## Time Spent
1. Facebook Login and create User - 1.5hrs
2. Setup Google Maps and Markers - 2hrs
3. Save Notes and display list of Notes - 2hrs
4. Search landmark - 1hr
5. Testing and code optimization - 3hrs
6. Comments and Documentation - 1hrs

## Screen Recording and Screenshots
Recording of running Landmark Remark App on Google Pixel 2 can be viewed under ScreenRecording(Video) and Screenshots Folders.

## APK
An APK file is provided under APK folder.












