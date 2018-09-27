package com.sales.tracking.salestracking.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Bean.UpdateViewTaskSpBean;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;


public class VisitTaskUpdateSpFragment extends Fragment{

    View view;

    @BindView(R.id.timeUpdateVisitTaskSp_rl)
    RelativeLayout timeUpdateVisitTaskSp_rl;

    @BindView(R.id.dateUpdateVisitTaskSp_rl)
    RelativeLayout dateUpdateVisitTaskSp_rl;

    @BindView(R.id.separatorBelowStatusUpdateVisitTaskSp)
    View separatorBelowStatusUpdateVisitTaskSp;

    @BindView(R.id.separatorBelowDateUpdateVisitTaskSp)
    View separatorBelowDateUpdateVisitTaskSp;

    @BindView(R.id.vtaskUpdateVisitTaskSp_sp)
    Spinner vtaskUpdateVisitTaskSp_sp;

    @BindView(R.id.statusUpdateVisitTaskSp_sp)
    Spinner statusUpdateVisitTaskSp_sp;

    @BindView(R.id.commentUpdateVisitTaskSp_et)
    EditText commentUpdateVisitTaskSp_et;

    @BindView(R.id.dateUpdateVisitTaskSp_tv)
    TextView dateUpdateVisitTaskSp_tv;

    @BindView(R.id.timeUpdateVisitTaskSp_tv)
    TextView timeUpdateVisitTaskSp_tv;

    @BindView(R.id.submitUpdateVisitTaskSp_btn)
    Button submitUpdateVisitTaskSp_btn;

    @BindView(R.id.photoUpdateVisitTaskSp_iv)
    ImageView photoUpdateVisitTaskSp_iv;

    private int SELECT_FILE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Bitmap bitmap;

    String selectedVisitTask, selectedVisitTaskId, selectedvisit_id, selectTaskStatus, selectedTaskStatusId;
    ArrayList<String> visitTaskUpdateVisitSp;

    Map<String, String> visitTaskMap = new HashMap<>();
    ProgressDialog pDialog;

