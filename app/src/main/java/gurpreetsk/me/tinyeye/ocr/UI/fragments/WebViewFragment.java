package gurpreetsk.me.tinyeye.ocr.UI.fragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Locale;

import gurpreetsk.me.tinyeye.R;
import gurpreetsk.me.tinyeye.cloudvision.fragments.WikiFragment;
import gurpreetsk.me.tinyeye.cloudvision.utils.Constants;

public class WebViewFragment extends Fragment {
    private TextView textView;
    private ImageView search, download, share;
    TextToSpeech tts;
    ProgressDialog progressDialog;

    public WebViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_web_view, container, false);

        Bundle data = getArguments();
        final String ocrText = data.getString(Constants.OCR_MAIN_ACTIVITY_INTENT_KEY);

        textView = (TextView) v.findViewById(R.id.ocr_text);
        textView.setText(ocrText);
        getActivity().setTitle(ocrText);

        search = (ImageView) v.findViewById(R.id.google);
        download = (ImageView) v.findViewById(R.id.download);
        share = (ImageView) v.findViewById(R.id.share);

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String textToSpeak = "";
                try {
                    textToSpeak = textView.getText().toString().substring(0, 50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = textView.getText().toString().substring(0, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = textView.getText().toString().substring(0, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = textView.getText().toString().substring(0, 300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = textView.getText().toString().substring(0, 400);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = textView.getText().toString().substring(0, 500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = textView.getText().toString().substring(0, 600);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = textView.getText().toString().substring(0, 700);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = textView.getText().toString().substring(0, 800);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = textView.getText().toString().substring(0, 900);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textToSpeak = textView.getText().toString().substring(0, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        });

        String url = "http://www.google.com/#q=";
        String query = null;
        if (ocrText != null) {
            query = ocrText.trim();
        }
        final String final_url = url + query;

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(final_url);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Fetched data!", ocrText);
                Toast.makeText(getContext(), "Fetched data copied to Clipboard!", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Fetched data by TinyEye!\n");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, ocrText);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        String wikiUrl = "https://en.wikipedia.org/wiki/" + query;

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute(wikiUrl);

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
                } else {
                    alteredString = title.replaceAll("\\[.*?\\]", "")
                            .replace("[", "")
                            .replace("]", "")
                            .replace("null", "");
                }
                textView.setText(alteredString);
            } catch (Exception e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
        }
    }

    @Override
    public void onPause() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}