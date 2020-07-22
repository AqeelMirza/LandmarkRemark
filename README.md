# LandmarkRemark
## Landmark Remark Android App

**The application demonstrates the following functionality:**
1. As a user (of the application) I can see my current location on a map
2. As a user I can save a short note at my current location
3. As a user I can see notes that I have saved at the location they were saved
on the map
4. As a user I can see the location, text, and user-name of notes other users
have saved
5. As a user I have the ability to search for a note based on contained text or
user-name

## Implementation 
1. Facebook Login feature to create a user, better than tradition way of long user forms.
2. Implemented View Binding to avoid NullPointer Exceptions and Type safety.
3. Checking for Runtime Permissions to get the current location of the device.
4. Check if the location settings are ON, redirect to the settings page to enable location on the device. 
5. NavigationView header displays the Name and Email address of the logged in User.
6. Displaying the current location using Google Maps and allowing to save note.
7. Implemented Firebase Realtime Database to save notes.
8. User can view it's notes on 'My Landmarks' and all notes on 'All Landmarks On Maps' as markers on Google Maps and more information can be viewed by clicking on a specific marker.
9. Implemented Consumer Object to pass functions as asynchronous callbacks to find or load data from Firebase Database. 
10. Designed Custom Dialog to view notes.
11. All Notes is also displayed as List using Recyclerview.
12. SearchView to find the specific note by Name or Title.
13. Logout functionality, logs out from Facebook and also clears Shared Preference data.

## Technologies & Tool used
1. Facebook Login.
2. Google Maps.
3. Firebase Realtime Database.
4. Java 1.8
5. Android Studio 4.0

## Testing
1. Application is tested on Google Pixel 2.
2. On launch, Login page is loaded and Facebook login is used.
3. Runtime location permission is requested to get the current location of the user.
4. User can see the logged In user details on Navigation Header, i.e Username and Email.
5. Current location is Marked on Google Maps and Marker is used to add Notes at current location.
6. After successfully saving the Note in the Firebase Realtime Database, the user gets a Toast message.
7. User's notes are displayed on Google Maps, can be viewed at 'My Landmarks' tab in Navigation Drawer.
8. All users notes are also displayed on Google Maps, can be viewed at 'All Landmarks on Map' tab in Navigation Drawer. 
9. More information of notes can be viewed by clicking on the marker.
10. List of notes are displayed in 'All Landmarks' tab from all users.
11. Search option is provided to filter this list by 'Name' of the user or 'Title' of the note.
12. Logout option is provided.

## Limitations
1. User cannot access the application if the Location permission is not granted.
2. Note can only be added at the current location.
3. Search by name and title only.

## Time Spent
1. Facebook Login and create User - 1.5hrs
2. Setup Google Maps and Markers - 2hrs
3. Save Notes and display list of Notes - 2hrs
4. Testing and code optimization - 3hrs
5. Comments and Documentation - 1hrs

## Screen Recording and Screenshots
Working of Landmark Remark App on Google Pixel 2 can be viewed under ScreenRecording(Video) and Screenshots Folders.











