package com.example.jonathan.arbaeen;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jonathan.arbaeen.adapter.DateModel;
import com.example.jonathan.arbaeen.classes.DateParser;
import com.example.jonathan.arbaeen.classes.MyReceiver;
import com.example.jonathan.arbaeen.classes.QuiblaCalculator;
import com.example.jonathan.arbaeen.praytimes.Clock;
import com.example.jonathan.arbaeen.praytimes.Coordinate;
import com.example.jonathan.arbaeen.praytimes.PrayTime;
import com.example.jonathan.arbaeen.praytimes.PrayTimesCalculator;
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import static android.telephony.PhoneNumberUtils.formatNumber;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class TodayFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, DatePickerDialog.OnDateSetListener {

    TextView todaydate,date1,date2;
    public TextView distance,azansobh,toloaftab,azanzohr,asr,ghorobaftab,azanmaghreb,esha,nimeshab;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private TextView lblLocation;
    SharedPreferences sp,sp2;
    Bundle bundle;
    PrayTimesCalculator prayTimesCalculator;
    Coordinate coor;
    int retry=0;
    boolean azan;
    ProgressDialog processDialog;
    Button menu;
    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_today, container, false);
        prayTimesCalculator = new PrayTimesCalculator();
        todaydate = (TextView)view.findViewById(R.id.today_date);
        azansobh = (TextView)view.findViewById(R.id.today_azansobh);
        toloaftab = (TextView)view.findViewById(R.id.today_toloaftab);
        azanzohr = (TextView)view.findViewById(R.id.today_azanzohr);
        asr = (TextView)view.findViewById(R.id.today_asr);
        ghorobaftab = (TextView)view.findViewById(R.id.today_ghorobaftab);
        azanmaghreb = (TextView)view.findViewById(R.id.today_azanmaghreb);
        esha = (TextView)view.findViewById(R.id.today_esha);
        nimeshab = (TextView)view.findViewById(R.id.today_nimeshab);
        bundle = getActivity().getIntent().getExtras();
        date1 = (TextView)view.findViewById(R.id.today_date1);
        date2 = (TextView)view.findViewById(R.id.today_date2);
        distance = (TextView)view.findViewById(R.id.today_distance);
        lblLocation = (TextView) view.findViewById(R.id.today_loc);
        sp= getActivity().getSharedPreferences("location",0);
        sp2= getActivity().getSharedPreferences("logininfo",0);
        azan=sp2.getBoolean("azan",false);
        if (checkPlayServices(getActivity())) {
            buildGoogleApiClient(getContext());
        }
        processDialog = new ProgressDialog(getContext());
        processDialog.setMessage("در حال تشخیص مکان...");
        processDialog.setIndeterminate(true);
        processDialog.setCancelable(false);

        menu=(Button)view.findViewById(R.id.today_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),IntroActivity.class));
                getActivity().finish();
            }
        });


        Button btn = (Button)view.findViewById(R.id.today_do);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getloc();
            }
        });

        Button pickdate = (Button)view.findViewById(R.id.today_pickdate);
        pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersianCalendar persianCalendar = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        TodayFragment.this,
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay()
                );
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int stat= sp.getInt("stat",99);
        if(stat!=1){
            getloc();
        }else {
            setToday();
        }
    }

    @Override
    public void onDestroy() {
        try {
            mGoogleApiClient.disconnect();
        }catch (Exception e){}
        super.onDestroy();
    }

    private void displayLocation() {
        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                try {
                    double latitude = mLastLocation.getLatitude();
                    double longitude = mLastLocation.getLongitude();
                    Locale locale = new Locale("fa");

                    Geocoder gcd = new Geocoder(getActivity().getApplicationContext(), locale);
                    List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0) {
                        SharedPreferences.Editor e=sp.edit();
                        e.putInt("stat",1);
                        e.putString("lat",latitude+"");
                        e.putString("lon",longitude+"");
                        e.putString("loc",addresses.get(0).getLocality());
                        e.putInt("quibla",(int) QuiblaCalculator.doCalculate(latitude,longitude));
                        e.apply();
                        lblLocation.setText(addresses.get(0).getLocality());
                        setToday();
                        processDialog.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), "خطایی روی داده لطفا دوباره امتحان کنید", Toast.LENGTH_LONG).show();
                    processDialog.dismiss();
                }
            } else {
                if(retry!=30){
                    retry++;
                    displayLocation();
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "خطایی روی داده لطفا دوباره امتحان کنید", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor e = sp.edit();
                    e.putInt("stat", 1);
                    double lat, lon;
                    lat = Double.valueOf(getResources().getString(R.string.default_lat));
                    lon = Double.valueOf(getResources().getString(R.string.default_lon));
                    e.putString("lat", String.valueOf(getResources().getString(R.string.default_lat)));
                    e.putString("lon", String.valueOf(getResources().getString(R.string.default_lon)));
                    e.putInt("quibla", (int) QuiblaCalculator.doCalculate(lat, lon));
                    e.putString("loc", getResources().getString(R.string.default_loc));
                    e.apply();
                    lblLocation.setText(getResources().getString(R.string.default_loc));
                    setToday();
                    processDialog.dismiss();
                    retry=0;
                }
            }
    }
    protected synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    private boolean checkPlayServices(Activity context) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(resultCode)) {
                googleAPI.getErrorDialog(context,resultCode,1).show();
            } else {
                Toast.makeText(context,
                        "گوشی شما از سرویس های گوگل پشتیبانی نمی کند.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }
    private void setToday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.ENGLISH);
        String date = df.format(c.getTime());
        DateParser dp = new DateParser(date);
        DateModel dm = dp.dateAndTimeParser();
        String temp = dm.toString();
        date1.setText(temp);
        UmmalquraCalendar cal = new UmmalquraCalendar();
        String temp2;
        temp2= cal.get(Calendar.DAY_OF_MONTH)+" ";
        temp2+= cal.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("ar"))+" ";
        temp2 += cal.get(Calendar.YEAR)+" ";
        date2.setText(temp2);
        float[] results = new float[1];
        Location.distanceBetween(Double.parseDouble(sp.getString("lat","0")), Double.parseDouble(sp.getString("lon","0")),
                32.6073951, 43.9765972, results);
        int km =(int) results[0]/1000;
        distance.setText("تا کربلا "+km+" کیلومتر");

        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Double d = (double) (TimeZone.getTimeZone(Time.getCurrentTimezone()).getOffset(System.currentTimeMillis()));
        Double lat = Double.parseDouble(sp.getString("lat",getResources().getString(R.string.default_lat)));
        Double lon = Double.parseDouble(sp.getString("lon",getResources().getString(R.string.default_lon)));
        coor =new Coordinate(lat,lon);
        Date date3 =c2.getTime();
        Map<PrayTime, Clock> prayTimes = prayTimesCalculator.calculate(date3,coor);

                azansobh.setText(getPersianFormattedClock(prayTimes.get(PrayTime.FAJR)));
                toloaftab.setText(getPersianFormattedClock(prayTimes.get(PrayTime.SUNRISE)));
                azanzohr.setText(getPersianFormattedClock(prayTimes.get(PrayTime.DHUHR)));
                asr.setText(getPersianFormattedClock(prayTimes.get(PrayTime.ASR)));
                ghorobaftab.setText(getPersianFormattedClock(prayTimes.get(PrayTime.SUNSET)));
                azanmaghreb.setText(getPersianFormattedClock(prayTimes.get(PrayTime.MAGHRIB)));
                esha.setText(getPersianFormattedClock(prayTimes.get(PrayTime.ISHA)));
                nimeshab.setText(getPersianFormattedClock(prayTimes.get(PrayTime.MIDNIGHT)));
                if(azan) {
                     updateAlarm();
                }else{
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    Intent myIntent = new Intent(getActivity().getApplicationContext(),
                            MyReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            getActivity().getApplicationContext(), 1, myIntent,     PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.cancel(pendingIntent);
                }
                lblLocation.setText(sp.getString("loc","تهران"));

    }
    @Override
    public void onStart() {
        super.onStart();
        try {
            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        }catch (Exception e){
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public String getPersianFormattedClock(Clock clock) {
        String timeText = null;
        int hour = clock.getHour();
        String result = clockToString(hour, clock.getMinute());
        return result;
    }
    public String clockToString(int hour, int minute) {
        return formatNumber(String.format(Locale.ENGLISH, "%02d:%02d", hour, minute));
    }
    private void getloc(){
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(getContext());
        materialDialog
                .cancelable(false)
                .title(getResources().getString(R.string.todaydialogtitle))
                .content(getResources().getString(R.string.todaydialogcontex))
                .positiveText(getResources().getString(R.string.todaydialogpos))
                .negativeText(getResources().getString(R.string.todaydialogneg))
                .neutralText(getResources().getString(R.string.todaydialogna))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(checkPlayServices(getActivity())){
                            dialog.dismiss();
                                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    materialDialog.show();
                                }else if(!checknet()){
                                    Intent intent = new Intent();
                                    intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                                    startActivity(intent);
                                    materialDialog.show();
                                }
                                else {
                                    processDialog.show();
                                    displayLocation();
                                }

                        }
                        else {
                            Toast.makeText(getContext(),"گوشی شما از سرویس های گوگل پشتیبانی نمی کند",Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor e=sp.edit();
                            e.putInt("stat",1);
                            e.putString("lat",getResources().getString(R.string.default_lat));
                            e.putString("lon",getResources().getString(R.string.default_lon));
                            e.putInt("quibla",(int) QuiblaCalculator.doCalculate(35.6719,51.4244));
                            e.putString("loc",getResources().getString(R.string.default_loc));
                            e.apply();
                            lblLocation.setText(getResources().getString(R.string.default_loc));
                            dialog.dismiss();
                        }

                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        setToday();
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        sp.edit().putInt("nazr",3).commit();
                        startActivity(new Intent(getActivity(),SelectCityActivity.class));
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void SetAlarm(int reqCode,int year,int month,int day,int hour,int min,String title,String context) {
        String dateName = title;
        String dateNote = context;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(year,month,day,hour,min,1);
        Intent myIntent = new Intent(getActivity(), MyReceiver.class);
        myIntent.putExtra("title", dateName);
        myIntent.putExtra("notes", dateNote);
        myIntent.putExtra("code", reqCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                reqCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity()
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
    }
    public void updateAlarm(){
        String sobh=azansobh.getText().toString();
        String zohr=azanzohr.getText().toString();
        String maghreb=azanmaghreb.getText().toString();
        Coordinate coordinate = coor;

        Calendar c = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        long diffsobh=0,diffzohr=0,diffmaghreb=0;
        try {
            String time1 = sobh;
            String time2 = format.format(c.getTime());
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            diffsobh = date2.getTime() - date1.getTime();
        }catch (Exception e){}
        try {
            String time1 = zohr;
            String time2 = format.format(c.getTime());
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            diffzohr = date2.getTime() - date1.getTime();
        }catch (Exception e){}
        try {
            String time1 = maghreb;
            String time2 = format.format(c.getTime());
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            diffmaghreb = date2.getTime() - date1.getTime();
        }catch (Exception e){}
        if (diffsobh>=0 && diffzohr<0){

            SetAlarm(1111,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),Integer.parseInt(zohr.substring(0,2)),Integer.parseInt(zohr.substring(3,5)),"اذن","اذان ظهر");
          //  Toast.makeText(getContext(),c.get(Calendar.YEAR)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.DAY_OF_MONTH)+" "+Integer.parseInt(zohr.substring(0,2))+":"+Integer.parseInt(zohr.substring(3,5))+"",Toast.LENGTH_LONG).show();

        }else if (diffzohr>=0 && diffmaghreb<0){

            SetAlarm(1111,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),Integer.parseInt(maghreb.substring(0,2)),Integer.parseInt(maghreb.substring(3,5)),"اذن","اذان مغرب");
          //  Toast.makeText(getContext(),c.get(Calendar.YEAR)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.DAY_OF_MONTH)+" "+Integer.parseInt(maghreb.substring(0,2))+":"+Integer.parseInt(maghreb.substring(3,5))+"",Toast.LENGTH_LONG).show();

        }
        else if(diffzohr<0 && c.get(Calendar.HOUR_OF_DAY)<7) {
            SetAlarm(1111, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), Integer.parseInt(sobh.substring(0, 2)), Integer.parseInt(sobh.substring(3, 5)), "اذن", "اذان صبح");
         //   Toast.makeText(getContext(), "امروز" + c.get(Calendar.YEAR) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.DAY_OF_MONTH) + " " + Integer.parseInt(sobh.substring(0, 2)) + ":" + Integer.parseInt(sobh.substring(3, 5)) + "", Toast.LENGTH_LONG).show();
        }
        else if(diffmaghreb>=0 && c.get(Calendar.HOUR_OF_DAY)>12){
            Date dt = new Date();
            c.setTime(dt);
            c.add(Calendar.DATE, 1);
            dt = c.getTime();
            Map<PrayTime, Clock> prayTimes = prayTimesCalculator.calculate(dt,coordinate);
            String alarm=getPersianFormattedClock(prayTimes.get(PrayTime.FAJR));
            SetAlarm(1111,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),Integer.parseInt(alarm.substring(0,2)),Integer.parseInt(alarm.substring(3,5)),"اذن","اذان صبح");
          //  Toast.makeText(getContext(),"فردا"+c.get(Calendar.YEAR)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.DAY_OF_MONTH)+" "+Integer.parseInt(alarm.substring(0,2))+":"+Integer.parseInt(alarm.substring(3,5))+"",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
       // String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
      //  Toast.makeText(getContext(),date,Toast.LENGTH_LONG).show();
    }
    private boolean checknet(){
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            return true;
        }else{
            return false;
        }
    }
}
