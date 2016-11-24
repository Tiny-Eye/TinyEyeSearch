package gurpreetsk.me.tinyeye.cloudvision.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import gurpreetsk.me.tinyeye.R;
import gurpreetsk.me.tinyeye.cloudvision.adapter.LinksAdapter;
import gurpreetsk.me.tinyeye.cloudvision.utils.Constants;

public class WikiFragment extends Fragment {
    private TextView descriptionTV;
    private String strtitle = "Minor";
    ProgressDialog progressDialog;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imageView;
    ExpandableHeightListView listView;
    LinksAdapter adapter;
    ArrayList<String> links = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    TextToSpeech tts;
    Vibrator vib;

    private static final String TAG = "WikiFragment";

    public WikiFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        imageView = (ImageView) getActivity().findViewById(R.id.photo);
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.share_fab);
        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar_layout);
    }

    @Override
    public void onStart() {
        super.onStart();
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wiki, container, false);

        Bundle data = getArguments();
        String APIresponse = data.getString(Constants.WIKI_DATA_INTENT_KEY);
        vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        strtitle = APIresponse;
        getActivity().setTitle(strtitle);
        String strResponse = APIresponse.replace(" ", "%20");
        final String strlinks = APIresponse.replace(" ", "+");

        descriptionTV = (TextView) v.findViewById(R.id.description);
//        coordinateTV = (TextView) v.findViewById(R.id.coordinateTV);

        listView = (ExpandableHeightListView) v.findViewById(R.id.linklist);
        adapter = new LinksAdapter(getContext(), links);
        listView.setAdapter(adapter);
        listView.setExpanded(true);

        fetchimage(strResponse);
        externallinks(strlinks);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_TEXT, descriptionTV.getText().toString());
                startActivity(Intent.createChooser(share, "Share link!"));
            }
        });

        descriptionTV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String textToSpeak = "";
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 400);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 600);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 700);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 800);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 900);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = descriptionTV.getText().toString().substring(0, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });

        String url = "https://en.wikipedia.org/wiki/" + strResponse;

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute(url);

        return v;
    }

    private class JsoupAsyncTask extends AsyncTask<String, Void, String> {

        String title;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String str = params[0];
                Document doc = Jsoup.connect(str).get();
                Elements paragraphs = doc.select("p");
                for (Element p : paragraphs) {
                    title += (p.text() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            try {
                String alteredString;
                if (title.contains("Coordinates:")) {
                    alteredString = title.substring(title.indexOf('\n') + 1)
                            .replaceAll("\\[.*?\\]", "")
                            .replace("[", "")
                            .replace("]", "")
                            .replace("null", "");
                } else{
                    alteredString = title.replaceAll("\\[.*?\\]", "")
                            .replace("[", "")
                            .replace("]", "")
                            .replace("null", "");
                }
                descriptionTV.setText(alteredString);
            } catch (Exception e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void fetchimage(final String str) {
        try {
            final String BASE_URL = "https://en.wikipedia.org/w/api.php?action=query&titles=Image:" + str + ".jpg&prop=imageinfo&iiprop=url&format=json";

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    BASE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                String strkey = "";
                                JSONObject object = new JSONObject(response);
                                String syncresponse = object.getString("query");
                                JSONObject object2 = new JSONObject(syncresponse);
                                String syncresponse2 = object2.getString("pages");
                                JSONObject object3 = new JSONObject(syncresponse2);
                                Iterator<String> iter = object3.keys();
                                while (iter.hasNext()) {
                                    strkey = iter.next();
                                }
                                JSONObject title = object3.getJSONObject(strkey);
                                String imageinfo = title.getString("imageinfo");
                                JSONArray a1obj = new JSONArray(imageinfo);
                                for (int j = 0; j < a1obj.length(); j++) {
                                    JSONObject obj = a1obj.getJSONObject(j);
                                    String imageUrl = obj.getString("url");
                                    Picasso.with(getContext())
                                            .load(imageUrl)
                                            .into(imageView);
//                                    Toast.makeText(getContext(), imageUrl, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                FirebaseCrash.report(e);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(getContext(), "No internet connections!", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
    }

    private void externallinks(final String str) {
        try {
            final String BASE_URL = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extlinks&titles=" + str;

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    BASE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                String strkey = "";
                                JSONObject object = new JSONObject(response);
                                String syncresponse = object.getString("query");
                                JSONObject object2 = new JSONObject(syncresponse);
                                String syncresponse2 = object2.getString("pages");
                                JSONObject object3 = new JSONObject(syncresponse2);
                                Iterator<String> iter = object3.keys();
                                while (iter.hasNext()) {
                                    strkey = iter.next();
                                }
                                JSONObject title = object3.getJSONObject(strkey);
                                strtitle = title.getString("extlinks");
                                JSONArray array = new JSONArray(strtitle);
                                for (int j = 1; j < array.length(); j++) {
                                    JSONObject obj = array.getJSONObject(j);
                                    links.add(obj.getString("*"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                FirebaseCrash.report(e);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError) {
                        Toast.makeText(getContext(), "No internet connections!", Toast.LENGTH_SHORT).show();
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.report(e);
        }
    }

}