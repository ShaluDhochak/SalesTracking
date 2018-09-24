package com.sales.tracking.salestracking.Fragment;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Bean.ExpencesSpBean;
import com.sales.tracking.salestracking.Bean.LeadSpBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.Connectivity;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.JSONParser;
import com.sales.tracking.salestracking.Utility.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;


public class AddTotalExpensesFragment extends Fragment {

    View view;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref, userCompIdPref;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private int SELECT_FILE =1,REQUEST_CODE = 0 ;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    JSONParser jsonParser = new JSONParser();

    String selectedCategory, selectedCategoryId,selectMode, selectModeId, leadType_id;

    ArrayList<String> categoryExpensesSp;
    ArrayList<String> modeExpensesSp;

    Map<String, String> categoryExpensesMap = new HashMap<>();
    ProgressDialog pDialog;

    @BindView(R.id.categoryAddExpensesSp_sp)
    Spinner categoryAddExpensesSp_sp;

    @BindView(R.id.amountAddExpensesSp_et)
    EditText amountAddExpensesSp_et;

    @BindView(R.id.modeAddExpensesSp_sp)
    Spinner modeAddExpensesSp_sp;

    @BindView(R.id.detailsAddExpensesSp_et)
    EditText detailsAddExpensesSp_et;

    @BindView(R.id.submitAddExpensesSp_btn)
    Button submitAddExpensesSp_btn;

    @BindView(R.id.chooseFromGalleryExpenses_tv)
    TextView chooseFromGalleryExpenses_tv;

    @BindView(R.id.ClickPhotoExpenses_tv)
    TextView ClickPhotoExpenses_tv;

