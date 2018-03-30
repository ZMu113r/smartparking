package com.example.zacharymuller.smartparking.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amazonaws.http.HttpClient;
import com.amazonaws.http.HttpResponse;
import com.example.zacharymuller.smartparking.APIClient.JSObject;
import com.example.zacharymuller.smartparking.APIClient.RequestTask;
import com.example.zacharymuller.smartparking.Entities.Destination;
import com.example.zacharymuller.smartparking.Entities.Garage;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.Entities.User;

import com.example.zacharymuller.smartparking.Handlers.DestinationSortingHandler;
import com.example.zacharymuller.smartparking.Handlers.UserLocationSortingHandler;
import com.example.zacharymuller.smartparking.R;
import com.google.gson.JsonSerializer;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.google.gson.Gson;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationSelectionActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView lvData;

    private Destination dest = new Destination();
    private HashMap<String, String> destinations_list = new HashMap<>();

    private ArrayList<Garage> garages = new ArrayList<>();
    private ArrayList<Garage> closestGarages = new ArrayList<>();

    private LatLng latLng = null;

    private User currentUser;

    private String pwdDecrypted = "";
    private String[] indices = {"A", "B", "C", "D", "H", "I", "Libra", "Test"};

    private static int OPEN = 0;
    private static int CLOSED = 1;

    public Destination getDestination() {
        return this.dest;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_destination_selection);
        // Get current user from intent
        Gson gs = new Gson();

        Bundle bundle = getIntent().getExtras();

        String currentUserJSON = bundle.getString("curruser");
        currentUser = gs.fromJson(currentUserJSON, User.class);

        // region destinations
        // Populate destinations list
        // #
        destinations_list.put("63 South", "7aa");
        // A
        destinations_list.put("All Knight Study (FC-A)", "7h");
        destinations_list.put("Alpha Delta Pi", "406");
        destinations_list.put("Alpha Epsilon Phi", "409");
        destinations_list.put("Alpha Tau Omega", "410");
        destinations_list.put("Alpha Xi Delta", "404");
        destinations_list.put("Ampac", "152");
        destinations_list.put("Aquarius Courtyard", "55");
        destinations_list.put("Ara Dr. Research Facility", "117");
        destinations_list.put("Arboretum", "525");
        destinations_list.put("Art Gallery", "51a");
        // B
        destinations_list.put("Barbara Ying Center", "71");
        destinations_list.put("Baseball Field", "basefield");
        destinations_list.put("Bennett Research 2", "3251");
        destinations_list.put("Bio Molecular Meeting Portable", "601");
        destinations_list.put("Biological Sciences", "20");
        destinations_list.put("Biological Transgenic Greenhouse", "124");
        destinations_list.put("Biology Field Research Center", "92");
        destinations_list.put("Biomolecular Research Annex", "8114");
        destinations_list.put("Brevard Hall", "30");
        destinations_list.put("Burnett Honors College", "95");
        destinations_list.put("Burnett School of Biomedical Sciences", "1002");
        destinations_list.put("Business Administration 1", "45");
        destinations_list.put("Business Administration 2", "94");
        // C
        destinations_list.put("Career Services and Experiential Learning", "140");
        destinations_list.put("Center for Emerging Media", "906");
        destinations_list.put("CFE Arena", "50");
        destinations_list.put("Challenge Course", "u4");
        destinations_list.put("Chemistry", "5");
        destinations_list.put("Chi Omega", "416");
        destinations_list.put("Citrus Hall", "85");
        destinations_list.put("Classroom Building 1", "79");
        destinations_list.put("Classroom Building 2", "98");
        destinations_list.put("Colbourn Hall", "18");
        destinations_list.put("College of Arts and Humanities", "87");
        destinations_list.put("College of Sciences Building", "54");
        destinations_list.put("Counseling and Psychological Services", "27");
        destinations_list.put("Creative School 1st Grade", "529");
        destinations_list.put("Creative School for Children 1", "24");
        destinations_list.put("Creative School Module 2", "540");
        destinations_list.put("CREOL", "53");
        // D
        destinations_list.put("Delta Delta Delta", "403");
        destinations_list.put("Disc Gold Course", "dgc");
        destinations_list.put("Duke Energy UCF Welcome Center", "96");
        // E
        destinations_list.put("Early Childhood Center", "28");
        destinations_list.put("Education", "21");
        destinations_list.put("Emergency Operation Center", "49");
        destinations_list.put("Emergency Services and Training Building", "350");
        destinations_list.put("Engine Research Lab", "76");
        destinations_list.put("Engineering 1", "40");
        destinations_list.put("Engineering 2", "91");
        destinations_list.put("Executive Development Center - UCF College of Business", "902");
        // F
        destinations_list.put("Facilities and Safety", "16");
        destinations_list.put("Fairwinds Alumni Center", "126");
        destinations_list.put("Ferrell Commons", "7g");
        destinations_list.put("FIEA - Florida Interactive Entertainment Academy", "fiea-florida-interactive-entertainment-academy");
        destinations_list.put("Flagler Hall", "86");
        destinations_list.put("Football Practice Field", "footpracfld");
        destinations_list.put("Fraternity and Sorority Life", "415");
        // G
        destinations_list.put("Gemini Courtyard", "66");
        // H
        destinations_list.put("Harris Corporation Engineering Center", "116");
        destinations_list.put("Health and Public Affairs 1", "80");
        destinations_list.put("Health and Public Affairs 2", "90");
        destinations_list.put("Hercules", "108");
        destinations_list.put("Housing Administration", "73");
        destinations_list.put("Howard Phillips Hall", "14");
        // I
        destinations_list.put("In VIVO Research", "12601");
        destinations_list.put("Innovative Center", "8112");
        destinations_list.put("IST Technology Development Center", "12423");
        // J
        destinations_list.put("Jay Bergman Field", "82");
        destinations_list.put("John C. Hitt Library", "2");
        destinations_list.put("John T. Washington Center", "26");
        // K
        destinations_list.put("Kappa Alpha Theta", "411");
        destinations_list.put("Kappa Delta", "407");
        destinations_list.put("Kappa Kappa Gamma", "417");
        destinations_list.put("Kappa Signma", "413");
        destinations_list.put("Knight's Pantry", "knights-pantry");
        destinations_list.put("Knightro's", "50b");
        // L
        destinations_list.put("Lake Claire", "65");
        destinations_list.put("Lake Hall", "9");
        destinations_list.put("Leisure Pool", "118");
        destinations_list.put("Libra Community Center", "33");
        // M
        destinations_list.put("Mathematical Sciences Building", "12");
        destinations_list.put("Medical Education Building", "1001");
        destinations_list.put("Memory Mall", "memorymall");
        destinations_list.put("Millican Hall", "1");
        // N
        destinations_list.put("Neptune", "156");
        destinations_list.put("Nicholson School of Communication", "75");
        destinations_list.put("Nike", "101");
        // O
        destinations_list.put("Office of Student Rights and Responsibilities", "7c");
        destinations_list.put("Orange Hall", "31");
        destinations_list.put("Osceola Hall", "10");
        // P
        destinations_list.put("Parking and Transportation Services", "u1");
        destinations_list.put("Partnership 1", "8111");
        destinations_list.put("Partnership 2", "8119");
        destinations_list.put("Partnership 3", "8126");
        destinations_list.put("Pegasus Courtyard", "60");
        destinations_list.put("Performing Arts Center", "119");
        destinations_list.put("Physical Sciences Building", "121");
        destinations_list.put("Pi Beta Phi", "405");
        destinations_list.put("Polk Hall", "11");
        destinations_list.put("Print Shop", "22");
        destinations_list.put("Psychology Building", "99");
        destinations_list.put("Public Safety Building (Police Station)", "150");
        // R
        destinations_list.put("Rec Services Soccer Field Pavilion", "317");
        destinations_list.put("Recreation and Wellness Center", "88");
        destinations_list.put("Recreation Service Pavilion", "318");
        destinations_list.put("Recreation Services Field Maintenance", "321");
        destinations_list.put("Recreation Services Field Restroom", "320");
        destinations_list.put("Recreation Support", "25");
        destinations_list.put("Recycling Center", "327");
        destinations_list.put("Reflecting Pond", "pond");
        destinations_list.put("Reflections Kiosk", "310");
        destinations_list.put("Rehearsal Hall", "19");
        destinations_list.put("Research Pavilion", "8102");
        destinations_list.put("Retirement Planning and Investments / Athletics", "623");
        destinations_list.put("Robinson Observatory", "74");
        destinations_list.put("RWC Park", "u3");
        // S
        destinations_list.put("SDES Information Technology", "7b");
        destinations_list.put("Seminole Hall", "32");
        destinations_list.put("Siemens Energy Center", "44");
        destinations_list.put("Sigma Chi", "412");
        destinations_list.put("Simulation, Training and Technology Center", "8125");
        destinations_list.put("Small Business Development Center", "small-business-development-center");
        destinations_list.put("Soccer Practice Field", "socpracfld");
        destinations_list.put("Softball Stadium", "125");
        destinations_list.put("Spectrum Stadium", "135");
        destinations_list.put("SRC Auditorium", "7e");
        destinations_list.put("Storm Water Research Lab", "4");
        destinations_list.put("Student Accessibility Services", "7f");
        destinations_list.put("Student Union", "52");
        destinations_list.put("Sumter Hall", "84");
        // T
        destinations_list.put("Teaching Academy", "93");
        destinations_list.put("Technology Commons 1", "13");
        destinations_list.put("Technology Commons 2", "29");
        destinations_list.put("Technology Incubator", "3267");
        destinations_list.put("The Venue", "50c");
        destinations_list.put("Theatre", "6");
        destinations_list.put("Timothy R. Newman Nature Pavilion", "329");
        destinations_list.put("Tower 1", "129");
        destinations_list.put("Tower 2", "130");
        destinations_list.put("Tower 3", "132");
        destinations_list.put("Tower 4", "133");
        destinations_list.put("Track and Soccer Field", "142");
        // U
        destinations_list.put("UCF Global", "139");
        // V
        destinations_list.put("Visitor and Parking Information Center", "153");
        destinations_list.put("Visual Arts Building", "51");
        destinations_list.put("Volusia Hall", "8");
        // W
        destinations_list.put("Wayne Densch Center 1", "38");
        destinations_list.put("Wayne Densch Sports Center", "77");
        // Z
        destinations_list.put("Zeta Tau Alpha", "401");
        // endregion

        // Create list view access
        lvData = findViewById(R.id.lvData);
        // Listen for a user to choose their destination
        lvData.setOnItemClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Garages.resetClosestGarages();
        garages = new ArrayList<>();
        closestGarages = new ArrayList<>();
        latLng = null;
        dest.setName("");
    }

    // General method to show that moving between screens works
    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
        Garages.resetClosestGarages();

        // get name from clicked item
        String selectedDestination = lvData.getItemAtPosition(position).toString();
        dest.setName(selectedDestination);

        // Find location ID for selected destination on campus
        String locationID = destinations_list.get(selectedDestination);

        // Form query string to hit UCF locations API
        String ucfLocApiString = "http://map.ucf.edu/locations/" + locationID + ".json";

        // Spin up an async task to get json from site
        JsonSerializer serializer = new JsonSerializer();
        try {
            serializer.execute(ucfLocApiString).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        garages = new ArrayList<>();

        for(Garage g: Garages.getGarages())
        {
            garages.add(g);
        }

        // Set distance to destination and from user for all garages
        for (Garage g : garages) {
            g.setDestinationDistance(Math.hypot((g.getLatitude() - dest.getLatitude()), (g.getLongitude() - dest.getLongitude())));
            g.setUserDistance(Math.hypot((currentUser.getLatitude() - g.getLatitude()), (currentUser.getLongitude() - g.getLongitude())));
        }

        // sort destinationDistance
        Collections.sort(garages, new DestinationSortingHandler());
        // Get 1st and 2nd items in destinationDistances as options for user
        closestGarages.add(garages.get(0));
        if(!closestGarages.contains(garages.get(1))) {
            closestGarages.add(garages.get(1));
        }

        // sort userDistance
        Collections.sort(garages, new UserLocationSortingHandler());
        // Get 1st item from userLocationDistances as option for user
        if(!closestGarages.contains(garages.get(0))) {
            closestGarages.add(garages.get(0));
        }

        for(Garage g : closestGarages)
            Garages.addClosestGarage(g);

        Intent intent = new Intent(DestinationSelectionActivity.this, RouteSelectionActivity.class);

        // Convert objects to JSON strings
        Gson gs = new Gson();
        String currentUserJSON = gs.toJson(currentUser);
        String destinationJSON = gs.toJson(dest);
        String garagesJSON = gs.toJson(garages);
        String closestGaragesJSON = gs.toJson(closestGarages);

        // Pass the JSON strings to the next activity through intent
        intent.putExtra("current user", currentUserJSON);
        intent.putExtra("destination", destinationJSON);

        /*
        Log.i("COORDFETCH", closestGarages.size() + "");

        Log.i("COORDFETCH", currentUserJSON);
        Log.i("COORDFETCH", destinationJSON);
        Log.i("COORDFETCH", garagesJSON);
        Log.i("COORDFETCH", closestGaragesJSON);

        // Pass the JSON strings to the next activity through intent
        intent.putExtra("current user", currentUserJSON);
        intent.putExtra("destination", destinationJSON);
        */

        startActivity(intent);
        //Log.i("CoordFetch", latLng.getLatitude() + ", " + latLng.getLongitude());
        /*

        // Add Orlando, FL specification to location
        selectedDestination = "UCF " + selectedDestination + ", Orlando, FL. 32816";

        // Spin up an async task to...
        // Retrieve coordinates and put them into Destination object
        // in the background
        //new CoordinateFetchTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, selectedDestination);

        com.mapbox.services.api.geocoding.v5.MapboxGeocoding mapboxGeocoding = new com.mapbox.services.api.geocoding.v5.MapboxGeocoding.Builder()
                .setAccessToken("pk.eyJ1Ijoibjhob3VsIiwiYSI6ImNqZWQ1c2g4MzFmdHMycGxuZW03aW44ZnoifQ.tctQ1Hd69Bw3zWdqn56How")
                .setLocation(selectedDestination)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().getFeatures();
                if (results.size() > 0) {
                    // Log the first results position.
                    latLng = new LatLng(results.get(0).asPosition().getLatitude(), results.get(0).asPosition().getLongitude());
                    dest.setLatitude(latLng.getLatitude());
                    dest.setLongitude(latLng.getLongitude());

                    garages = new ArrayList<>();

                    for(Garage g: Garages.getGarages())
                    {
                        garages.add(g);
                    }


                    for (Garage g : garages) {
                        g.setDestinationDistance(Math.hypot((g.getLatitude() - dest.getLatitude()), (g.getLongitude() - dest.getLongitude())));
                        g.setUserDistance(Math.hypot((currentUser.getLatitude() - g.getLatitude()), (currentUser.getLongitude() - g.getLongitude())));
                    }


                    // sort destinationDistance
                    Collections.sort(garages, new DestinationSortingHandler());
                    // Get 1st and 2nd items in destinationDistances as options for user
                    closestGarages.add(garages.get(0));
                    if(!closestGarages.contains(garages.get(1))) {
                        closestGarages.add(garages.get(1));
                    }

                    // sort userDistance
                    Collections.sort(garages, new UserLocationSortingHandler());
                    // Get 1st item from userLocationDistances as option for user
                    if(!closestGarages.contains(garages.get(0))) {
                        closestGarages.add(garages.get(0));
                    }

                    for(Garage g : closestGarages)
                        Garages.addClosestGarage(g);

                    Intent intent = new Intent(DestinationSelectionActivity.this, RouteSelectionActivity.class);

                    // Convert objects to JSON strings
                    Gson gs = new Gson();
                    String currentUserJSON = gs.toJson(currentUser);
                    String destinationJSON = gs.toJson(dest);
                    String garagesJSON = gs.toJson(garages);
                    String closestGaragesJSON = gs.toJson(closestGarages);

                    Log.i("COORDFETCH", closestGarages.size() + "");

                    Log.i("COORDFETCH", currentUserJSON);
                    Log.i("COORDFETCH", destinationJSON);
                    Log.i("COORDFETCH", garagesJSON);
                    Log.i("COORDFETCH", closestGaragesJSON);

                    // Pass the JSON strings to the next activity through intent
                    intent.putExtra("current user", currentUserJSON);
                    intent.putExtra("destination", destinationJSON);

                    startActivity(intent);
                    Log.i("CoordFetch", latLng.getLatitude() + ", " + latLng.getLongitude());
                } else {
                    // No result for your request were found.
                    Log.d("CoordFetch", "onResponse: No result found");
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                Log.i("CoordFetch", "Failed to get coordinatates");
                throwable.printStackTrace();
            }
        });
    */
        // Create a new activity intent
    }

    private class JsonSerializer extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params){
            try {
                // Send HTTP GET request
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                // Read in response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                } in.close();

                // Convert to JSON
                JSONObject myJSON = new JSONObject(response.toString());

                // Get lat/lng from JSON Object
                String lt = myJSON.getJSONArray("googlemap_point").getString(0);
                String lg = myJSON.getJSONArray("googlemap_point").getString(1);
                // Convert to double
                double lat = Double.parseDouble(lt);
                double lng = Double.parseDouble(lg);

                dest.setLatitude(lat);
                dest.setLongitude(lng);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result){
        }
    }
}