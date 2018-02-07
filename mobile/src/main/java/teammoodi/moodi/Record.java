package teammoodi.moodi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Record extends AppCompatActivity{ //currently doesn't do shit so fuck you

    private Button recordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordButton = findViewById(R.id.recordbutton);
        recordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                recordButton.setBackgroundColor(0xffffff);
            }
        });
    }
}
