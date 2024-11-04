package com.example.medease;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ProfileActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private ImageView profileImageView;
    private EditText editTextUsername, editTextAddress, editTextPinCode;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);

        // Inisialisasi Views
        profileImageView = findViewById(R.id.profileImageView);
        editTextUsername = findViewById(R.id.editTextBMBFullName);
        editTextAddress = findViewById(R.id.editTextBMBAddress);
        editTextPinCode = findViewById(R.id.editTextBMBPinCode);
        Button changePictureButton = findViewById(R.id.btn_change_picture);
        Button saveProfileButton = findViewById(R.id.btn_change_picture2);

        // Memuat data dari SharedPreferences
        loadUserProfile();

        // Memeriksa dan meminta izin kamera secara runtime
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

        // Event untuk mengubah atau menghapus gambar profil
        changePictureButton.setOnClickListener(view -> showPictureDialog());

        // Event untuk menyimpan informasi profil
        saveProfileButton.setOnClickListener(view -> saveUserProfile());
    }

    // Menampilkan dialog pilihan untuk mengambil, menghapus, atau memilih gambar dari galeri
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select from Gallery",
                "Capture from Camera",
                "Delete Picture"  // Tambahkan opsi untuk menghapus gambar
        };
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            choosePhotoFromGallery();
                            break;
                        case 1:
                            takePhotoFromCamera();
                            break;
                        case 2:
                            deleteProfilePicture();  // Panggil metode untuk menghapus gambar
                            break;
                    }
                });
        pictureDialog.show();
    }

    // Fungsi memilih foto dari galeri
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    // Fungsi mengambil foto menggunakan kamera
    private void takePhotoFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                Uri contentUri = data.getData();
                if (contentUri != null) {
                    profileImageView.setImageURI(contentUri);
                    saveImageUriToPreferences(contentUri);
                } else {
                    Toast.makeText(this, "Failed to get image from gallery", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if (thumbnail != null) {
                    profileImageView.setImageBitmap(thumbnail);
                    Uri imageUri = saveImageToGallery(thumbnail);
                    if (imageUri != null) {
                        saveImageUriToPreferences(imageUri);
                    }
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Action canceled", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to save Bitmap to gallery and get URI
    private Uri saveImageToGallery(Bitmap bitmap) {
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "ProfileImage",
                "Image of profile"
        );
        return Uri.parse(savedImageURL);
    }

    // Helper method untuk menyimpan URI gambar ke SharedPreferences
    private void saveImageUriToPreferences(Uri imageUri) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile_image_uri", imageUri.toString());
        editor.apply();
    }

    // Helper method untuk mengambil URI gambar dari SharedPreferences
    private Uri getImageUriFromPreferences() {
        String imageUriString = sharedPreferences.getString("profile_image_uri", null);
        if (imageUriString != null) {
            return Uri.parse(imageUriString);
        } else {
            return null;
        }
    }

    // Helper method untuk menghapus gambar profil
    private void deleteProfilePicture() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("profile_image_uri");
        editor.apply();

        // Set gambar default di ImageView setelah dihapus
        profileImageView.setImageResource(R.drawable.ic_baseline_person_24);
        Toast.makeText(this, "Profile picture removed", Toast.LENGTH_SHORT).show();
    }

    // Helper method untuk menyimpan data pengguna ke SharedPreferences
    private void saveUserProfile() {
        String username = editTextUsername.getText().toString();
        String address = editTextAddress.getText().toString();
        String pinCode = editTextPinCode.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("address", address);
        editor.putString("pin_code", pinCode);
        editor.apply();

        Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show();
    }

    // Helper method untuk memuat data pengguna dari SharedPreferences
    private void loadUserProfile() {
        String username = sharedPreferences.getString("username", "");
        String address = sharedPreferences.getString("address", "");
        String pinCode = sharedPreferences.getString("pin_code", "");

        editTextUsername.setText(username);
        editTextAddress.setText(address);
        editTextPinCode.setText(pinCode);

        // Load profile image if available
        Uri imageUri = getImageUriFromPreferences();
        if (imageUri != null) {
            profileImageView.setImageURI(imageUri);
        } else {
            // Set gambar default jika tidak ada gambar yang disimpan
            profileImageView.setImageResource(R.drawable.ic_baseline_person_24);
        }
    }
}
