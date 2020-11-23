# SpringBoot User Registeration Test
<hr>

## prerequisites and info:
1) MySQL DB
2) Netbeans
3) DB details in "application.properties"
4) Spring Boot Dependencies in pom.xml

## Mappings:

1) "/" or "/home":
<br>Welcome page: signup or login if an account is registered
<img src="screens/main.png" width="400">

2) "/signup":
<br> Registeration of a new user (Input validation + duplicate email check + password encoding)
<img src="screens/signup.png" width="400">
Account saved to db and user directed to login page
<img src="screens/DB.png">

3) "/login":
<br> User enters previously registered email and password
<img src="screens/login.png" width="400">

4) "/profile": (if not logged in, the user is redirected to login page)
<br> Displays details of session user with options to log out, edit details, or request a new password 
<img src="screens/profile.png" width="400">

5) "/profileEdit": (if not logged in, the user is redirected to login page)
<br> Allows the user to edit first and last name with the option to cancel
<img src="screens/profile_edit.png" width="400">

6) "/passEdit": (if not logged in, the user is redirected to login page)
<br> Allows the user to change password after providing the current one.
<img src="screens/profile_pass.png" width="400">

<hr>

## Credits:
tracking-in-expand and text-flicker-in-glow CSS animations from http://animista.net
