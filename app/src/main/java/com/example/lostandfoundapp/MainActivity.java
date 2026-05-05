package com.example.lostandfoundapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int PICK_IMAGE_REQUEST = 101;

    private final String[] categories = {"Electronics", "Pets", "Wallets", "Keys", "Bags", "Clothing", "Other"};
    private final String[] filterCategories = {"All Categories", "Electronics", "Pets", "Wallets", "Keys", "Bags", "Clothing", "Other"};

    private DatabaseHelper databaseHelper;
    private String currentScreen = "home";
    private String selectedImagePath = "";

    private ImageView itemImageView;
    private ListView itemsListView;
    private Spinner filterSpinner;

    private ArrayList<LostFoundItem> currentItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);
        showHomeScreen();
    }

    // ---------------- HOME ----------------
    private void showHomeScreen() {
        currentScreen = "home";
        setContentView(R.layout.activity_main);

        Button createAdvertButton = findViewById(R.id.createAdvertButton);
        Button showItemsButton = findViewById(R.id.showItemsButton);

        createAdvertButton.setOnClickListener(v -> showCreateAdvertScreen());
        showItemsButton.setOnClickListener(v -> showItemsScreen());
    }

    // ---------------- CREATE ----------------
    private void showCreateAdvertScreen() {
        currentScreen = "create";
        selectedImagePath = "";
        setContentView(R.layout.activity_create_advert);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> showHomeScreen());

        RadioButton lostRadio = findViewById(R.id.lostRadio);
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText phoneEditText = findViewById(R.id.phoneEditText);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);
        EditText locationEditText = findViewById(R.id.locationEditText);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        itemImageView = findViewById(R.id.itemImageView);

        Button selectImageButton = findViewById(R.id.selectImageButton);
        Button saveButton = findViewById(R.id.saveButton);

        categorySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));

        selectImageButton.setOnClickListener(v -> openImagePicker());

        saveButton.setOnClickListener(v -> {
            String postType = lostRadio.isChecked() ? "Lost" : "Found";

            String name = nameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String category = categorySpinner.getSelectedItem().toString();
            String location = locationEditText.getText().toString();

            String createdAt = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

            if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImagePath.isEmpty()) {
                Toast.makeText(this, "Upload image", Toast.LENGTH_SHORT).show();
                return;
            }

            databaseHelper.insertAdvert(postType, name, phone, description, category, location, selectedImagePath, createdAt);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

            showItemsScreen();
        });
    }

    // ---------------- ITEMS ----------------
    private void showItemsScreen() {
        currentScreen = "items";
        setContentView(R.layout.activity_items);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> showHomeScreen());

        itemsListView = findViewById(R.id.itemsListView);
        filterSpinner = findViewById(R.id.filterSpinner);

        filterSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filterCategories));

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadItems(filterCategories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        itemsListView.setOnItemClickListener((parent, view, position, id) -> {
            showItemDetailScreen(currentItems.get(position).getId());
        });
    }

    private void loadItems(String category) {
        currentItems = databaseHelper.getAdvertsByCategory(category);
        itemsListView.setAdapter(new ItemsAdapter());
    }

    // ---------------- DETAIL ----------------
    private void showItemDetailScreen(int id) {
        currentScreen = "detail";
        setContentView(R.layout.activity_item_detail);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> showItemsScreen());

        LostFoundItem item = databaseHelper.getAdvertById(id);

        TextView title = findViewById(R.id.detailTitleText);
        TextView info = findViewById(R.id.detailInfoText);
        ImageView image = findViewById(R.id.detailImageView);
        Button remove = findViewById(R.id.removeButton);

        title.setText(item.getPostType() + ": " + item.getName());

        File f = new File(item.getImagePath());
        if (f.exists()) {
            image.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
        }

        info.setText(
            "Category: " + item.getCategory() +
                    "\nPhone: " + item.getPhone() +
                    "\nLocation: " + item.getLocation() +
                    "\nPosted: " + item.getCreatedAt()
        );

        remove.setOnClickListener(v -> {
            databaseHelper.deleteAdvert(item.getId());
            Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
            showItemsScreen();
        });
    }

    // ---------------- IMAGE ----------------
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            selectedImagePath = copyImage(uri);
            itemImageView.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
        }
    }

    private String copyImage(Uri uri) {
        try {
            File dir = new File(getFilesDir(), "images");
            dir.mkdirs();

            File file = new File(dir, "img_" + System.currentTimeMillis() + ".jpg");

            InputStream in = getContentResolver().openInputStream(uri);
            FileOutputStream out = new FileOutputStream(file);

            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);

            in.close();
            out.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            return "";
        }
    }

    // ---------------- BACK ----------------
    @Override
    public void onBackPressed() {
        if (currentScreen.equals("home")) {
            super.onBackPressed();
        } else {
            showHomeScreen();
        }
    }

    // ---------------- ADAPTER ----------------
    private class ItemsAdapter extends BaseAdapter {

        public int getCount() { return currentItems.size(); }

        public Object getItem(int i) { return currentItems.get(i); }

        public long getItemId(int i) { return currentItems.get(i).getId(); }

        public View getView(int i, View v, ViewGroup p) {

            if (v == null) {
                v = getLayoutInflater().inflate(R.layout.list_item_advert, p, false);
            }

            LostFoundItem item = currentItems.get(i);

            ImageView imageView = v.findViewById(R.id.rowImageView);
            TextView title = v.findViewById(R.id.rowTitleText);
            TextView subtitle = v.findViewById(R.id.rowSubtitleText);

            // Text
            title.setText(item.getPostType() + ": " + item.getName());
            subtitle.setText(item.getCategory() + " • " + item.getLocation());

            // Image
            File imageFile = new File(item.getImagePath());

            if (imageFile.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options));
            } else {
                imageView.setImageResource(R.drawable.image_placeholder);
            }

            return v;
        }
    }
}