    @BindView(R.id.photoAddExpensesSp_iv)
    ImageView photoAddExpensesSp_iv;

    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_total_expenses, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseUI();
    }

    private void initialiseUI(){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");
        userCompIdPref = sharedPref.getString("user_com_id", "");

        if (userTypePref.equals("Sales Manager")) {
            selectCategory();
            selectMode();
        }else if (userTypePref.equals("Sales Executive")){
            selectCategory();
            selectMode();
        }

    }

    @OnClick(R.id.submitAddExpensesSp_btn)
    public void addExpenses(){
        if (!selectedCategory.equals("Category")) {
            if (!(amountAddExpensesSp_et.getText().toString().equals(""))) {
                if (!selectMode.equals("Mode")) {
                    if (!(detailsAddExpensesSp_et.getText().toString().equals(""))){
                          new addExpensesSp().execute();

                    }else{
                        Toast.makeText(getActivity(), "Please Enter Details", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Select Mode", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(), "Please Enter Amount", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Please select Category", Toast.LENGTH_SHORT).show();
        }
    }

    public class addExpensesSp extends AsyncTask<String, JSONObject, JSONObject> {
        String filetoUpload, expense_details,expense_uid, expense_mode, expense_cat, expense_amt, add_exp, expense_compid;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            filetoUpload  = getBase64(bitmap);
            expense_amt =amountAddExpensesSp_et.getText().toString();
            expense_cat = selectedCategoryId;
            expense_compid = "1";
            expense_details = detailsAddExpensesSp_et.getText().toString();
            add_exp = "";
            expense_mode = selectModeId;
            expense_uid = userIdPref;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Expenses...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("fileToUpload", filetoUpload));
            params.add(new BasicNameValuePair("expense_mode", expense_mode));
            params.add(new BasicNameValuePair("expense_amt", expense_amt));
            params.add(new BasicNameValuePair("add_exp", "add_exp"));
            params.add(new BasicNameValuePair("expense_uid", expense_uid));
            params.add(new BasicNameValuePair("expense_details", expense_details));
            params.add(new BasicNameValuePair("expense_compid", userCompIdPref));
            params.add(new BasicNameValuePair("expense_cat", expense_cat));


            String url_add_task = ApiLink.ROOT_URL + ApiLink.Expenses_SP;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Added Successfully")) {
                    return json;
                }
                else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject response) {
            try {
                pDialog.dismiss();
                if (!(response == null)) {
                    makeText(getActivity(),"Added Successfully", Toast.LENGTH_SHORT).show();
                    clearAll();
                }
                else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private void clearAll(){
        categoryAddExpensesSp_sp.setSelection(0);
        detailsAddExpensesSp_et.setText("");
        amountAddExpensesSp_et.setText("");
        modeAddExpensesSp_sp.setSelection(0);
    }

    private void selectCategory() {

        try{
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Expenses_SP;
                Map<String, String> map = new HashMap<>();
                map.put("expense_comid",userCompIdPref );
                map.put("expcat_dropdown", "");

                final GSONRequest<ExpencesSpBean> locationSpinnerGsonRequest = new GSONRequest<ExpencesSpBean>(
                        Request.Method.POST,
                        Url,
                        ExpencesSpBean.class,map,
                        new com.android.volley.Response.Listener<ExpencesSpBean>() {
                            @Override
                            public void onResponse(ExpencesSpBean response) {
                                categoryExpensesSp.clear();
                                categoryExpensesSp.add("Category");
                                for(int i=0;i<response.getExpcat_dropdown().size();i++)
                                {
                                    categoryExpensesSp.add(response.getExpcat_dropdown().get(i).getExpcat_name());
                                    categoryExpensesMap.put(response.getExpcat_dropdown().get(i).getExpcat_Id(),response.getExpcat_dropdown().get(i).getExpcat_name() );
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utilities.serverError(getActivity());
                            }
                        });
                locationSpinnerGsonRequest.setShouldCache(false);
                Utilities.getRequestQueue(getActivity()).add(locationSpinnerGsonRequest);
            }
            categoryExpensesSp = new ArrayList<String>();
            categoryExpensesSp.clear();
            categoryExpensesSp.add("Category");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, categoryExpensesSp);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            categoryAddExpensesSp_sp.setAdapter(quotationLocationDataAdapter);

        }catch (Exception e){
        }
    }

    @OnItemSelected(R.id.categoryAddExpensesSp_sp)
    public void categorySelected(Spinner spinner, int position) {
        selectedCategory = spinner.getSelectedItem().toString();

        for (Map.Entry<String, String> e : categoryExpensesMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectedCategory)) {
                selectedCategoryId = (String) key;
                leadType_id= (String) key;
            }
        }

    }

    private void selectMode() {

        List<String> modeSpinner = new ArrayList<String>();
        modeSpinner.add("Mode");
        modeSpinner.add("Cash");
        modeSpinner.add("Card");
        modeSpinner.add("Online Transfer");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, modeSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        modeAddExpensesSp_sp.setAdapter(statusAdapter);
    }

    @OnItemSelected(R.id.modeAddExpensesSp_sp)
    public void modeSelected(Spinner spinner, int position) {
        selectMode = spinner.getSelectedItem().toString();

        if (selectMode.equals("Mode")) {
            selectModeId = "";
        }else if (selectMode.equals("Cash")){
            selectModeId = "Cash";
        }else if (selectMode.equals("Card")){
            selectModeId = "Card";
        }else if (selectMode.equals("Online Transfer")){
            selectModeId = "Online Transfer";
        }
    }

    @OnClick(R.id.chooseFromGalleryExpenses_tv)
    public void openGalleryExpenses(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK && data != null && data.getData() != null){
            try{

                Uri selectedImageUri = data.getData();
                bitmap = getBitmapFromUri(selectedImageUri);
                photoAddExpensesSp_iv.setImageBitmap(bitmap);

            }catch(Exception e){

            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            photoAddExpensesSp_iv.setImageBitmap(bitmap);
        }
    }

    private String getBase64(Bitmap bitmap){
        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            return encoded;
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    @OnClick(R.id.ClickPhotoExpenses_tv)
    public void clickViaCamera(){
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }
}
