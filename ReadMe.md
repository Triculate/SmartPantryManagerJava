Smart Pantry Manager is a Java-based Android application made for Mobile App Development 700
practical assignment.
It aims to reduce household food waste by helping users manage ingredients they
already have and discover recipes they can prepare without purchasing additional
ingredients.

Main Features:
Pantry Management: Add, view, edit, and delete ingredients.
SQLite Database: Stores ingredients and recipes locally on the device.
Strict Recipe Matching: Suggest recipes only when every needed ingredients is available
in the quantity.
Recipe Details: display ingredients, quantities and preparation instructions.
Ingredient Name Normalisation: Recognise common variations like egss and eggs.
Conversion: supports compatible measurements like grams,kilograms, millilitres and litres
Expiry Warnings/Reminder: points out ingredients that have expired or will expire soon
Settings: expiry warnings/reminders can be enabled or disabled.
Custom ListView adapter: shows pantry ingredients with edit and delete individual buttons.

Technologies used:
Java = Application functionality
Android Studio = Development environment
XML = User interface layouts
SQLite = local database
SQLiteOpenHelper = database creation and management
SharedPreferences = saving application settings
Custom adapter and listView = showing pantry data
Git and GitHub = version control

Database design:
pantry_items: stores ingredient names, quantities, measurement units and optional expiry dates
recipes: stores recipe names and preparation instruction
recipe_ingredients: stores ingredients and quantities needed for each recipe

Recipe Matching:
Recipe suggestion comes in when:
every needed ingredient is present.
each ingredient is available and has the right quantity.

Why SQLite was chosen:
It is lightweight, works without internet connection and is supported directly by Android.
Users can store and manage pantry ingredients locally without needing a separate
database server or cloud account.

How to run the application:
1.Download or clone this GitHub repository.
2.Open Android Studio.
3.Select Open and choose the SmartPantryManagerJava project folder.
4.Allow Gradle to async and install any required dependencies.
5.Select a compatible Android emulator or connected Android device.
6.Click Run.
7.Use the application to add pantry ingredients and discover matching recipes.
it was tested and developed using a Pixel 4 Android emulator running Android 10 (API 29).

Application Screens:
-Home Screen
-Pantry List Screen
-Add/Edit Ingredient Screen
-Suggested Recipes Screen
-Recipe Detail Screen
-Settings Screen