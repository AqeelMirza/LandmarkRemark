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
2. NavigationView displays the Name and Email address of the logged in User.
3. Displaying the location and saved markers notes using Google Maps.
4. Tap on the Marker on Google Maps to add Notes.
5. View Binding for NullPointer safety and Type safety.
6. Firebase Realtime Database to save notes.
7. Designed Custom Dialog to view Notes.
8. Recyclerview to display the list of Notes.
9. SearchView to find the specific note by Name or Title.

## Technologies & Tool used
1. Facebook Login.
2. Google Maps.
3. Firebase Realtime Database.
4. Java
5. Android Studio 4.0

## Testing
1. Application is tested on Google Pixel 2.
2. On launch, Login page is loaded and Facebook login is used.
3. Runtime location permission is requested to get the current location of the user.
4. User can see the loggedIn user details on Navigation Header, i.e Username and Email.
5. Current location is Marked on Google Maps and Marker is used to add Notes at the location.
6. After successfully saving the Note in the Firebase Realtime Database, the user gets a Toast message.
7. The User's notes are displayed on Google Maps and can be viewed at 'My Landmarks' tab in Navigation Drawer.
8. A list of notes are displayed in 'All Landmarks' tab from all users.
9. Search option is provided to filter this list by 'Name' of the user or 'Title' of the note.
10. Logout options is provided.

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