    SharedPreferences sharedPref;
    String userIdPref, userTypePref;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_visit_task_update_sp, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseUI();
    }

    private void initialiseUI() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userIdPref = sharedPref.getString("user_id", "");
        userTypePref = sharedPref.getString("user_type", "");

        setDefaultDateTimeVisibility();

        selectVisittask();
        selectTaskStatus();
    }

    private void selectVisittask() {
        try {
            if (Connectivity.isConnected(getActivity())) {
                String Url = ApiLink.ROOT_URL + ApiLink.Dashboard_SalesPerson;
                Map<String, String> map = new HashMap<>();
                map.put("sp_visittask", "");
                map.put("visit_uid", userIdPref);

                final GSONRequest<UpdateViewTaskSpBean> locationSpinnerGsonRequest = new GSONRequest<UpdateViewTaskSpBean>(
                        Request.Method.POST,
                        Url,
                        UpdateViewTaskSpBean.class, map,
                        new com.android.volley.Response.Listener<UpdateViewTaskSpBean>() {
                            @Override
                            public void onResponse(UpdateViewTaskSpBean response) {
                                visitTaskUpdateVisitSp.clear();
                                visitTaskUpdateVisitSp.add("Visit Task");
                                for (int i = 0; i < response.getSp_tasks_dd().size(); i++) {
                                    visitTaskUpdateVisitSp.add(response.getSp_tasks_dd().get(i).getLead_company());
                                    visitTaskMap.put(response.getSp_tasks_dd().get(i).getVisit_id(), response.getSp_tasks_dd().get(i).getLead_company());
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
            visitTaskUpdateVisitSp = new ArrayList<String>();
            visitTaskUpdateVisitSp.clear();
            visitTaskUpdateVisitSp.add("Visit Task");
            ArrayAdapter<String> quotationLocationDataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, visitTaskUpdateVisitSp);
            quotationLocationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            vtaskUpdateVisitTaskSp_sp.setAdapter(quotationLocationDataAdapter);

        } catch (Exception e) {
        }
    }

    @OnItemSelected(R.id.vtaskUpdateVisitTaskSp_sp)
    public void visitTaskAddSelected(Spinner spinner, int position) {
        selectedVisitTask = spinner.getSelectedItem().toString();
        for (Map.Entry<String, String> e : visitTaskMap.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (value.equals(selectedVisitTask)) {
                selectedVisitTaskId = (String) key;
                selectedvisit_id = (String) key;
            }
        }
    }

    private void selectTaskStatus() {

        List<String> statusSpinner = new ArrayList<String>();
        statusSpinner.add("Status");
        statusSpinner.add("Pending");
        statusSpinner.add("Done");
        statusSpinner.add("Cancel");
        statusSpinner.add("Followup");
        statusSpinner.add("Not Interested");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_textview, statusSpinner);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        statusUpdateVisitTaskSp_sp.setAdapter(statusAdapter);
    }

    @OnItemSelected(R.id.statusUpdateVisitTaskSp_sp)
    public void taskStatusSelected(Spinner spinner, int position) {
        selectTaskStatus = spinner.getSelectedItem().toString();

        if (selectTaskStatus.equals("Status")) {
            selectedTaskStatusId = "";
            setDefaultDateTimeVisibility();
        } else if (selectTaskStatus.equals("Pending")) {
            selectedTaskStatusId = "Pending";
            setDefaultDateTimeVisibility();
        } else if (selectTaskStatus.equals("Done")) {
            selectedTaskStatusId = "Done";
            setDefaultDateTimeVisibility();
        } else if (selectTaskStatus.equals("Cancel")) {
            selectedTaskStatusId = "Cancel";
            setDefaultDateTimeVisibility();
        } else if (selectTaskStatus.equals("Followup")) {
            selectedTaskStatusId = "Followup";

            timeUpdateVisitTaskSp_rl.setVisibility(View.VISIBLE);
            dateUpdateVisitTaskSp_rl.setVisibility(View.VISIBLE);
            separatorBelowStatusUpdateVisitTaskSp.setVisibility(View.VISIBLE);
            separatorBelowDateUpdateVisitTaskSp.setVisibility(View.VISIBLE);
        } else if (selectTaskStatus.equals("Not Interested")) {
            selectedTaskStatusId = "Not Interested";
            setDefaultDateTimeVisibility();
        }
    }

    private void setDefaultDateTimeVisibility() {
        timeUpdateVisitTaskSp_rl.setVisibility(View.GONE);
        dateUpdateVisitTaskSp_rl.setVisibility(View.GONE);
        separatorBelowStatusUpdateVisitTaskSp.setVisibility(View.GONE);
        separatorBelowDateUpdateVisitTaskSp.setVisibility(View.GONE);
    }

    @OnClick(R.id.submitUpdateVisitTaskSp_btn)
    public void updateVisitTask() {

        if (!selectedVisitTask.equals("Visit Task")) {
            if (commentUpdateVisitTaskSp_et.getText().toString().length() > 0) {
                if (!selectTaskStatus.equals("Status")) {
                    if (selectTaskStatus.equals("Followup")) {
                        if (dateUpdateVisitTaskSp_tv.getText().toString().length() > 0) {
                            if (timeUpdateVisitTaskSp_tv.getText().toString().length() > 0) {
                                new UpdateVisitTaskSp().execute();
                            } else {
                                Toast.makeText(getActivity(), "Please Enter Time", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please Enter Date", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        new UpdateVisitTaskSp().execute();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Select Status", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please Enter Comment", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please Select Visit Task", Toast.LENGTH_SHORT).show();
        }
    }


    public class UpdateVisitTaskSp extends AsyncTask<String, JSONObject, JSONObject> {
        String visit_id, visit_photo, visit_status, visit_time, visit_date;
        String visit_comment;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            visit_comment = commentUpdateVisitTaskSp_et.getText().toString();

            visit_date = dateUpdateVisitTaskSp_tv.getText().toString();
            visit_time = timeUpdateVisitTaskSp_tv.getText().toString();
            visit_status = selectedTaskStatusId;
            visit_photo = "";
            visit_id = selectedvisit_id;

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Task...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("filter[visit_photo]",getBase64(bitmap)));
            params.add(new BasicNameValuePair("filter[visit_comments]", visit_comment));
            params.add(new BasicNameValuePair("filter[followup_date]", visit_date));
            params.add(new BasicNameValuePair("filter[followup_time]", visit_time));
            params.add(new BasicNameValuePair("visit_id", visit_id));
            params.add(new BasicNameValuePair("filter[visit_status]", visit_status));
            params.add(new BasicNameValuePair("update", "update"));

            String url_add_task = ApiLink.ROOT_URL + ApiLink.VISIT_TASK_SPINNER;
            JSONObject json = jsonParser.makeHttpRequest(url_add_task, "POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                String message = json.getString(TAG_MESSAGE);
                if (success == 1 && message.equals("Updated Successfully")) {
                    return json;
                } else {
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
                    makeText(getActivity(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    clearAll();
                } else {
                    makeText(getActivity(), "Not Updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private void clearAll() {
        statusUpdateVisitTaskSp_sp.setSelection(0);
        commentUpdateVisitTaskSp_et.setText("");
        dateUpdateVisitTaskSp_tv.setText("");
        timeUpdateVisitTaskSp_tv.setText("");
        vtaskUpdateVisitTaskSp_sp.setSelection(0);
    }

    @OnClick(R.id.timeUpdateVisitTaskSp_tv)
    public void updateVisitTime() {
        final String time;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(mcurrentTime.getTime());
        timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeUpdateVisitTaskSp_tv.setText(selectedHour % 12 + ":" + selectedMinute + ((selectedHour >= 12) ? " PM" : " AM"));
            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.show();
    }

    @OnClick(R.id.dateUpdateVisitTaskSp_tv)
    public void updatevisitTime() {
        final Calendar calenderObj = Calendar.getInstance();
        int mYear = calenderObj.get(Calendar.YEAR);
        int mMonth = calenderObj.get(Calendar.MONTH);
        int mDay = calenderObj.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateUpdateVisitTaskSp_tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @OnClick(R.id.chooseFromGalleryExpenses_tv)
    public void openGalleryExpenses() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {

                Uri selectedImageUri = data.getData();
                bitmap = getBitmapFromUri(selectedImageUri);
                photoUpdateVisitTaskSp_iv.setImageBitmap(bitmap);

            } catch (Exception e) {
            }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            photoUpdateVisitTaskSp_iv.setImageBitmap(bitmap);
        }

    }

    private String getBase64(Bitmap bitmap) {
        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            return encoded;
        } catch (Exception e) {
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
    public void clickViaCamera() {
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
