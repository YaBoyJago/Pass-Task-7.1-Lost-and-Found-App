# Lost and Found App

This is a simple Android Lost and Found app created for Task 7.1P. The app allows users to create posts for lost or found items, upload an image, view saved items, filter items by category, and remove an item once it has been returned.

## Project Overview

The app is designed to help connect lost items with their owners. A user can add an advert by entering the item details, selecting whether it is lost or found, choosing a category, adding a location, and uploading an image.

All adverts are saved locally using SQLite, so the data stays available while using the app.

## Main Features

- Create a lost or found advert
- Upload an image for each advert
- Save advert details using SQLite
- View all lost and found adverts in a list
- Filter adverts by category
- View full details of each advert
- Remove adverts once the item has been returned
- Back buttons between screens
- Date and time stamp for each post

## Categories

The app includes the following item categories:

- Electronics
- Pets
- Wallets
- Keys
- Bags
- Clothing
- Other

## Main Files

### `MainActivity.java`

This is the main activity for the app. It handles the screen navigation, button clicks, image selection, form validation, list display, detail display, and removal of adverts.

The app uses one activity and switches screens using different XML layouts.

### `DatabaseHelper.java`

This file handles the SQLite database. It creates the database table, inserts new adverts, retrieves adverts, filters adverts by category, gets a single advert by ID, and deletes adverts.

### `LostFoundItem.java`

This is the model class for a lost or found item. It stores the details for each advert, such as the name, phone number, description, category, location, image path, and created date.

### Layout Files

The main XML layout files are:

- `activity_main.xml` - home screen
- `activity_create_advert.xml` - form for creating an advert
- `activity_items.xml` - list of saved adverts
- `activity_item_detail.xml` - full detail view of one advert
- `list_item_advert.xml` - layout for each item in the list

## How to Run the App

1. Open the project in Android Studio.
2. Let Gradle sync.
3. Start an emulator or connect an Android device.
4. Press Run.
5. Use the home screen to create a new advert or view saved items.

## How to Use the App

1. Tap **Create New Lost and Found Advert**.
2. Select whether the item is lost or found.
3. Enter the item details.
4. Choose a category.
5. Upload an image.
6. Save the advert.
7. Tap **Show Lost and Found Items** to view saved adverts.
8. Use the category filter to narrow down the list.
9. Tap an item to view more details.
10. Remove the advert once the item has been returned.

## Notes

This project uses a local SQLite database rather than an online database. This was done because the task focuses on using SQLite in an Android app.

The images are copied into the app’s internal storage and the saved image path is stored in the database.

## Author

Adam Jago
