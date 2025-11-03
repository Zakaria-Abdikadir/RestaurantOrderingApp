package com.example.restaurantorderingapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderReceiptActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private TextView tvOrderItems, tvTotal, tvSubtotal, tvTax, tvOrderNumber, tvDate;
    private ImageView qrCodeImageView;
    private Button btnPrint, btnClearCart;
    private ScrollView scrollView;
    private android.widget.Spinner spinnerPaymentMethod;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_receipt);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Order Receipt");

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        tvOrderItems = findViewById(R.id.tvOrderItems);
        tvTotal = findViewById(R.id.tvTotal);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTax = findViewById(R.id.tvTax);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvDate = findViewById(R.id.tvDate);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod);
        btnPrint = findViewById(R.id.btnPrint);
        btnClearCart = findViewById(R.id.btnClearCart);
        scrollView = findViewById(R.id.scrollView);
        
        // Setup payment method spinner
        setupPaymentMethodSpinner();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        
        // Update navigation header with user email
        android.content.SharedPreferences sharedPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        android.view.View headerView = navigationView.getHeaderView(0);
        android.widget.TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        String userEmail = sharedPrefs.getString("loggedInUser", "user@example.com");
        tvUserEmail.setText(userEmail);
        
        cartManager = CartManager.getInstance();

        displayOrder();
        generateQRCode();

        btnPrint.setOnClickListener(v -> printReceipt());

        btnClearCart.setOnClickListener(v -> {
            cartManager.clearCart();
            Toast.makeText(this, "Cart cleared", Toast.LENGTH_SHORT).show();
            displayOrder();
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_dashboard) {
                finish();
                startActivity(new android.content.Intent(this, DashboardActivity.class));
                return true;
            } else if (id == R.id.nav_food) {
                startActivity(new android.content.Intent(this, FoodActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_drinks) {
                startActivity(new android.content.Intent(this, DrinksActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_order) {
                // Already here
                return true;
            }
            return false;
        });
    }

    private void setupPaymentMethodSpinner() {
        String[] paymentMethods = {"Cash", "Credit Card", "Debit Card", "Mobile Payment", "Online Payment"};
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentMethod.setAdapter(adapter);
        spinnerPaymentMethod.setSelection(0); // Default to Cash
    }

    private void displayOrder() {
        List<CartItem> items = cartManager.getCartItems();
        
        if (items.isEmpty()) {
            tvOrderItems.setText("No items in cart");
            tvSubtotal.setText("$0.00");
            tvTax.setText("$0.00");
            tvTotal.setText("$0.00");
            tvOrderNumber.setText("Order #: -");
            tvDate.setText("Date: -");
            qrCodeImageView.setVisibility(View.GONE);
            btnPrint.setEnabled(false);
            return;
        }

        btnPrint.setEnabled(true);
        qrCodeImageView.setVisibility(View.VISIBLE);

        StringBuilder orderText = new StringBuilder();
        for (CartItem item : items) {
            orderText.append("• ")
                    .append(item.getName())
                    .append(" (x").append(item.getQuantity())
                    .append(") = $").append(String.format("%.2f", item.getTotalPrice()))
                    .append("\n");
        }

        tvOrderItems.setText(orderText.toString());
        
        // Calculate subtotal, tax (10%), and total
        double subtotal = cartManager.getTotalPrice();
        double tax = subtotal * 0.10;
        double total = subtotal + tax;
        
        tvSubtotal.setText("$" + String.format("%.2f", subtotal));
        tvTax.setText("$" + String.format("%.2f", tax));
        tvTotal.setText("$" + String.format("%.2f", total));

        // Generate order number
        String orderNumber = "ORD" + System.currentTimeMillis();
        tvOrderNumber.setText("Order #: " + orderNumber);

        // Set date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        tvDate.setText("Date: " + sdf.format(new Date()));
    }

    private void generateQRCode() {
        List<CartItem> items = cartManager.getCartItems();
        if (items.isEmpty()) {
            return;
        }

        double subtotal = cartManager.getTotalPrice();
        double tax = subtotal * 0.10;
        double total = subtotal + tax;
        String paymentMethod = spinnerPaymentMethod.getSelectedItem().toString();
        
        StringBuilder qrData = new StringBuilder();
        qrData.append("Restaurant Order\n");
        qrData.append("Order #: ORD").append(System.currentTimeMillis()).append("\n");
        qrData.append("Date: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date())).append("\n");
        qrData.append("Items:\n");
        
        for (CartItem item : items) {
            qrData.append(item.getName()).append(" x").append(item.getQuantity()).append("\n");
        }
        qrData.append("Subtotal: $").append(String.format("%.2f", subtotal)).append("\n");
        qrData.append("Tax: $").append(String.format("%.2f", tax)).append("\n");
        qrData.append("Total: $").append(String.format("%.2f", total)).append("\n");
        qrData.append("Payment: ").append(paymentMethod);

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(qrData.toString(), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            qrCodeImageView.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
        }
    }

    private void printReceipt() {
        PrintManager printManager = (PrintManager) getSystemService(PRINT_SERVICE);
        String jobName = "Order Receipt";

        PrintDocumentAdapter printAdapter = new PrintDocumentAdapter() {
            @Override
            public void onWrite(android.print.PageRange[] pages, android.os.ParcelFileDescriptor destination, android.os.CancellationSignal cancellationSignal, WriteResultCallback callback) {
                // Simple print implementation
                callback.onWriteFinished(new android.print.PageRange[]{android.print.PageRange.ALL_PAGES});
            }

            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, android.os.CancellationSignal cancellationSignal, LayoutResultCallback callback, android.os.Bundle metadata) {
                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }
                PrintDocumentInfo info = new PrintDocumentInfo.Builder(jobName)
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                        .build();
                callback.onLayoutFinished(info, true);
            }
        };

        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
        PrintJob printJob = printManager.print(jobName, printAdapter, builder.build());
        
        if (printJob.isCompleted()) {
            Toast.makeText(this, "Print job completed", Toast.LENGTH_SHORT).show();
        } else if (printJob.isFailed()) {
            Toast.makeText(this, "Print job failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Print job started", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayOrder();
        generateQRCode();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            android.content.SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.remove("loggedInUser");
            editor.apply();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new android.content.Intent(this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_profile) {
            Toast.makeText(this, "Profile feature coming soon", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Settings feature coming soon", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "Restaurant Ordering App v1.0", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

